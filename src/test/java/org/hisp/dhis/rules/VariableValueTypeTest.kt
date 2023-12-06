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
class VariableValueTypeTest {
    @Test
    fun testNumericVariablesAreComparedCorrectly() {
        val ruleAction: RuleAction = RuleActionText
            .createForFeedback("test_action_content", "#{test_variable}")
        val rule: Rule = Rule("#{test_variable} > #{test_variable2}", java.util.List.of(ruleAction), "", "")
        val ruleVariable: RuleVariable = RuleVariableCurrentEvent
            .create("test_variable", "test_data_element", RuleValueType.NUMERIC, true, ArrayList())
        val ruleVariable2: RuleVariable = RuleVariableCurrentEvent
            .create("test_variable2", "test_data_element2", RuleValueType.NUMERIC, true, ArrayList())
        val ruleEngine = getRuleEngine(rule, java.util.List.of(ruleVariable, ruleVariable2))
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, Date(), Date(), null, "", null,
            java.util.List.of(
                RuleDataValue.create(Date(), "", "test_data_element", "30"),
                RuleDataValue.create(Date(), "", "test_data_element2", "4")
            )
        )
        val ruleEffects = ruleEngine.evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size)
        assertEquals("30", ruleEffects[0].data())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
    }

    @Test
    fun testTextVariablesAreComparedCorrectly() {
        val ruleAction: RuleAction = RuleActionText
            .createForFeedback("test_action_content", "#{test_variable}")
        val rule: Rule = Rule("#{test_variable} > #{test_variable2}", java.util.List.of(ruleAction), "", "")
        val ruleVariable: RuleVariable = RuleVariableCurrentEvent
            .create("test_variable", "test_data_element", RuleValueType.TEXT, true, ArrayList())
        val ruleVariable2: RuleVariable = RuleVariableCurrentEvent
            .create("test_variable2", "test_data_element2", RuleValueType.TEXT, true, ArrayList())
        val ruleEngine = getRuleEngine(rule, java.util.List.of(ruleVariable, ruleVariable2))
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, Date(), Date(), null, "", null,
            listOf(
                RuleDataValue.create(Date(), "", "test_data_element", "30"),
                RuleDataValue.create(Date(), "", "test_data_element2", "4")
            )
        )
        val ruleEffects = ruleEngine.evaluate(ruleEvent).call()
        assertEquals(0, ruleEffects.size)
    }

    private fun getRuleEngine(rule: Rule, ruleVariables: List<RuleVariable>): RuleEngine {
        return RuleEngineContext
            .builder()
            .rules(listOf(rule))
            .ruleVariables(ruleVariables)
            .supplementaryData(HashMap())
            .constantsValue(HashMap())
            .build().toEngineBuilder().triggerEnvironment(TriggerEnvironment.SERVER)
            .build()
    }
}
