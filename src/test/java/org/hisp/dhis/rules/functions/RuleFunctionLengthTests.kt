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

import org.hisp.dhis.rules.RuleVariableValue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import java.util.ArrayList
import java.util.Arrays
import java.util.HashMap

import java.util.Arrays.asList
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import kotlin.test.assertFailsWith

@RunWith(JUnit4::class)
class RuleFunctionLengthTests {

    private val variableValues = HashMap<String, RuleVariableValue>()

    private val lengthFunction = RuleFunctionLength.create()

    @Test
    fun return_length_of_argument() {

        assertThat(lengthFunction.evaluate(listOf(""), variableValues, null), `is`("0"))
        assertThat(lengthFunction.evaluate(listOf("abc"), variableValues, null), `is`("3"))
        assertThat(lengthFunction.evaluate(listOf("abcdef"), variableValues, null), `is`("6"))
    }

    @Test
    fun throw_illegal_argument_exception_if_first_parameter_is_empty_list() {
        assertFailsWith<IllegalArgumentException> {
            lengthFunction.evaluate(listOf(), variableValues, null)
        }
    }

    @Test
    fun throw_illegal_argument_exception_when_argument_count_is_greater_than_expected() {
        assertFailsWith<IllegalArgumentException> {
            lengthFunction.evaluate(listOf("cdcdcd", "2"), variableValues, null)
        }
    }

    @Test
    fun throw_null_pointer_exception_when_arguments_is_null() {
        assertFailsWith<NullPointerException> {
            lengthFunction.evaluate(null!!, variableValues, null)
        }
    }
}
