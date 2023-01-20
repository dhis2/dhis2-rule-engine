package org.dhis2.ruleengine.functions

import org.dhis2.ruleengine.RuleEngineTestUtils.assertThrowsIllegalArgumentException
import org.dhis2.ruleengine.exprk.functions.MonthsBetween
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


class RuleFunctionMonthsBetweenTest {

    @Test
    fun return_zero_if_some_date_is_not_present() {
        assertMonthsBetween(null, null, "0")
        assertMonthsBetween(null, "", "0")
        assertMonthsBetween("", null, "0")
        assertMonthsBetween("", "", "0")
    }

    @Test
    fun return_difference_of_months_of_two_dates() {
        assertMonthsBetween("2010-10-15", "2010-10-22", "0")
        assertMonthsBetween("2010-09-30", "2010-10-31", "1")
        assertMonthsBetween("2013-01-31", "2013-02-01", "0")
        assertMonthsBetween("2016-01-01", "2016-07-31", "6")
        assertMonthsBetween("2015-01-01", "2016-06-30", "17")
        assertMonthsBetween("2010-10-22", "2010-10-15", "0")
        assertMonthsBetween("2010-10-31", "2010-09-30", "-1")
        assertMonthsBetween("2013-02-01", "2013-01-31", "0")
        assertMonthsBetween("2016-07-31", "2016-01-01", "-6")
        assertMonthsBetween("2016-06-30", "2015-01-01", "-17")
    }

    @Test
    fun throw_illegal_argument_exception_if_first_date_is_invalid() {
        assertThrowsIllegalArgumentException {
            assertMonthsBetween("bad date", "2010-01-01", null)
        }
    }

    @Test
    fun throw_illegal_argument_exception_if_second_date_is_invalid() {
        assertThrowsIllegalArgumentException {
            assertMonthsBetween("2010-01-01", "bad date", null)
        }

    }

    @Test
    fun throw_illegal_argument_exception_if_first_and_second_date_is_invalid() {
        assertThrowsIllegalArgumentException {
            assertMonthsBetween("bad date", "bad date", null)
        }
    }

    private fun assertMonthsBetween(startDate: String?, endDate: String?, monthsBetween: String?) {
        assertTrue {
            MonthsBetween().call(listOf(startDate, endDate)) == monthsBetween
        }
    }
}