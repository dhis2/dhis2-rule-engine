package org.dhis2.ruleengine.exprk.functions

import org.dhis2.ruleengine.exprk.internal.Function

const val USER = "USER"
const val HAS_USER_ROLE = "d2:hasUserRole"
class HasUserRole(val supplementaryData: Map<String, List<String>>) : Function() {
    override fun call(arguments: List<String?>): String {
        if (!supplementaryData.containsKey(USER)) throw IllegalArgumentException("Supplementary data needs to be provided")
        return supplementaryData[USER]?.contains(arguments[0])?.toString() ?: "false"
    }
}