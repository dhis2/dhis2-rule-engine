package org.hisp.dhis.rules

import kotlin.time.Instant
import kotlinx.datetime.LocalDate
import org.hisp.dhis.rules.models.RuleDataValue
import org.hisp.dhis.rules.models.RuleEventStatus

@OptIn(kotlin.time.ExperimentalTime::class)
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
