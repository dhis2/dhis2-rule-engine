package org.dhis2.ruleengine

import org.dhis2.ruleengine.models.RuleValueType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RuleVariableValueTest {
    @Test
    fun textValuesMostBeWrapped() {
        val variableValue = RuleVariableValue(
            "test_value",
            RuleValueType.TEXT,
            listOf(
                "test_value_candidate_one", "test_value_candidate_two"
            )
        )
        assertTrue(variableValue.value == "test_value")
        assertEquals(variableValue.ruleValueType, RuleValueType.TEXT)
        assertTrue(variableValue.candidates.size == 2)
        assertTrue(variableValue.candidates[0] == "test_value_candidate_one")
        assertTrue(variableValue.candidates[1] == "test_value_candidate_two")
    }

    @Test
    fun wrappedTextValuesMustNotBeDoubleWrapped() {
        val variableValue: RuleVariableValue = RuleVariableValue(
            "test_value", RuleValueType.TEXT, listOf(
                "test_value_candidate_one", "test_value_candidate_two"
            )
        )
        assertTrue(variableValue.value == "test_value")
        assertEquals(variableValue.ruleValueType, RuleValueType.TEXT)
        assertTrue(variableValue.candidates.size == 2)
        assertTrue(variableValue.candidates.get(0) == "test_value_candidate_one")
        assertTrue(variableValue.candidates.get(1) == "test_value_candidate_two")
    }

    @Test
    fun numericValuesMostNotBeWrapped() {
        val variableValue: RuleVariableValue = RuleVariableValue(
            "1", RuleValueType.NUMERIC, listOf("2", "3")
        )
        assertTrue(variableValue.value == "1")
        assertEquals(variableValue.ruleValueType, RuleValueType.NUMERIC)
        assertTrue(variableValue.candidates.size == 2)
        assertTrue(variableValue.candidates.get(0) == "2")
        assertTrue(variableValue.candidates.get(1) == "3")
    }

    @Test
    fun booleanValuesMostNotBeWrapped() {
        val variableValue: RuleVariableValue = RuleVariableValue(
            "true", RuleValueType.BOOLEAN, listOf("false", "false")
        )
        assertTrue(variableValue.value == "true")
        assertEquals(variableValue.ruleValueType, RuleValueType.BOOLEAN)
        assertTrue(variableValue.candidates.size == 2)
        assertTrue(variableValue.candidates.get(0) == "false")
        assertTrue(variableValue.candidates.get(1) == "false")
    }

    /*    @Test(expected = IllegalArgumentException::class)
        fun createShouldThrowOnNullValueType() {
            RuleVariableValue("test_value", null)
        }

        @Test(expected = IllegalArgumentException::class)
        fun createShouldThrowOnNullCandidateList() {
            RuleVariableValue("test_value", RuleValueType.TEXT, null)
        }*/

    companion object {
        private const val DATE_PATTERN = "yyyy-MM-dd"
    }
}