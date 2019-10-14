package org.hisp.dhis.rules

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

import org.assertj.core.api.Assertions.assertThat
import org.hisp.dhis.rules.models.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock
import java.util.*
import kotlin.test.assertFailsWith

@RunWith(JUnit4::class)
class ConstantsValueTests {

    private val constantsValueMap = hashMapOf<String, String>()

    @Test
    fun shouldThrowExceptionIfConstantsValueMapIsNull() {
        assertFailsWith<IllegalArgumentException> {
            RuleEngineContext.builder(ExpressionEvaluator())
                    .rules(listOf(mock(Rule::class.java)))
                    .ruleVariables(listOf(mock(RuleVariable::class.java)))
                    .supplementaryData(hashMapOf())
                    .calculatedValueMap(hashMapOf())
                    .constantsValue(null)
                    .build()
        }
    }

    @Test
    fun assignConstantValueFromAssignActionInEnrollment() {
        val assignAction = RuleActionAssign.create(null, "C{test_constant_value}", "test_data_element")
        val rule = Rule.create(null, 1, "true", listOf(assignAction), "test_program_rule1")

        constantsValueMap["test_constant_value"] = "3.14"

        val ruleEngineBuilder = getRuleEngine(listOf(rule))

        val enrollment = RuleEnrollment.builder()
                .enrollment("test_enrollment")
                .programName("test_program")
                .incidentDate(Date())
                .enrollmentDate(Date())
                .status(RuleEnrollment.Status.ACTIVE)
                .organisationUnit("test_ou")
                .organisationUnitCode("test_ou_code")
                .attributeValues(listOf(RuleAttributeValue.create("test_attribute", "test_value")))
                .build()

        val ruleEngine = ruleEngineBuilder.build()
        val ruleEffects = ruleEngine.evaluate(enrollment).call()

        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].data).isEqualTo("3.14")
        assertThat(ruleEffects[0].ruleAction).isEqualTo(assignAction)
    }

    @Test
    fun assignConstantValueFromAssignActionInEvent() {
        val assignAction = RuleActionAssign.create(null, "C{test_constant_value}", "test_data_element")
        val rule = Rule.create(null, 1, "true", listOf(assignAction), "test_program_rule1")

        constantsValueMap["test_constant_value"] = "3.14"

        val ruleEngineBuilder = getRuleEngine(listOf(rule))

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

        val ruleEngine = ruleEngineBuilder.build()
        val ruleEffects = ruleEngine.evaluate(ruleEvent).call()

        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].data).isEqualTo("3.14")
        assertThat(ruleEffects[0].ruleAction).isEqualTo(assignAction)

    }

    private fun getRuleEngine(rules: List<Rule>): RuleEngine.Builder {
        return RuleEngineContext
                .builder(ExpressionEvaluator())
                .rules(rules)
                .ruleVariables(listOf())
                .calculatedValueMap(hashMapOf())
                .supplementaryData(hashMapOf())
                .constantsValue(constantsValueMap)
                .build().toEngineBuilder().triggerEnvironment(TriggerEnvironment.SERVER)
    }
}
