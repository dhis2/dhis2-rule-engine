package org.hisp.dhis.rules.functions

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hisp.dhis.rules.RuleVariableValue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.lang.NullPointerException
import kotlin.test.assertFailsWith

@RunWith(JUnit4::class)
class RuleFunctionCeilTests {

    private val variableValues = HashMap<String, RuleVariableValue>()

    @Test
    fun evaluateMustReturnCeilValue() {
        val ceil = RuleFunctionCeil.create()

        assertThat(ceil.evaluate(listOf("0"), variableValues, null), `is`("0"))
        assertThat(ceil.evaluate(listOf("0.8"), variableValues, null), `is`("1"))
        assertThat(ceil.evaluate(listOf("5.1"), variableValues, null), `is`("6"))
        assertThat(ceil.evaluate(listOf("1"), variableValues, null), `is`("1"))
        assertThat(ceil.evaluate(listOf("-9.3"), variableValues, null), `is`("-9"))
        assertThat(ceil.evaluate(listOf("-5.9"), variableValues, null), `is`("-5"))
    }

    @Test
    fun throw_illegal_argument_exception_when_argument_count_is_greater_than_expected() {
        assertFailsWith<IllegalArgumentException> {
            RuleFunctionCeil.create().evaluate(listOf("5.9", "6.8"), variableValues, null)
        }
    }

    @Test
    fun throw_illegal_argument_exception_when_arguments_count_is_lower_than_expected() {
        assertFailsWith<IllegalArgumentException> {
            RuleFunctionCeil.create().evaluate(emptyList(), variableValues, null)
        }
    }

    @Test
    fun throw_null_pointer_exception_when_arguments_is_null() {
        assertFailsWith<NullPointerException> {
            RuleFunctionCeil.create().evaluate(null!!, variableValues, null)
        }
    }
}
