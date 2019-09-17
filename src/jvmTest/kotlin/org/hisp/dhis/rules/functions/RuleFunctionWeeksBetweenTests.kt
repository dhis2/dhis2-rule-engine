package org.hisp.dhis.rules.functions

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hisp.dhis.rules.RuleVariableValue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertFailsWith

@RunWith(JUnit4::class)
class RuleFunctionWeeksBetweenTests {

    private val variableValues = hashMapOf<String, RuleVariableValue>()

    private val weeksBetween = RuleFunctionWeeksBetween.create()

    @Test
    fun return_zero_if_some_date_is_not_present() {


        assertThat(weeksBetween.evaluate(listOf(null, null), variableValues, null), `is`("0"))
        assertThat(weeksBetween.evaluate(listOf(null, ""), variableValues, null), `is`("0"))
        assertThat(weeksBetween.evaluate(listOf("", null), variableValues, null), `is`("0"))
        assertThat(weeksBetween.evaluate(listOf("", ""), variableValues, null), `is`("0"))
    }

    @Test
    fun return_difference_of_weeks_of_two_dates() {

        assertThat(weeksBetween.evaluate(listOf("2010-10-15", "2010-10-22"), variableValues, null), `is`("1"))
        assertThat(weeksBetween.evaluate(listOf("2010-09-30", "2010-10-15"), variableValues, null), `is`("2"))
        assertThat(weeksBetween.evaluate(listOf("2016-01-01", "2016-01-31"), variableValues, null), `is`("4"))
        assertThat(weeksBetween.evaluate(listOf("2010-12-31", "2011-01-01"), variableValues, null), `is`("0"))

        assertThat(weeksBetween.evaluate(listOf("2010-10-22", "2010-10-15"), variableValues, null), `is`("-1"))
        assertThat(weeksBetween.evaluate(listOf("2010-10-15", "2010-09-30"), variableValues, null), `is`("-2"))
        assertThat(weeksBetween.evaluate(listOf("2016-01-31", "2016-01-01"), variableValues, null), `is`("-4"))
        assertThat(weeksBetween.evaluate(listOf("2011-01-01", "2010-12-31"), variableValues, null), `is`("0"))
    }

    @Test
    fun throw_illegal_argument_exception_if_first_date_is_invalid() {
        assertFailsWith<RuntimeException> {
            weeksBetween.evaluate(listOf("bad date", "2010-01-01"), variableValues, null)
        }
    }

    @Test
    fun throw_illegal_argument_exception_if_second_date_is_invalid() {
        assertFailsWith<RuntimeException> {
            weeksBetween.evaluate(listOf("2010-01-01", "bad date"), variableValues, null)
        }
    }

    @Test
    fun throw_illegal_argument_exception_if_first_and_second_date_is_invalid() {
        assertFailsWith<RuntimeException> {
            RuleFunctionWeeksBetween.create().evaluate(listOf("bad date", "bad date"), HashMap(), null)
        }
    }

    @Test
    fun throw_illegal_argument_exception_when_argument_count_is_greater_than_expected() {
        assertFailsWith<IllegalArgumentException> {
            RuleFunctionWeeksBetween.create().evaluate(listOf("2016-01-01", "2016-01-01", "2016-01-01"), variableValues, null)
        }
    }

    @Test
    fun throw_illegal_argument_exception_when_arguments_count_is_lower_than_expected() {
        assertFailsWith<IllegalArgumentException> {
            RuleFunctionWeeksBetween.create().evaluate(listOf("2016-01-01"), variableValues, null)
        }
    }

    @Test
    fun throw_null_pointer_exception_when_arguments_is_null() {
        assertFailsWith<NullPointerException> {
            RuleFunctionWeeksBetween.create().evaluate(null!!, variableValues, null)
        }
    }
}
