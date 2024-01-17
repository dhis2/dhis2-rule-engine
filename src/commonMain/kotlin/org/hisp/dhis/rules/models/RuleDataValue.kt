package org.hisp.dhis.rules.models

import kotlinx.datetime.Instant

data class RuleDataValue(
    val eventDate: Instant,
    val programStage: String,
    val dataElement: String,
    val value: String
)
