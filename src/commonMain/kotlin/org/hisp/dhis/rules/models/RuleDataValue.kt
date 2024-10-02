package org.hisp.dhis.rules.models

import kotlinx.datetime.Instant

data class RuleDataValue(
    val dataElement: String,
    val value: String
)
