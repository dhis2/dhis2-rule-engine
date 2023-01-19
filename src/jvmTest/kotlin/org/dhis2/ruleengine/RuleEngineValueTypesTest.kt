package org.dhis2.ruleengine

import org.assertj.core.api.Assertions.assertThat
import org.dhis2.ruleengine.RuleEngineTestUtils.currentDate
import org.dhis2.ruleengine.RuleEngineTestUtils.getRuleEngine
import org.dhis2.ruleengine.models.*
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class RuleEngineValueTypesTest {
    @Test
    @Throws(Exception::class)
    fun booleanVariableWithoutValueMustFallbackToDefaultBooleanValue() {
        val ruleAction: RuleAction = RuleAction.DisplayKeyValuePair(
            "test_action_content",
            DisplayLocation.LOCATION_FEEDBACK_WIDGET,
            "#{test_variable}"
        )
        val rule = Rule("", null, null, "true", listOf(ruleAction), "")
        val ruleVariable: RuleVariable =
            RuleVariable.RuleVariableCurrentEvent("test_variable", "test_data_element", RuleValueType.BOOLEAN)
        val ruleEngine = getRuleEngine(rule, listOf(ruleVariable))
        val ruleEvent = RuleEvent(
            "test_event",
            "test_program_stage",
            "",
            RuleEvent.Status.ACTIVE,
            currentDate(),
            currentDate(),
            null,
            "",
            null,
            ArrayList()
        )
        val ruleEffects: List<RuleEffect> = ruleEngine.evaluate(ruleEvent)
        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].data).isEqualTo("false")
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun numericVariableWithoutValueMustFallbackToDefaultNumericValue() {
        val ruleAction: RuleAction = RuleAction.DisplayKeyValuePair(
            "test_action_content",
            DisplayLocation.LOCATION_FEEDBACK_WIDGET,
            "#{test_variable}"
        )
        val rule = Rule("", null, null, "true", listOf(ruleAction), "")
        val ruleVariable: RuleVariable =
            RuleVariable.RuleVariableCurrentEvent("test_variable", "test_data_element", RuleValueType.NUMERIC)
        val ruleEngine = getRuleEngine(rule, listOf(ruleVariable))
        val ruleEvent = RuleEvent(
            "test_event",
            "test_program_stage",
            "",
            RuleEvent.Status.ACTIVE,
            currentDate(),
            currentDate(),
            null,
            "",
            null,
            ArrayList()
        )
        val ruleEffects: List<RuleEffect> = ruleEngine.evaluate(ruleEvent)
        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].data).isEqualTo("0.0")
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun textVariableWithoutValueMustFallbackToDefaultTextValue() {
        val ruleAction: RuleAction = RuleAction.DisplayKeyValuePair(
            "test_action_content",
            DisplayLocation.LOCATION_FEEDBACK_WIDGET,
            "#{test_variable}"
        )
        val rule = Rule("", null, null, "true", listOf(ruleAction), "")
        val ruleVariable =
            RuleVariable.RuleVariableCurrentEvent("test_variable", "test_data_element", RuleValueType.TEXT)
        val ruleEngine = getRuleEngine(rule, listOf<RuleVariable>(ruleVariable))
        val ruleEvent = RuleEvent(
            "test_event",
            "test_program_stage",
            "",
            RuleEvent.Status.ACTIVE,
            currentDate(),
            currentDate(),
            null,
            "",
            null,
            ArrayList()
        )
        val ruleEffects: List<RuleEffect> = ruleEngine.evaluate(ruleEvent)
        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].data).isEqualTo("")
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
    }
}