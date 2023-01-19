package org.dhis2.ruleengine.functions

import org.dhis2.ruleengine.RuleVariableValue
import org.dhis2.ruleengine.exprk.functions.HasValue
import org.dhis2.ruleengine.models.RuleValueType
import kotlin.test.Test
import kotlin.test.assertTrue


class RuleFunctionHasValueTest {

    @Test
    fun return_false_for_non_existing_variable() {
        val variableValues: Map<String, RuleVariableValue> = givenAEmptyVariableValues()
        assertHasValue("nonexisting", variableValues, "false")
    }

    @Test
    fun return_false_for_existing_variable_without_value() {
        val variableName = "non_value_var"
        val variableValues: Map<String, RuleVariableValue> = givenAVariableValuesAndOneWithoutValue(variableName)
        assertHasValue(variableName, variableValues, "false")
    }

    @Test
    fun return_true_for_existing_variable_with_value() {
        val variableName = "with_value_var"
        val variableValues: Map<String, RuleVariableValue> = givenAVariableValuesAndOneWithValue(variableName)
        assertHasValue(variableName, variableValues, "true")
    }

    private fun givenAEmptyVariableValues(): Map<String, RuleVariableValue> {
        return emptyMap()
    }

    private fun givenAVariableValuesAndOneWithoutValue(
        variableNameWithoutValue: String
    ): Map<String, RuleVariableValue> {
        val variableValues: MutableMap<String, RuleVariableValue> = mutableMapOf()
        variableValues[variableNameWithoutValue] = RuleVariableValue(ruleValueType = RuleValueType.TEXT)
        variableValues["test_variable_two"] =
            RuleVariableValue(variableValue = "Value two", ruleValueType = RuleValueType.TEXT)
        return variableValues
    }

    private fun givenAVariableValuesAndOneWithValue(
        variableNameWithValue: String
    ): Map<String, RuleVariableValue> {
        val variableValues: MutableMap<String, RuleVariableValue> = mutableMapOf()
        variableValues["test_variable_one"] = RuleVariableValue(ruleValueType = RuleValueType.TEXT)
        variableValues[variableNameWithValue] =
            RuleVariableValue(variableValue = "Value two", ruleValueType = RuleValueType.TEXT)
        return variableValues
    }

    private fun assertHasValue(value: String, valueMap: Map<String, RuleVariableValue>, hasValue: String) {
        assertTrue {
            HasValue(valueMap).call(listOf(value)) == hasValue
        }
    }
}