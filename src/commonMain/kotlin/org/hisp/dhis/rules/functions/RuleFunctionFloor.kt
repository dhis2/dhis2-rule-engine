package org.hisp.dhis.rules.functions

import org.hisp.dhis.rules.RuleVariableValue
import org.hisp.dhis.rules.toDouble
import kotlin.math.floor
import kotlin.jvm.JvmStatic

class RuleFunctionFloor : RuleFunction() {

    override fun evaluate(arguments: List<String?>,
                          valueMap: Map<String, RuleVariableValue>?, supplementaryData: Map<String, List<String>>?): String {
        when {
            arguments.size != 1 -> throw IllegalArgumentException("One argument was expected, ${arguments.size} were supplied")
            else -> return floor(arguments[0].toDouble(0.0)).toLong().toString()
        }
    }

    companion object {
        const val D2_FLOOR = "d2:floor"

        @JvmStatic fun create() = RuleFunctionFloor()
    }
}
