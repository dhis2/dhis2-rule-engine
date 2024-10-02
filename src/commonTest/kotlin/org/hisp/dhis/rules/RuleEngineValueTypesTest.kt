package org.hisp.dhis.rules

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import org.hisp.dhis.rules.api.RuleEngine
import org.hisp.dhis.rules.api.RuleEngineContext
import org.hisp.dhis.rules.models.*
import org.hisp.dhis.rules.utils.currentDate
import kotlin.test.Test
import kotlin.test.assertEquals

class RuleEngineValueTypesTest {
    @Test
    fun booleanVariableWithoutValueMustFallbackToDefaultBooleanValue() {
        val ruleAction = RuleAction("#{test_variable}", "DISPLAYTEXT", mapOf(Pair("content", "test_action_content"), Pair("location", "feedback")))
        val rule = Rule("true", listOf(ruleAction), "", "")
        val ruleVariable: RuleVariable = RuleVariableCurrentEvent("test_variable", true, ArrayList(), "test_data_element", RuleValueType.BOOLEAN)
        val ruleEngineContext = getRuleEngineContext(rule, listOf(ruleVariable))
        val ruleEvent = RuleEvent(
            "test_event",
            "test_program_stage",
            "",
            RuleEventStatus.ACTIVE,
            Clock.System.now(),
            Clock.System.now(),
            LocalDate.currentDate(),
            null,
            "",
            null,
            ArrayList()
        )
        val ruleEffects = RuleEngine.getInstance().evaluate(ruleEvent, null, emptyList(), ruleEngineContext)
        assertEquals(1, ruleEffects.size)
        assertEquals("false", ruleEffects[0].data)
        assertEquals(ruleAction, ruleEffects[0].ruleAction)
    }

    @Test
    
    fun numericVariableWithoutValueMustFallbackToDefaultNumericValue() {
        val ruleAction = RuleAction("#{test_variable}", "DISPLAYTEXT", mapOf(Pair("content", "test_action_content"), Pair("location", "feedback")))
        val rule = Rule("true", listOf(ruleAction), "", "")
        val ruleVariable: RuleVariable = RuleVariableCurrentEvent("test_variable", true, ArrayList(), "test_data_element", RuleValueType.NUMERIC)
        val ruleEngineContext = getRuleEngineContext(rule, listOf(ruleVariable))
        val ruleEvent = RuleEvent(
            "test_event",
            "test_program_stage",
            "",
            RuleEventStatus.ACTIVE,
            Clock.System.now(),
            Clock.System.now(),
            LocalDate.currentDate(),
            null,
            "",
            null,
            ArrayList()
        )
        val ruleEffects = RuleEngine.getInstance().evaluate(ruleEvent, null, emptyList(), ruleEngineContext)
        assertEquals(1, ruleEffects.size)
        assertEquals("0", ruleEffects[0].data)
        assertEquals(ruleAction, ruleEffects[0].ruleAction)
    }

    @Test
    
    fun textVariableWithoutValueMustFallbackToDefaultTextValue() {
        val ruleAction = RuleAction("#{test_variable}", "DISPLAYTEXT", mapOf(Pair("content", "test_action_content"), Pair("location", "feedback")))
        val rule = Rule("true", listOf(ruleAction), "", "")
        val ruleVariable: RuleVariable = RuleVariableCurrentEvent("test_variable", true, ArrayList(), "test_data_element", RuleValueType.TEXT)
        val ruleEngineContext = getRuleEngineContext(rule, listOf(ruleVariable))
        val ruleEvent = RuleEvent(
            "test_event",
            "test_program_stage",
            "",
            RuleEventStatus.ACTIVE,
            Clock.System.now(),
            Clock.System.now(),
            LocalDate.currentDate(),
            null,
            "",
            null,
            ArrayList()
        )
        val ruleEffects = RuleEngine.getInstance().evaluate(ruleEvent, null, emptyList(), ruleEngineContext)
        assertEquals(1, ruleEffects.size)
        assertEquals("", ruleEffects[0].data)
        assertEquals(ruleAction, ruleEffects[0].ruleAction)
    }

    private fun getRuleEngineContext(rule: Rule, ruleVariables: List<RuleVariable>): RuleEngineContext {
        return RuleEngineContext(listOf(rule), ruleVariables)
    }
}
