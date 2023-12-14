package org.hisp.dhis.rules

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.hisp.dhis.rules.models.RuleEvent

fun values(ruleDataValues: List<org.hisp.dhis.rules.models.RuleDataValue>): List<String> {
        val values: MutableList<String> = ArrayList(ruleDataValues.size)
        for (ruleDataValue in ruleDataValues) {
            values.add(ruleDataValue.value)
        }
        return values
    }

    fun getLastUpdateDateForPrevious(
        ruleDataValues: List<org.hisp.dhis.rules.models.RuleDataValue>,
        ruleEvent: RuleEvent
    ): String {
        val dates: MutableList<LocalDate> = ArrayList()
        for (date in ruleDataValues) {
            val d = date.eventDate
            if (d < ruleEvent.eventDate) {
                dates.add(d)
            }
        }
        return dates.max().toString()
    }

    fun getLastUpdateDate(ruleDataValues: List<org.hisp.dhis.rules.models.RuleDataValue>): String {
        val dates: MutableList<LocalDate> = ArrayList()
        for (date in ruleDataValues) {
            val d = date.eventDate
            dates.add(d)
        }
        return dates.max().toString()
    }

    fun unwrapVariableName(variable: String): String {
        if (variable.startsWith("#{") && variable.endsWith("}")) {
            return variable.substring(2,variable.length-1)
        }
        throw IllegalArgumentException("Malformed variable: $variable")
    }

    fun LocalDate.Companion.currentDate(): LocalDate {
        return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    }
