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
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertFailsWith


@RunWith(JUnit4::class)
class RuleFunctionValidatePatternTests {

    private val variableValues = hashMapOf<String, RuleVariableValue>()

    private val validatePattern = RuleFunctionValidatePattern.create()

    @Test
    fun return_true_if_pattern_match() {

        assertThat(validatePattern.evaluate(listOf("123", "123"), variableValues, null), `is`("'true'"))
        assertThat(validatePattern.evaluate(listOf("27123456789", "27\\d{2}\\d{3}\\d{4}"), variableValues, null), `is`("'true'"))
        assertThat(validatePattern.evaluate(listOf("27123456789", "27\\d{9}"), variableValues, null), `is`("'true'"))
        assertThat(validatePattern.evaluate(listOf("abc123", "abc123"), variableValues, null), `is`("'true'"))
        assertThat(validatePattern.evaluate(listOf("9999/99/9", "\\d{4}/\\d{2}/\\d"), variableValues, null), `is`("'true'"))
        assertThat(validatePattern.evaluate(listOf("9999/99/9", "[0-9]{4}/[0-9]{2}/[0-9]"), variableValues, null), `is`("'true'"))
    }

    @Test
    fun return_false_for_non_matching_pairs() {

        assertThat(validatePattern.evaluate(listOf("1999/99/9", "\\[9]{4}/\\d{2}/\\d"), variableValues, null), `is`("'false'"))
        assertThat(validatePattern.evaluate(listOf("9999/99/", "[0-9]{4}/[0-9]{2}/[0-9]"), variableValues, null), `is`("'false'"))
        assertThat(validatePattern.evaluate(listOf("abc123", "xyz"), variableValues, null), `is`("'false'"))
        assertThat(validatePattern.evaluate(listOf("abc123", "^bc"), variableValues, null), `is`("'false'"))
        assertThat(validatePattern.evaluate(listOf("abc123", "abc12345"), variableValues, null), `is`("'false'"))
        assertThat(validatePattern.evaluate(listOf("123", "567"), variableValues, null), `is`("'false'"))
    }

    @Test
    fun throw_illegal_argument_exception_when_argument_count_is_greater_than_expected() {
        assertFailsWith<IllegalArgumentException> {
            RuleFunctionValidatePattern.create().evaluate(listOf("\\d{4}/\\d{2}/\\d", "9999/99/9", "2016-01-01"), variableValues, null)
        }
    }

    @Test
    fun throw_illegal_argument_exception_when_arguments_count_is_lower_than_expected() {
        assertFailsWith<IllegalArgumentException> {
            RuleFunctionValidatePattern.create().evaluate(listOf("\\d{4}/\\d{2}/\\d"), variableValues, null)
        }
    }

    @Test
    fun throw_null_pointer_exception_when_arguments_is_null() {
        assertFailsWith<NullPointerException> {
            RuleFunctionValidatePattern.create().evaluate(null!!, variableValues, null)
        }
    }
}
