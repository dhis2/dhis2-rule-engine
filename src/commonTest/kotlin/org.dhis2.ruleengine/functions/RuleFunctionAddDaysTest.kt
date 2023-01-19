package org.dhis2.ruleengine.functions

import org.dhis2.ruleengine.exprk.functions.AddDays
import kotlin.test.Test
import kotlin.test.assertTrue


class RuleFunctionAddDaysTest {
    private lateinit var mockedDateExpr: String
    private lateinit var mockedIntExpr: String
    private val addDaysFunction = AddDays()

    @Test
    fun return_new_date_with_days_added() {
        mockedDateExpr = "2011-01-01"
        mockedIntExpr = "6.0"
        assertTrue { addDaysFunction.call(listOf(mockedDateExpr, mockedIntExpr)) == "2011-01-07" }
        mockedDateExpr = "2010-10-10"
        mockedIntExpr = "1"
        assertTrue { addDaysFunction.call(listOf(mockedDateExpr, mockedIntExpr)) == "2010-10-11" }
        mockedDateExpr = "2010-10-10"
        mockedIntExpr = "1.3"
        assertTrue { addDaysFunction.call(listOf(mockedDateExpr, mockedIntExpr)) == "2010-10-11" }
        mockedDateExpr = "2010-10-31"
        mockedIntExpr = "1"
        assertTrue { addDaysFunction.call(listOf(mockedDateExpr, mockedIntExpr)) == "2010-11-01" }
        mockedDateExpr = "2010-12-01"
        mockedIntExpr = "31"
        assertTrue { addDaysFunction.call(listOf(mockedDateExpr, mockedIntExpr)) == "2011-01-01" }
    }

    @Test
    fun throw_runtime_exception_if_first_argument_is_invalid() {
        mockedDateExpr = "bad date"
        mockedIntExpr = "6"
        try {
            addDaysFunction.call(listOf(mockedDateExpr, mockedIntExpr))
        } catch (e: Exception) {
            assertTrue { e is IllegalArgumentException }
        }
    }

    @Test
    fun throw_illegal_argument_exception_if_second_argument_is_invalid() {
        mockedDateExpr = "2010-01-01"
        mockedIntExpr = "bad number"
        try {
            addDaysFunction.call(listOf(mockedDateExpr, mockedIntExpr))
        } catch (e: Exception) {
            assertTrue { e is IllegalArgumentException }
        }
    }

    @Test
    fun throw_illegal_argument_exception_if_first_and_second_argument_is_invalid() {
        mockedDateExpr = "bad date"
        mockedIntExpr = "bad number"
        try {
            addDaysFunction.call(listOf(mockedDateExpr, mockedIntExpr))
        } catch (e: Exception) {
            assertTrue { e is IllegalArgumentException }
        }
    }
}