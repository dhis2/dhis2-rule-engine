package org.dhis2.ruleengine.exprk.functions

import org.dhis2.ruleengine.exprk.internal.Function

const val ZING = "d2:zing"

class Zing : Function() {
    override fun call(arguments: List<String?>): String {
        val input = arguments[0]
        return input?.toDouble()?.let { value ->
            if (value < 0) {
                "0"
            } else {
                input
            }
        } ?: throw IllegalArgumentException("d2:zing requires a nonnull value")
    }
}