package org.dhis2.ruleengine.exprk.functions

import org.dhis2.ruleengine.RuleVariableValue
import org.dhis2.ruleengine.exprk.internal.Function

const val MAX_VALUE = "d2:maxValue"
class MaxValue(private val valueMap: Map<String, RuleVariableValue>) : Function() {
    override fun call(arguments: List<String?>): String {
        return valueMap[arguments[0]]?.candidates?.maxBy { it.toDouble() }?.toDouble()?.toString() ?: ""
    }
}