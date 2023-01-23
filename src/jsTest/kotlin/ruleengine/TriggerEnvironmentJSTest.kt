package ruleengine

import org.dhis2.ruleengine.RuleEffect
import org.dhis2.ruleengine.RuleEngineTestUtils
import org.dhis2.ruleengine.models.*
import kotlin.test.Test
import kotlin.test.assertEquals

class TriggerEnvironmentJSTest {
    @Test
    fun testTriggerEnvironment() {
        val ruleAction: RuleAction = RuleAction.HideField(
            AttributeType.UNKNOWN, "test_action_content", "test_data_element", ""
        )
        val rule = Rule(null, null, null, "V{environment} =='JS'", listOf(ruleAction), "")
        val ruleEngine = RuleEngineTestUtils.getRuleEngine(
            rule = rule,
            ruleVariables = emptyList()
        )
        val ruleEffects: List<RuleEffect> = ruleEngine.evaluate(getTestRuleEvent(RuleEvent.Status.ACTIVE))
        assertEquals(ruleEffects.size, 1)
        assertEquals(ruleEffects[0].data, "")
        assertEquals(ruleEffects[0].ruleAction, ruleAction)
    }

    private fun getTestRuleEvent(status: RuleEvent.Status): RuleEvent {
        return RuleEvent(
            "test_event",
            "test_program_stage",
            "",
            status,
            RuleEngineTestUtils.currentDate(),
            RuleEngineTestUtils.currentDate(),
            null,
            "",
            "",
            listOf(
                RuleDataValue(
                    RuleEngineTestUtils.currentDate(), "test_program_stage", "test_data_element", "test_value"
                )
            )
        )
    }
}