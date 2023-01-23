package org.dhis2.ruleengine.exprk.functions

import org.dhis2.ruleengine.RuleVariableValue
import org.dhis2.ruleengine.exprk.internal.Function
import org.dhis2.ruleengine.utils.VariableNameUnwrapper

const val COUNT = "d2:count"

class Count(val valueMap: () -> Map<String, RuleVariableValue>) : Function() {
    override fun call(arguments: List<String?>): String {
        if (arguments.size != 1) throw IllegalArgumentException("One arguments are required")
        val variable = arguments.first() ?: return "0"
        return valueMap()[VariableNameUnwrapper.unwrap(variable, variable)]?.candidates?.size?.toString() ?: "0"
    }

    override fun requiresArgumentEvaluation(): Boolean = false

}