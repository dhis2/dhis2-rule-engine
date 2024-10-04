package org.hisp.dhis.rules

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import org.hisp.dhis.rules.RuleEngineTestUtils.getRuleEngineContext
import org.hisp.dhis.rules.api.RuleEngine
import org.hisp.dhis.rules.api.RuleEngineContext
import org.hisp.dhis.rules.models.*
import org.hisp.dhis.rules.utils.currentDate
import kotlin.test.Test
import kotlin.test.assertEquals

// ToDo: function tests (check that function invocations are producing expected values; check nested function invocation)
// ToDo: various source type tests (referencing variables from different events)
class RuleEngineEffectTypesTest {
    private fun getTestRuleEvent(status: RuleEventStatus): RuleEvent =
        RuleEvent(
            event = "test_event",
            programStage = "test_program_stage",
            programStageName = "",
            status = status,
            eventDate = Clock.System.now(),
            createdDate = Clock.System.now(),
            dueDate = LocalDate.currentDate(),
            organisationUnit = "",
            organisationUnitCode = "",
            completedDate = null,
            dataValues =
                listOf(
                    RuleDataValue(
                        "test_data_element",
                        "test_value",
                    ),
                ),
        )

    @Test
    fun simpleConditionMustResultInAssignEffect() {
        val ruleAction = RuleAction("'test_string'", "ASSIGN", mapOf(Pair("field", "test_data_element")))
        val rule = Rule("true", listOf(ruleAction))
        val ruleEffects =
            RuleEngine.getInstance().evaluate(
                getTestRuleEvent(RuleEventStatus.ACTIVE),
                null,
                emptyList(),
                RuleEngineContext(listOf(rule)),
            )
        assertEquals(1, ruleEffects.size)
        assertEquals("test_string", ruleEffects[0].data)
        assertEquals(ruleAction, ruleEffects[0].ruleAction)
    }

    @Test
    fun simpleConditionMustResultInAssignEffectMultipleExecution() {
        val ruleAction = RuleAction("'test_string'", "ASSIGN", mapOf(Pair("field", "test_data_element")))
        val rule = Rule("true", listOf(ruleAction))
        val ruleEffects =
            RuleEngine.getInstance().evaluateAll(
                null,
                listOf(getTestRuleEvent(RuleEventStatus.ACTIVE)),
                getRuleEngineContext(listOf(rule)),
            )
        assertEquals(1, ruleEffects.size)
        assertEquals("test_string", ruleEffects[0].ruleEffects[0].data)
        assertEquals(ruleAction, ruleEffects[0].ruleEffects[0].ruleAction)
    }

    @Test
    fun testEnvironmentVariableExpression() {
        val ruleAction = RuleAction("", "HIDEFIELD", mapOf(Pair("content", "test_action_content"), Pair("field", "test_data_element")))
        val rule = Rule("V{event_status} =='COMPLETED'", listOf(ruleAction))
        val ruleEffects =
            RuleEngine.getInstance().evaluate(
                getTestRuleEvent(RuleEventStatus.COMPLETED),
                null,
                emptyList(),
                RuleEngineContext(listOf(rule)),
            )
        assertEquals(1, ruleEffects.size)
        assertEquals("", ruleEffects[0].data)
        assertEquals(ruleAction, ruleEffects[0].ruleAction)
    }
}
