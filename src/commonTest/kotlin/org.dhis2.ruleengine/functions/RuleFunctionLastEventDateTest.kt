package org.dhis2.ruleengine.functions

import kotlinx.datetime.toLocalDate
import org.dhis2.ruleengine.RuleEngineTestUtils.currentDate
import org.dhis2.ruleengine.RuleVariableValue
import org.dhis2.ruleengine.exprk.functions.LastEventDate
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

class RuleFunctionLastEventDateTest {

    @Test
    fun returnNothingWhenValueMapDoesNotHaveValue() {
        val valueMap: Map<String, RuleVariableValue> = emptyValueMap
        assertLastEventDate("test_variable", valueMap, "")
    }

    @Test
    fun returnLatestDateWhenValueExist() {
        val variableWithValue = "test_variable_one"
        val valueMap: Map<String, RuleVariableValue> = getValueMapWithValue(variableWithValue)
        assertLastEventDate(variableWithValue, valueMap, currentDate().toString())
    }

    private val emptyValueMap: Map<String, RuleVariableValue>
        get() = mutableMapOf()

    private fun getValueMapWithValue(variableNameWithValue: String, date: String): Map<String, RuleVariableValue> {
        val valueMap: MutableMap<String, RuleVariableValue> = mutableMapOf()
        valueMap[variableNameWithValue] = RuleVariableValue(
            variableValue = "value",
            eventDate = date.toLocalDate(),
            ruleValueType = RuleValueType.TEXT
        )
        return valueMap
    }

    private fun getValueMapWithValue(variableNameWithValue: String): Map<String, RuleVariableValue> {
        return getValueMapWithValue(variableNameWithValue, currentDate().toString())
    }

    private fun assertLastEventDate(value: String, valueMap: Map<String, RuleVariableValue>, lastEventDate: String) {
        assertTrue {
            LastEventDate{valueMap}.call(listOf(value)) == lastEventDate
        }
    }
}