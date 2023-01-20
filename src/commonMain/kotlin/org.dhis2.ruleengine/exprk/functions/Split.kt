package org.dhis2.ruleengine.exprk.functions

import org.dhis2.ruleengine.exprk.internal.Function

const val SPLIT = "d2:split"

class Split : Function() {
    override fun call(arguments: List<String?>): String {
        val input = arguments[0]
        val delimiter = arguments[1]
        if (input == null || delimiter == null) return ""
        val index = arguments[2]?.toDoubleOrNull()?.toInt() ?: throw IllegalArgumentException("d2:split requires a numeric index as third argument")
        val tokens = input.split(delimiter)
        return if (tokens.size > index && index >= 0) {
            tokens[index]
        } else {
            ""
        }
    }
}