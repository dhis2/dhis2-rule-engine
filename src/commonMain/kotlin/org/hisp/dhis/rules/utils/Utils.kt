package org.hisp.dhis.rules.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

fun unwrapVariableName(variable: String): String {
    if (variable.startsWith("#{") && variable.endsWith("}")) {
        return variable.substring(2, variable.length - 1)
    }
    throw IllegalArgumentException(
        "Malformed variable: expected a program rule variable reference like #{variableName}, but was: $variable",
    )
}

fun currentDate(): LocalDate =
    Clock.System
        .now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .date
