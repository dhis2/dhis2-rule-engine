package org.hisp.dhis.rules.models

import org.hisp.dhis.rules.engine.RuleVariableValue
import org.hisp.dhis.rules.engine.RuleVariableValueMapBuilder
import org.hisp.dhis.rules.utils.getLastUpdateDate
import org.hisp.dhis.rules.utils.values

class RuleVariableNewestEvent(
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
        val ruleDataValues = allEventValues[field]
        if (ruleDataValues.isNullOrEmpty()) {
            valueMap[name] = RuleVariableValue(fieldType)
        } else {
            val variableValue: RuleVariableValue
            val value = ruleDataValues[0]
            val optionValue = if (useCodeForOptionSet) value.value else getOptionName(value.value)!!
            variableValue = RuleVariableValue(
                fieldType, optionValue,
                values(ruleDataValues), getLastUpdateDate(ruleDataValues)
            )
            valueMap[name] = variableValue
        }
        return valueMap
    }
}
