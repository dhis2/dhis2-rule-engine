package org.dhis2.ruleengine.exprk.functions

import org.dhis2.ruleengine.exprk.internal.Function

const val ZPVC = "d2:zpvc"

class Zpvc : Function() {
    override fun call(arguments: List<String?>): String {
        return arguments.filterNotNull().count {
            it.toDouble() >= 0
        }.toString()
    }
}