package org.hisp.dhis.rules.models

import kotlinx.datetime.LocalDate
import org.hisp.dhis.rules.engine.RuleVariableValue
import org.hisp.dhis.rules.engine.RuleVariableValueMapBuilder
import org.hisp.dhis.rules.utils.currentDate

data class RuleVariableAttribute(
    val name: String,
    val useCodeForOptionSet: Boolean,
    override val options: List<Option>,
    val trackedEntityAttribute: String,
    val trackedEntityAttributeType: RuleValueType
) : RuleVariable {
    override fun createValues(
        builder: RuleVariableValueMapBuilder,
        allEventValues: Map<String, List<RuleDataValue>>,
        currentEnrollmentValues: Map<String, RuleAttributeValue>,
        currentEventValues: Map<String, RuleDataValue>
    ): Map<String, RuleVariableValue> {
        val valueMap: MutableMap<String, RuleVariableValue> = HashMap()
        val currentDate = LocalDate.Companion.currentDate()
        val variableValue = if (currentEnrollmentValues.containsKey(trackedEntityAttribute)) {
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
