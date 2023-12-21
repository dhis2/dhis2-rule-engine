package org.hisp.dhis.rules

import org.hisp.dhis.lib.expression.spi.ValueType
import org.hisp.dhis.lib.expression.spi.VariableValue
import org.hisp.dhis.rules.models.RuleValueType

data class RuleVariableValue(
    val type: RuleValueType,
    val value: String? = null,
    val candidates: List<String> = listOf(),
    val eventDate: String? = null
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
