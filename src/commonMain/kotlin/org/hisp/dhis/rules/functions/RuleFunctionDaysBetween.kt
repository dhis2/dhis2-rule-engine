package org.hisp.dhis.rules.functions

import com.soywiz.klock.*
import org.hisp.dhis.rules.RuleVariableValue
import kotlin.jvm.JvmStatic

class RuleFunctionDaysBetween : RuleFunction() {

    override fun evaluate(arguments: List<String?>,
                          valueMap: Map<String, RuleVariableValue>?,
                          supplementaryData: Map<String, List<String>>?): String {
        when {
            arguments.size != 2 -> throw IllegalArgumentException("Two arguments were expected, ${arguments.size} were supplied")
            else -> return daysBetween(arguments[0], arguments[1]).toString()
        }

    }

    companion object {
        const val D2_DAYS_BETWEEN = "d2:daysBetween"

        @JvmStatic fun create() = RuleFunctionDaysBetween()

        /**
         * Function which will return the number of days between the two given dates.
         *
         * @param start the start date.
         * @param end   the end date.
         * @return number of days between dates.
         */
        @JvmStatic fun daysBetween(start: String?, end: String?): Int {
            return when {
                start.isNullOrEmpty() || end.isNullOrEmpty() -> 0
                else -> {
                    try {
                        val startDate = DateFormat.FORMAT_DATE.parse(start)
                        val endDate = DateFormat.FORMAT_DATE.parse(end)
                        DateTimeRange(startDate.local, endDate.local, true).duration.days.toInt()
                    } catch (ex: DateException) {
                        throw RuntimeException(ex)
                    }
                }
            }
        }
    }
}
