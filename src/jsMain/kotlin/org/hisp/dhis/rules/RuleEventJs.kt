package org.hisp.dhis.rules

import kotlinx.datetime.internal.JSJoda.Instant
import kotlinx.datetime.internal.JSJoda.LocalDate

@JsExport
@OptIn(ExperimentalJsExport::class)
data class RuleEventJs(
    val event: String,
    val programStage: String,
    val programStageName: String,
    val status: String,
    val eventDate: Instant,
    val dueDate: LocalDate?,
    val completedDate: LocalDate?,
    val organisationUnit: String,
    val organisationUnitCode: String?,
    val dataValues: Array<RuleDataValueJs>
)