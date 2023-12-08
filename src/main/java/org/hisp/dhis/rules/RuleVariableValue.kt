package org.hisp.dhis.rules

import org.hisp.dhis.lib.expression.spi.ValueType
import org.hisp.dhis.lib.expression.spi.VariableValue
import org.hisp.dhis.rules.models.RuleValueType
import java.text.SimpleDateFormat
import java.util.Date

fun getFormattedDate(date: Date): String {
    val format = SimpleDateFormat()
    format.applyPattern("yyyy-MM-dd")
    return format.format(date)
}

data class RuleVariableValue(
    val type: RuleValueType,
    val value: String? = null,
    val candidates: List<String> = listOf(),
    val eventDate: String? = getFormattedDate(Date())
) {
    fun toVariableValue(): VariableValue {
        return VariableValue(valueType(), value, candidates, eventDate)
    }

    private fun valueType(): ValueType {
        return when (type) {
            RuleValueType.DATE -> ValueType.DATE
            RuleValueType.NUMERIC -> ValueType.NUMBER
            RuleValueType.BOOLEAN -> ValueType.BOOLEAN
            else -> ValueType.STRING
        }
    }
}
