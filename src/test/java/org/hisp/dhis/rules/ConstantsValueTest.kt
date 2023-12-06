package org.hisp.dhis.rules

import org.hisp.dhis.rules.models.*
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

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
class ConstantsValueTest {

    @Test
    fun assignConstantValueFromAssignActionInEnrollment() {
        val assignAction: RuleAction = RuleActionAssign.create(null, "C{A1234567890}", "#{test_attribute}")
        val rule: Rule = Rule("true", java.util.List.of(assignAction), "test_program_rule1", "")
        val constantsValueMap: MutableMap<String, String> = HashMap()
        constantsValueMap["A1234567890"] = "3.14"
        val ruleEngineBuilder = getRuleEngine(java.util.List.of(rule), constantsValueMap)
        val enrollment = RuleEnrollment.builder()
            .enrollment("test_enrollment")
            .programName("test_program")
            .incidentDate(Date())
            .enrollmentDate(Date())
            .status(RuleEnrollment.Status.ACTIVE)
            .organisationUnit("test_ou")
            .organisationUnitCode("test_ou_code")
            .attributeValues(java.util.List.of(RuleAttributeValue.create("test_attribute", "test_value")))
            .build()
        val ruleEngine = ruleEngineBuilder.build()
        val ruleEffects = ruleEngine.evaluate(enrollment).call()
        assertEquals(1, ruleEffects.size)
        assertEquals("3.14", ruleEffects[0].data())
        assertEquals(assignAction, ruleEffects[0].ruleAction())
    }

    @Test
    fun assignValue() {
        val assignAction: RuleAction = RuleActionAssign.create(null, "4", "test_attribute")
        val action: RuleAction =
            RuleActionMessage.create(null, "#{test_attribute}", "", RuleActionMessage.Type.SHOW_ERROR)
        val rule: Rule = Rule("true", java.util.List.of(assignAction), "test_program_rule1", "")
        val rule2: Rule = Rule("#{test_attribute} > 3", java.util.List.of(action), "test_program_rule2", "")
        val ruleEngineBuilder = getRuleEngine(
            java.util.List.of(rule, rule2),
            HashMap()
        )
        val enrollment = RuleEnrollment.builder()
            .enrollment("test_enrollment")
            .programName("test_program")
            .incidentDate(Date())
            .enrollmentDate(Date())
            .status(RuleEnrollment.Status.ACTIVE)
            .organisationUnit("test_ou")
            .organisationUnitCode("test_ou_code")
            .attributeValues(java.util.List.of(RuleAttributeValue.create("test_attribute", "test_value")))
            .build()
        val ruleEngine = ruleEngineBuilder.build()
        val ruleEffects = ruleEngine.evaluate(enrollment).call()
        assertEquals(2, ruleEffects.size)
        assertEquals("4", ruleEffects[0].data())
        assertEquals(assignAction, ruleEffects[0].ruleAction())
        assertEquals("4", ruleEffects[1].data())
        assertEquals(action, ruleEffects[1].ruleAction())
    }

    @Test
    fun assignValueThroughVariable() {
        val assignAction: RuleAction = RuleActionAssign.create("#{test_attribute}", "4", null)
        val action: RuleAction =
            RuleActionMessage.create(null, "#{test_attribute}", "", RuleActionMessage.Type.SHOW_ERROR)
        val rule: Rule = Rule("true", java.util.List.of(assignAction), "test_program_rule1", "")
        val rule2: Rule = Rule("#{test_attribute} > 3", java.util.List.of(action), "test_program_rule2", "")
        val ruleEngineBuilder = getRuleEngine(
            java.util.List.of(rule, rule2),
            HashMap()
        )
        val enrollment = RuleEnrollment.builder()
            .enrollment("test_enrollment")
            .programName("test_program")
            .incidentDate(Date())
            .enrollmentDate(Date())
            .status(RuleEnrollment.Status.ACTIVE)
            .organisationUnit("test_ou")
            .organisationUnitCode("test_ou_code")
            .attributeValues(java.util.List.of(RuleAttributeValue.create("test_attribute", "test_value")))
            .build()
        val ruleEngine = ruleEngineBuilder.build()
        val ruleEffects = ruleEngine.evaluate(enrollment).call()
        assertEquals(1, ruleEffects.size)
        assertEquals("4", ruleEffects[0].data())
        assertEquals(action, ruleEffects[0].ruleAction())
    }

    @Test
    fun assignConstantValueFromAssignActionInEvent() {
        val assignAction: RuleAction = RuleActionAssign.create(null, "C{A1234567890}", "#{test_data_element}")
        val rule: Rule = Rule("true", java.util.List.of(assignAction), "test_program_rule1", "")
        val constantsValueMap: MutableMap<String, String> = HashMap()
        constantsValueMap["A1234567890"] = "3.14"
        val ruleEngineBuilder = getRuleEngine(java.util.List.of(rule), constantsValueMap)
        val ruleEvent = RuleEvent(event ="test_event", programStage = "test_program_stage", programStageName = "",
            status = RuleEvent.Status.ACTIVE, eventDate = Date(), dueDate = Date(), organisationUnit = "", organisationUnitCode = "", completedDate = Date(), dataValues =
            listOf(
                    RuleDataValue.create(
                        Date(), "test_program_stage", "test_data_element", "test_value"
                    )
                )
            )
        val ruleEngine = ruleEngineBuilder.build()
        val ruleEffects = ruleEngine.evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size)
        assertEquals("3.14", ruleEffects[0].data())
        assertEquals(assignAction, ruleEffects[0].ruleAction())
    }

    private fun getRuleEngine(
        rules: List<Rule>,
        constantsValueMap: Map<String, String>
    ): RuleEngine.Builder {
        return RuleEngineContext
            .builder()
            .rules(rules)
            .ruleVariables(ArrayList())
            .supplementaryData(HashMap())
            .constantsValue(constantsValueMap)
            .build().toEngineBuilder().triggerEnvironment(TriggerEnvironment.SERVER)
    }
}
