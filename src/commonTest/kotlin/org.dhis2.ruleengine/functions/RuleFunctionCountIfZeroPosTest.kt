package org.dhis2.ruleengine.functions

import org.dhis2.ruleengine.RuleVariableValue
import org.dhis2.ruleengine.RuleVariableValueBuilder
import org.dhis2.ruleengine.exprk.functions.CountIfZeroPos
import org.dhis2.ruleengine.models.RuleValueType
import kotlin.test.Test
import kotlin.test.assertEquals

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

class RuleFunctionCountIfZeroPosTest {

    @Test
    fun return_zero_for_non_existing_variable() {
        assertCountValue("nonexisting", givenAEmptyVariableValues(), "0")
    }

    @Test
    fun return_zero_for_variable_without_values() {
        val variableName = "non_value_var"
        val variableValues: Map<String, RuleVariableValue> = givenAVariableValuesAndOneWithoutValue(variableName)
        assertCountValue(variableName, variableValues, "0")
    }

    @Test
    fun return_size_of_zero_or_positive_values_for_variable_with_value_and_candidates() {
        val variableName = "with_value_var"
        val variableValues: Map<String, RuleVariableValue> = givenAVariableValuesAndOneWithCandidates(
            variableName, listOf<String>("0", "-1", "2")
        )
        assertCountValue(variableName, variableValues, "2")
    }

    @Test
    fun return_zero_for_non_zero_or_positive_values_for_variable_with_value_and_candidates() {
        val variableName = "with_value_var"
        val variableValues: Map<String, RuleVariableValue> = givenAVariableValuesAndOneWithCandidates(
            variableName, listOf<String>("-1", "-6")
        )
        assertCountValue(variableName, variableValues, "0")
    }

    @Test
    fun return_zero_for_non_zero_or_positive_value_for_variable_with_value_and_without_candidates() {
        val variableName = "with_value_var"
        val variableValues: Map<String, RuleVariableValue> = givenAVariableValuesAndOneWithUndefinedCandidates(
            variableName,
            "-10"
        )
        assertCountValue(variableName, variableValues, "0")
    }

    private fun givenAEmptyVariableValues(): Map<String, RuleVariableValue> {
        return mapOf()
    }

    private fun givenAVariableValuesAndOneWithoutValue(
        variableNameWithoutValue: String
    ): Map<String, RuleVariableValue> {
        val variableValues: MutableMap<String, RuleVariableValue> = mutableMapOf()
        variableValues[variableNameWithoutValue] = RuleVariableValue(
            ruleValueType = RuleValueType.TEXT
        )
        variableValues["test_variable_two"] = RuleVariableValue(
            variableValue = "Value two",
            ruleValueType = RuleValueType.TEXT
        )
        return variableValues
    }

    private fun givenAVariableValuesAndOneWithCandidates(
        variableNameWithValueAndCandidates: String, candidates: List<String>
    ): Map<String, RuleVariableValue> {
        val variableValues: MutableMap<String, RuleVariableValue> = mutableMapOf()
        variableValues["test_variable_one"] =  RuleVariableValue(
            ruleValueType = RuleValueType.TEXT
        )
        variableValues[variableNameWithValueAndCandidates] = RuleVariableValue(
            variableValue = candidates[0],
            candidates = candidates,
            ruleValueType = RuleValueType.TEXT
        )
        return variableValues
    }

    private fun givenAVariableValuesAndOneWithUndefinedCandidates(
        variableNameWithValueAndNonCandidates: String, value: String
    ): Map<String, RuleVariableValue> {
        val variableValues: MutableMap<String, RuleVariableValue> = mutableMapOf()
        variableValues["test_variable_one"] = RuleVariableValue(
            ruleValueType = RuleValueType.TEXT
        )
        variableValues[variableNameWithValueAndNonCandidates] = RuleVariableValue(
            variableValue = value,
            ruleValueType = RuleValueType.TEXT
        )
        return variableValues
    }

    private fun assertCountValue(value: String, valueMap: Map<String, RuleVariableValue>, countValue: String) {
        assertEquals(
            countValue,
            CountIfZeroPos { valueMap }.call(listOf(value))
        )
    }
}