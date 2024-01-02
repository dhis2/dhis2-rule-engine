package org.hisp.dhis.rules.models

import org.hisp.dhis.rules.engine.RuleVariableValue
import org.hisp.dhis.rules.engine.RuleVariableValueMapBuilder
import org.hisp.dhis.rules.utils.getLastUpdateDateForPrevious
import org.hisp.dhis.rules.utils.values

class RuleVariablePreviousEvent(
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
        var variableValue: RuleVariableValue? = null
        val ruleDataValues = allEventValues[dataElement()]
        if (builder.ruleEvent != null && ruleDataValues != null && !ruleDataValues.isEmpty()) {
            for (ruleDataValue in ruleDataValues) {
                // We found preceding value to the current currentEventValues,
                // which is assumed to be best candidate.
                if (builder.ruleEvent!!.eventDate.compareTo(ruleDataValue.eventDate) > 0) {
                    val optionValue =
                        if (useCodeForOptionSet) ruleDataValue.value else getOptionName(ruleDataValue.value)!!
                    variableValue = RuleVariableValue(
                        dataElementType(), optionValue,
                        values(ruleDataValues),
                        getLastUpdateDateForPrevious(ruleDataValues, builder.ruleEvent!!)
                    )
                    break
                }
            }
        }
        if (variableValue == null) {
            variableValue = RuleVariableValue(dataElementType())
        }
        valueMap[name] = variableValue
        return valueMap
    }
}
