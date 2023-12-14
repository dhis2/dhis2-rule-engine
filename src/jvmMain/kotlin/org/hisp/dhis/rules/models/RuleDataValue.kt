package org.hisp.dhis.rules.models

import kotlinx.datetime.LocalDate

data class RuleDataValue(
    val eventDate: LocalDate,
    val programStage: String,
    val dataElement: String,
    val value: String
)
