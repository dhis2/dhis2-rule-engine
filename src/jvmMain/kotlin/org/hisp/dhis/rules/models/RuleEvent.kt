package org.hisp.dhis.rules.models

import kotlinx.datetime.LocalDate

data class RuleEvent(
    val event: String,
    val programStage: String,
    val programStageName: String,
    val status: Status,
    val eventDate: LocalDate,
    val dueDate: LocalDate?,
    val completedDate: LocalDate?,
    val organisationUnit: String,
    val organisationUnitCode: String?,
    val dataValues: List<RuleDataValue>
) {
    enum class Status {
        ACTIVE,
        COMPLETED,
        SCHEDULE,
        SKIPPED,
        VISITED,
        OVERDUE
    }

    fun event(): String {
        return event
    }

    fun programStage(): String {
        return programStage
    }

    fun programStageName(): String {
        return programStageName
    }

    fun status(): Status {
        return status
    }

    fun eventDate(): LocalDate {
        return eventDate
    }

    fun dueDate(): LocalDate? {
        return dueDate
    }

    fun completedDate(): LocalDate? {
        return completedDate
    }

    fun organisationUnit(): String {
        return organisationUnit
    }

    fun organisationUnitCode(): String? {
        return organisationUnitCode
    }

    fun dataValues(): List<RuleDataValue> {
        return dataValues
    }
}
