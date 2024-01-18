package org.hisp.dhis.rules.models

import org.hisp.dhis.rules.engine.RuleVariableValue
import org.hisp.dhis.rules.utils.getLastUpdateDate
import org.hisp.dhis.rules.utils.values

class RuleVariableNewestStageEvent(
    override val name: String,
    override val useCodeForOptionSet: Boolean,
    override val options: List<Option>,
    override val field: String,
    override val fieldType: RuleValueType,
    val programStage: String
) : RuleVariable {

    override fun createValues(
        ruleEvent: RuleEvent?,
        allEventValues: Map<String, List<RuleDataValue>>,
        currentEnrollmentValues: Map<String, RuleAttributeValue>,
        currentEventValues: Map<String, RuleDataValue>
    ): Map<String, RuleVariableValue> {
        val valueMap: MutableMap<String, RuleVariableValue> = HashMap()
        val stageRuleDataValues: MutableList<RuleDataValue> = ArrayList()
        val sourceRuleDataValues = allEventValues[field]
        if (!sourceRuleDataValues.isNullOrEmpty()) {

            // filter data values based on program stage
            for (ruleDataValue in sourceRuleDataValues) {
                if (programStage == ruleDataValue.programStage) {
                    stageRuleDataValues.add(ruleDataValue)
                }
            }
        }
        if (stageRuleDataValues.isEmpty()) {
            valueMap[name] = RuleVariableValue(fieldType)
        } else {
            val variableValue: RuleVariableValue
            val value = stageRuleDataValues[0]
            val optionValue = if (useCodeForOptionSet) value.value else getOptionName(value.value)!!
            variableValue = RuleVariableValue(
                fieldType, optionValue,
                values(stageRuleDataValues),
                getLastUpdateDate(stageRuleDataValues)
            )
            valueMap[name] = variableValue
        }
        return valueMap
    }
}
