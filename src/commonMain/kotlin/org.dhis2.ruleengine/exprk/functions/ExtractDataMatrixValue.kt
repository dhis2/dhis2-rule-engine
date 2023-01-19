package org.dhis2.ruleengine.exprk.functions

import org.dhis2.ruleengine.exprk.internal.Function
import org.dhis2.ruleengine.gs1.GS1BaseValueFormatter
import org.dhis2.ruleengine.gs1.GS1Elements
import org.dhis2.ruleengine.gs1.GS1ValueFormatter

const val EXTRACT_DATAMATRIX_VALUE = "d2:extractDataMatrixValue"
class ExtractDataMatrixValue : Function() {
    override fun call(arguments: List<String?>): String {
        if (arguments.size != 2) throw IllegalArgumentException("two arguments required")
        val gs1Key = arguments[0]
        val value = arguments[1]
        return when {
            gs1Key == null -> ""
            value == null -> ""
            else -> extractDataMatrixValue(value, gs1Key) ?: ""
        }
    }

    private fun extractDataMatrixValue(value: String, key: String): String? {
        val formatter: GS1ValueFormatter = GS1BaseValueFormatter.getFormatterFromValue(value)
        return formatter.formatValue(value, GS1Elements.fromKey(key))
    }
}