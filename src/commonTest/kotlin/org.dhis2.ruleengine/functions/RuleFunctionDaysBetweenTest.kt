package org.dhis2.ruleengine.functions

import org.dhis2.ruleengine.exprk.functions.DaysBetween
import kotlin.test.Test
import kotlin.test.assertTrue


class RuleFunctionDaysBetweenTest {

    @Test
    fun return_zero_if_some_date_is_not_present() {
        assertDaysBetween(null, null, "0")
        assertDaysBetween(null, "", "0")
        assertDaysBetween("", null, "0")
        assertDaysBetween("", "", "0")
    }

    @Test
    fun evaluate_correct_number_of_days() {
        assertDaysBetween("2010-10-15", "2010-10-20", "5")
        assertDaysBetween("2010-09-30", "2010-10-15", "15")
        assertDaysBetween("2010-12-31", "2011-01-01", "1")
        assertDaysBetween("2010-10-20", "2010-10-15", "-5")
        assertDaysBetween("2010-10-15", "2010-09-30", "-15")
        assertDaysBetween("2011-01-01", "2010-12-31", "-1")
    }

    @Test
    fun throw_illegal_argument_exception_if_first_date_is_invalid() {
        try {
            assertDaysBetween("bad date", "2010-01-01", null)
        } catch (e: Exception) {
            assertTrue { e is IllegalArgumentException }
        }
    }

    @Test
    fun throw_illegal_argument_exception_if_second_date_is_invalid() {
        try {
            assertDaysBetween("2010-01-01", "bad date", null)
        } catch (e: Exception) {
            assertTrue { e is IllegalArgumentException }
        } catch (e: Exception) {
            assertTrue { e is IllegalArgumentException }
        }
    }

    @Test
    fun throw_illegal_argument_exception_if_first_and_second_date_is_invalid() {
        try {
            assertDaysBetween("bad date", "bad date", null)
        } catch (e: Exception) {
            assertTrue { e is IllegalArgumentException }
        } catch (e: Exception) {
            assertTrue { e is IllegalArgumentException }
        }
    }

    private fun assertDaysBetween(startDate: String?, endDate: String?, daysBetween: String?) {
        assertTrue {
            DaysBetween().call(listOf(startDate, endDate)) == daysBetween
        }
    }
}