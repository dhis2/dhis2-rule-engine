package org.hisp.dhis.rules.models

import org.hisp.dhis.rules.Option
import org.hisp.dhis.rules.RuleVariableValue
import org.hisp.dhis.rules.RuleVariableValueMapBuilder
import org.hisp.dhis.rules.getLastUpdateDate

class RuleVariableCurrentEvent(
    val name: String,
    val useCodeForOptionSet: Boolean,
    val options2: List<Option>,
    val dataElement2: String,
    val dataElementType2: RuleValueType
) : RuleVariableDataElement {
    override fun dataElement(): String {
        return dataElement2
    }

    override fun dataElementType(): RuleValueType {
        return dataElementType2
    }

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
        val variableValue: RuleVariableValue
        variableValue = if (currentEventValues.containsKey(dataElement())) {
            val value = currentEventValues[dataElement()]
            val optionValue = if (useCodeForOptionSet) value!!.value else getOptionName(value!!.value)!!
            RuleVariableValue(
                dataElementType(), optionValue,
                listOf(optionValue), getLastUpdateDate(listOf(value))
            )
        } else {
            RuleVariableValue(dataElementType())
        }
        valueMap[name] = variableValue
        return valueMap
    }
}
