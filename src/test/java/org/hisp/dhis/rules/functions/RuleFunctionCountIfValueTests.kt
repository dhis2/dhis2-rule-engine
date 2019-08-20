package org.hisp.dhis.rules.functions

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

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hisp.dhis.rules.RuleVariableValue
import org.hisp.dhis.rules.RuleVariableValueBuilder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertFailsWith

@RunWith(JUnit4::class)
class RuleFunctionCountIfValueTests {

    private var variableValues: MutableMap<String, RuleVariableValue> = HashMap()
    private val countIfValueFunction = RuleFunctionCountIfValue.create()

    @Test
    fun return_zero_for_non_existing_variable() {
        variableValues = givenAEmptyVariableValues()

        assertThat(countIfValueFunction.evaluate(
                listOf("non existing variable", "value"), variableValues, null), `is`("0"))
    }

    @Test
    fun return_zero_for_empty_value_to_compare() {
        variableValues = givenAEmptyVariableValues()

        assertFailsWith<IllegalArgumentException> {
            countIfValueFunction.evaluate(listOfNotNull("var1", null), variableValues, null)
        }

        assertThat(countIfValueFunction.evaluate(
                listOf("var1", ""), variableValues, null), `is`("0"))
    }

    @Test
    fun return_zero_for_variable_without_values() {
        val variableName = "non_value_var"

        variableValues = givenAVariableValuesAndOneWithoutValue(variableName)

        assertThat(countIfValueFunction.evaluate(
                listOf(variableName, "valueToCompare"), variableValues, null), `is`("0"))
    }

    @Test
    fun return_size_of_matched_values_for_variable_with_value_and_candidates() {
        val variableName = "with_value_var"
        val value = "valueA"

        variableValues = givenAVariableValuesAndOneWithTwoExpectedCountCandidates(variableName, value)

        assertThat(countIfValueFunction.evaluate(
                listOf(variableName, value), variableValues, null), `is`("2"))
    }

    @Test
    fun return_zero_for_variable_with_no_matched_value_and_candidates() {
        val variableName = "with_value_var"
        val value = "valueA"

        variableValues = givenAVariableValuesAndOneWithTwoExpectedCountCandidates(variableName, value)

        assertThat(countIfValueFunction.evaluate(
                listOf(variableName, "NoMatchedValue"), variableValues, null), `is`("0"))
    }

    @Test
    fun return_zero_for_no_matched_variable_with_value_and_without_candidates() {
        val variableName = "with_value_var"
        val value = "valueA"

        variableValues = givenAVariableValuesAndOneWithUndefinedCandidates(variableName, value)

        assertThat(countIfValueFunction.evaluate(
                listOf(variableName, "NoMatchedValue"), variableValues, null), `is`("0"))
    }

    @Test
    fun throw_illegal_argument_exception_when_variable_map_is_null() {
        assertFailsWith<IllegalArgumentException> {
            RuleFunctionCountIfValue.create().evaluate(listOf("variable_name", "Value_to_compare"),
                    null, null)
        }
    }

    @Test
    fun throw_illegal_argument_exception_when_argument_count_is_greater_than_expected() {
        assertFailsWith<IllegalArgumentException> {
            RuleFunctionCountIfValue.create().evaluate(listOf("variable_name", "ded", "5"),
                    variableValues, null)
        }
    }

    @Test
    fun throw_illegal_argument_exception_when_arguments_count_is_lower_than_expected() {
        assertFailsWith<IllegalArgumentException> {
            RuleFunctionCountIfValue.create().evaluate(listOf("variable_name"), variableValues, null)
        }
    }

    @Test
    fun throw_illegal_argument_exception_when_arguments_is_empty() {
        assertFailsWith<IllegalArgumentException> {
            RuleFunctionCountIfValue.create().evaluate(ArrayList(), variableValues, null)
        }
    }

    @Test
    fun throw_null_pointer_exception_when_arguments_is_null() {
        assertFailsWith<NullPointerException> {
            RuleFunctionCountIfValue.create().evaluate(null!!, variableValues, null)
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

    private fun givenAVariableValuesAndOneWithTwoExpectedCountCandidates(
            variableNameWithValueAndCandidates: String, valueToCompare: String): MutableMap<String, RuleVariableValue> {
        variableValues["test_variable_one"]

        variableValues[variableNameWithValueAndCandidates] = RuleVariableValueBuilder.create()
                .withValue(valueToCompare)
                .withCandidates(listOf("one", valueToCompare, valueToCompare))
                .build()

        return variableValues
    }

    private fun givenAVariableValuesAndOneWithUndefinedCandidates(
            variableNameWithValueAndNonCandidates: String, valueToCompare: String): MutableMap<String, RuleVariableValue> {
        variableValues["test_variable_one"]

        variableValues[variableNameWithValueAndNonCandidates] = RuleVariableValueBuilder.create()
                .withValue(valueToCompare)
                .build()

        return variableValues
    }
}
