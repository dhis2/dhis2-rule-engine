package org.dhis2.ruleengine.exprk.functions

import org.dhis2.ruleengine.exprk.internal.Function

const val SUB_STRING = "d2:subString"

class SubString : Function() {
    override fun call(arguments: List<String?>): String {
        val input = arguments[0]
            ?: return ""
        val fromIndex = arguments[1]?.toInt()?.let { index ->
            when {
                index < 0 -> 0
                index > input.length -> input.length
                else -> index
            }
        }
            ?: throw IllegalArgumentException("d2:substring requires an integer as second argument")
        val toIndex = arguments[2]?.toInt()?.let { index ->
            when {
                index < 0 -> 0
                index > input.length -> input.length
                else -> index
            }
        }
            ?: throw IllegalArgumentException("d2:substring requires an integer as third argument")
        return input.substring(startIndex = fromIndex, endIndex = toIndex)
    }
}