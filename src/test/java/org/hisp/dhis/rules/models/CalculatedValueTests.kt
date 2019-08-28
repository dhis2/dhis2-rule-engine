package org.hisp.dhis.rules.models

/*
 * Copyright (c) 2004-2018, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the HISP project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import com.google.common.truth.Truth.assertThat
import org.hisp.dhis.rules.ExpressionEvaluator
import org.hisp.dhis.rules.RuleEngine
import org.hisp.dhis.rules.RuleEngineContext
import org.hisp.dhis.rules.RuleExpressionEvaluator
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.mock
import java.util.*
import kotlin.collections.HashMap
import kotlin.test.assertFailsWith


@RunWith(JUnit4::class)
class CalculatedValueTests {

    @Mock private lateinit var ruleExpressionEvaluator: RuleExpressionEvaluator

    private val calculatedValueMap = HashMap<String, Map<String, String>>()

    @Before
    fun setUp() {
        ruleExpressionEvaluator = ExpressionEvaluator()
    }

    @Test
    fun shouldThrowExceptionIfCalculatedValueMapIsNull() {
        assertFailsWith<IllegalArgumentException> {
            RuleEngineContext.builder(ruleExpressionEvaluator)
                    .ruleVariables(listOf(mock(RuleVariable::class.java)))
                    .supplementaryData(HashMap())
                    .calculatedValueMap(null)
                    .rules(listOf(mock(Rule::class.java)))
                    .build()
        }
    }

    @Test
    fun sendMessageMustGetValueFromAssignAction() {
        val assignAction = RuleActionAssign.create(null, "2+2", "#{test_calculated_value}")
        val rule = Rule.create(null, 1, "true", listOf(assignAction), "test_program_rule1")

        val sendMessageAction = RuleActionSendMessage.create("test_notification", "4")
        val rule2 = Rule.create(null, 4, "#{test_calculated_value}==4", listOf(sendMessageAction), "test_program_rule2")

        val ruleEngineBuilder = getRuleEngine(listOf(rule))

        val enrollment = RuleEnrollment.builder()
                .enrollment("test_enrollment")
                .programName("test_program")
                .incidentDate(Date())
                .enrollmentDate(Date())
                .status(RuleEnrollment.Status.ACTIVE)
                .organisationUnit("test_ou")
                .organisationUnitCode("test_ou_code")
                .attributeValues(listOf())
                .build()

        val ruleEvent = RuleEvent.builder()
                .event("test_event")
                .programStage("test_program_stage")
                .programStageName("")
                .status(RuleEvent.Status.ACTIVE)
                .eventDate(Date())
                .dueDate(Date())
                .organisationUnit("")
                .organisationUnitCode("")
                .dataValues(listOf(RuleDataValue.create(
                        Date(), "test_program_stage", "test_data_element", "test_value")))
                .build()

        val ruleEngine = ruleEngineBuilder.enrollment(enrollment).build()
        val ruleEffects = ruleEngine.evaluate(ruleEvent).call()

        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].data).isEqualTo("4")
        assertThat(ruleEffects[0].ruleAction).isEqualTo(assignAction)

        val valueMap = hashMapOf<String, String>()
        valueMap["test_calculated_value"] = ruleEffects[0].data!!
        calculatedValueMap[enrollment.enrollment!!] = valueMap

        val ruleEngine2 = getRuleEngine(listOf(rule, rule2)).enrollment(enrollment).build()
        val ruleEffects2 = ruleEngine2.evaluate(ruleEvent).call()

        val ruleActions = ruleEffects2.asSequence().map { (ruleAction) -> ruleAction }

        assertThat(ruleActions.contains(assignAction)).isEqualTo(true)
        assertThat(ruleActions.contains(sendMessageAction)).isEqualTo(true)

    }

    @Test
    fun sendMessageMustGetValueFromAssignActionInSingleExecution() {
        val assignAction = RuleActionAssign.create(null, "2+2", "#{test_calculated_value}")
        val rule = Rule.create(null, 1, "true", listOf(assignAction), "test_program_rule1")

        val sendMessageAction = RuleActionSendMessage.create("test_notification", "4")
        val rule2 = Rule.create(null, 4, "#{test_calculated_value}==4", listOf(sendMessageAction), "test_program_rule2")

        val ruleEngineBuilder = getRuleEngine(listOf(rule, rule2))

        val enrollment = RuleEnrollment.builder()
                .enrollment("test_enrollment")
                .programName("test_program")
                .incidentDate(Date())
                .enrollmentDate(Date())
                .status(RuleEnrollment.Status.ACTIVE)
                .organisationUnit("test_ou")
                .organisationUnitCode("test_ou_code")
                .attributeValues(listOf())
                .build()

        val ruleEvent = RuleEvent.builder()
                .event("test_event")
                .programStage("test_program_stage")
                .programStageName("")
                .status(RuleEvent.Status.ACTIVE)
                .eventDate(Date())
                .dueDate(Date())
                .organisationUnit("")
                .organisationUnitCode("")
                .dataValues(listOf(RuleDataValue.create(
                        Date(), "test_program_stage", "test_data_element", "test_value")))
                .build()

        val ruleEngine = ruleEngineBuilder.enrollment(enrollment).build()
        val call = ruleEngine.evaluate(ruleEvent)
        val ruleEffects = call.call()

        assertThat(ruleEffects.size).isEqualTo(2)
        assertThat(ruleEffects[0].data).isEqualTo("4")
        assertThat(ruleEffects[0].ruleAction).isEqualTo(assignAction)
        assertThat(ruleEffects[1].ruleAction).isEqualTo(sendMessageAction)

    }

    private fun getRuleEngine(rules: List<Rule>): RuleEngine.Builder {
        val ruleVariable = RuleVariableCalculatedValue.create("test_calculated_value", "", RuleValueType.TEXT)

        return RuleEngineContext
                .builder(ExpressionEvaluator())
                .rules(rules)
                .ruleVariables(listOf(ruleVariable))
                .calculatedValueMap(calculatedValueMap)
                .supplementaryData(hashMapOf())
                .constantsValue(hashMapOf())
                .build().toEngineBuilder().triggerEnvironment(TriggerEnvironment.SERVER)
    }
}
