package org.hisp.dhis.rules.functions

import org.hisp.dhis.rules.RuleVariableValue
import java.text.ParseException
import java.text.SimpleDateFormat

const val D2_DAYS_BETWEEN = "d2:daysBetween"

internal class RuleFunctionDaysBetween : RuleFunction() {
    override fun evaluate(arguments: List<String>,
                          valueMap: Map<String, RuleVariableValue>, supplementaryData: Map<String, List<String>>?): String {
        require(arguments.size == 2) {
            "Two arguments were expected, " +
                    arguments.size + " were supplied"
        }
        return daysBetween(arguments[0], arguments[1]).toString()
    }

    /**
     * Function which will return the number of days between the two given dates.
     *
     * @param start the start date.
     * @param end   the end date.
     * @return number of days between dates.
     */
    private fun daysBetween(start: String, end: String): Int {
        if (isEmpty(start) || isEmpty(end)) {
            return 0
        }
        val format = SimpleDateFormat()
        format.applyPattern(DATE_PATTERN)
        return try {
            val startDate = format.parse(start)
            val endDate = format.parse(end)
            java.lang.Long.valueOf((endDate.time - startDate.time) / 86400000).toInt()
        } catch (parseException: ParseException) {
            throw RuntimeException(parseException)
        }
    }
}