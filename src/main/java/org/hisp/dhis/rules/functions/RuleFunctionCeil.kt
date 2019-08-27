package org.hisp.dhis.rules.functions

import org.hisp.dhis.rules.RuleVariableValue
import org.hisp.dhis.rules.toDouble
import kotlin.math.ceil

class RuleFunctionCeil : RuleFunction() {

    override fun evaluate(arguments: List<String?>,
                          valueMap: Map<String, RuleVariableValue>?, supplementaryData: Map<String, List<String>>?): String {
        when {
            arguments.size != 1 -> throw IllegalArgumentException("One argument was expected, ${arguments.size} were supplied")
            else -> return ceil(arguments[0].toDouble(0.0)).toLong().toString()
        }
    }

    companion object {
        const val D2_CEIL = "d2:ceil"

        @JvmStatic
        fun create() = RuleFunctionCeil()
    }
}
