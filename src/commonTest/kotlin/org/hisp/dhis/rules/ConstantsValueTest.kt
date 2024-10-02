package org.hisp.dhis.rules

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import org.hisp.dhis.rules.api.RuleEngine
import org.hisp.dhis.rules.api.RuleEngineContext
import org.hisp.dhis.rules.models.*
import org.hisp.dhis.rules.utils.currentDate
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
        val assignAction = RuleAction("C{A1234567890}", "ASSIGN", mapOf(Pair("field", "#{test_attribute}")))
        val rule = Rule("true", listOf(assignAction), "test_program_rule1", "")
        val constantsValueMap: MutableMap<String, String> = HashMap()
        constantsValueMap["A1234567890"] = "3.14"
        val ruleEngineContext = getRuleEngineContext(listOf(rule), constantsValueMap)
        val enrollment = RuleEnrollment(
            enrollment = "test_enrollment",
            programName = "test_program",
            incidentDate = LocalDate.Companion.currentDate(),
            enrollmentDate = LocalDate.Companion.currentDate(),
            status = RuleEnrollmentStatus.ACTIVE,
            organisationUnit = "test_ou",
            organisationUnitCode = "test_ou_code",
            attributeValues = listOf(RuleAttributeValue("test_attribute", "test_value"))
        )
        val ruleEffects = RuleEngine.getInstance().evaluate(enrollment, emptyList(), ruleEngineContext)
        assertEquals(1, ruleEffects.size)
        assertEquals("3.14", ruleEffects[0].data)
        assertEquals(assignAction, ruleEffects[0].ruleAction)
    }

    @Test
    fun assignValue() {
        val assignAction = RuleAction("4", "ASSIGN", mapOf(Pair("field", "test_attribute")))
        val action = RuleAction("#{test_attribute}", "SHOWERROR")
        val rule = Rule("true", listOf(assignAction), "test_program_rule1", "")
        val rule2 = Rule("#{test_attribute} > 3", listOf(action), "test_program_rule2", "")
        val ruleEngineContext = getRuleEngineContext(
            listOf(rule, rule2),
            HashMap()
        )
        val enrollment = RuleEnrollment(
            enrollment = "test_enrollment",
            programName = "test_program",
            incidentDate = LocalDate.Companion.currentDate(),
            enrollmentDate = LocalDate.Companion.currentDate(),
            status = RuleEnrollmentStatus.ACTIVE,
            organisationUnit = "test_ou",
            organisationUnitCode = "test_ou_code",
            attributeValues = listOf(RuleAttributeValue("test_attribute", "test_value"))
        )
        val ruleEffects = RuleEngine.getInstance().evaluate(enrollment, emptyList(), ruleEngineContext)
        assertEquals(2, ruleEffects.size)
        assertEquals("4", ruleEffects[0].data)
        assertEquals(assignAction, ruleEffects[0].ruleAction)
        assertEquals("4", ruleEffects[1].data)
        assertEquals(action, ruleEffects[1].ruleAction)
    }

    @Test
    fun assignValueThroughVariable() {
        val assignAction = RuleAction("4", "ASSIGN", mapOf(Pair("content", "#{test_attribute}")))
        val action = RuleAction("#{test_attribute}", "SHOWERROR")
        val rule = Rule("true", listOf(assignAction), "test_program_rule1", "")
        val rule2 = Rule("#{test_attribute} > 3", listOf(action), "test_program_rule2", "")
        val ruleEngineContext = getRuleEngineContext(
            listOf(rule, rule2),
            HashMap()
        )
        val enrollment = RuleEnrollment(
            enrollment = "test_enrollment",
            programName = "test_program",
            incidentDate = LocalDate.Companion.currentDate(),
            enrollmentDate = LocalDate.Companion.currentDate(),
            status = RuleEnrollmentStatus.ACTIVE,
            organisationUnit = "test_ou",
            organisationUnitCode = "test_ou_code",
            attributeValues = listOf(RuleAttributeValue("test_attribute", "test_value"))
        )
        val ruleEffects = RuleEngine.getInstance().evaluate(enrollment, emptyList(), ruleEngineContext)
        assertEquals(1, ruleEffects.size)
        assertEquals("4", ruleEffects[0].data)
        assertEquals(action, ruleEffects[0].ruleAction)
    }

    @Test
    fun assignConstantValueFromAssignActionInEvent() {
        val assignAction = RuleAction("C{A1234567890}", "ASSIGN", mapOf(Pair("field", "#{test_data_element}")))
        val rule = Rule("true", listOf(assignAction), "test_program_rule1", "")
        val constantsValueMap: MutableMap<String, String> = HashMap()
        constantsValueMap["A1234567890"] = "3.14"
        val ruleEngineContext = getRuleEngineContext(listOf(rule), constantsValueMap)
        val ruleEvent = RuleEvent(
            event = "test_event",
            programStage = "test_program_stage",
            programStageName = "",
            status = RuleEventStatus.ACTIVE,
            eventDate = Clock.System.now(),
            createdDate = Clock.System.now(),
            dueDate = LocalDate.currentDate(),
            organisationUnit = "",
            organisationUnitCode = "",
            completedDate = LocalDate.currentDate(),
            dataValues =
            listOf(
                RuleDataValue(
                    "test_data_element", "test_value"
                )
            )
        )
        val ruleEffects = RuleEngine.getInstance().evaluate(ruleEvent, null, emptyList(),  ruleEngineContext)
        assertEquals(1, ruleEffects.size)
        assertEquals("3.14", ruleEffects[0].data)
        assertEquals(assignAction, ruleEffects[0].ruleAction)
    }

    private fun getRuleEngineContext(
        rules: List<Rule>,
        constantsValueMap: Map<String, String>
    ): RuleEngineContext {
        return RuleEngineContext(rules = rules, constantsValues = constantsValueMap)
    }
}
