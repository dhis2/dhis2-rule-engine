package org.hisp.dhis.rules.models

import org.hisp.dhis.rules.engine.RuleVariableValue
import org.hisp.dhis.rules.engine.RuleVariableValueMapBuilder
import org.hisp.dhis.rules.utils.getLastUpdateDateForPrevious
import org.hisp.dhis.rules.utils.values

class RuleVariablePreviousEvent(
    val name: String,
    val useCodeForOptionSet: Boolean,
    override val options: List<Option>,
    override val dataElement: String,
    override val dataElementType: RuleValueType
) : RuleVariableDataElement {
    override fun createValues(
        ruleEvent: RuleEvent?,
        allEventValues: Map<String, List<RuleDataValue>>,
        currentEnrollmentValues: Map<String, RuleAttributeValue>,
        currentEventValues: Map<String, RuleDataValue>
    ): Map<String, RuleVariableValue> {
        val valueMap: MutableMap<String, RuleVariableValue> = HashMap()
        var variableValue: RuleVariableValue? = null
        val ruleDataValues = allEventValues[dataElement]
        if (ruleEvent != null && !ruleDataValues.isNullOrEmpty()) {
            for (ruleDataValue in ruleDataValues) {
                // We found preceding value to the current currentEventValues,
                // which is assumed to be best candidate.
                if (ruleEvent.eventDate > ruleDataValue.eventDate) {
                    val optionValue =
                        if (useCodeForOptionSet) ruleDataValue.value else getOptionName(ruleDataValue.value)!!
                    variableValue = RuleVariableValue(
                        dataElementType, optionValue,
                        values(ruleDataValues),
                        getLastUpdateDateForPrevious(ruleDataValues, ruleEvent)
                    )
                    break
                }
            }
        }
        if (variableValue == null) {
            variableValue = RuleVariableValue(dataElementType)
        }
        valueMap[name] = variableValue
        return valueMap
    }
}