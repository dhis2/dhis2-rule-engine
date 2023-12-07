package org.hisp.dhis.rules.models

import org.hisp.dhis.rules.Option
import org.hisp.dhis.rules.RuleVariableValue
import org.hisp.dhis.rules.RuleVariableValueMapBuilder
import org.hisp.dhis.rules.dateFormat
import java.util.*
import javax.annotation.Nonnull

data class RuleVariableAttribute(
    val name: String,
    val useCodeForOptionSet: Boolean,
    val options: List<Option>,
    val trackedEntityAttribute: String,
    val trackedEntityAttributeType: RuleValueType
) : RuleVariable {
    override fun options(): List<Option> {
        return options
    }

    override fun createValues(
        builder: RuleVariableValueMapBuilder,
        allEventValues: Map<String, List<RuleDataValue>>,
        currentEnrollmentValues: Map<String, RuleAttributeValue>,
        currentEventValues: Map<String, RuleDataValue>
    ): Map<String, RuleVariableValue> {
        val valueMap: MutableMap<String, RuleVariableValue> = HashMap()
        val currentDate = dateFormat.format(Date())
        val variableValue: RuleVariableValue
        variableValue = if (currentEnrollmentValues.containsKey(trackedEntityAttribute)) {
            val value = currentEnrollmentValues[trackedEntityAttribute]
            val optionValue = if (useCodeForOptionSet) value!!.value else getOptionName(value!!.value)!!
            RuleVariableValue.create(
                optionValue, trackedEntityAttributeType,
                java.util.List.of(optionValue), currentDate
            )
        } else {
            RuleVariableValue.create(trackedEntityAttributeType)
        }
        valueMap[name] = variableValue
        return valueMap
    }
}
