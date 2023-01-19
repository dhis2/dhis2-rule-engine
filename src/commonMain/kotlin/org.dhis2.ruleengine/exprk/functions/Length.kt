package org.dhis2.ruleengine.exprk.functions

import org.dhis2.ruleengine.exprk.internal.Function

const val LENGTH = "d2:length"

class Length : Function() {
    override fun call(arguments: List<String?>): String {
        return arguments[0]?.length?.toString() ?: throw NullPointerException("length requires not null argument")
    }
}