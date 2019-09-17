package org.hisp.dhis.rules.functions

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hisp.dhis.rules.RuleVariableValue
import org.hisp.dhis.rules.RuleVariableValueBuilder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertFailsWith

@RunWith(JUnit4::class)
class RuleFunctionHasValueTests {

    private var variableValues: MutableMap<String, RuleVariableValue> = HashMap()

    private val hasValueFunction = RuleFunctionHasValue.create()

    @Test
    fun return_false_for_non_existing_variable() {
        variableValues = givenAEmptyVariableValues()

        assertThat(hasValueFunction.evaluate(
                listOf("nonexisting"), variableValues, null), `is`("false"))
    }

    @Test
    fun return_false_for_existing_variable_without_value() {

        val variableName = "non_value_var"

        variableValues = givenAVariableValuesAndOneWithoutValue(variableName)

        assertThat(hasValueFunction.evaluate(
                listOf(variableName), variableValues, null), `is`("false"))
    }

    @Test
    fun return_true_for_existing_variable_with_value() {

        val variableName = "with_value_var"

        variableValues = givenAVariableValuesAndOneWithValue(variableName)

        assertThat(hasValueFunction.evaluate(
                listOf(variableName), variableValues, null), `is`("true"))
    }

    @Test
    fun throw_illegal_argument_exception_when_variable_map_is_null() {
        assertFailsWith<IllegalArgumentException> {
            RuleFunctionHasValue.create().evaluate(listOf("variable_name"), null, null)
        }
    }

    @Test
    fun throw_illegal_argument_exception_when_argument_count_is_greater_than_expected() {
        assertFailsWith<IllegalArgumentException> {
            RuleFunctionHasValue.create().evaluate(listOf("variable_name", "6.8"), variableValues, null)
        }
    }

    @Test
    fun throw_illegal_argument_exception_when_arguments_count_is_lower_than_expected() {
        assertFailsWith<IllegalArgumentException> {
            RuleFunctionHasValue.create().evaluate(ArrayList(), variableValues, null)
        }
    }

    @Test
    fun throw_null_pointer_exception_when_arguments_is_null() {
        assertFailsWith<NullPointerException> {
            RuleFunctionHasValue.create().evaluate(null!!, variableValues, null)
        }
    }

    private fun givenAEmptyVariableValues(): MutableMap<String, RuleVariableValue> {
        return hashMapOf()
    }

    private fun givenAVariableValuesAndOneWithoutValue(
            variableNameWithoutValue: String): MutableMap<String, RuleVariableValue> {
        variableValues[variableNameWithoutValue]

        variableValues["test_variable_two"] = RuleVariableValueBuilder.create()
                .withValue("Value two")
                .build()

        return variableValues
    }

    private fun givenAVariableValuesAndOneWithValue(
            variableNameWithValue: String): MutableMap<String, RuleVariableValue> {
        variableValues["test_variable_one"]

        variableValues[variableNameWithValue] = RuleVariableValueBuilder.create()
                .withValue("Value two")
                .build()

        return variableValues
    }
}
