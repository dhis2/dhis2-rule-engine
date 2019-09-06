package org.hisp.dhis.rules.functions

import org.hisp.dhis.rules.RuleVariableValue

import org.hisp.dhis.rules.functions.RuleFunctionDaysBetween.Companion.daysBetween
import kotlin.jvm.JvmStatic

class RuleFunctionWeeksBetween : RuleFunction() {

    override fun evaluate(arguments: List<String?>,
                          valueMap: Map<String, RuleVariableValue>?, supplementaryData: Map<String, List<String>>?): String {
        return when {
            arguments.size != 2 -> throw IllegalArgumentException("Two arguments were expected, ${arguments.size} were supplied")
            else -> (daysBetween(arguments[0], arguments[1]) / 7).toString()
        }
    }

    companion object {
        const val D2_WEEKS_BETWEEN = "d2:weeksBetween"

        @JvmStatic fun create() = RuleFunctionWeeksBetween()
    }
}
