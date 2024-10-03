package org.hisp.dhis.rules.models

import kotlin.js.JsExport

@JsExport
data class RuleDataValue(
    val dataElement: String,
    val value: String
)
