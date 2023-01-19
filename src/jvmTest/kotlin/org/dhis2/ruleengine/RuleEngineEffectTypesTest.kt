package org.dhis2.ruleengine

import org.assertj.core.api.Assertions.assertThat
import org.dhis2.ruleengine.RuleEngineTestUtils.currentDate
import org.dhis2.ruleengine.RuleEngineTestUtils.getRuleEngine
import org.dhis2.ruleengine.models.*
import org.junit.jupiter.api.Test

// ToDo: function tests (check that function invocations are producing expected values; check nested function invocation)
// ToDo: various source type tests (referencing variables from different events)
class RuleEngineEffectTypesTest {
    private fun getTestRuleEvent(status: RuleEvent.Status): RuleEvent {
        return RuleEvent(
            "test_event",
            "test_program_stage",
            "",
            status,
            currentDate(),
            currentDate(),
            null,
            "",
            "",
            listOf(
                RuleDataValue(
                    currentDate(), "test_program_stage", "test_data_element", "test_value"
                )
            )
        )
    }

    @Test
    @Throws(Exception::class)
    fun simpleConditionMustResultInAssignEffect() {
        val ruleAction: RuleAction = RuleAction.Assign(
            "#{test_data_element}", "'test_string'", ""
        )
        val rule: Rule = Rule("", null, null, "true", listOf(ruleAction), "")
        val ruleEngine = getRuleEngine(rule)
        val ruleEffects: List<RuleEffect> = ruleEngine.evaluate(getTestRuleEvent(RuleEvent.Status.ACTIVE))
        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].data).isEqualTo("test_string")
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun simpleConditionMustResultInAssignEffectMultipleExecution() {
        val ruleAction: RuleAction = RuleAction.Assign(
            "#{test_data_element}", "'test_string'", ""
        )
        val rule = Rule("", null, null, "true", listOf(ruleAction), "")
        val ruleEngine = getRuleEngineMultiple(rule, getTestRuleEvent(RuleEvent.Status.ACTIVE))
        val ruleEffects:List<RuleEffects> = ruleEngine.evaluate()
        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].ruleEffects[0].data).isEqualTo("test_string")
        assertThat(ruleEffects[0].ruleEffects[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun simpleConditionMustResultInCreateEventEffect() {
        val ruleAction: RuleAction = RuleAction.CreateEvent(
            "test_action_content", "test_program_stage","'event_uid;test_data_value_one'"
        )
        val rule = Rule("", null, null, "true", listOf(ruleAction), "")
        val ruleEngine = getRuleEngine(rule)
        val ruleEffects: List<RuleEffect> = ruleEngine.evaluate(getTestRuleEvent(RuleEvent.Status.ACTIVE))
        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].data).isEqualTo("event_uid;test_data_value_one")
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun simpleConditionMustResultInDisplayKeyValuePairEffect() {
        val ruleAction: RuleAction = RuleAction.DisplayKeyValuePair(
            "test_action_content", DisplayLocation.LOCATION_FEEDBACK_WIDGET, "2 + 2"
        )
        val rule: Rule = Rule(null, null, null, "true", listOf(ruleAction), "")
        val ruleEngine = getRuleEngine(rule)
        val ruleEffects: List<RuleEffect> = ruleEngine.evaluate(getTestRuleEvent(RuleEvent.Status.ACTIVE))
        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].data).isEqualTo("4")
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun simpleConditionMustResultInDisplayTextEffect() {
        val ruleAction = RuleAction.DisplayText(
            "test_action_content", DisplayLocation.LOCATION_FEEDBACK_WIDGET, "2 + 2"
        )
        val rule = Rule(null, null, null, "true", listOf(ruleAction), "")
        val ruleEngine = getRuleEngine(rule)
        val ruleEffects: List<RuleEffect> = ruleEngine.evaluate(getTestRuleEvent(RuleEvent.Status.ACTIVE))
        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].data).isEqualTo("4")
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun simpleConditionMustResultInErrorOnCompletionEffect() {
        val ruleAction: RuleAction = RuleAction.ErrorOnCompletion(
            AttributeType.UNKNOWN, "test_action_content", "test_data_element", "2 + 2"
        )
        val rule: Rule = Rule(null, null, null, "true", listOf(ruleAction), "")
        val ruleEngine = getRuleEngine(rule)
        val ruleEffects: List<RuleEffect> = ruleEngine.evaluate(getTestRuleEvent(RuleEvent.Status.ACTIVE))
        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].data).isEqualTo("4")
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun simpleConditionMustResultInHideFieldEffect() {
        val ruleAction: RuleAction = RuleAction.HideField(
            AttributeType.UNKNOWN, "test_action_content", "test_data_element", ""
        )
        val rule: Rule = Rule(null, null, null, "true", listOf(ruleAction), "")
        val ruleEngine: RuleEngine = getRuleEngine(rule)
        val ruleEffects: List<RuleEffect> = ruleEngine.evaluate(getTestRuleEvent(RuleEvent.Status.ACTIVE))
        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].data).isEqualTo("")
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun testEnvironmentVariableExpression() {
        val ruleAction: RuleAction = RuleAction.HideField(
            AttributeType.UNKNOWN, "test_action_content", "test_data_element", ""
        )
        val rule: Rule = Rule(null, null, null, "V{event_status} =='COMPLETED'", listOf(ruleAction), "")
        val ruleEngine = getRuleEngine(rule)
        val ruleEffects: List<RuleEffect> = ruleEngine.evaluate(getTestRuleEvent(RuleEvent.Status.COMPLETED))
        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].data).isEqualTo("")
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun testTriggerEnvironment() {
        val ruleAction: RuleAction = RuleAction.HideField(
            AttributeType.UNKNOWN, "test_action_content", "test_data_element", ""
        )
        val rule = Rule(null, null, null, "V{environment} =='JVM'", listOf(ruleAction), "")
        val ruleEngine = getRuleEngine(rule)
        val ruleEffects: List<RuleEffect> = ruleEngine.evaluate(getTestRuleEvent(RuleEvent.Status.ACTIVE))
        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].data).isEqualTo("")
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun simpleConditionMustResultInHideProgramStageEffect() {
        val ruleAction: RuleAction = RuleAction.HideProgramStage("test_program_stage", "")
        val rule: Rule = Rule(null, null, null, "true", listOf(ruleAction), "")
        val ruleEngine = getRuleEngine(rule)
        val ruleEffects: List<RuleEffect> = ruleEngine.evaluate(getTestRuleEvent(RuleEvent.Status.ACTIVE))
        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].data).isEqualTo("")
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun simpleConditionMustResultInScheduleMessage() {
        val ruleAction: RuleAction = RuleAction.ScheduleMessage("", "'2018-04-24'")
        val rule: Rule = Rule(null, null, null, "true", listOf(ruleAction), "")
        val ruleEngine = getRuleEngine(rule)
        val ruleEffects: List<RuleEffect> = ruleEngine.evaluate(getTestRuleEvent(RuleEvent.Status.ACTIVE))
        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].ruleAction is RuleAction.ScheduleMessage)
        assertThat(ruleEffects[0].data).isEqualTo("2018-04-24")
    }

    @Test
    @Throws(Exception::class)
    fun simpleConditionMustResultInHideSectionEffect() {
        val ruleAction: RuleAction = RuleAction.HideSection("test_section", "")
        val rule: Rule = Rule(null, null, null, "true", listOf(ruleAction), "")
        val ruleEngine = getRuleEngine(rule)
        val ruleEffects: List<RuleEffect> = ruleEngine.evaluate(getTestRuleEvent(RuleEvent.Status.ACTIVE))
        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].data).isEqualTo("")
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun simpleConditionMustResultInHideOptionEffect() {
        val ruleAction: RuleAction =
            RuleAction.HideOption(AttributeType.UNKNOWN, "test_content", "test_option", "test_field", "")
        val rule: Rule = Rule(null, null, null, "true", listOf(ruleAction), "")
        val ruleEngine = getRuleEngine(rule)
        val ruleEffects: List<RuleEffect> = ruleEngine.evaluate(getTestRuleEvent(RuleEvent.Status.ACTIVE))
        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].data).isEqualTo("")
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun simpleConditionMustResultInHideOptionGroupEffect() {
        val ruleAction: RuleAction =
            RuleAction.HideOptionGroup(AttributeType.UNKNOWN, "test_content", "test_option_group", "field", "")
        val rule: Rule = Rule(null, null, null, "true", listOf(ruleAction), "")
        val ruleEngine = getRuleEngine(rule)
        val ruleEffects: List<RuleEffect> = ruleEngine.evaluate(getTestRuleEvent(RuleEvent.Status.ACTIVE))
        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].data).isEqualTo("")
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun simpleConditionMustResultInSetMandatoryFieldEffect() {
        val ruleAction: RuleAction = RuleAction.SetMandatory(AttributeType.UNKNOWN, "test_data_element", "")
        val rule: Rule = Rule(null, null, null, "true", listOf(ruleAction), "")
        val ruleEngine = getRuleEngine(rule)
        val ruleEffects: List<RuleEffect> = ruleEngine.evaluate(getTestRuleEvent(RuleEvent.Status.ACTIVE))
        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].data).isEqualTo("")
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun simpleConditionMustResultInWarningEffect() {
        val ruleAction: RuleAction = RuleAction.ShowWarning(
            AttributeType.UNKNOWN, "test_warning_message",  "target_field", ""
        )
        val rule: Rule = Rule(null, null, null, "true", listOf(ruleAction), "")
        val ruleEngine = getRuleEngine(rule)
        val ruleEffects: List<RuleEffect> = ruleEngine.evaluate(getTestRuleEvent(RuleEvent.Status.ACTIVE))
        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].data).isEqualTo("")
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun simpleConditionMustResultInErrorEffect() {
        val ruleAction: RuleAction = RuleAction.ShowError(
            AttributeType.UNKNOWN, "test_error_message", "target_field", "2 + 2"
        )
        val rule: Rule = Rule(null, null, null, "true", listOf(ruleAction), "")
        val ruleEngine = getRuleEngine(rule)
        val ruleEffects: List<RuleEffect> = ruleEngine.evaluate(getTestRuleEvent(RuleEvent.Status.ACTIVE))
        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].data).isEqualTo("4")
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun simpleConditionMustResultInOnCompletionWarningEffect() {
        val ruleAction: RuleAction = RuleAction.WarningOnCompletion(
            AttributeType.UNKNOWN, "test_warning_message", "target_field", "2 + 2"
        )
        val rule: Rule = Rule(null, null, null, "true", listOf(ruleAction), "")
        val ruleEngine = getRuleEngine(rule)
        val ruleEffects: List<RuleEffect> = ruleEngine.evaluate(getTestRuleEvent(RuleEvent.Status.ACTIVE))
        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].data).isEqualTo("4")
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
    }

    private fun getRuleEngine(rule: Rule): RuleEngine {
        return getRuleEngine(
            rule = rule,
            ruleVariables = emptyList()
        )
    }

    private fun getRuleEngineMultiple(rule: Rule, ruleEvent: RuleEvent): RuleEngine {
        return RuleEngineContext(
            rules = listOf(rule),
            ruleVariables = emptyList(),
            supplementaryData = emptyMap(),
            constantsValues = emptyMap(),
            dataItemStore = emptyMap()
        ).toRuleEngine(listOf(ruleEvent), null)
    }
}