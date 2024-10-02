package org.hisp.dhis.rules.models

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

data class RuleEvent(
    val event: String,
    val programStage: String,
    val programStageName: String,
    val status: RuleEventStatus,
    val eventDate: Instant,
    val createdDate: Instant,
    val dueDate: LocalDate?,
    val completedDate: LocalDate?,
    val organisationUnit: String,
    val organisationUnitCode: String?,
    val dataValues: List<RuleDataValue>
)
