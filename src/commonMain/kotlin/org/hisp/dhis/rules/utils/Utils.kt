package org.hisp.dhis.rules.utils

import kotlinx.datetime.*
import org.hisp.dhis.rules.models.RuleDataValueHistory
import org.hisp.dhis.rules.models.RuleEvent

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
