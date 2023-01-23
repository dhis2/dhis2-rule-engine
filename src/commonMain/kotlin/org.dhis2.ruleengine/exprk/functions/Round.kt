package org.dhis2.ruleengine.exprk.functions

import org.dhis2.ruleengine.exprk.internal.Function
import kotlin.math.roundToInt

const val ROUND = "d2:round"

class Round : Function() {
    override fun call(arguments: List<String?>): String {
        return arguments[0]?.toDoubleOrNull()?.roundToInt()?.toString()
            ?: "0"
    }

    override fun requiresArgumentEvaluation(): Boolean = true

}