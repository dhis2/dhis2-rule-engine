package org.hisp.dhis.rules.api

import org.hisp.dhis.lib.expression.spi.ValueType
import kotlin.js.JsExport

/**
 * The type of a value accepted or returned by a function or operator of the rule engine expression language.
 */
@JsExport
enum class ExpressionValueType {
    /**
     * The value can be of more than one type (or the type cannot be determined statically).
     */
    MIXED,
    NUMBER,
    BOOLEAN,
    DATE,
    STRING,

    /**
     * The value can be of any type, but all values typed as SAME in a signature must have the same actual type.
     * When used as a return type, the result has the actual type of the SAME parameter(s).
     */
    SAME,
    ;

    internal companion object {
        fun from(valueType: ValueType): ExpressionValueType =
            when (valueType) {
                ValueType.MIXED -> MIXED
                ValueType.NUMBER -> NUMBER
                ValueType.BOOLEAN -> BOOLEAN
                ValueType.DATE -> DATE
                ValueType.STRING -> STRING
                ValueType.SAME -> SAME
            }
    }
}
