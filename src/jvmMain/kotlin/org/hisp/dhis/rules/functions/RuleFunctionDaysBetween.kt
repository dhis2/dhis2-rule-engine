package org.hisp.dhis.rules.functions

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit
import kotlin.jvm.JvmStatic

actual class RuleFunctionDaysBetween : RuleFunction() {

    actual override fun evaluate(arguments: List<String?>,
                          valueMap: Map<String, RuleVariableValue>?,
                          supplementaryData: Map<String, List<String>>?): String {
        when {
            arguments.size != 2 -> throw IllegalArgumentException("Two arguments were expected, ${arguments.size} were supplied")
            else -> return daysBetween(arguments[0], arguments[1]).toString()
        }

    }

    actual companion object {
        actual val D2_DAYS_BETWEEN = "d2:daysBetween"

        @JvmStatic actual fun create() = RuleFunctionDaysBetween()

        /**
         * Function which will return the number of days between the two given dates.
         *
         * @param start the start date.
         * @param end   the end date.
         * @return number of days between dates.
         */
        @JvmStatic actual fun daysBetween(start: String?, end: String?): Int {
            return when {
                start.isNullOrEmpty() || end.isNullOrEmpty() ->  0
                else -> {
                    val formatter = DateTimeFormatter.ofPattern(DATE_PATTERN)

                    try {
                        val startDate = LocalDate.parse(start, formatter)
                        val endDate = LocalDate.parse(end, formatter)

                        ChronoUnit.DAYS.between(startDate, endDate).toInt()

                    } catch (ex: DateTimeParseException) {
                        throw RuntimeException(ex)
                    }
                }
            }
        }
    }
}
