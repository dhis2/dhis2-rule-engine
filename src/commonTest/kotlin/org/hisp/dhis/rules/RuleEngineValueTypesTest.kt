package org.hisp.dhis.rules

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import org.hisp.dhis.rules.models.Rule
import org.hisp.dhis.rules.api.RuleEngineContext
import org.hisp.dhis.rules.engine.DefaultRuleEngine
import org.hisp.dhis.rules.models.*
import org.hisp.dhis.rules.utils.currentDate
import kotlin.test.Test
import kotlin.test.assertEquals

class RuleEngineValueTypesTest {
    @Test
    fun booleanVariableWithoutValueMustFallbackToDefaultBooleanValue() {
        val ruleAction: RuleAction = RuleActionText
            .createForFeedback(RuleActionText.Type.DISPLAYTEXT,"test_action_content", "#{test_variable}")
        val rule = Rule("true", listOf(ruleAction), "", "")
        val ruleVariable: RuleVariable = RuleVariableCurrentEvent("test_variable", true, ArrayList(), "test_data_element", RuleValueType.BOOLEAN)
        val ruleEngineContext = getRuleEngineContext(rule, listOf(ruleVariable))
        val ruleEvent = RuleEvent(
            "test_event",
            "test_program_stage",
            "",
            RuleEvent.Status.ACTIVE,
            Clock.System.now(),
            LocalDate.currentDate(),
            null,
            "",
            null,
            ArrayList()
        )
        val ruleEffects = DefaultRuleEngine().evaluate(ruleEvent, ruleEngineContext)
        assertEquals(1, ruleEffects.size)
        assertEquals("false", ruleEffects[0].data)
        assertEquals(ruleAction, ruleEffects[0].ruleAction)
    }

    @Test
    
    fun numericVariableWithoutValueMustFallbackToDefaultNumericValue() {
        val ruleAction: RuleAction = RuleActionText
            .createForFeedback(RuleActionText.Type.DISPLAYTEXT,"test_action_content", "#{test_variable}")
        val rule = Rule("true", listOf(ruleAction), "", "")
        val ruleVariable: RuleVariable = RuleVariableCurrentEvent("test_variable", true, ArrayList(), "test_data_element", RuleValueType.NUMERIC)
        val ruleEngineContext = getRuleEngineContext(rule, listOf(ruleVariable))
        val ruleEvent = RuleEvent(
            "test_event",
            "test_program_stage",
            "",
            RuleEvent.Status.ACTIVE,
            Clock.System.now(),
            LocalDate.currentDate(),
            null,
            "",
            null,
            ArrayList()
        )
        val ruleEffects = DefaultRuleEngine().evaluate(ruleEvent, ruleEngineContext)
        assertEquals(1, ruleEffects.size)
        assertEquals("0", ruleEffects[0].data)
        assertEquals(ruleAction, ruleEffects[0].ruleAction)
    }

    @Test
    
    fun textVariableWithoutValueMustFallbackToDefaultTextValue() {
        val ruleAction: RuleAction = RuleActionText
            .createForFeedback(RuleActionText.Type.DISPLAYTEXT,"test_action_content", "#{test_variable}")
        val rule = Rule("true", listOf(ruleAction), "", "")
        val ruleVariable: RuleVariable = RuleVariableCurrentEvent("test_variable", true, ArrayList(), "test_data_element", RuleValueType.TEXT)
        val ruleEngineContext = getRuleEngineContext(rule, listOf(ruleVariable))
        val ruleEvent = RuleEvent(
            "test_event",
            "test_program_stage",
            "",
            RuleEvent.Status.ACTIVE,
            Clock.System.now(),
            LocalDate.currentDate(),
            null,
            "",
            null,
            ArrayList()
        )
        val ruleEffects = DefaultRuleEngine().evaluate(ruleEvent, ruleEngineContext)
        assertEquals(1, ruleEffects.size)
        assertEquals("", ruleEffects[0].data)
        assertEquals(ruleAction, ruleEffects[0].ruleAction)
    }

    private fun getRuleEngineContext(rule: Rule, ruleVariables: List<RuleVariable>): RuleEngineContext {
        return RuleEngineContext(listOf(rule), ruleVariables)
    }
}
