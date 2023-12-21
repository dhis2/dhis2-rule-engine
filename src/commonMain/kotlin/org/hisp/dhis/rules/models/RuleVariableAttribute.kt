package org.hisp.dhis.rules.models

import kotlinx.datetime.LocalDate
import org.hisp.dhis.rules.RuleVariableValue
import org.hisp.dhis.rules.RuleVariableValueMapBuilder
import org.hisp.dhis.rules.currentDate

data class RuleVariableAttribute(
    val name: String,
    val useCodeForOptionSet: Boolean,
    val options2: List<Option>,
    val trackedEntityAttribute: String,
    val trackedEntityAttributeType: RuleValueType
) : RuleVariable {
    override fun options(): List<Option> {
        return options2
    }

    override fun createValues(
        builder: RuleVariableValueMapBuilder,
        allEventValues: Map<String, List<RuleDataValue>>,
        currentEnrollmentValues: Map<String, RuleAttributeValue>,
        currentEventValues: Map<String, RuleDataValue>
    ): Map<String, RuleVariableValue> {
        val valueMap: MutableMap<String, RuleVariableValue> = HashMap()
        val currentDate = LocalDate.Companion.currentDate()
        val variableValue: RuleVariableValue
        variableValue = if (currentEnrollmentValues.containsKey(trackedEntityAttribute)) {
            val value = currentEnrollmentValues[trackedEntityAttribute]
            val optionValue = if (useCodeForOptionSet) value!!.value else getOptionName(value!!.value)!!
            RuleVariableValue(
                trackedEntityAttributeType, optionValue,
                listOf(optionValue), currentDate.toString()
            )
        } else {
            RuleVariableValue(trackedEntityAttributeType)
        }
        valueMap[name] = variableValue
        return valueMap
    }
}
