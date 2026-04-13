package org.hisp.dhis.rules

import kotlin.time.Clock
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
            eventDate = RuleLocalDate.currentDate(),
            createdDate = RuleInstant.now(),
            createdAtClientDate = null,
            dueDate = RuleLocalDate.currentDate(),
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
    fun evaluateAllProducesSameEffectsAsIndividualEvaluateCalls() {
        val ruleAction = RuleAction("'text'", "DISPLAYTEXT", mapOf("content" to "label"))
        val testRule = Rule("true", listOf(ruleAction))
        val context = RuleEngineContext(listOf(testRule))
        val engine = RuleEngine.getInstance()

        val enrollment = RuleEnrollment(
            enrollment = "enrollment_uid",
            programName = "Test Program",
            incidentDate = RuleLocalDate(2023, 1, 1),
            enrollmentDate = RuleLocalDate(2023, 1, 1),
            status = RuleEnrollmentStatus.ACTIVE,
            organisationUnit = "org_unit",
            organisationUnitCode = null,
            attributeValues = emptyList(),
        )
        val event = RuleEvent(
            event = "event_uid",
            programStage = "stage_uid",
            programStageName = "Stage",
            status = RuleEventStatus.ACTIVE,
            eventDate = RuleLocalDate(2023, 1, 2),
            createdDate = RuleInstant.now(),
            createdAtClientDate = null,
            dueDate = null,
            completedDate = null,
            organisationUnit = "org_unit",
            organisationUnitCode = null,
            dataValues = emptyList(),
        )

        val allEffects = engine.evaluateAll(enrollment, listOf(event), context)
        val enrollmentEffectsAll = allEffects.single { it.isEnrollment }.ruleEffects
        val eventEffectsAll = allEffects.single { it.isEvent }.ruleEffects

        val enrollmentEffects = engine.evaluate(enrollment, listOf(event), context)
        val eventEffects = engine.evaluate(event, enrollment, emptyList(), context)

        assertEquals(enrollmentEffects.map { it.data }, enrollmentEffectsAll.map { it.data })
        assertEquals(eventEffects.map { it.data }, eventEffectsAll.map { it.data })
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
