package org.hisp.dhis.rules

import org.assertj.core.api.Assertions.assertThat
import org.hisp.dhis.rules.models.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*
import kotlin.collections.HashMap

// ToDo: function tests (check that function invocations are producing expected values; check nested function invocation)
// ToDo: various source type tests (referencing variables from different events)
@RunWith(JUnit4::class)
class RuleEngineEffectTypesTests {

    @Test
    @Throws(Exception::class)
    fun simpleConditionMustResultInAssignEffect() {
        val ruleAction = RuleActionAssign.create(null, "\'test_string\'", "test_data_element")
        val rule = Rule.create(null, null, "true", listOf(ruleAction), "")

        val ruleEngine = getRuleEngine(rule)


        val ruleEffects = ruleEngine.evaluate(getTestRuleEvent(RuleEvent.Status.ACTIVE)).call()

        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].data).isEqualTo("test_string")
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun simpleConditionMustResultInCreateEventEffect() {
        val ruleAction = RuleActionCreateEvent.create(
                "test_action_content", "'event_uid;test_data_value_one'", "test_program_stage")
        val rule = Rule.create(null, null, "true", listOf(ruleAction), "")

        val ruleEngine = getRuleEngine(rule)


        val ruleEffects = ruleEngine.evaluate(getTestRuleEvent(RuleEvent.Status.ACTIVE)).call()

        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].data).isEqualTo("event_uid;test_data_value_one")
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun simpleConditionMustResultInDisplayKeyValuePairEffect() {
        val ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
                "test_action_content", "2 + 2")
        val rule = Rule.create(null, null, "true", listOf(ruleAction), "")

        val ruleEngine = getRuleEngine(rule)


        val ruleEffects = ruleEngine.evaluate(getTestRuleEvent(RuleEvent.Status.ACTIVE)).call()

        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].data).isEqualTo("4")
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun simpleConditionMustResultInDisplayTextEffect() {
        val ruleAction = RuleActionDisplayText.createForFeedback(
                "test_action_content", "2 + 2")
        val rule = Rule.create(null, null, "true", listOf(ruleAction), "")

        val ruleEngine = getRuleEngine(rule)


        val ruleEffects = ruleEngine.evaluate(getTestRuleEvent(RuleEvent.Status.ACTIVE)).call()

        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].data).isEqualTo("4")
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun simpleConditionMustResultInErrorOnCompletionEffect() {
        val ruleAction = RuleActionErrorOnCompletion.create(
                "test_action_content", "2 + 2", "test_data_element")
        val rule = Rule.create(null, null, "true", listOf(ruleAction), "")

        val ruleEngine = getRuleEngine(rule)


        val ruleEffects = ruleEngine.evaluate(getTestRuleEvent(RuleEvent.Status.ACTIVE)).call()

        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].data).isEqualTo("4")
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun simpleConditionMustResultInHideFieldEffect() {
        val ruleAction = RuleActionHideField.create(
                "test_action_content", "test_data_element")
        val rule = Rule.create(null, null, "true", listOf(ruleAction), "")
        val rule2 = Rule.create(null, null, "!d2:", listOf(ruleAction), "")

        val ruleEngine = RuleEngineContext
                .builder(ExpressionEvaluator())
                .rules(listOf(rule, rule2))
                .calculatedValueMap(HashMap())
                .supplementaryData(HashMap())
                .constantsValue(HashMap())
                .build().toEngineBuilder().triggerEnvironment(TriggerEnvironment.SERVER)
                .build()


        val ruleEffects = ruleEngine.evaluate(getTestRuleEvent(RuleEvent.Status.ACTIVE)).call()

        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].data).isEqualTo("")
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun testEnvironmentVariableExpression() {
        val ruleAction = RuleActionHideField.create(
                "test_action_content", "test_data_element")
        val rule = Rule.create(null, null, "V{event_status} =='COMPLETED'", listOf(ruleAction), "")

        val ruleEngine = getRuleEngine(rule)

        val ruleEffects = ruleEngine.evaluate(getTestRuleEvent(RuleEvent.Status.COMPLETED)).call()

        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].data).isEqualTo("")
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun testTriggerEnvironment() {
        val ruleAction = RuleActionHideField.create(
                "test_action_content", "test_data_element")
        val rule = Rule.create(null, null, "V{environment} =='Server'", listOf(ruleAction), "")

        val ruleEngine = getRuleEngine(rule)


        val ruleEffects = ruleEngine.evaluate(getTestRuleEvent(RuleEvent.Status.ACTIVE)).call()

        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].data).isEqualTo("")
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun simpleConditionMustResultInHideProgramStageEffect() {
        val ruleAction = RuleActionHideProgramStage.create("test_program_stage")
        val rule = Rule.create(null, null, "true", listOf(ruleAction), "")

        val ruleEngine = getRuleEngine(rule)


        val ruleEffects = ruleEngine.evaluate(getTestRuleEvent(RuleEvent.Status.ACTIVE)).call()

        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].data).isEqualTo("")
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun simpleConditionMustResultInScheduleMessage() {
        val ruleAction = RuleActionScheduleMessage.create("", "'2018-04-24'")
        val rule = Rule.create(null, null, "true", listOf(ruleAction), "")

        val ruleEngine = getRuleEngine(rule)


        val ruleEffects = ruleEngine.evaluate(getTestRuleEvent(RuleEvent.Status.ACTIVE)).call()

        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].ruleAction is RuleActionScheduleMessage)
        assertThat(ruleEffects[0].data).isEqualTo("2018-04-24")
    }

    @Test
    @Throws(Exception::class)
    fun simpleConditionMustResultInHideSectionEffect() {
        val ruleAction = RuleActionHideSection.create("test_section")
        val rule = Rule.create(null, null, "true", listOf(ruleAction), "")

        val ruleEngine = getRuleEngine(rule)


        val ruleEffects = ruleEngine.evaluate(getTestRuleEvent(RuleEvent.Status.ACTIVE)).call()

        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].data).isEqualTo("")
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun simpleConditionMustResultInHideOptionEffect() {
        val ruleAction = RuleActionHideOption.create("test_content", "test_option", "test_field")
        val rule = Rule.create(null, null, "true", listOf(ruleAction), "")

        val ruleEngine = getRuleEngine(rule)


        val ruleEffects = ruleEngine.evaluate(getTestRuleEvent(RuleEvent.Status.ACTIVE)).call()

        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].data).isEqualTo("")
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun simpleConditionMustResultInHideOptionGroupEffect() {
        val ruleAction = RuleActionHideOptionGroup.create("test_content", "test_option_group")
        val rule = Rule.create(null, null, "true", listOf(ruleAction), "")

        val ruleEngine = getRuleEngine(rule)


        val ruleEffects = ruleEngine.evaluate(getTestRuleEvent(RuleEvent.Status.ACTIVE)).call()

        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].data).isEqualTo("")
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun simpleConditionMustResultInSetMandatoryFieldEffect() {
        val ruleAction = RuleActionSetMandatoryField.create("test_data_element")
        val rule = Rule.create(null, null, "true", listOf(ruleAction), "")

        val ruleEngine = getRuleEngine(rule)


        val ruleEffects = ruleEngine.evaluate(getTestRuleEvent(RuleEvent.Status.ACTIVE)).call()

        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].data).isEqualTo("")
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun simpleConditionMustResultInWarningEffect() {
        val ruleAction = RuleActionShowWarning.create(
                "test_warning_message", null, "target_field")
        val rule = Rule.create(null, null, "true", listOf(ruleAction), "")

        val ruleEngine = getRuleEngine(rule)


        val ruleEffects = ruleEngine.evaluate(getTestRuleEvent(RuleEvent.Status.ACTIVE)).call()

        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].data).isEqualTo("")
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun simpleConditionMustResultInErrorEffect() {
        val ruleAction = RuleActionShowError.create(
                "test_error_message", "2 + 2", "target_field")
        val rule = Rule.create(null, null, "true", listOf(ruleAction), "")

        val ruleEngine = getRuleEngine(rule)


        val ruleEffects = ruleEngine.evaluate(getTestRuleEvent(RuleEvent.Status.ACTIVE)).call()

        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].data).isEqualTo("4")
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun simpleConditionMustResultInOnCompletionWarningEffect() {
        val ruleAction = RuleActionWarningOnCompletion.create(
                "test_warning_message", "2 + 2", "target_field")
        val rule = Rule.create(null, null, "true", listOf(ruleAction), "")

        val ruleEngine = getRuleEngine(rule)


        val ruleEffects = ruleEngine.evaluate(getTestRuleEvent(RuleEvent.Status.ACTIVE)).call()

        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].data).isEqualTo("4")
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
    }


    private fun getTestRuleEvent(status: RuleEvent.Status): RuleEvent {
        return RuleEvent.builder()
                .event("test_event")
                .programStage("test_program_stage")
                .programStageName("")
                .status(status)
                .eventDate(Date())
                .dueDate(Date())
                .organisationUnit("")
                .organisationUnitCode("")
                .dataValues(listOf(RuleDataValue.create(
                        Date(), "test_program_stage", "test_data_element", "test_value")))
                .build()
    }

    private fun getRuleEngine(rule: Rule): RuleEngine {
        return RuleEngineContext
                .builder(ExpressionEvaluator())
                .rules(listOf(rule))
                .calculatedValueMap(hashMapOf())
                .supplementaryData(hashMapOf())
                .constantsValue(hashMapOf())
                .build().toEngineBuilder().triggerEnvironment(TriggerEnvironment.SERVER)
                .build()
    }
}
