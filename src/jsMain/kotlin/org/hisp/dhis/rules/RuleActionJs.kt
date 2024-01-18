package org.hisp.dhis.rules

import org.hisp.dhis.lib.expression.js.Entry

@JsExport
@OptIn(ExperimentalJsExport::class)
data class RuleActionJs(
    val data: String?,
    val type: String,
    val values: Array<Entry<String, String>> = emptyArray()
)