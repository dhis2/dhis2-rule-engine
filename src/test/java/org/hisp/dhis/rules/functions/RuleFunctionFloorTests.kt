package org.hisp.dhis.rules.functions

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hisp.dhis.rules.RuleVariableValue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*
import kotlin.test.assertFailsWith

@RunWith(JUnit4::class)
class RuleFunctionFloorTests {

    private val variableValues = HashMap<String, RuleVariableValue>()
    private val floorFunction = RuleFunctionFloor.create()

    @Test
    fun return_argument_rounded_down_to_nearest_whole_number() {

        assertThat(floorFunction.evaluate(listOf("0"), variableValues, null), `is`("0"))
        assertThat(floorFunction.evaluate(listOf("0.8"), variableValues, null), `is`("0"))
        assertThat(floorFunction.evaluate(listOf("1.0"), variableValues, null), `is`("1"))
        assertThat(floorFunction.evaluate(listOf("-9.3"), variableValues, null), `is`("-10"))
        assertThat(floorFunction.evaluate(listOf("5.9"), variableValues, null), `is`("5"))
        assertThat(floorFunction.evaluate(listOf("5"), variableValues, null), `is`("5"))
        assertThat(floorFunction.evaluate(listOf("-5"), variableValues, null), `is`("-5"))
    }

    @Test
    fun throw_illegal_argument_exception_when_argument_count_is_greater_than_expected() {
        assertFailsWith<IllegalArgumentException> {
            RuleFunctionFloor.create().evaluate(listOf("5.9", "6.8"), variableValues, null)
        }
    }

    @Test
    fun throw_illegal_argument_exception_when_arguments_count_is_lower_than_expected() {
        assertFailsWith<IllegalArgumentException> {
            RuleFunctionFloor.create().evaluate(ArrayList(), variableValues, null)
        }
    }

    @Test
    fun throw_null_pointer_exception_when_arguments_is_null() {
        assertFailsWith<NullPointerException> {
            RuleFunctionFloor.create().evaluate(null!!, variableValues, null)
        }
    }
}
