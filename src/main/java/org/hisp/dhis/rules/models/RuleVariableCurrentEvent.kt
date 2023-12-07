package org.hisp.dhis.rules.models

import org.hisp.dhis.rules.*
import javax.annotation.Nonnull

class RuleVariableCurrentEvent(
    val name: String,
    val useCodeForOptionSet: Boolean,
    val options: List<Option>,
    val dataElement: String,
    val dataElementType: RuleValueType
) : RuleVariableDataElement {
    override fun dataElement(): String {
        return dataElement
    }

    override fun dataElementType(): RuleValueType {
        return dataElementType
    }

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
        val variableValue: RuleVariableValue
        variableValue = if (currentEventValues.containsKey(dataElement)) {
            val value = currentEventValues[dataElement]
            val optionValue = if (useCodeForOptionSet) value!!.value else getOptionName(value!!.value)!!
            RuleVariableValue.create(
                optionValue, dataElementType,
                java.util.List.of(optionValue), getLastUpdateDate(java.util.List.of(value))
            )
        } else {
            RuleVariableValue.create(dataElementType)
        }
        valueMap[name] = variableValue
        return valueMap
    }
}
