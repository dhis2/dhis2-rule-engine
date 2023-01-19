package org.dhis2.ruleengine

import org.dhis2.ruleengine.models.RuleValueType
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.text.SimpleDateFormat
import java.util.*

@RunWith(JUnit4::class)
class RuleVariableValueTest {
    private val dateFormat = SimpleDateFormat(DATE_PATTERN, Locale.US)

    @Test
    fun textValuesMostBeWrapped() {
        val variableValue = RuleVariableValue(
            "test_value",
            RuleValueType.TEXT,
            listOf(
                "test_value_candidate_one", "test_value_candidate_two"
            )
        )
        assert(variableValue.value == "test_value")
        assert(variableValue.ruleValueType == RuleValueType.TEXT)
        assert(variableValue.candidates.size == 2)
        assert(variableValue.candidates[0] == "test_value_candidate_one")
        assert(variableValue.candidates[1] == "test_value_candidate_two")
    }

    @Test
    fun wrappedTextValuesMustNotBeDoubleWrapped() {
        val variableValue: RuleVariableValue = RuleVariableValue(
            "test_value", RuleValueType.TEXT, Arrays.asList(
                "test_value_candidate_one", "test_value_candidate_two"
            )
        )
        assert(variableValue.value == "test_value")
        assert(variableValue.ruleValueType == RuleValueType.TEXT)
        assert(variableValue.candidates.size == 2)
        assert(variableValue.candidates.get(0) == "test_value_candidate_one")
        assert(variableValue.candidates.get(1) == "test_value_candidate_two")
    }

    @Test
    fun numericValuesMostNotBeWrapped() {
        val variableValue: RuleVariableValue = RuleVariableValue(
            "1", RuleValueType.NUMERIC, Arrays.asList("2", "3")
        )
        assert(variableValue.value == "1")
        assert(variableValue.ruleValueType == RuleValueType.NUMERIC)
        assert(variableValue.candidates.size == 2)
        assert(variableValue.candidates.get(0) == "2")
        assert(variableValue.candidates.get(1) == "3")
    }

    @Test
    fun booleanValuesMostNotBeWrapped() {
        val variableValue: RuleVariableValue = RuleVariableValue(
            "true", RuleValueType.BOOLEAN, Arrays.asList("false", "false")
        )
        assert(variableValue.value == "true")
        assert(variableValue.ruleValueType == RuleValueType.BOOLEAN)
        assert(variableValue.candidates.size == 2)
        assert(variableValue.candidates.get(0) == "false")
        assert(variableValue.candidates.get(1) == "false")
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