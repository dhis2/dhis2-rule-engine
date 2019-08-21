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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.test.assertFailsWith


class RuleFunctionLastEventDateTests {

    private var valueMap: MutableMap<String, RuleVariableValue> = hashMapOf()

    private val todayDate = SimpleDateFormat(DATE_PATTERN, Locale.US).format(Date())

    private val lastEventDateFunction = RuleFunctionLastEventDate.create()

    @Test
    fun throwExceptionIfArgumentListIsZero() {

        assertFailsWith<IllegalArgumentException> {
            lastEventDateFunction.evaluate(listOf(), valueMap, null)
        }
    }

    @Test
    fun returnNothingWhenValueMapDoesNotHaveValue() {

        assertThat(lastEventDateFunction.evaluate(listOf("test_variable"), valueMap, null), `is`(""))
    }

    @Test
    fun returnLatestDateWhenValueExist() {
        val variableWithValue = "test_variable_one"

        valueMap = getValueMapWithValue(variableWithValue)

        assertThat(lastEventDateFunction.evaluate(listOf(variableWithValue), valueMap, null), `is`("'$todayDate'"))
    }

    private fun getValueMapWithValue(variableNameWithValue: String): MutableMap<String, RuleVariableValue> {
        valueMap[variableNameWithValue] = RuleVariableValueBuilder
                .create()
                .withValue("value")
                .withCandidates(listOf())
                .withEventDate(todayDate).build()

        return valueMap
    }

    companion object {
        private const val DATE_PATTERN = "yyyy-MM-dd"
    }
}
