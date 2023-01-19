package org.dhis2.ruleengine.models

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
    val dataValues: List<RuleDataValue>,
){
    enum class Status {
        ACTIVE,
        COMPLETED,
        SCHEDULE,
        SKIPPED,
        VISITED,
        OVERDUE
    }
}