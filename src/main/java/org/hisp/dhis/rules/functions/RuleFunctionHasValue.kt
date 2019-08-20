package org.hisp.dhis.rules.functions

import org.hisp.dhis.rules.RuleVariableValue


class RuleFunctionHasValue : RuleFunction() {

    override fun evaluate(arguments: List<String>,
                          valueMap: Map<String, RuleVariableValue>?, supplementaryData: Map<String, List<String>>?): String {
        when {
            valueMap == null -> throw IllegalArgumentException("valueMap is expected")
            arguments.size != 1 -> throw IllegalArgumentException("One argument was expected, ${arguments.size} were supplied")

            // ToDo: make sure that argument names are actually argument names and not values.
            else -> {
                val variableName = arguments[0].replace("'", "")
                //val variableValue = valueMap[variableName] ?: return false.toString()

                return (!valueMap[variableName]?.value().isNullOrEmpty()).toString()
            }
        }
    }

    companion object {
        const val D2_HAS_VALUE = "d2:hasValue"

        @JvmStatic
        fun create() = RuleFunctionHasValue()
    }
}
