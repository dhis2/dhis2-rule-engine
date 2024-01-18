package org.hisp.dhis.rules.models

import kotlinx.datetime.LocalDate
import org.hisp.dhis.rules.engine.RuleVariableValue
import org.hisp.dhis.rules.utils.currentDate

data class RuleVariableAttribute(
    override val name: String,
    override val useCodeForOptionSet: Boolean,
    override val options: List<Option>,
    override val field: String,
    override val fieldType: RuleValueType
) : RuleVariable {
    override fun createValues(
        ruleEvent: RuleEvent?,
        allEventValues: Map<String, List<RuleDataValue>>,
        currentEnrollmentValues: Map<String, RuleAttributeValue>,
        currentEventValues: Map<String, RuleDataValue>
    ): Map<String, RuleVariableValue> {
        val valueMap: MutableMap<String, RuleVariableValue> = HashMap()
        val currentDate = LocalDate.Companion.currentDate()
        val variableValue = if (currentEnrollmentValues.containsKey(field)) {
            val value = currentEnrollmentValues[field]
            val optionValue = if (useCodeForOptionSet) value!!.value else getOptionName(value!!.value)!!
            RuleVariableValue(
                fieldType, optionValue,
                listOf(optionValue), currentDate.toString()
            )
        } else {
            RuleVariableValue(fieldType)
        }
        valueMap[name] = variableValue
        return valueMap
    }
}
