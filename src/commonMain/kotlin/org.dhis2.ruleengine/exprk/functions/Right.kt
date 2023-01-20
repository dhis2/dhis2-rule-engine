package org.dhis2.ruleengine.exprk.functions

import org.dhis2.ruleengine.exprk.internal.Function

const val RIGHT = "d2:right"

class Right : Function() {
    override fun call(arguments: List<String?>): String {
        val numericValue = arguments[1]?.toIntOrNull() ?: throw IllegalArgumentException("Number has to be an integer")
        val value = arguments[0]?.reversed()
        return when {
            numericValue < 0 -> value?.substring(0, numericValue + value.length)
            numericValue > (value?.length ?: 0) -> value?.substring(0, value.length)
            else -> value?.substring(0, numericValue)
        }?.reversed() ?: ""
    }
}