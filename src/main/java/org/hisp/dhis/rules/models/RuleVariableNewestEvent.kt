package org.hisp.dhis.rules.models

import org.hisp.dhis.rules.*

class RuleVariableNewestEvent(
    val name: String,
    val useCodeForOptionSet: Boolean,
    val options: List<Option>,
    val  dataElement: String,
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
        val ruleDataValues = allEventValues[dataElement]
        if (ruleDataValues == null || ruleDataValues.isEmpty()) {
            valueMap[name] = RuleVariableValue(dataElementType)
        } else {
            val variableValue: RuleVariableValue
            val value = ruleDataValues[0]
            val optionValue = if (useCodeForOptionSet) value.value else getOptionName(value.value)!!
            variableValue = RuleVariableValue(
                dataElementType, optionValue,
                values(ruleDataValues), getLastUpdateDate(ruleDataValues)
            )
            valueMap[name] = variableValue
        }
        return valueMap
    }
}
