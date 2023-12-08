package org.hisp.dhis.rules

import org.hisp.dhis.rules.models.*
import java.util.Date
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

// ToDo: function tests (check that function invocations are producing expected values; check nested function invocation)
// ToDo: various source type tests (referencing variables from different events)
class RuleEngineEffectTypesTest {
    private fun getTestRuleEvent(status: RuleEvent.Status): RuleEvent {
        return RuleEvent(event = "test_event", programStage = "test_program_stage",
            programStageName = "", status = status, eventDate = Date(), dueDate = Date(),
            organisationUnit = "", organisationUnitCode = "", completedDate = null,
            dataValues = listOf(
                    RuleDataValue(
                        Date(), "test_program_stage", "test_data_element", "test_value"
                    )
                )
            )
    }

    @Test
    fun simpleConditionMustResultInAssignEffect() {
        val ruleAction: RuleAction = RuleActionAssign.create(
            null, "'test_string'", "#{test_data_element}"
        )
        val rule = Rule("true", listOf(ruleAction))
        val ruleEngine = getRuleEngine(rule)
        val ruleEffects = ruleEngine.evaluate(getTestRuleEvent(RuleEvent.Status.ACTIVE)).call()
        assertEquals(1, ruleEffects.size)
        assertEquals("test_string", ruleEffects[0].data)
        assertEquals(ruleAction, ruleEffects[0].ruleAction)
    }

    @Test
    fun simpleConditionMustResultInAssignEffectMultipleExecution() {
        val ruleAction: RuleAction = RuleActionAssign.create(
            null, "'test_string'", "#{test_data_element}"
        )
        val rule = Rule("true", listOf(ruleAction))
        val ruleEngine = getRuleEngineMultiple(rule, getTestRuleEvent(RuleEvent.Status.ACTIVE))
        val ruleEffects = ruleEngine.evaluate()
        assertEquals(1, ruleEffects.size)
        assertEquals("test_string", ruleEffects[0].ruleEffects[0].data)
        assertEquals(ruleAction, ruleEffects[0].ruleEffects[0].ruleAction)
    }

    @Test
    fun simpleConditionMustResultInCreateEventEffect() {
        val ruleAction: RuleAction = RuleActionCreateEvent(
            "test_program_stage",
            "test_action_content", "'event_uid;test_data_value_one'"
        )
        val rule = Rule("true", listOf(ruleAction))
        val ruleEngine = getRuleEngine(rule)
        val ruleEffects = ruleEngine.evaluate(getTestRuleEvent(RuleEvent.Status.ACTIVE)).call()
        assertEquals(1, ruleEffects.size)
        assertEquals("event_uid;test_data_value_one", ruleEffects[0].data)
        assertEquals(ruleAction, ruleEffects[0].ruleAction)
    }

