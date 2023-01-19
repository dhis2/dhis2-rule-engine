package org.dhis2.ruleengine.exprk.functions

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDate
import org.dhis2.ruleengine.exprk.internal.Function

const val ADD_DAYS = "d2:addDays"

class AddDays : Function() {
    override fun call(arguments: List<String>): String {
        if (arguments.size != 2) throw IllegalArgumentException("Expected two parameters")
        return arguments[0].toLocalDate().plus(arguments[1].toDouble().toInt(), DateTimeUnit.DAY).toString()
    }
}