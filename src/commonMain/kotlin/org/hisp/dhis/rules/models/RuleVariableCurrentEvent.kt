package org.hisp.dhis.rules.models

import org.hisp.dhis.rules.engine.RuleVariableValue
import org.hisp.dhis.rules.engine.RuleVariableValueMapBuilder
import org.hisp.dhis.rules.utils.getLastUpdateDate

class RuleVariableCurrentEvent(
    val name: String,
    val useCodeForOptionSet: Boolean,
    override val options: List<Option>,
    override val dataElement: String,
    override val dataElementType: RuleValueType,
) : RuleVariableDataElement {
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
            RuleVariableValue(
                dataElementType, optionValue,
                listOf(optionValue), getLastUpdateDate(listOf(value))
            )
        } else {
            RuleVariableValue(dataElementType)
        }
        valueMap[name] = variableValue
        return valueMap
    }
}
