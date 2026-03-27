package org.hisp.dhis.rules.models

import kotlin.js.JsExport
import org.hisp.dhis.lib.expression.spi.ValueType

@JsExport
enum class RuleValueType(
    private val defaultValue: Any,
) {
    TEXT(""),
    NUMERIC(0.0),
    BOOLEAN(false),
    DATE("2020-01-01"),
    ;

    fun defaultValue(): Any = defaultValue

    internal fun toValueType(): ValueType = when (this) {
        DATE -> ValueType.DATE
        NUMERIC -> ValueType.NUMBER
        BOOLEAN -> ValueType.BOOLEAN
        else -> ValueType.STRING
    }
}
