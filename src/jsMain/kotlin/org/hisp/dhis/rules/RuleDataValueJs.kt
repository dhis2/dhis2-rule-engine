package org.hisp.dhis.rules

import kotlinx.datetime.internal.JSJoda.Instant

@JsExport
@OptIn(ExperimentalJsExport::class)
data class RuleDataValueJs(
    val eventDate: Instant,
    val programStage: String,
    val dataElement: String,
    val value: String
)
