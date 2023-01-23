package org.dhis2.ruleengine

import org.dhis2.ruleengine.models.*
import org.junit.Test
import kotlin.test.assertEquals

class TriggerEnvironmentJVMTest {
    @Test
    fun testTriggerEnvironment() {
        val ruleAction: RuleAction = RuleAction.HideField(
            AttributeType.UNKNOWN, "test_action_content", "test_data_element", ""
        )
        val rule = Rule(null, null, null, "V{environment} =='JVM'", listOf(ruleAction), "")
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