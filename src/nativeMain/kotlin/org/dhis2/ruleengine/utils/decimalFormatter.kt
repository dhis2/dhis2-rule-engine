package org.dhis2.ruleengine.utils

actual fun decimalFormatter(): DecimalFormatter {
    return object :DecimalFormatter{
        override fun format(value: Float, decimalPoints: Int): String {
            TODO("Not yet implemented")
        }

        override fun format(value: Double, decimalPoints: Int): String {
            TODO("Not yet implemented")
        }

    }
}