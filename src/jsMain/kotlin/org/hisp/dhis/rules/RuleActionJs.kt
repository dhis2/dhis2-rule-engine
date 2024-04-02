package org.hisp.dhis.rules

import js.collections.JsMap

@JsExport
@OptIn(ExperimentalJsExport::class)
data class RuleActionJs(
    val data: String?,
    val type: String,
    val values: JsMap<String, String> = JsMap()
)