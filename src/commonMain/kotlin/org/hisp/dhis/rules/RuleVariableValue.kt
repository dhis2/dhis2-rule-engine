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
            org.hisp.dhis.rules.models.RuleValueType.DATE -> ValueType.DATE
            org.hisp.dhis.rules.models.RuleValueType.NUMERIC -> ValueType.NUMBER
            org.hisp.dhis.rules.models.RuleValueType.BOOLEAN -> ValueType.BOOLEAN
            else -> ValueType.STRING
        }
    }
}
