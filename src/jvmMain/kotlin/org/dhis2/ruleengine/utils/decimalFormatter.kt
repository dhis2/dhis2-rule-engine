package org.dhis2.ruleengine.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

actual fun decimalFormatter(): DecimalFormatter {
    return object : DecimalFormatter {
        override fun format(value: Float): String {
            val decimalFormatSymbols = DecimalFormatSymbols.getInstance()
            decimalFormatSymbols.decimalSeparator = '.'
            return DecimalFormat("##0.00", decimalFormatSymbols).format(value)
        }

    }
}