package org.hisp.dhis.rules.functions

import org.hisp.dhis.rules.RuleVariableValue
import kotlin.jvm.JvmStatic

expect class RuleFunctionDaysBetween : RuleFunction {

    override fun evaluate(arguments: List<String?>,
                          valueMap: Map<String, RuleVariableValue>?,
                          supplementaryData: Map<String, List<String>>?): String

    companion object {
        val D2_DAYS_BETWEEN: String

        @JvmStatic fun create(): RuleFunctionDaysBetween

        /**
         * Function which will return the number of days between the two given dates.
         *
         * @param start the start date.
         * @param end   the end date.
         * @return number of days between dates.
         */
        @JvmStatic fun daysBetween(start: String?, end: String?): Int
    }
}
