package org.dhis2.ruleengine.utils

actual fun decimalFormatter(): DecimalFormatter {
    return object : DecimalFormatter {
        override fun format(value: Float, decimalPoints: Int): String {
            return value.asDynamic().toFixed(decimalPoints) as String
        }

        override fun format(value: Double, decimalPoints: Int): String {
            return value.asDynamic().toFixed(decimalPoints) as String
        }
    }
}