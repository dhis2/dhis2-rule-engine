package org.dhis2.ruleengine.exprk.functions

import org.dhis2.ruleengine.exprk.internal.Function

const val CONCATENATE = "d2:concatenate"
class Concatenate : Function() {
    override fun call(arguments: List<String?>): String {
        return arguments.filterNotNull().joinToString(separator = "")
    }
}