package org.hisp.dhis.rules.models

import kotlinx.datetime.LocalDate
import org.hisp.dhis.rules.RuleEngine
import org.hisp.dhis.rules.RuleEngineContext
import org.hisp.dhis.rules.currentDate
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
class CalculatedValueTest {
    @Test
    fun evaluateOneThousandRulesTest() {
        val i = 1000
        val ruleEngineContext = getRuleEngineContext(createRules(i))
        val enrollment = RuleEnrollment(
            "test_enrollment",
            "test_program",
            LocalDate.currentDate(),
            LocalDate.currentDate(),
            RuleEnrollment.Status.ACTIVE,
            "test_ou",
            "test_ou_code",
            listOf()
        )
        val ruleEvent = RuleEvent(
            event = "test_event",
            programStage = "test_program_stage",
            programStageName = "",
            status = RuleEvent.Status.ACTIVE,
            eventDate = LocalDate.currentDate(),
            dueDate = LocalDate.currentDate(),
            organisationUnit = "",
            organisationUnitCode = "",
            completedDate = null,
            dataValues = listOf(
                RuleDataValue(
                    LocalDate.currentDate(), "test_program_stage", "test_data_element", "test_value"
                )
            )
        )
        val ruleEffects = RuleEngine().evaluate(ruleEvent, ruleEngineContext.copy(enrollment = enrollment))
        assertEquals(i, ruleEffects.size)
    }

    @Test
    fun sendMessageMustGetValueFromAssignAction() {
        val assignAction: RuleAction = RuleActionAssign.create("#{test_calculated_value}", "2+2", null)
        val rule = Rule("true", listOf(assignAction), "test_program_rule1")
        val sendMessageAction: RuleAction =
            RuleActionSendMessage("test_notification", "4")
        val rule2 = Rule(
            "#{test_calculated_value}==4",
            listOf(sendMessageAction),
            "test_program_rule2"
        )
        val enrollment = RuleEnrollment(
            "test_enrollment",
            "test_program",
            LocalDate.currentDate(),
            LocalDate.currentDate(),
            RuleEnrollment.Status.ACTIVE,
            "test_ou",
            "test_ou_code",
            listOf()
        )
        val ruleEvent = RuleEvent(
            event = "test_event",
            programStage = "test_program_stage",
            programStageName = "",
            status = RuleEvent.Status.ACTIVE,
            eventDate = LocalDate.currentDate(),
            dueDate = LocalDate.currentDate(),
            organisationUnit = "",
            organisationUnitCode = "",
            completedDate = null,
            dataValues = listOf(
                RuleDataValue(
                    LocalDate.currentDate(), "test_program_stage", "test_data_element", "test_value"
                )
            )
        )
        val ruleEffects = RuleEngine().evaluate(ruleEvent, getRuleEngineContext(listOf(rule, rule2)).copy(enrollment = enrollment))
        assertEquals("4", ruleEffects[0].data)
        assertEquals(sendMessageAction, ruleEffects[0].ruleAction)
    }

    private fun createRules(i: Int): List<Rule> {
        val rules: MutableList<Rule> = ArrayList()
        val assignAction: RuleAction = RuleActionAssign.create("#{test_calculated_value}", "2+2", null)
        val rule = Rule("true", listOf(assignAction), "test_program_rule1")
        val sendMessageAction: RuleAction =
            RuleActionSendMessage("test_notification", "4")
        val rule2 = Rule(
            "#{test_calculated_value}==4",
            listOf(sendMessageAction),
            "test_program_rule2"
        )
        for (j in 0 until i) {
            rules.add(rule)
            rules.add(rule2)
        }
        return rules
    }

    @Test
    fun sendMessageMustGetValueFromAssignActionInSingleExecution() {
        val assignAction: RuleAction = RuleActionAssign.create("#{test_calculated_value}", "2+2", null)
        val rule = Rule("true", listOf(assignAction), "test_program_rule1", "")
        val sendMessageAction: RuleAction =
            RuleActionSendMessage("test_notification", "4.0")
        val rule2 = Rule(
            "#{test_calculated_value}==4.0", listOf(sendMessageAction),
            "test_program_rule2", ""
        )
        val ruleEngineContext = getRuleEngineContext(listOf(rule, rule2))
        val enrollment = RuleEnrollment(
            "test_enrollment",
            "test_program",
            LocalDate.currentDate(),
            LocalDate.currentDate(),
            RuleEnrollment.Status.ACTIVE,
            "test_ou",
            "test_ou_code",
            listOf()
        )
        val ruleEvent = RuleEvent(
            event = "test_event",
            programStage = "test_program_stage",
            programStageName = "",
            status = RuleEvent.Status.ACTIVE,
            eventDate = LocalDate.currentDate(),
            dueDate = LocalDate.currentDate(),
            organisationUnit = "",
            organisationUnitCode = "",
            completedDate = null,
            dataValues = listOf(
                RuleDataValue(
                    LocalDate.currentDate(), "test_program_stage", "test_data_element", "test_value"
                )
            )
        )
        val ruleEffects = RuleEngine().evaluate(ruleEvent, ruleEngineContext.copy(enrollment = enrollment))
        assertEquals(1, ruleEffects.size)
        assertEquals("4", ruleEffects[0].data)
        assertEquals(sendMessageAction, ruleEffects[0].ruleAction)
    }

    private fun getRuleEngineContext(rules: List<Rule>): RuleEngineContext {
        val ruleVariable: RuleVariable = RuleVariableCalculatedValue("test_calculated_value", true, ArrayList(), "", RuleValueType.TEXT)
        return RuleEngineContext(rules, listOf(ruleVariable))
    }
}
