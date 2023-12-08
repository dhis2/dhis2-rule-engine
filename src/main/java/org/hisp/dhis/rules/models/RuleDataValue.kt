package org.hisp.dhis.rules.models

import java.util.*
import javax.annotation.Nonnull

data class RuleDataValue(
    val eventDate: Date,
    val programStage: String,
    val dataElement: String,
    val value: String
)
