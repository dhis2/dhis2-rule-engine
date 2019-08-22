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
import java.util.*
import kotlin.test.assertFailsWith


@RunWith(JUnit4::class)
class RuleFunctionMonthsBetweenTests {

    private val variableValues = hashMapOf<String, RuleVariableValue>()

    private val monthsBetween = RuleFunctionMonthsBetween.create()

    @Test
    fun return_zero_if_some_date_is_not_present() {

        assertThat(monthsBetween.evaluate(listOf(null, null), variableValues, null), `is`("0"))
        assertThat(monthsBetween.evaluate(listOf(null, ""), variableValues, null), `is`("0"))
        assertThat(monthsBetween.evaluate(listOf("", null), variableValues, null), `is`("0"))
        assertThat(monthsBetween.evaluate(listOf("", ""), variableValues, null), `is`("0"))
    }

    @Test
    fun return_difference_of_months_of_two_dates() {

        assertThat(monthsBetween.evaluate(listOf("2010-10-15", "2010-10-22"), variableValues, null), `is`("0"))
        assertThat(monthsBetween.evaluate(listOf("2010-09-30", "2010-10-31"), variableValues, null), `is`("1"))
        assertThat(monthsBetween.evaluate(listOf("2013-01-31", "2013-02-01"), variableValues, null), `is`("0"))
        assertThat(monthsBetween.evaluate(listOf("2016-01-01", "2016-07-31"), variableValues, null), `is`("6"))
        assertThat(monthsBetween.evaluate(listOf("2015-01-01", "2016-06-30"), variableValues, null), `is`("17"))

        assertThat(monthsBetween.evaluate(listOf("2010-10-22", "2010-10-15"), variableValues, null), `is`("0"))
        assertThat(monthsBetween.evaluate(listOf("2010-10-31", "2010-09-30"), variableValues, null), `is`("-1"))
        assertThat(monthsBetween.evaluate(listOf("2013-02-01", "2013-01-31"), variableValues, null), `is`("0"))
        assertThat(monthsBetween.evaluate(listOf("2016-07-31", "2016-01-01"), variableValues, null), `is`("-6"))
        assertThat(monthsBetween.evaluate(listOf("2016-06-30", "2015-01-01"), variableValues, null), `is`("-17"))
        assertThat(monthsBetween.evaluate(listOf("2018-06-01", "2017-06-01"), variableValues, null), `is`("-12"))
        assertThat(monthsBetween.evaluate(listOf("2017-06-01", "2018-06-01"), variableValues, null), `is`("12"))
    }

    @Test
    fun throw_illegal_argument_exception_if_first_date_is_invalid() {
        assertFailsWith<IllegalArgumentException> {
            monthsBetween.evaluate(listOf("bad date", "2010-01-01"), variableValues, null)
        }
    }

    @Test
    fun throw_illegal_argument_exception_if_second_date_is_invalid() {
        assertFailsWith<IllegalArgumentException> {
            monthsBetween.evaluate(listOf("2010-01-01", "bad date"), variableValues, null)
        }
    }

    @Test
    fun throw_illegal_argument_exception_if_first_and_second_date_is_invalid() {
        assertFailsWith<RuntimeException> {
            RuleFunctionMonthsBetween.create().evaluate(listOf("bad date", "bad date"), hashMapOf(), null)
        }
    }

    @Test
    fun throw_illegal_argument_exception_when_argument_count_is_greater_than_expected() {
        assertFailsWith<IllegalArgumentException> {
            RuleFunctionMonthsBetween.create().evaluate(listOf("2016-01-01", "2016-01-01", "2016-01-01"), variableValues, null)
        }
    }

    @Test
    fun throw_illegal_argument_exception_when_arguments_count_is_lower_than_expected() {
        assertFailsWith<IllegalArgumentException> {
            RuleFunctionMonthsBetween.create().evaluate(listOf("2016-01-01"), variableValues, null)
        }
    }

    @Test
    fun throw_null_pointer_exception_when_arguments_is_null() {
        assertFailsWith<NullPointerException> {
            RuleFunctionMonthsBetween.create().evaluate(null!!, variableValues, null)
        }
    }
}
