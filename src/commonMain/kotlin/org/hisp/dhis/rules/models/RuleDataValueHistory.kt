package org.hisp.dhis.rules.models

import kotlin.time.Instant

data class RuleDataValueHistory(
    val value: String,
    val eventDate: RuleLocalDate,
    val createdDate: Instant,
    val programStage: String,
)
