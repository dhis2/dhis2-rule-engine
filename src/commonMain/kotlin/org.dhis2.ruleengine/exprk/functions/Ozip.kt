package org.dhis2.ruleengine.exprk.functions

import org.dhis2.ruleengine.exprk.internal.Function

const val O_ZIP = "d2:Ozip"

class Ozip : Function() {
    override fun call(arguments: List<String?>): String {
        val value = arguments[0]?.toDoubleOrNull()
            ?: throw IllegalArgumentException("d2:Ozip requires a non null numeric argument")
        return if (value >= 0.0) {
            "1"
        } else {
            "0"
        }
    }
}