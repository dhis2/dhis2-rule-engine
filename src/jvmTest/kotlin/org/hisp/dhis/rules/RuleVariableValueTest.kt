package org.hisp.dhis.rules

import org.hisp.dhis.rules.models.RuleValueType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.test.Test
import kotlin.test.assertEquals

class RuleVariableValueTest {
    private val dateFormat = SimpleDateFormat(DATE_PATTERN, Locale.US)
    @Test
    fun textValuesMostBeWrapped() {
        val variableValue = RuleVariableValue(
            RuleValueType.TEXT, "test_value", listOf(
                "test_value_candidate_one", "test_value_candidate_two"
            ), dateFormat.format(Date())
        )
        assertEquals("test_value", variableValue.value)
        assertEquals(RuleValueType.TEXT, variableValue.type)
        assertEquals(2, variableValue.candidates.size)
        assertEquals("test_value_candidate_one", variableValue.candidates.get(0))
        assertEquals("test_value_candidate_two", variableValue.candidates.get(1))
    }

    @Test
    fun wrappedTextValuesMustNotBeDoubleWrapped() {
        val variableValue = RuleVariableValue(
            RuleValueType.TEXT, "test_value", listOf(
                "test_value_candidate_one", "test_value_candidate_two"
            ), dateFormat.format(Date())
        )
        assertEquals("test_value", variableValue.value)
        assertEquals(RuleValueType.TEXT, variableValue.type)
        assertEquals(2, variableValue.candidates.size)
        assertEquals("test_value_candidate_one", variableValue.candidates.get(0))
        assertEquals("test_value_candidate_two", variableValue.candidates.get(1))
    }

    @Test
    fun numericValuesMostNotBeWrapped() {
        val variableValue = RuleVariableValue(
            RuleValueType.NUMERIC, "1", listOf("2", "3"), dateFormat.format(Date())
        )
        assertEquals("1", variableValue.value)
        assertEquals(RuleValueType.NUMERIC, variableValue.type)
        assertEquals(2, variableValue.candidates.size)
        assertEquals("2", variableValue.candidates.get(0))
        assertEquals("3", variableValue.candidates.get(1))
    }

    @Test
    fun booleanValuesMostNotBeWrapped() {
        val variableValue = RuleVariableValue(
            RuleValueType.BOOLEAN, "true", listOf("false", "false"), dateFormat.format(Date())
        )
        assertEquals("true", variableValue.value)
        assertEquals(RuleValueType.BOOLEAN, variableValue.type)
        assertEquals(2, variableValue.candidates.size)
        assertEquals("false", variableValue.candidates.get(0))
        assertEquals("false", variableValue.candidates.get(1))
    }

    companion object {
        private const val DATE_PATTERN = "yyyy-MM-dd"
    }
}
