package org.hisp.dhis.rules.models

import org.hisp.dhis.rules.engine.RuleVariableValue
import org.hisp.dhis.rules.engine.RuleVariableValueMapBuilder
import org.hisp.dhis.rules.utils.getLastUpdateDate

class RuleVariableCurrentEvent(
    override val name: String,
    override val useCodeForOptionSet: Boolean,
    override val options: List<Option>,
    override val field: String,
    override val fieldType: RuleValueType,
) : RuleVariable {
    override fun createValues(
        ruleEvent: RuleEvent?,
        allEventValues: Map<String, List<RuleDataValue>>,
        currentEnrollmentValues: Map<String, RuleAttributeValue>,
        currentEventValues: Map<String, RuleDataValue>
    ): Map<String, RuleVariableValue> {
        val valueMap: MutableMap<String, RuleVariableValue> = HashMap()
        val variableValue: RuleVariableValue
        variableValue = if (currentEventValues.containsKey(field)) {
            val value = currentEventValues[field]
            val optionValue = if (useCodeForOptionSet) value!!.value else getOptionName(value!!.value)!!
            RuleVariableValue(
                fieldType, optionValue,
                listOf(optionValue), getLastUpdateDate(listOf(value))
            )
        } else {
            RuleVariableValue(fieldType)
        }
        valueMap[name] = variableValue
        return valueMap
    }
}
