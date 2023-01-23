package org.dhis2.ruleengine.exprk.functions

import org.dhis2.ruleengine.RuleVariableValue
import org.dhis2.ruleengine.exprk.internal.Function
import org.dhis2.ruleengine.utils.VariableNameUnwrapper

const val MAX_VALUE = "d2:maxValue"
class MaxValue(private val valueMap: ()-> Map<String, RuleVariableValue>) : Function() {
    override fun call(arguments: List<String?>): String {
        return arguments[0]?.let { value ->
            val variableName = VariableNameUnwrapper.unwrap(value, value)
            valueMap()[variableName]?.candidates?.maxBy { it.toDouble() }?.toString()
        } ?: ""
    }
}