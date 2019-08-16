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
import java.util.*
import kotlin.test.assertFailsWith

/**
 * @Author Zubair Asghar.
 */

@RunWith(JUnit4::class)
class RuleFunctionCountIfZeroPosTests {

    private var variableValues: MutableMap<String, RuleVariableValue> = HashMap()
    private val ifZeroPosFunction = RuleFunctionCountIfZeroPos.create()

    @Test
    fun return_zero_for_non_existing_variable() {
        variableValues = givenAEmptyVariableValues()

        assertThat(ifZeroPosFunction.evaluate(
                listOf("nonexisting"), variableValues, null), `is`("0"))
    }

    @Test
    fun return_zero_for_variable_without_values() {
        val variableName = "non_value_var"

        variableValues = givenAVariableValuesAndOneWithoutValue(variableName)

        assertThat(ifZeroPosFunction.evaluate(
                listOf(variableName), variableValues, null), `is`("0"))
    }

    @Test
    fun return_size_of_zero_or_positive_values_for_variable_with_value_and_candidates() {
        val variableName = "with_value_var"

        variableValues = givenAVariableValuesAndOneWithCandidates(variableName, listOf("0", "-1", "2"))

        assertThat(ifZeroPosFunction.evaluate(
                listOf(variableName), variableValues, null), `is`("2"))
    }

    @Test
    fun return_zero_for_non_zero_or_positive_values_for_variable_with_value_and_candidates() {
        val variableName = "with_value_var"

        variableValues = givenAVariableValuesAndOneWithCandidates(
                variableName, listOf("-1", "-6"))

        assertThat(ifZeroPosFunction.evaluate(
                listOf(variableName), variableValues, null), `is`("0"))
    }

    @Test
    fun return_zero_for_non_zero_or_positive_value_for_variable_with_value_and_without_candidates() {
        val variableName = "with_value_var"

        variableValues = givenAVariableValuesAndOneWithUndefinedCandidates(variableName, "-10")

        assertThat(ifZeroPosFunction.evaluate(
                listOf(variableName), variableValues, null), `is`("0"))
    }

    @Test
    fun throw_illegal_argument_exception_when_variable_map_is_null() {
        assertFailsWith<IllegalArgumentException> {
            RuleFunctionCountIfZeroPos.create().evaluate(listOf("variable_name"), null, null)
        }
    }

    @Test
    fun throw_illegal_argument_exception_when_arguments_count_is_lower_than_expected() {
        assertFailsWith<IllegalArgumentException> {
            RuleFunctionCountIfZeroPos.create().evaluate(ArrayList(), variableValues, null)
        }
    }

    @Test
    fun throw_null_pointer_exception_when_arguments_is_null() {
        assertFailsWith<NullPointerException> {
            RuleFunctionCountIfZeroPos.create().evaluate(null!!, variableValues, null)
        }
    }

    @Test
    fun throw_illegal_argument_exception_when_argument_count_is_greater_than_expected() {
        assertFailsWith<IllegalArgumentException> {
            RuleFunctionCountIfZeroPos.create().evaluate(listOf("variable_name", "6.8"),
                    variableValues, null)
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

    private fun givenAVariableValuesAndOneWithCandidates(
            variableNameWithValueAndCandidates: String, candidates: List<String>): MutableMap<String, RuleVariableValue> {
        variableValues["test_variable_one"]

        variableValues[variableNameWithValueAndCandidates] = RuleVariableValueBuilder.create()
                .withValue(candidates[0])
                .withCandidates(candidates)
                .build()

        return variableValues
    }

    private fun givenAVariableValuesAndOneWithUndefinedCandidates(
            variableNameWithValueAndNonCandidates: String, value: String): MutableMap<String, RuleVariableValue> {
        variableValues["test_variable_one"]

        variableValues[variableNameWithValueAndNonCandidates] = RuleVariableValueBuilder.create()
                .withValue(value)
                .build()

        return variableValues
    }
}
