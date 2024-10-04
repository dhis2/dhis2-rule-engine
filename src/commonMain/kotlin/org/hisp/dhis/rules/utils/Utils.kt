package org.hisp.dhis.rules.utils

import kotlinx.datetime.*
import org.hisp.dhis.rules.models.RuleDataValueHistory
import org.hisp.dhis.rules.models.RuleEvent

fun getLastUpdateDateForPrevious(
    ruleDataValues: List<RuleDataValueHistory>,
    ruleEvent: RuleEvent,
): String {
    val dates: MutableList<Instant> = ArrayList()
    for (date in ruleDataValues) {
        val d = date.eventDate
        if (d < ruleEvent.eventDate ||
            (ruleEvent.eventDate == d && ruleEvent.createdDate > date.createdDate)
        ) {
            dates.add(d)
        }
    }
    return dates
        .max()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .date
        .toString()
}

fun getLastUpdateDate(ruleDataValues: List<RuleDataValueHistory>): String {
    val dates: MutableList<Instant> = ArrayList()
    for (date in ruleDataValues) {
        val d = date.eventDate
        dates.add(d)
    }
    return dates
        .max()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .date
        .toString()
}

fun unwrapVariableName(variable: String): String {
    if (variable.startsWith("#{") && variable.endsWith("}")) {
        return variable.substring(2, variable.length - 1)
    }
    throw IllegalArgumentException("Malformed variable: $variable")
}

fun LocalDate.Companion.currentDate(): LocalDate =
    Clock.System
        .now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .date
