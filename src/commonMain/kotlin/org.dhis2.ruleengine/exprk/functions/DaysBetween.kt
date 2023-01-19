package org.dhis2.ruleengine.exprk.functions

import kotlinx.datetime.LocalDate
import kotlinx.datetime.periodUntil
import kotlinx.datetime.toLocalDate
import org.dhis2.ruleengine.exprk.internal.Function

const val DAYS_BETWEEN = "d2:daysBetween"
class DaysBetween : Function() {
    override fun call(arguments: List<String?>): String {
        if (arguments.size != 2) throw IllegalArgumentException("daysBetween requires two arguments")
        val startDate = arguments[0]?.takeIf { it.isNotBlank() }?.let { parseToDate(it) }
        val endDate = arguments[1]?.takeIf { it.isNotBlank() }?.let { parseToDate(it) }

        return when {
            startDate == null -> "0"
            endDate == null -> "0"
            else -> startDate.periodUntil(endDate).days.toString()
        }
    }

    private fun parseToDate(value: String): LocalDate = try {
        value.toLocalDate()
    } catch (e: Exception) {
        throw IllegalArgumentException("$value can't be pased to date")
    }
}