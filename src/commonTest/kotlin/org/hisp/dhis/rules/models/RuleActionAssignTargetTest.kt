package org.hisp.dhis.rules.models

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

class RuleActionAssignTargetTest {
    @Test
    fun assignTargetIsNullForNonAssignActions() {
        val action = RuleAction("'text'", "DISPLAYTEXT", mapOf("content" to "label"))
        assertNull(action.assignTarget())
    }

    @Test
    fun assignTargetIsFieldWhenFieldIsSet() {
        val action = RuleAction("'value'", RuleAction.ASSIGN, mapOf("field" to "test_data_element"))
        assertEquals(AssignTarget.Field("test_data_element"), action.assignTarget())
    }

    @Test
    fun assignTargetPrefersFieldOverContent() {
        val action =
            RuleAction(
                "'value'",
                RuleAction.ASSIGN,
                mapOf("field" to "test_data_element", "content" to "#{test_variable}"),
            )
        assertEquals(AssignTarget.Field("test_data_element"), action.assignTarget())
    }

    @Test
    fun assignTargetIsVariableWhenOnlyContentIsSet() {
        val action = RuleAction("2+2", RuleAction.ASSIGN, mapOf("content" to "#{test_variable}"))
        assertEquals(AssignTarget.Variable("test_variable"), action.assignTarget())
    }

    @Test
    fun assignTargetAcceptsAttributeNotationInContent() {
        val action = RuleAction("2+2", RuleAction.ASSIGN, mapOf("content" to "A{test_variable}"))
        assertEquals(AssignTarget.Variable("test_variable"), action.assignTarget())
    }

    @Test
    fun assignTargetIsVariableWhenFieldIsEmpty() {
        val action = RuleAction("2+2", RuleAction.ASSIGN, mapOf("field" to "", "content" to "#{test_variable}"))
        assertEquals(AssignTarget.Variable("test_variable"), action.assignTarget())
    }

    @Test
    fun assignTargetIsInvalidWhenNeitherFieldNorContentIsSet() {
        val action = RuleAction("2+2", RuleAction.ASSIGN)
        val target = action.assignTarget()
        assertIs<AssignTarget.Invalid>(target)
        assertTrue(target.reason.contains("'field'"))
        assertTrue(target.reason.contains("'content'"))
    }

    @Test
    fun assignTargetIsInvalidWhenContentIsNotAVariableReference() {
        val action = RuleAction("2+2", RuleAction.ASSIGN, mapOf("content" to "test_variable"))
        val target = action.assignTarget()
        assertIs<AssignTarget.Invalid>(target)
        assertTrue(target.reason.contains("#{variableName}"))
        assertTrue(target.reason.contains("test_variable"))
    }

    @Test
    fun assignTargetIsInvalidWhenVariableNameIsEmptyOrMalformed() {
        for (content in listOf("#{}", "A{}", "#{a}b}", "#{a{b}", "#{", "A{{}")) {
            val action = RuleAction("2+2", RuleAction.ASSIGN, mapOf("content" to content))
            assertIs<AssignTarget.Invalid>(action.assignTarget(), "content: $content")
        }
    }
}
