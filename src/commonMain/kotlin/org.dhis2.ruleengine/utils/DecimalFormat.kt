package org.dhis2.ruleengine.utils

interface DecimalFormatter {
    fun format(value: Float, decimalPoints:Int): String
    fun format(value: Double, decimalPoints:Int): String
}

expect fun decimalFormatter(): DecimalFormatter