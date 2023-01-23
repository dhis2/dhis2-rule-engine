package org.dhis2.ruleengine.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

actual fun decimalFormatter(): DecimalFormatter {
    return object : DecimalFormatter {
        override fun format(value: Float, decimalPoints:Int): String {
            val decimalFormatSymbols = DecimalFormatSymbols.getInstance()
            decimalFormatSymbols.decimalSeparator = '.'
            return DecimalFormat(getPattern(decimalPoints), decimalFormatSymbols).format(value)
        }

        override fun format(value: Double, decimalPoints: Int): String {
            val decimalFormatSymbols = DecimalFormatSymbols.getInstance()
            decimalFormatSymbols.decimalSeparator = '.'
            return DecimalFormat(getPattern(decimalPoints), decimalFormatSymbols).format(value)
        }

        private fun getPattern(decimalPoints:Int):String {
            var initialPattern = "##0."
            repeat(decimalPoints){
                initialPattern += "0"
            }
            return initialPattern
        }
    }

}