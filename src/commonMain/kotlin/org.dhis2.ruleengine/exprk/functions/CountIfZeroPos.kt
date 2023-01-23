package org.dhis2.ruleengine.exprk.functions

import org.dhis2.ruleengine.RuleVariableValue
import org.dhis2.ruleengine.exprk.internal.Function
import org.dhis2.ruleengine.models.RuleValueType
import org.dhis2.ruleengine.utils.VariableNameUnwrapper

const val COUNT_IF_ZERO_POS = "d2:countIfZeroPos"
class CountIfZeroPos(val valueMap: ()-> Map<String, RuleVariableValue>) : Function() {
    override fun call(arguments: List<String?>): String {
        if (arguments.size != 1) throw IllegalArgumentException("One arguments are required")
        val variable = arguments.first()?: return "0"
        return valueMap()[VariableNameUnwrapper.unwrap(variable, variable)]?.let { ruleVariableValue ->
            ruleVariableValue.candidates.count { isZeroPos(it) }.toString()
        } ?: "0"
    }

    private fun isZeroPos(input: String): Boolean {
        val value = try {
            input.toDouble()
        } catch (e: NumberFormatException) {
            throw IllegalArgumentException("Invalid number format")
        }
        return value >= 0
    }

    override fun requiresArgumentEvaluation():Boolean = false

}