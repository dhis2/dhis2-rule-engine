package org.dhis2.ruleengine.exprk.functions

import org.dhis2.ruleengine.exprk.internal.Function
import kotlin.math.floor

const val FLOOR = "d2:floor"

class Floor : Function() {
    override fun call(arguments: List<String?>): String {
        if (arguments.size != 1) throw IllegalArgumentException("One argument is required")
        return arguments[0]?.toDoubleOrNull()?.let { floor(it).toInt().toString() } ?: "0"
    }
}