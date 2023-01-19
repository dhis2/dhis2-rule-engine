package org.dhis2.ruleengine.exprk.functions

import org.dhis2.ruleengine.RuleVariableValue
import org.dhis2.ruleengine.exprk.internal.Function

const val LAST_EVENT_DATE = "d2:lastEventDate"

class LastEventDate(private val valueMap: Map<String, RuleVariableValue>) : Function() {
    override fun call(arguments: List<String?>): String {
        return arguments.first()?.let { key ->
            valueMap[key]?.eventDate?.toString() ?: ""
        } ?: ""
    }
}