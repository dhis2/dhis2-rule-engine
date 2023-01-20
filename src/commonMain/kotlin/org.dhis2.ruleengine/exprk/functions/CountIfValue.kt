package org.dhis2.ruleengine.exprk.functions

import org.dhis2.ruleengine.RuleVariableValue
import org.dhis2.ruleengine.exprk.internal.Function
import org.dhis2.ruleengine.models.RuleValueType

const val COUNT_IF_VALUE = "d2:countIfValue"
class CountIfValue(val valueMap: Map<String, RuleVariableValue>) : Function() {
    override fun call(arguments: List<String?>): String {
        if (arguments.size != 2) throw IllegalArgumentException("Twi arguments are required")
        return valueMap[arguments.first()]?.let { ruleVariableValue ->
            val valueToFind = when {
                ruleVariableValue.ruleValueType == RuleValueType.BOOLEAN && arguments[1] == "1" -> "true"
                ruleVariableValue.ruleValueType == RuleValueType.BOOLEAN && arguments[1] == "0" -> "false"
                else -> arguments[1]
            }
            ruleVariableValue.candidates.count { it == valueToFind }.toString()
        } ?: "0"

    }
}