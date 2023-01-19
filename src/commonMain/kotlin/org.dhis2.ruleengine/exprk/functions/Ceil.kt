package org.dhis2.ruleengine.exprk.functions

import org.dhis2.ruleengine.exprk.internal.Function
import kotlin.math.ceil

const val CEIL = "d2:ceil"

class Ceil : Function() {
    override fun call(arguments: List<String>): String {
        if (arguments.size != 1) throw IllegalArgumentException("One argument is required")
        return arguments[0].toDoubleOrNull()?.let { ceil(it).toInt().toString() } ?: "0"
    }
}