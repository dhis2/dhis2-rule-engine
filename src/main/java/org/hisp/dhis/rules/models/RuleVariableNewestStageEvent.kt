package org.hisp.dhis.rules.models

import org.hisp.dhis.rules.*
import javax.annotation.Nonnull

class RuleVariableNewestStageEvent(
    val name: String,
    val useCodeForOptionSet: Boolean,
    val options: List<Option>,
    val dataElement: String,
    val dataElementType: RuleValueType,
    val programStage: String
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
        val stageRuleDataValues: MutableList<RuleDataValue> = ArrayList()
        val sourceRuleDataValues = allEventValues[dataElement]
        if (sourceRuleDataValues != null && !sourceRuleDataValues.isEmpty()) {

            // filter data values based on program stage
            for (ruleDataValue in sourceRuleDataValues) {
                if (programStage == ruleDataValue.programStage) {
                    stageRuleDataValues.add(ruleDataValue)
                }
            }
        }
        if (stageRuleDataValues.isEmpty()) {
            valueMap[name] = RuleVariableValue.create(dataElementType)
        } else {
            val variableValue: RuleVariableValue
            val value = stageRuleDataValues[0]
            val optionValue = if (useCodeForOptionSet) value.value else getOptionName(value.value)!!
            variableValue = RuleVariableValue.create(
                optionValue,
                dataElementType, values(stageRuleDataValues),
                getLastUpdateDate(stageRuleDataValues)
            )
            valueMap[name] = variableValue
        }
        return valueMap
    }
}
