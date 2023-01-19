package org.dhis2.ruleengine.functions

import org.dhis2.ruleengine.exprk.functions.Ceil
import kotlin.test.Test
import kotlin.test.assertTrue


class RuleFunctionCeilTest {

    private lateinit var mockedFirstExpr: String
    private val ceil = Ceil()

    @Test
    fun evaluateMustReturnCeiledValue() {
        mockedFirstExpr = "4.1"
        assertTrue { ceil.call(listOf(mockedFirstExpr)) == "5" }
        mockedFirstExpr = "0.8"
        assertTrue { ceil.call(listOf(mockedFirstExpr)) == "1" }
        mockedFirstExpr = "5.1"
        assertTrue { ceil.call(listOf(mockedFirstExpr)) == "6" }
        mockedFirstExpr = "1"
        assertTrue { ceil.call(listOf(mockedFirstExpr)) == "1" }
        mockedFirstExpr = "-9.3"
        assertTrue { ceil.call(listOf(mockedFirstExpr)) == "-9" }
        mockedFirstExpr = "-5.9"
        assertTrue { ceil.call(listOf(mockedFirstExpr)) == "-5" }
    }

    @Test
    fun return_zero_when_number_is_invalid() {
        mockedFirstExpr = "not a number"
        assertTrue { ceil.call(listOf(mockedFirstExpr)) == "0" }
    }
}