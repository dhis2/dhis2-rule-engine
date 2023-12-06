package org.hisp.dhis.rules

import org.hisp.dhis.rules.models.*
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class RuleEngineValueTypesTest {
    @Test
    fun booleanVariableWithoutValueMustFallbackToDefaultBooleanValue() {
        val ruleAction: RuleAction = RuleActionText
            .createForFeedback("test_action_content", "#{test_variable}")
        val rule: Rule = Rule("true", java.util.List.of(ruleAction), "", "")
        val ruleVariable: RuleVariable = RuleVariableCurrentEvent
            .create("test_variable", "test_data_element", RuleValueType.BOOLEAN, true, ArrayList())
        val ruleEngine = getRuleEngine(rule, java.util.List.of(ruleVariable))
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, Date(), Date(), null, "", null, ArrayList())
        val ruleEffects = ruleEngine.evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size)
        assertEquals("false", ruleEffects[0].data())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
    }

    @Test
    
    fun numericVariableWithoutValueMustFallbackToDefaultNumericValue() {
        val ruleAction: RuleAction = RuleActionText
            .createForFeedback("test_action_content", "#{test_variable}")
        val rule: Rule = Rule("true", java.util.List.of(ruleAction), "", "")
        val ruleVariable: RuleVariable = RuleVariableCurrentEvent
            .create("test_variable", "test_data_element", RuleValueType.NUMERIC, true, ArrayList())
        val ruleEngine = getRuleEngine(rule, java.util.List.of(ruleVariable))
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, Date(), Date(), null, "", null, ArrayList())
        val ruleEffects = ruleEngine.evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size)
        assertEquals("0", ruleEffects[0].data())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
    }

    @Test
    
    fun textVariableWithoutValueMustFallbackToDefaultTextValue() {
        val ruleAction: RuleAction = RuleActionText
            .createForFeedback("test_action_content", "#{test_variable}")
        val rule: Rule = Rule("true", java.util.List.of(ruleAction), "", "")
        val ruleVariable: RuleVariable = RuleVariableCurrentEvent
            .create("test_variable", "test_data_element", RuleValueType.TEXT, true, ArrayList())
        val ruleEngine = getRuleEngine(rule, java.util.List.of(ruleVariable))
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage","",
            RuleEvent.Status.ACTIVE, Date(), Date(), null, "", null, ArrayList()
        )
        val ruleEffects = ruleEngine.evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size)
        assertEquals("", ruleEffects[0].data())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
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
