package org.dhis2.ruleengine.models

import kotlinx.datetime.LocalDate

data class RuleDataValue(
    val eventDate: LocalDate,
    val programStage: String,
    val dataElement: String,
    val value: String
)

fun List<RuleDataValue>.getLastUpdateList(): LocalDate {
    return this.map { it.eventDate }.sorted().max()
}

fun List<RuleDataValue>.getLastUpdateDateForPrevious(ruleEvent: RuleEvent): LocalDate {
    return this.filter { it.eventDate < ruleEvent.eventDate }.map { it.eventDate }.sorted().max()
}