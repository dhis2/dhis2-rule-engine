package org.dhis2.ruleengine.functions

import org.dhis2.ruleengine.models.TimeInterval
import org.dhis2.ruleengine.models.TimeInterval.Companion.empty
import org.dhis2.ruleengine.models.TimeInterval.Companion.fromTo
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat

abstract class RuleFunction {
    fun toDouble(str: String?, defaultValue: Double): Double {
        return if (str == null) {
            defaultValue
        } else try {
            str.toDouble()
        } catch (nfe: NumberFormatException) {
            defaultValue
        }
    }

    companion object {
        const val DATE_PATTERN = "yyyy-MM-dd"
        @JvmStatic
        fun getTimeInterval(start: String?, end: String?): TimeInterval {
            if (isEmpty(start) || isEmpty(end)) {
                return empty()
            }
            val startDate = LocalDate.parse(start, DateTimeFormat.forPattern(DATE_PATTERN))
            val endDate = LocalDate.parse(end, DateTimeFormat.forPattern(DATE_PATTERN))
            return fromTo(startDate, endDate)
        }

        @JvmStatic
        fun wrap(input: String?): String {
            return input ?: ""
        }

        fun isEmpty(input: String?): Boolean {
            return input == null || input.length == 0
        }
    }
}