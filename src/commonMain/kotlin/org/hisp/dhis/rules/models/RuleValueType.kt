package org.hisp.dhis.rules.models

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@JsExport
@OptIn(ExperimentalJsExport::class)
enum class RuleValueType(private val defaultValue: Any) {
    TEXT(""),
    NUMERIC(0.0),
    BOOLEAN(false),
    DATE("2020-01-01");

    fun defaultValue(): Any {
        return defaultValue
    }
}
