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
import kotlin.test.assertTrue

// ToDo: function tests (check that function invocations are producing expected values; check nested function invocation)
// ToDo: various source type tests (referencing variables from different events)

class RuleEngineEffectTypesTest {
    private fun getTestRuleEnrollment(): RuleEnrollment =
        RuleEnrollment(
            enrollment = "test_enrollment",
            programName = "test_program",
            incidentDate = RuleLocalDate.currentDate(),
            enrollmentDate = RuleLocalDate.currentDate(),
            status = RuleEnrollmentStatus.ACTIVE,
            organisationUnit = "test_ou",
            organisationUnitCode = "test_ou_code",
            attributeValues = listOf(RuleAttributeValue("test_attribute_one", "test_value")),
        )

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
    fun shouldAssignCorrectlyToAnAttribute() {
        val ruleAction = RuleAction("'test_string'", "ASSIGN",
            mapOf(Pair("content", "A{attribute}")))
        val rule = Rule("true", listOf(ruleAction))
        val ruleVariable = RuleVariableAttribute(
            "attribute",
            true,
            ArrayList(),
            "test_attribute_one",
            RuleValueType.TEXT,
        )
        val ruleEffects =
            RuleEngine.getInstance().evaluate(
                getTestRuleEnrollment(),
                emptyList(),
                RuleEngineContext(listOf(rule), listOf(ruleVariable)),
            )
        assertEquals(1, ruleEffects.size)
        assertEquals("test_string", ruleEffects[0].data)
        assertEquals("test_attribute_one", ruleEffects[0].ruleAction.field())
        assertEquals(AttributeType.TRACKED_ENTITY_ATTRIBUTE.name, ruleEffects[0].ruleAction.attributeType())
        assertEquals("A{attribute}", ruleEffects[0].ruleAction.content())
    }

    @Test
    fun assignToAttributeVariableMustNotProduceEventEffect() {
        val ruleAction = RuleAction("'test_string'", "ASSIGN", mapOf(Pair("content", "A{attribute}")))
        val rule = Rule("true", listOf(ruleAction))
        val ruleVariable = RuleVariableAttribute(
            "attribute",
            true,
            ArrayList(),
            "test_attribute_one",
            RuleValueType.TEXT,
        )
        val ruleEffects =
            RuleEngine.getInstance().evaluate(
                getTestRuleEvent(RuleEventStatus.ACTIVE),
                null,
                emptyList(),
                RuleEngineContext(listOf(rule), listOf(ruleVariable)),
            )
        assertEquals(0, ruleEffects.size)
    }

    @Test
    fun shouldAssignCorrectlyToADataElementBackedVariable() {
        val ruleAction = RuleAction("'test_string'", "ASSIGN", mapOf(Pair("content", "#{variable}")))
        val rule = Rule("true", listOf(ruleAction))
        val ruleVariable = RuleVariableCurrentEvent(
            "variable",
            true,
            ArrayList(),
            "test_data_element",
            RuleValueType.TEXT,
        )
        val ruleEffects =
            RuleEngine.getInstance().evaluate(
                getTestRuleEvent(RuleEventStatus.ACTIVE),
                null,
                emptyList(),
                RuleEngineContext(listOf(rule), listOf(ruleVariable)),
            )
        assertEquals(1, ruleEffects.size)
        assertEquals("test_string", ruleEffects[0].data)
        assertEquals("test_data_element", ruleEffects[0].ruleAction.field())
        assertEquals(AttributeType.DATA_ELEMENT.name, ruleEffects[0].ruleAction.attributeType())
    }

    @Test
    fun assignToAttributeVariableMustProduceSingleEffectInMultipleExecution() {
        val ruleAction = RuleAction("'test_string'", "ASSIGN", mapOf(Pair("content", "A{attribute}")))
        val rule = Rule("true", listOf(ruleAction))
        val ruleVariable = RuleVariableAttribute(
            "attribute",
            true,
            ArrayList(),
            "test_attribute_one",
            RuleValueType.TEXT,
        )
        val ruleEffects =
            RuleEngine.getInstance().evaluateAll(
                getTestRuleEnrollment(),
                listOf(getTestRuleEvent(RuleEventStatus.ACTIVE)),
                RuleEngineContext(listOf(rule), listOf(ruleVariable)),
            )
        val allEffects = ruleEffects.flatMap { it.ruleEffects }
        assertEquals(1, allEffects.size)
        assertEquals("test_attribute_one", allEffects[0].ruleAction.field())
        assertEquals(1, ruleEffects.single { it.isEnrollment }.ruleEffects.size)
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
    fun malformedAssignProducesClearErrorEffectDuringEvaluation() {
        val action = RuleAction("2+2", RuleAction.ASSIGN)
        val rule = Rule("true", listOf(action), "rule_uid")
        val ruleEffects =
            RuleEngine.getInstance().evaluateAll(
                null,
                listOf(getTestRuleEvent(RuleEventStatus.ACTIVE)),
                getRuleEngineContext(listOf(rule)),
            )
        assertEquals(1, ruleEffects.size)
        assertEquals(1, ruleEffects[0].ruleEffects.size)
        val errorEffect = ruleEffects[0].ruleEffects[0]
        assertEquals(RuleAction.ERROR, errorEffect.ruleAction.type)
        assertTrue(errorEffect.data!!.contains("raised an error"))
        assertTrue(errorEffect.data!!.contains("'field'"))
        assertTrue(errorEffect.data!!.contains("'content'"))
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
