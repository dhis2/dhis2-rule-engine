package org.dhis2.ruleengine.exprk.functions

import org.dhis2.ruleengine.exprk.internal.Function

const val MODULUS = "d2:modulus"

class Modulus : Function() {
    override fun call(arguments: List<String?>): String {
        val arg0 = arguments[0]?.toDoubleOrNull() ?: 0.0
        val arg1 = arguments[1]?.toDoubleOrNull() ?: 0.0
        return (arg0 % arg1).toString()
    }
}