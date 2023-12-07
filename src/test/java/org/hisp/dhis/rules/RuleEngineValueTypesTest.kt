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
        val rule: Rule = Rule("true", listOf(ruleAction), "", "")
        val ruleVariable: RuleVariable = RuleVariableCurrentEvent("test_variable", true, ArrayList(), "test_data_element", RuleValueType.BOOLEAN)
        val ruleEngine = getRuleEngine(rule, listOf(ruleVariable))
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
        val rule: Rule = Rule("true", listOf(ruleAction), "", "")
        val ruleVariable: RuleVariable = RuleVariableCurrentEvent("test_variable", true, ArrayList(), "test_data_element", RuleValueType.NUMERIC)
        val ruleEngine = getRuleEngine(rule, listOf(ruleVariable))
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
        val rule: Rule = Rule("true", listOf(ruleAction), "", "")
        val ruleVariable: RuleVariable = RuleVariableCurrentEvent("test_variable", true, ArrayList(), "test_data_element", RuleValueType.TEXT)
        val ruleEngine = getRuleEngine(rule, listOf(ruleVariable))
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
        return RuleEngine(RuleEngineContext(listOf(rule), ruleVariables))
    }
}
