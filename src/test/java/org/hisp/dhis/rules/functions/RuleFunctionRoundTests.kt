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
class RuleFunctionRoundTests {

    private val variableValues = hashMapOf<String, RuleVariableValue>()

    private val roundFunction = RuleFunctionRound.create()

    @Test
    fun return_argument_rounded_up_to_nearest_whole_number() {

        assertThat(roundFunction.evaluate(listOf("0"), variableValues, null), `is`("0"))
        assertThat(roundFunction.evaluate(listOf("0.8"), variableValues, null), `is`("1"))
        assertThat(roundFunction.evaluate(listOf("0.4999"), variableValues, null), `is`("0"))
        assertThat(roundFunction.evaluate(listOf("0.5001"), variableValues, null), `is`("1"))
        assertThat(roundFunction.evaluate(listOf("-9.3"), variableValues, null), `is`("-9"))
        assertThat(roundFunction.evaluate(listOf("-9.8"), variableValues, null), `is`("-10"))
        assertThat(roundFunction.evaluate(listOf(null), variableValues, null), `is`("0"))
    }

    @Test
    fun throw_illegal_argument_exception_when_argument_count_is_greater_than_expected() {
        assertFailsWith<IllegalArgumentException> {
            RuleFunctionRound.create().evaluate(listOf("5.9", "6.8"), variableValues, null)
        }
    }

    @Test
    fun throw_illegal_argument_exception_when_arguments_count_is_lower_than_expected() {
        assertFailsWith<IllegalArgumentException> {
            RuleFunctionRound.create().evaluate(listOf(), variableValues, null)
        }
    }

    @Test
    fun throw_null_pointer_exception_when_arguments_is_null() {
        assertFailsWith<NullPointerException> {
            RuleFunctionRound.create().evaluate(null!!, variableValues, null)
        }
    }
}
