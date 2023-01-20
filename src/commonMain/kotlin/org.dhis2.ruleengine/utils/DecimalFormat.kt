package org.dhis2.ruleengine.utils

interface DecimalFormatter {
    fun format(value: Float): String
}

expect fun decimalFormatter(): DecimalFormatter