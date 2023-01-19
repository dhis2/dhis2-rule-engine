package org.dhis2.ruleengine

import com.google.common.collect.Maps
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.assertj.core.api.Assertions.assertThat
import org.dhis2.ruleengine.RuleEngineTestUtils.getRuleEngine
import org.dhis2.ruleengine.models.*
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*

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
@RunWith(JUnit4::class)
class ConstantsValueTest {
    /*@Test(expected = IllegalArgumentException::class)
    fun shouldThrowExceptionIfConstantsValueMapIsNull() {
        RuleEngineContext()
            .rules(listOf(Mockito.mock(Rule::class.java)))
            .ruleVariables(listOf(Mockito.mock(RuleVariable::class.java)))
            .supplementaryData(HashMap<String, List<String>>())
            .constantsValue(null)
            .build()
    }*/

    @Test
    @Throws(Exception::class)
    fun assignConstantValueFromAssignActionInEnrollment() {
        val assignAction: RuleAction = RuleAction.Assign("#{test_attribute}", "C{A1234567890}", "")
        val rule = Rule(null, null, 1, "true", listOf(assignAction), "test_program_rule1")
        val constantsValueMap: MutableMap<String, String> = HashMap()
        constantsValueMap["A1234567890"] = "3.14"

        val enrollment = RuleEnrollment(
            "test_enrollment",
            "test_program",
            Clock.System.now().toLocalDateTime(TimeZone.UTC).date,
            Clock.System.now().toLocalDateTime(TimeZone.UTC).date,
            RuleEnrollment.Status.ACTIVE,
            "test_ou",
            "test_ou_code",
            listOf(RuleAttributeValue("test_attribute", "test_value"))
        )

        val ruleEngine = getRuleEngine(
            rules = listOf(rule),
            ruleEnrollment = enrollment,
            constantsValue = constantsValueMap
        )

        val ruleEffects: List<RuleEffect> = ruleEngine.evaluate(ruleEnrollment = enrollment)
        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].data).isEqualTo("3.14")
        assertThat(ruleEffects[0].ruleAction).isEqualTo(assignAction)
    }

    @Test
    @Throws(Exception::class)
    fun assignValue() {
        val assignAction: RuleAction = RuleAction.Assign("test_attribute", "4", "")
        val action: RuleAction = RuleAction.ShowError(AttributeType.UNKNOWN, "", "", "#{test_attribute}")
        val rule = Rule("test_program_rule1", null, 1, "true", listOf(assignAction), "")
        val rule2 = Rule("test_program_rule2", null, 1, "#{test_attribute} > 3", listOf(action), "")
        val enrollment = RuleEnrollment(
            "test_enrollment",
            "test_program",
            Clock.System.now().toLocalDateTime(TimeZone.UTC).date,
            Clock.System.now().toLocalDateTime(TimeZone.UTC).date,
            RuleEnrollment.Status.ACTIVE,
            "test_ou",
            "test_ou_code",
            listOf(RuleAttributeValue("test_attribute", "test_value"))
        )

        val ruleEngine = getRuleEngine(
            rules = listOf(rule, rule2),
            ruleEnrollment = enrollment
        )

        val ruleEffects: List<RuleEffect> = ruleEngine.evaluate(ruleEnrollment = enrollment)
        assertThat(ruleEffects.size).isEqualTo(2)
        assertThat(ruleEffects[0].data).isEqualTo("4")
        assertThat(ruleEffects[0].ruleAction).isEqualTo(assignAction)
        assertThat(ruleEffects[1].data).isEqualTo("4")
        assertThat(ruleEffects[1].ruleAction).isEqualTo(action)
    }

    @Test
    @Throws(Exception::class)
    fun assignValueThroughVariable() {
        val assignAction: RuleAction = RuleAction.Assign(null, "4", "#{test_attribute}")
        val action: RuleAction = RuleAction.ShowError(AttributeType.UNKNOWN, "", "", "#{test_attribute}")
        val rule = Rule("test_program_rule1", null, 1, "true", listOf(assignAction), "")
        val rule2 = Rule("test_program_rule2", null, 1, "#{test_attribute} > 3", listOf(action), "")

        val enrollment = RuleEnrollment(
            "test_enrollment",
            "test_program",
            Clock.System.now().toLocalDateTime(TimeZone.UTC).date,
            Clock.System.now().toLocalDateTime(TimeZone.UTC).date,
            RuleEnrollment.Status.ACTIVE,
            "test_ou",
            "test_ou_code",
            listOf(RuleAttributeValue("test_attribute", "test_value"))
        )
        val ruleEngine = getRuleEngine(
            rules = listOf(rule, rule2),
            ruleEnrollment = enrollment
        )
        val ruleEffects: List<RuleEffect> = ruleEngine.evaluate(ruleEnrollment = enrollment)
        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].data).isEqualTo("4")
        assertThat(ruleEffects[0].ruleAction).isEqualTo(action)
    }

    @Test
    @Throws(Exception::class)
    fun assignConstantValueFromAssignActionInEvent() {
        val assignAction: RuleAction = RuleAction.Assign("#{test_data_element}", "C{A1234567890}", "")
        val rule = Rule("test_program_rule1", null, 1, "true", listOf(assignAction), "")
        val constantsValueMap: MutableMap<String, String> = HashMap()
        constantsValueMap["A1234567890"] = "3.14"
        val ruleEvent = RuleEvent(
            event = "test_event",
            programStage = "test_program_stage",
            programStageName = "",
            status = RuleEvent.Status.ACTIVE,
            eventDate = Clock.System.now().toLocalDateTime(TimeZone.UTC).date,
            dueDate = Clock.System.now().toLocalDateTime(TimeZone.UTC).date,
            completedDate = Clock.System.now().toLocalDateTime(TimeZone.UTC).date,
            organisationUnit = "",
            organisationUnitCode = "",
            dataValues = listOf(
                RuleDataValue(
                    Clock.System.now().toLocalDateTime(TimeZone.UTC).date,
                    "test_program_stage",
                    "test_data_element",
                    "test_value"
                )
            )
        )
        val ruleEngine = getRuleEngine(rules = listOf(rule), constantsValue = constantsValueMap)
        val ruleEffects: List<RuleEffect> = ruleEngine.evaluate(ruleEvent)
        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].data).isEqualTo("3.14")
        assertThat(ruleEffects[0].ruleAction).isEqualTo(assignAction)
    }
}