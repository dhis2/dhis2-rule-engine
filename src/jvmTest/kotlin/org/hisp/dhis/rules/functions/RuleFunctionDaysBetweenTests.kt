package org.hisp.dhis.rules.functions

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hisp.dhis.rules.RuleVariableValue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertFailsWith

@RunWith(JUnit4::class)
class RuleFunctionDaysBetweenTests {

    private val variableValues = HashMap<String, RuleVariableValue>()
    private val daysBetween = RuleFunctionDaysBetween.create()

    @Test
    fun throw_runtime_exception_if_first_date_is_invalid() {
        assertFailsWith<java.lang.RuntimeException> {
            daysBetween.evaluate(listOf("bad date", "2010-01-01"), variableValues, null)
        }

    }

    @Test
    fun throw_runtime_exception_if_second_date_is_invalid() {
        assertFailsWith<RuntimeException> {
            daysBetween.evaluate(listOf("2010-01-01", "bad date"), variableValues, null)
        }

    }

    @Test
    fun return_zero_if_some_date_is_not_present() {

        assertThat(daysBetween.evaluate(listOf(null, null), variableValues, null),
                `is`("0"))
        assertThat(daysBetween.evaluate(listOf(null, ""), variableValues, null), `is`("0"))
        assertThat(daysBetween.evaluate(listOf("", null), variableValues, null), `is`("0"))
        assertThat(daysBetween.evaluate(listOf("2010-10-15", "2010-10-20"), variableValues, null),
                `is`("5"))
        assertThat(daysBetween.evaluate(listOf("2010-09-30", "2010-10-15"), variableValues, null),
                `is`("15"))
        assertThat(daysBetween.evaluate(listOf("2010-12-31", "2011-01-01"), variableValues, null),
                `is`("1"))

        assertThat(daysBetween.evaluate(listOf("2010-10-20", "2010-10-15"), variableValues, null),
                `is`("-5"))
        assertThat(daysBetween.evaluate(listOf("2010-10-15", "2010-09-30"), variableValues, null),
                `is`("-15"))
        assertThat(daysBetween.evaluate(listOf("2011-01-01", "2010-12-31"), variableValues, null),
                `is`("-1"))
        assertThat(daysBetween.evaluate(listOf("2017-01-01", "2017-02-01"), variableValues, null),
                `is`("31"))
        assertThat(daysBetween.evaluate(listOf("2019-01-01", "2020-01-01"), variableValues, null),
                `is`("365"))
        // leap year
        assertThat(daysBetween.evaluate(listOf("2020-01-01", "2021-01-01"), variableValues, null),
                `is`("366"))
    }

    @Test
    fun throw_illegal_argument_exception_when_argument_count_is_greater_than_expected() {
        assertFailsWith<IllegalArgumentException> {
            RuleFunctionDaysBetween.create().evaluate(
                    listOf("2016-01-01", "2016-01-01", "2016-01-01"),
                    variableValues, null)

        }
    }

    @Test
    fun throw_illegal_argument_exception_when_arguments_count_is_lower_than_expected() {
        assertFailsWith<IllegalArgumentException> {
            RuleFunctionDaysBetween.create().evaluate(listOf("2016-01-01"), variableValues, null)

        }
    }

    @Test
    fun throw_null_pointer_exception_when_arguments_is_null() {
        assertFailsWith<NullPointerException> {
            RuleFunctionDaysBetween.create().evaluate(null!!, variableValues, null)

        }
    }
}
