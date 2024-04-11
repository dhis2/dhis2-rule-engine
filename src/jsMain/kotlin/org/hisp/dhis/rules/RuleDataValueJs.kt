package org.hisp.dhis.rules

import kotlinx.datetime.internal.JSJoda.Instant

@JsExport
data class RuleDataValueJs(
    val eventDate: Instant,
    val programStage: String,
    val dataElement: String,
    val value: String
)
