package org.dhis2.ruleengine.exprk.functions

import org.dhis2.ruleengine.RuleVariableValue
import org.dhis2.ruleengine.exprk.internal.Function

const val MIN_VALUE = "d2:minValue"
class MinValue(private val valueMap: Map<String, RuleVariableValue>) : Function() {
    override fun call(arguments: List<String?>): String {
        return valueMap[arguments[0]]?.candidates?.minBy { it.toDouble() }?.toDouble()?.toString() ?: ""
    }
}