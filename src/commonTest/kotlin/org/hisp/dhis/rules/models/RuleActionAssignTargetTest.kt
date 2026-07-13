package org.hisp.dhis.rules.models

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
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
    fun assignTargetFailsWhenNeitherFieldNorContentIsSet() {
        val action = RuleAction("2+2", RuleAction.ASSIGN)
        val exception = assertFailsWith<IllegalArgumentException> { action.assignTarget() }
        assertTrue(exception.message!!.contains("'field'"))
        assertTrue(exception.message!!.contains("'content'"))
    }

    @Test
    fun assignTargetFailsWhenContentIsNotAVariableReference() {
        val action = RuleAction("2+2", RuleAction.ASSIGN, mapOf("content" to "test_variable"))
        val exception = assertFailsWith<IllegalArgumentException> { action.assignTarget() }
        assertTrue(exception.message!!.contains("#{variableName}"))
        assertTrue(exception.message!!.contains("test_variable"))
    }
}
