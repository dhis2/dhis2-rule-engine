package org.hisp.dhis.rules.models

import java.util.*
import java.util.Comparator.comparing
import kotlin.Comparator

data class RuleEvent(
    val event: String,
    val programStage: String,
    val programStageName: String,
    val status: Status,
    val eventDate: Date,
    val dueDate: Date?,
    val completedDate: Date?,
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

    fun eventDate(): Date {
        return eventDate
    }

    fun dueDate(): Date? {
        return dueDate
    }

    fun completedDate(): Date? {
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

    companion object {
        val EVENT_DATE_COMPARATOR: Comparator<RuleEvent> = Comparator.comparing{e: RuleEvent-> e.eventDate()}.reversed()
    }
}
