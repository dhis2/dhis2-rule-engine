package org.hisp.dhis.rules.models

import kotlinx.datetime.Instant

data class RuleDataValueHistory(
    val value: String,
    val eventDate: Instant,
    val createdDate: Instant,
    val programStage: String,
)
