package org.hisp.dhis.rules

import kotlinx.datetime.LocalDate
import org.hisp.dhis.rules.models.*
import kotlin.test.Test
import kotlin.test.assertEquals

class RuleEngineValueTypesTest {
    @Test
    fun booleanVariableWithoutValueMustFallbackToDefaultBooleanValue() {
        val ruleAction: org.hisp.dhis.rules.models.RuleAction = RuleActionText
            .createForFeedback(RuleActionText.Type.DISPLAYTEXT,"test_action_content", "#{test_variable}")
        val rule = Rule("true", listOf(ruleAction), "", "")
        val ruleVariable: RuleVariable = RuleVariableCurrentEvent("test_variable", true, ArrayList(), "test_data_element", RuleValueType.BOOLEAN)
        val ruleEngine = getRuleEngine(rule, listOf(ruleVariable))
        val ruleEvent = RuleEvent(
            "test_event",
            "test_program_stage",
            "",
            RuleEvent.Status.ACTIVE,
            LocalDate.currentDate(),
            LocalDate.currentDate(),
            null,
            "",
            null,
            ArrayList()
        )
        val ruleEffects = ruleEngine.evaluate(ruleEvent)
        assertEquals(1, ruleEffects.size)
        assertEquals("false", ruleEffects[0].data)
        assertEquals(ruleAction, ruleEffects[0].ruleAction)
    }

    @Test
    
    fun numericVariableWithoutValueMustFallbackToDefaultNumericValue() {
        val ruleAction: org.hisp.dhis.rules.models.RuleAction = RuleActionText
            .createForFeedback(RuleActionText.Type.DISPLAYTEXT,"test_action_content", "#{test_variable}")
        val rule = Rule("true", listOf(ruleAction), "", "")
        val ruleVariable: RuleVariable = RuleVariableCurrentEvent("test_variable", true, ArrayList(), "test_data_element", RuleValueType.NUMERIC)
        val ruleEngine = getRuleEngine(rule, listOf(ruleVariable))
        val ruleEvent = RuleEvent(
            "test_event",
            "test_program_stage",
            "",
            RuleEvent.Status.ACTIVE,
            LocalDate.currentDate(),
            LocalDate.currentDate(),
            null,
            "",
            null,
            ArrayList()
        )
        val ruleEffects = ruleEngine.evaluate(ruleEvent)
        assertEquals(1, ruleEffects.size)
        assertEquals("0", ruleEffects[0].data)
        assertEquals(ruleAction, ruleEffects[0].ruleAction)
    }

    @Test
    
    fun textVariableWithoutValueMustFallbackToDefaultTextValue() {
        val ruleAction: org.hisp.dhis.rules.models.RuleAction = RuleActionText
            .createForFeedback(RuleActionText.Type.DISPLAYTEXT,"test_action_content", "#{test_variable}")
        val rule = Rule("true", listOf(ruleAction), "", "")
        val ruleVariable: RuleVariable = RuleVariableCurrentEvent("test_variable", true, ArrayList(), "test_data_element", RuleValueType.TEXT)
        val ruleEngine = getRuleEngine(rule, listOf(ruleVariable))
        val ruleEvent = RuleEvent(
            "test_event",
            "test_program_stage",
            "",
            RuleEvent.Status.ACTIVE,
            LocalDate.currentDate(),
            LocalDate.currentDate(),
            null,
            "",
            null,
            ArrayList()
        )
        val ruleEffects = ruleEngine.evaluate(ruleEvent)
        assertEquals(1, ruleEffects.size)
        assertEquals("", ruleEffects[0].data)
        assertEquals(ruleAction, ruleEffects[0].ruleAction)
    }

    private fun getRuleEngine(rule: Rule, ruleVariables: List<RuleVariable>): RuleEngine {
        return RuleEngine(RuleEngineContext(listOf(rule), ruleVariables))
    }
}
