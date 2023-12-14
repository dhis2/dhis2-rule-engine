package org.hisp.dhis.rules.models

import org.hisp.dhis.rules.*

class RuleVariableNewestStageEvent(
    val name: String,
    val useCodeForOptionSet: Boolean,
    val options2: List<Option>,
    val dataElement2: String,
    val dataElementType2: RuleValueType,
    val programStage: String
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
        val stageRuleDataValues: MutableList<RuleDataValue> = ArrayList()
        val sourceRuleDataValues = allEventValues[dataElement()]
        if (sourceRuleDataValues != null && !sourceRuleDataValues.isEmpty()) {

            // filter data values based on program stage
            for (ruleDataValue in sourceRuleDataValues) {
                if (programStage == ruleDataValue.programStage) {
                    stageRuleDataValues.add(ruleDataValue)
                }
            }
        }
        if (stageRuleDataValues.isEmpty()) {
            valueMap[name] = RuleVariableValue(dataElementType())
        } else {
            val variableValue: RuleVariableValue
            val value = stageRuleDataValues[0]
            val optionValue = if (useCodeForOptionSet) value.value else getOptionName(value.value)!!
            variableValue = RuleVariableValue(
                dataElementType(), optionValue,
                values(stageRuleDataValues),
                getLastUpdateDate(stageRuleDataValues)
            )
            valueMap[name] = variableValue
        }
        return valueMap
    }
}
