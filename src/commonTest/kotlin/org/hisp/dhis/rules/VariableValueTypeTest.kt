package org.hisp.dhis.rules

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import org.hisp.dhis.rules.models.Rule
import org.hisp.dhis.rules.api.RuleEngineContext
import org.hisp.dhis.rules.engine.DefaultRuleEngine
import org.hisp.dhis.rules.models.*
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
class VariableValueTypeTest {
    @Test
    fun testNumericVariablesAreComparedCorrectly() {
        val ruleAction: RuleAction = RuleActionText
            .createForFeedback(RuleActionText.Type.DISPLAYTEXT,"test_action_content", "#{test_variable}")
        val rule = Rule("#{test_variable} > #{test_variable2}", listOf(ruleAction), "", "")
        val ruleVariable: RuleVariable = RuleVariableCurrentEvent("test_variable", true, ArrayList(), "test_data_element", RuleValueType.NUMERIC)
        val ruleVariable2: RuleVariable = RuleVariableCurrentEvent("test_variable2", true, ArrayList(), "test_data_element2", RuleValueType.NUMERIC)
        val ruleEngineContext = getRuleEngineContext(rule, listOf(ruleVariable, ruleVariable2))
        val now = Clock.System.now()
        val ruleEvent = RuleEvent(
            "test_event",
            "test_program_stage",
            "",
            RuleEvent.Status.ACTIVE,
            now,
            LocalDate.fromEpochDays(1),
            null,
            "",
            null,
            listOf(
                RuleDataValue(now, "", "test_data_element", "30"),
                RuleDataValue(now, "", "test_data_element2", "4")
            )
        )
        val ruleEffects = DefaultRuleEngine().evaluate(ruleEvent, ruleEngineContext)
        assertEquals(1, ruleEffects.size)
        assertEquals("30", ruleEffects[0].data)
        assertEquals(ruleAction, ruleEffects[0].ruleAction)
    }

    @Test
    fun testTextVariablesAreComparedCorrectly() {
        val ruleAction: RuleAction = RuleActionText
            .createForFeedback(RuleActionText.Type.DISPLAYTEXT,"test_action_content", "#{test_variable}")
        val rule = Rule("#{test_variable} > #{test_variable2}", listOf(ruleAction), "", "")
        val ruleVariable: RuleVariable = RuleVariableCurrentEvent("test_variable", true, ArrayList(), "test_data_element", RuleValueType.TEXT)
        val ruleVariable2: RuleVariable = RuleVariableCurrentEvent("test_variable2", true, ArrayList(), "test_data_element2", RuleValueType.TEXT)
        val ruleEngineContext = getRuleEngineContext(rule, listOf(ruleVariable, ruleVariable2))
        val now = Clock.System.now()
        val ruleEvent = RuleEvent(
            "test_event",
            "test_program_stage",
            "",
            RuleEvent.Status.ACTIVE,
            now,
            LocalDate.fromEpochDays(1),
            null,
            "",
            null,
            listOf(
                RuleDataValue(now, "", "test_data_element", "30"),
                RuleDataValue(now, "", "test_data_element2", "4")
            )
        )
        val ruleEffects = DefaultRuleEngine().evaluate(ruleEvent, ruleEngineContext)
        assertEquals(0, ruleEffects.size)
    }

    private fun getRuleEngineContext(rule: Rule, ruleVariables: List<RuleVariable>): RuleEngineContext {
        return RuleEngineContext(rules = listOf(rule), ruleVariables = ruleVariables)
    }
}
