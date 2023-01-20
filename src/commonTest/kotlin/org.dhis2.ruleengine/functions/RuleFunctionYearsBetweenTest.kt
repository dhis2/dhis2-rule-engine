package org.dhis2.ruleengine.functions

import org.dhis2.ruleengine.RuleEngineTestUtils
import org.dhis2.ruleengine.exprk.functions.YearsBetween
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


class RuleFunctionYearsBetweenTest {

    @Test
    fun return_empty_if_some_date_is_not_present() {
        assertYearsBetween(null, null, "0")
        assertYearsBetween(null, "", "0")
        assertYearsBetween("", null, "0")
        assertYearsBetween("", "", "0")
    }

    @Test
    fun throw_illegal_argument_exception_if_first_date_is_invalid() {
        RuleEngineTestUtils.assertThrowsIllegalArgumentException {
            assertYearsBetween("bad date", "2010-01-01", null)
        }
    }

    @Test
    fun throw_illegal_argument_exception_if_second_date_is_invalid() {
        RuleEngineTestUtils.assertThrowsIllegalArgumentException {
            assertYearsBetween("2010-01-01", "bad date", null)
        }
    }

    @Test
    fun throw_illegal_argument_exception_if_first_and_second_date_is_invalid() {
        RuleEngineTestUtils.assertThrowsIllegalArgumentException {
            assertYearsBetween("bad date", "bad date", null)
        }
    }

    @Test
    fun return_difference_of_years_of_two_dates() {
        assertYearsBetween("2010-10-15", "2010-10-22", "0")
        assertYearsBetween("2010-09-30", "2011-10-31", "1")
        assertYearsBetween("2015-01-01", "2016-06-30", "1")
        assertYearsBetween("2010-01-01", "2016-06-30", "6")
        assertYearsBetween("2010-10-22", "2010-10-15", "0")
        assertYearsBetween("2011-10-31", "2010-09-30", "-1")
        assertYearsBetween("2016-06-30", "2015-01-01", "-1")
        assertYearsBetween("2016-06-30", "2010-01-01", "-6")
        assertYearsBetween("2017-02-27", "2018-02-26", "0")
        assertYearsBetween("2017-02-27", "2018-02-27", "1")
        assertYearsBetween("2017-02-27", "2018-02-28", "1")
        assertYearsBetween("2015-02-27", "2018-02-27", "3")
        assertYearsBetween("2018-06-04", "2019-01-04", "0")
        assertYearsBetween("2019-10-10", "1995-11-02", "-23")
        assertYearsBetween("1995-11-02", "2019-10-10", "23")
    }

    private fun assertYearsBetween(startDate: String?, endDate: String?, yearsBetween: String?) {
        assertTrue {
            YearsBetween().call(listOf(startDate, endDate)) == yearsBetween
        }
    }
}