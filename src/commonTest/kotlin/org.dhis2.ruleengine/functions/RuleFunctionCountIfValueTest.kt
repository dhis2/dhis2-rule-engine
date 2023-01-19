package org.dhis2.ruleengine.functions

import org.dhis2.ruleengine.RuleVariableValue
import org.dhis2.ruleengine.exprk.functions.CountIfValue
import org.dhis2.ruleengine.models.RuleValueType
import kotlin.test.Test
import kotlin.test.assertTrue

/*
 * Copyright (c) 2004-2018, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the HISP project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

class RuleFunctionCountIfValueTest {

    @Test
    fun return_zero_for_non_existing_variable() {
        assertCountIfValue("non existing variable", "value", givenAEmptyVariableValues(), "0")
    }

    @Test
    fun return_zero_for_empty_value_to_compare() {
        assertCountIfValue("var1", null, givenAEmptyVariableValues(), "0")
        assertCountIfValue("var1", "", givenAEmptyVariableValues(), "0")
    }

    @Test
    fun return_zero_for_variable_without_values() {
        val variableName = "non_value_var"
        assertCountIfValue(
            variableName, "valueToCompare", givenAVariableValuesAndOneWithoutValue(variableName),
            "0"
        )
    }

    @Test
    fun return_size_of_matched_values_for_variable_with_value_and_candidates() {
        val variableName = "with_value_var"
        val value = "valueA"
        val variableValues: Map<String, RuleVariableValue> = givenAVariableValuesAndOneWithTwoExpectedCountCandidates(
            variableName, value
        )
        assertCountIfValue(variableName, value, variableValues, "2")
    }

    @Test
    fun return_zero_for_variable_with_no_matched_value_and_candidates() {
        val variableName = "with_value_var"
        val value = "valueA"
        val variableValues: Map<String, RuleVariableValue> = givenAVariableValuesAndOneWithTwoExpectedCountCandidates(
            variableName, value
        )
        assertCountIfValue(variableName, "NoMatchedValue", variableValues, "0")
    }

    @Test
    fun return_zero_for_no_matched_variable_with_value_and_without_candidates() {
        val variableName = "with_value_var"
        val value = "valueA"
        val variableValues: Map<String, RuleVariableValue> = givenAVariableValuesAndOneWithUndefinedCandidates(
            variableName, value
        )
        assertCountIfValue(variableName, "NoMatchedValue", variableValues, "0")
    }

    @Test
    fun support_numeric_values_in_expression_for_booleans() {
        val variableName = "boolean_variable"
        val variableValues: Map<String, RuleVariableValue> = givenVariableValueWithBooleanValues(variableName)
        assertCountIfValue("boolean_variable", "1", variableValues, "2")
        assertCountIfValue("boolean_variable", "0", variableValues, "1")
    }

    private fun givenAEmptyVariableValues(): Map<String, RuleVariableValue> {
        return mapOf<String, RuleVariableValue>()
    }

    private fun givenAVariableValuesAndOneWithoutValue(variableNameWithoutValue: String): Map<String, RuleVariableValue> {
        val variableValues: MutableMap<String, RuleVariableValue> = mutableMapOf()
        variableValues[variableNameWithoutValue] = RuleVariableValue(ruleValueType = RuleValueType.TEXT)
        RuleVariableValue(variableValue = "Value two", ruleValueType = RuleValueType.TEXT)
        variableValues["test_variable_two"] =
            RuleVariableValue(variableValue = "Value two", ruleValueType = RuleValueType.TEXT)
        return variableValues
    }

    private fun givenAVariableValuesAndOneWithTwoExpectedCountCandidates(
        variableNameWithValueAndCandidates: String, valueToCompare: String
    ): Map<String, RuleVariableValue> {
        val variableValues: MutableMap<String, RuleVariableValue> = mutableMapOf()
        variableValues["test_variable_one"] = RuleVariableValue(ruleValueType = RuleValueType.TEXT)
        variableValues[variableNameWithValueAndCandidates] =
            RuleVariableValue(
                variableValue = valueToCompare,
                ruleValueType = RuleValueType.TEXT,
                candidates = listOf("one", valueToCompare, valueToCompare)
            )
        return variableValues
    }

    private fun givenAVariableValuesAndOneWithUndefinedCandidates(
        variableNameWithValueAndNonCandidates: String, valueToCompare: String
    ): Map<String, RuleVariableValue> {
        val variableValues: MutableMap<String, RuleVariableValue> = mutableMapOf()
        variableValues["test_variable_one"] = RuleVariableValue(ruleValueType = RuleValueType.TEXT)
        variableValues[variableNameWithValueAndNonCandidates] =
            RuleVariableValue(variableValue = valueToCompare, ruleValueType = RuleValueType.TEXT)
        return variableValues
    }

    private fun givenVariableValueWithBooleanValues(
        variableNameWithValueAndNonCandidates: String
    ): Map<String, RuleVariableValue> {
        val variableValues: MutableMap<String, RuleVariableValue> = mutableMapOf()
        variableValues[variableNameWithValueAndNonCandidates] =
            RuleVariableValue(
                variableValue = "true",
                ruleValueType = RuleValueType.BOOLEAN,
                candidates = listOf("false", "true", "true")
            )
        return variableValues
    }

    private fun assertCountIfValue(
        variableName: String,
        valueToFind: String?,
        valueMap: Map<String, RuleVariableValue>,
        countIfValue: String
    ) {
        assertTrue {
            CountIfValue(valueMap).call(listOf(variableName, valueToFind)) == countIfValue
        }
    }
}