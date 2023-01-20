package org.dhis2.ruleengine.utils

actual fun decimalFormatter(): DecimalFormatter {
    return object :DecimalFormatter{
        override fun format(value: Float): String {
            //TODO: ADD format implementation in NATIVE
            return ""
        }

    }
}