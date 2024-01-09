package org.hisp.dhis.rules.models

import org.hisp.dhis.rules.engine.RuleVariableValue
import org.hisp.dhis.rules.engine.RuleVariableValueMapBuilder
import org.hisp.dhis.rules.utils.getLastUpdateDate
import org.hisp.dhis.rules.utils.values

class RuleVariableNewestStageEvent(
    val name: String,
    val useCodeForOptionSet: Boolean,
    override val options: List<Option>,
    override val dataElement: String,
    override val dataElementType: RuleValueType,
    val programStage: String
) : RuleVariableDataElement {

    override fun createValues(
        builder: RuleVariableValueMapBuilder,
        allEventValues: Map<String, List<RuleDataValue>>,
        currentEnrollmentValues: Map<String, RuleAttributeValue>,
        currentEventValues: Map<String, RuleDataValue>
    ): Map<String, RuleVariableValue> {
        val valueMap: MutableMap<String, RuleVariableValue> = HashMap()
        val stageRuleDataValues: MutableList<RuleDataValue> = ArrayList()
        val sourceRuleDataValues = allEventValues[dataElement]
        if (!sourceRuleDataValues.isNullOrEmpty()) {

            // filter data values based on program stage
            for (ruleDataValue in sourceRuleDataValues) {
                if (programStage == ruleDataValue.programStage) {
                    stageRuleDataValues.add(ruleDataValue)
                }
            }
        }
        if (stageRuleDataValues.isEmpty()) {
            valueMap[name] = RuleVariableValue(dataElementType)
        } else {
            val variableValue: RuleVariableValue
            val value = stageRuleDataValues[0]
            val optionValue = if (useCodeForOptionSet) value.value else getOptionName(value.value)!!
            variableValue = RuleVariableValue(
                dataElementType, optionValue,
                values(stageRuleDataValues),
                getLastUpdateDate(stageRuleDataValues)
            )
            valueMap[name] = variableValue
        }
        return valueMap
    }
}
