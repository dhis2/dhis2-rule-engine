package org.dhis2.ruleengine.utils

actual fun decimalFormatter(): DecimalFormatter {
    return object : DecimalFormatter {
        override fun format(value: Float): String {
            return value.asDynamic().toFixed(2) as String
        }
    }
}