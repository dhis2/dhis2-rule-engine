package org.dhis2.ruleengine.functions

import org.dhis2.ruleengine.RuleVariableValue
import org.dhis2.ruleengine.exprk.functions.MinValue
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

/**
 * @author Zubair Asghar.
 */

class RuleFunctionMinValueTest {

    @Test
    fun return_Min_Value() {
        val variableNameOne = "test_variable_one"
        val value = "5.0"
        val variableValues: MutableMap<String, RuleVariableValue> = mutableMapOf()
        variableValues[variableNameOne] = RuleVariableValue(
            variableValue = value,
            candidates = listOf(value, "6", "7"),
            ruleValueType = RuleValueType.TEXT
        )
        assertMinValue(variableNameOne, variableValues, "5.0")
    }

    @Test
    fun return_Empty_String_If_Value_Absent() {
        val variableNameOne = "test_variable_one"
        assertMinValue(variableNameOne, emptyMap(), "")
    }

    private fun assertMinValue(value: String, valueMap: Map<String, RuleVariableValue>, minValue: String) {
        assertTrue {
            MinValue(valueMap).call(listOf(value)) == minValue
        }
    }
}