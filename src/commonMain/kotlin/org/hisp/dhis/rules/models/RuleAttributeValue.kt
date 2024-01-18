package org.hisp.dhis.rules.models

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@JsExport
@OptIn(ExperimentalJsExport::class)
data class RuleAttributeValue(
    val trackedEntityAttribute: String,
    val value: String
)
