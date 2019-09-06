package org.hisp.dhis.rules

import org.assertj.core.api.Java6Assertions.assertThat
import org.hisp.dhis.rules.models.RuleValueType
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.text.SimpleDateFormat
import java.util.*
import kotlin.test.assertFailsWith

@RunWith(JUnit4::class)
class RuleVariableValueTests {

    private val dateFormat = SimpleDateFormat(DATE_PATTERN, Locale.US)

    @Test
    fun textValuesMostBeWrapped() {
        val variableValue = RuleVariableValue.create(
                "test_value",
                RuleValueType.TEXT,
                listOf("test_value_candidate_one", "test_value_candidate_two"),
                dateFormat.format(Date()))

        assertThat(variableValue.value).isEqualTo("'test_value'")
        assertThat(variableValue.type).isEqualTo(RuleValueType.TEXT)
        assertThat(variableValue.candidates.size).isEqualTo(2)
        assertThat(variableValue.candidates[0]).isEqualTo("test_value_candidate_one")
        assertThat(variableValue.candidates[1]).isEqualTo("test_value_candidate_two")
    }

    @Test
    fun wrappedTextValuesMustNotBeDoubleWrapped() {
        val variableValue = RuleVariableValue.create(
                "'test_value'",
                RuleValueType.TEXT,
                listOf("test_value_candidate_one", "test_value_candidate_two"),
                dateFormat.format(Date()))

        assertThat(variableValue.value).isEqualTo("'test_value'")
        assertThat(variableValue.type).isEqualTo(RuleValueType.TEXT)
        assertThat(variableValue.candidates.size).isEqualTo(2)
        assertThat(variableValue.candidates[0]).isEqualTo("test_value_candidate_one")
        assertThat(variableValue.candidates[1]).isEqualTo("test_value_candidate_two")
    }

    @Test
    fun numericValuesMostNotBeWrapped() {
        val variableValue = RuleVariableValue.create(
                "1",
                RuleValueType.NUMERIC,
                listOf("2", "3"),
                dateFormat.format(Date()))

        assertThat(variableValue.value).isEqualTo("1")
        assertThat(variableValue.type).isEqualTo(RuleValueType.NUMERIC)
        assertThat(variableValue.candidates.size).isEqualTo(2)
        assertThat(variableValue.candidates[0]).isEqualTo("2")
        assertThat(variableValue.candidates[1]).isEqualTo("3")
    }

    @Test
    fun booleanValuesMostNotBeWrapped() {
        val variableValue = RuleVariableValue.create(
                "true",
                RuleValueType.BOOLEAN,
                listOf("false", "false"),
                dateFormat.format(Date()))

        assertThat(variableValue.value).isEqualTo("true")
        assertThat(variableValue.type).isEqualTo(RuleValueType.BOOLEAN)
        assertThat(variableValue.candidates.size).isEqualTo(2)
        assertThat(variableValue.candidates[0]).isEqualTo("false")
        assertThat(variableValue.candidates[1]).isEqualTo("false")
    }

    @Test
    fun createShouldThrowOnNullValueType() {
        assertFailsWith<IllegalArgumentException> {
            RuleVariableValue.create("test_value", null)
        }
    }

    @Test
    fun createShouldThrowOnNullCandidateList() {
        assertFailsWith<IllegalArgumentException> {
            RuleVariableValue.create("test_value", RuleValueType.TEXT, null, dateFormat.format(Date()))
        }
    }

    companion object {
        const val DATE_PATTERN = "yyyy-MM-dd"
    }
}
