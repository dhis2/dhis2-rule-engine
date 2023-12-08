package org.hisp.dhis.rules.models

import java.util.Date

data class RuleDataValue(
    val eventDate: Date,
    val programStage: String,
    val dataElement: String,
    val value: String
)
