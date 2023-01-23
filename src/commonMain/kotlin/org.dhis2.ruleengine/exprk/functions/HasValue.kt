package org.dhis2.ruleengine.exprk.functions

import org.dhis2.ruleengine.RuleVariableValue
import org.dhis2.ruleengine.exprk.internal.Function
import org.dhis2.ruleengine.utils.VariableNameUnwrapper

const val HAS_VALUE = "d2:hasValue"
class HasValue(private val valueMap: () -> Map<String, RuleVariableValue>) : Function() {
    override fun call(arguments: List<String?>): String {
        if (arguments.size != 1) throw IllegalArgumentException("hasValue requires one argument")
        return arguments.first()?.let {argument->
            val unwrapped = VariableNameUnwrapper.unwrap(argument, argument)
            (valueMap()[unwrapped]?.value?.isNotBlank() == true).toString()
        }?:"false"
    }

    override fun requiresArgumentEvaluation():Boolean = false
}