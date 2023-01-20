package org.dhis2.ruleengine.functions

import org.dhis2.ruleengine.RuleEngineTestUtils
import org.dhis2.ruleengine.exprk.functions.WeeksBetween
import kotlin.test.Test
import kotlin.test.assertTrue

class RuleFunctionWeeksBetweenTest {

    @Test
    fun return_zero_if_some_date_is_not_present() {
        assertWeeksBetween(null, null, "0")
        assertWeeksBetween(null, "", "0")
        assertWeeksBetween("", null, "0")
        assertWeeksBetween("", "", "0")
    }

    @Test
    fun return_difference_of_weeks_of_two_dates() {
        assertWeeksBetween("2010-10-15", "2010-10-22", "1")
        assertWeeksBetween("2010-09-30", "2010-10-15", "2")
        assertWeeksBetween("2016-01-01", "2016-01-31", "4")
        assertWeeksBetween("2010-12-31", "2011-01-01", "0")
        assertWeeksBetween("2010-10-22", "2010-10-15", "-1")
        assertWeeksBetween("2010-10-15", "2010-09-30", "-2")
        assertWeeksBetween("2016-01-31", "2016-01-01", "-4")
        assertWeeksBetween("2011-01-01", "2010-12-31", "0")
    }

    @Test
    fun throw_illegal_argument_exception_if_first_date_is_invalid() {
        RuleEngineTestUtils.assertThrowsIllegalArgumentException {
            assertWeeksBetween("bad date", "2010-01-01", null)
        }
    }

    @Test
    fun throw_illegal_argument_exception_if_second_date_is_invalid() {
        RuleEngineTestUtils.assertThrowsIllegalArgumentException {
            assertWeeksBetween("2010-01-01", "bad date", null)
        }
    }

    @Test
    fun throw_illegal_argument_exception_if_first_and_second_date_is_invalid() {
        RuleEngineTestUtils.assertThrowsIllegalArgumentException {
            assertWeeksBetween("bad date", "bad date", null)
        }
    }

    private fun assertWeeksBetween(startDate: String?, endDate: String?, weeksBetween: String?) {
        assertTrue {
            WeeksBetween().call(listOf(startDate, endDate)) == weeksBetween
        }
    }
}