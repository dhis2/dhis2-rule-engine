package org.hisp.dhis.rules.functions


import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hisp.dhis.rules.RuleVariableValue
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertFailsWith

@RunWith(JUnit4::class)
class RuleFunctionAddDaysTests {


    private val variableValues = HashMap<String, RuleVariableValue>()

    @Test
    fun return_new_date_with_days_added() {
        val addDaysFunction = RuleFunctionAddDays.create()

        assertThat(addDaysFunction.evaluate(listOf("2011-01-01", "6"), variableValues, null),
                `is`("'2011-01-07'"))
        assertThat(addDaysFunction.evaluate(listOf("2010-10-10", "1"), variableValues, null),
                `is`("'2010-10-11'"))
        assertThat(addDaysFunction.evaluate(listOf("2010-10-31", "1"), variableValues, null),
                `is`("'2010-11-01'"))
        assertThat(addDaysFunction.evaluate(listOf("2010-12-01", "31"), variableValues, null),
                `is`("'2011-01-01'"))
    }

    @Test
    fun throw_runtime_exception_if_first_argument_is_invalid() {
        val addDaysFunction = RuleFunctionAddDays.create()
        assertFailsWith<RuntimeException> {
            addDaysFunction.evaluate(listOf("bad date", "2"), variableValues, null)
        }

    }

    @Test
    fun throw_illegal_argument_exception_if_second_argument_is_invalid() {
        val addDaysFunction = RuleFunctionAddDays.create()
        assertFailsWith<IllegalArgumentException> {
            addDaysFunction.evaluate(listOf("2010-01-01", "bad number"), variableValues, null)
        }
    }

    @Test
    fun throw_illegal_argument_exception_if_first_and_second_argument_is_invalid() {
        assertFailsWith<RuntimeException> {
            RuleFunctionAddDays.create().evaluate(listOf("bad date", "bad number"), HashMap(), null)
        }
    }

    @Test
    fun throw_illegal_argument_exception_when_argument_count_is_greater_than_expected() {
        assertFailsWith<IllegalArgumentException> {
            RuleFunctionAddDays.create().evaluate(listOf("2016-01-01", "2", "2016-01-01"), variableValues, null)
        }
    }

    @Test
    fun throw_illegal_argument_exception_when_arguments_count_is_lower_than_expected() {
        assertFailsWith<IllegalArgumentException> {
            RuleFunctionAddDays.create().evaluate(listOf("2016-01-01"), variableValues, null)
        }
    }

    @Test
    fun throw_null_pointer_exception_when_arguments_is_null() {
        assertFailsWith<NullPointerException> {
            RuleFunctionAddDays.create().evaluate(null!!, variableValues, null)
        }
    }
}
