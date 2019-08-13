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
class RuleFunctionConcatenateTests {

    private val variableValues = HashMap<String, RuleVariableValue>()

    @Test
    fun evaluateD2Concatenate() {
        val concatenateFunction = RuleFunctionConcatenate.create()

        assertThat(concatenateFunction.evaluate(listOf("hello"), variableValues, null),
                `is`("'hello'"))
        assertThat(concatenateFunction.evaluate(listOfNotNull("hello", null), variableValues, null),
                `is`("'hello'"))
        assertThat(
                concatenateFunction.evaluate(listOfNotNull(null, null), variableValues, null),
                `is`("''"))
        assertThat(
                concatenateFunction.evaluate(listOf("hello", " ", "there", "!"), variableValues, null),
                `is`("'hello there!'"))
    }

    @Test
    fun throw_null_pointer_exception_when_arguments_is_null() {
       assertFailsWith<NullPointerException> {
           RuleFunctionConcatenate.create().evaluate(null!!, variableValues, null)
       }
    }
}
