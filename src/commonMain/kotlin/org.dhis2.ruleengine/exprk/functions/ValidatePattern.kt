package org.dhis2.ruleengine.exprk.functions

import org.dhis2.ruleengine.exprk.internal.Function

const val VALIDATE_PATTERN = "d2:validatePattern"

class ValidatePattern : Function() {
    override fun call(arguments: List<String?>): String {
        val input = arguments[0]
        val regex = arguments[1]?.let { Regex(it) }
            ?: throw IllegalArgumentException("d2:validatePattern requires a nonnull second argument")

        return input?.let { regex.matches(input).toString() } ?: false.toString()
    }
}