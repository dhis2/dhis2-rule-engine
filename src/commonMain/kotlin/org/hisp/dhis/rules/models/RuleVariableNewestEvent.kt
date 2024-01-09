package org.hisp.dhis.rules.models

import org.hisp.dhis.rules.engine.RuleVariableValue
import org.hisp.dhis.rules.engine.RuleVariableValueMapBuilder
import org.hisp.dhis.rules.utils.getLastUpdateDate
import org.hisp.dhis.rules.utils.values

class RuleVariableNewestEvent(
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
        val ruleDataValues = allEventValues[dataElement]
        if (ruleDataValues.isNullOrEmpty()) {
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
