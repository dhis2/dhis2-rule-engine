package org.dhis2.ruleengine.functions

import org.dhis2.ruleengine.exprk.functions.Floor
import kotlin.test.Test
import kotlin.test.assertTrue


class RuleFunctionFloorTest {

    @Test
    fun evaluateMustReturnFlooredValue() {
        shouldFloorTo("4.1", "4")
        shouldFloorTo("0.8", "0")
        shouldFloorTo("5.1", "5")
        shouldFloorTo("1.0", "1")
        shouldFloorTo("-9.3", "-10")
        shouldFloorTo("-5.9", "-6")
    }

    @Test
    fun return_zero_when_number_is_invalid() {
        assertTrue { Floor().call(listOf("not a number")) == "0" }
    }

    private fun shouldFloorTo(currentValue: String, expectedValue: String) =
        assertTrue { Floor().call(listOf(currentValue)) == expectedValue }
}