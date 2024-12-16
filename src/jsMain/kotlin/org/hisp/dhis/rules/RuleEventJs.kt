package org.hisp.dhis.rules

import kotlinx.datetime.internal.JSJoda.Instant
import kotlinx.datetime.internal.JSJoda.LocalDate
import org.hisp.dhis.rules.models.RuleDataValue
import org.hisp.dhis.rules.models.RuleEventStatus

@JsExport
data class RuleEventJs(
    val event: String,
    val programStage: String,
    val programStageName: String,
    val status: RuleEventStatus,
    val eventDate: Instant?,
    val createdDate: Instant,
    val dueDate: LocalDate?,
    val completedDate: LocalDate?,
    val organisationUnit: String,
    val organisationUnitCode: String?,
    val dataValues: Array<RuleDataValue>
)