    @Test
    fun simpleConditionMustResultInDisplayKeyValuePairEffect() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "2 + 2"
        )
        val rule = Rule("true", listOf(ruleAction))
        val ruleEngine = getRuleEngine(rule)
        val ruleEffects = ruleEngine.evaluate(getTestRuleEvent(RuleEvent.Status.ACTIVE)).call()
        assertEquals(1, ruleEffects.size)
        assertEquals("4", ruleEffects[0].data)
        assertEquals(ruleAction, ruleEffects[0].ruleAction)
    }

    @Test
    fun simpleConditionMustResultInDisplayTextEffect() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "2 + 2"
        )
        val rule = Rule("true", listOf(ruleAction))
        val ruleEngine = getRuleEngine(rule)
        val ruleEffects = ruleEngine.evaluate(getTestRuleEvent(RuleEvent.Status.ACTIVE)).call()
        assertEquals(1, ruleEffects.size)
        assertEquals("4", ruleEffects[0].data)
        assertEquals(ruleAction, ruleEffects[0].ruleAction)
    }

    @Test
    fun simpleConditionMustResultInErrorOnCompletionEffect() {
        val ruleAction: RuleAction = RuleActionMessage.create(
            "test_action_content", "2 + 2", "test_data_element", null, RuleActionMessage.Type.ERROR_ON_COMPILATION
        )
        val rule = Rule("true", listOf(ruleAction))
        val ruleEngine = getRuleEngine(rule)
        val ruleEffects = ruleEngine.evaluate(getTestRuleEvent(RuleEvent.Status.ACTIVE)).call()
        assertEquals(1, ruleEffects.size)
        assertEquals("4", ruleEffects[0].data)
        assertEquals(ruleAction, ruleEffects[0].ruleAction)
    }

    @Test
    fun simpleConditionMustResultInHideFieldEffect() {
        val ruleAction: RuleAction = RuleActionHideField("test_data_element", "test_action_content" )
        val rule = Rule("true", listOf(ruleAction))
        val ruleEngine = RuleEngine(RuleEngineContext(listOf(rule), emptyList()), emptyList(), null, TriggerEnvironment.SERVER)
        val ruleEffects = ruleEngine.evaluate(getTestRuleEvent(RuleEvent.Status.ACTIVE)).call()
        assertEquals(1, ruleEffects.size)
        assertEquals("", ruleEffects[0].data)
        assertEquals(ruleAction, ruleEffects[0].ruleAction)
    }

    @Test
    fun testEnvironmentVariableExpression() {
        val ruleAction: RuleAction = RuleActionHideField("test_data_element", "test_action_content")
        val rule = Rule("V{event_status} =='COMPLETED'", listOf(ruleAction))
        val ruleEngine = getRuleEngine(rule)
        val ruleEffects = ruleEngine.evaluate(getTestRuleEvent(RuleEvent.Status.COMPLETED)).call()
        assertEquals(1, ruleEffects.size)
        assertEquals("", ruleEffects[0].data)
        assertEquals(ruleAction, ruleEffects[0].ruleAction)
    }

    @Test
    fun testTriggerEnvironment() {
        val ruleAction: RuleAction = RuleActionHideField("test_data_element", "test_action_content")
        val rule = Rule("V{environment} =='Server'", listOf(ruleAction))
        val ruleEngine = getRuleEngine(rule)
        val ruleEffects = ruleEngine.evaluate(getTestRuleEvent(RuleEvent.Status.ACTIVE)).call()
        assertEquals(1, ruleEffects.size)
        assertEquals("", ruleEffects[0].data)
        assertEquals(ruleAction, ruleEffects[0].ruleAction)
    }

    @Test
    fun simpleConditionMustResultInHideProgramStageEffect() {
        val ruleAction: RuleAction = RuleActionHideProgramStage("test_program_stage")
        val rule = Rule("true", listOf(ruleAction))
        val ruleEngine = getRuleEngine(rule)
        val ruleEffects = ruleEngine.evaluate(getTestRuleEvent(RuleEvent.Status.ACTIVE)).call()
        assertEquals(1, ruleEffects.size)
        assertEquals("", ruleEffects[0].data)
        assertEquals(ruleAction, ruleEffects[0].ruleAction)
    }

    @Test
    fun simpleConditionMustResultInScheduleMessage() {
        val ruleAction: RuleAction = RuleActionScheduleMessage( "", "'2018-04-24'")
        val rule = Rule("true", listOf(ruleAction))
        val ruleEngine = getRuleEngine(rule)
        val ruleEffects = ruleEngine.evaluate(getTestRuleEvent(RuleEvent.Status.ACTIVE)).call()
        assertEquals(1, ruleEffects.size)
        assertTrue(ruleEffects[0].ruleAction is RuleActionScheduleMessage)
        assertEquals("2018-04-24", ruleEffects[0].data)
    }

    @Test
    fun simpleConditionMustResultInHideSectionEffect() {
        val ruleAction: RuleAction = RuleActionHideSection("test_section")
        val rule = Rule("true", listOf(ruleAction))
        val ruleEngine = getRuleEngine(rule)
        val ruleEffects = ruleEngine.evaluate(getTestRuleEvent(RuleEvent.Status.ACTIVE)).call()
        assertEquals(1, ruleEffects.size)
        assertEquals("", ruleEffects[0].data)
        assertEquals(ruleAction, ruleEffects[0].ruleAction)
    }

    @Test
    fun simpleConditionMustResultInHideOptionEffect() {
        val ruleAction: RuleAction = RuleActionHideOption("test_field","test_option", "test_content")
        val rule = Rule("true", listOf(ruleAction))
        val ruleEngine = getRuleEngine(rule)
        val ruleEffects = ruleEngine.evaluate(getTestRuleEvent(RuleEvent.Status.ACTIVE)).call()
        assertEquals(1, ruleEffects.size)
        assertEquals("", ruleEffects[0].data)
        assertEquals(ruleAction, ruleEffects[0].ruleAction)
    }

    @Test
    fun simpleConditionMustResultInHideOptionGroupEffect() {
        val ruleAction: RuleAction = RuleActionHideOptionGroup("field", "test_option_group", "test_content" )
        val rule = Rule("true", listOf(ruleAction))
        val ruleEngine = getRuleEngine(rule)
        val ruleEffects = ruleEngine.evaluate(getTestRuleEvent(RuleEvent.Status.ACTIVE)).call()
        assertEquals(1, ruleEffects.size)
        assertEquals("", ruleEffects[0].data)
        assertEquals(ruleAction, ruleEffects[0].ruleAction)
    }

    @Test
    fun simpleConditionMustResultInSetMandatoryFieldEffect() {
        val ruleAction: RuleAction = RuleActionSetMandatoryField("test_data_element")
        val rule = Rule("true", listOf(ruleAction))
        val ruleEngine = getRuleEngine(rule)
        val ruleEffects = ruleEngine.evaluate(getTestRuleEvent(RuleEvent.Status.ACTIVE)).call()
        assertEquals(1, ruleEffects.size)
        assertEquals("", ruleEffects[0].data)
        assertEquals(ruleAction, ruleEffects[0].ruleAction)
    }

    @Test
    fun simpleConditionMustResultInWarningEffect() {
        val ruleAction: RuleAction = RuleActionMessage.create(
            "test_warning_message", null, "target_field", null, RuleActionMessage.Type.SHOW_WARNING
        )
        val rule = Rule("true", listOf(ruleAction))
        val ruleEngine = getRuleEngine(rule)
        val ruleEffects = ruleEngine.evaluate(getTestRuleEvent(RuleEvent.Status.ACTIVE)).call()
        assertEquals(1, ruleEffects.size)
        assertEquals("", ruleEffects[0].data)
        assertEquals(ruleAction, ruleEffects[0].ruleAction)
    }

    @Test
    fun simpleConditionMustResultInErrorEffect() {
        val ruleAction: RuleAction = RuleActionMessage.create(
            "test_error_message", "2 + 2", "target_field", null, RuleActionMessage.Type.SHOW_ERROR
        )
        val rule = Rule("true", listOf(ruleAction))
        val ruleEngine = getRuleEngine(rule)
        val ruleEffects = ruleEngine.evaluate(getTestRuleEvent(RuleEvent.Status.ACTIVE)).call()
        assertEquals(1, ruleEffects.size)
        assertEquals("4", ruleEffects[0].data)
        assertEquals(ruleAction, ruleEffects[0].ruleAction)
    }

    @Test
    fun simpleConditionMustResultInOnCompletionWarningEffect() {
        val ruleAction: RuleAction = RuleActionMessage.create(
            "test_warning_message", "2 + 2", "target_field", null, RuleActionMessage.Type.WARNING_ON_COMPILATION
        )
        val rule = Rule("true", listOf(ruleAction))
        val ruleEngine = getRuleEngine(rule)
        val ruleEffects = ruleEngine.evaluate(getTestRuleEvent(RuleEvent.Status.ACTIVE)).call()
        assertEquals(1, ruleEffects.size)
        assertEquals("4", ruleEffects[0].data)
        assertEquals(ruleAction, ruleEffects[0].ruleAction)
    }

    private fun getRuleEngine(rule: Rule): RuleEngine {
        return RuleEngine(RuleEngineContext(listOf(rule), emptyList()))
    }

    private fun getRuleEngineMultiple(rule: Rule, ruleEvent: RuleEvent): RuleEngine {
        return RuleEngine(RuleEngineContext(listOf(rule), emptyList()), listOf(ruleEvent))
    }
}
