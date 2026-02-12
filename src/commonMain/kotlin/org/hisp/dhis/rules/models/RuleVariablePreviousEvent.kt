package org.hisp.dhis.rules.models

import org.hisp.dhis.rules.engine.RuleVariableValue
import org.hisp.dhis.rules.utils.getPreviousDataValue


class RuleVariablePreviousEvent(
    override val name: String,
    override val useCodeForOptionSet: Boolean,
    override val options: List<Option>,
    override val field: String,
    override val fieldType: RuleValueType,
) : RuleVariable {
    override fun createValues(
        ruleEvent: RuleEvent?,
        allEventValues: Map<String, List<RuleDataValueHistory>>,
        currentEnrollmentValues: Map<String, RuleAttributeValue>,
    ): RuleVariableValue {
        val dataValues = allEventValues[field]
        if (ruleEvent != null && !dataValues.isNullOrEmpty()) {
            val ruleDataValue = getPreviousDataValue(dataValues, ruleEvent)
            if (ruleDataValue != null) {
                val optionValue =
                    if (useCodeForOptionSet) ruleDataValue.value else getOptionName(ruleDataValue.value)
                return RuleVariableValue(
                    fieldType,
                    optionValue,
                    dataValues.map { it.value },
                    ruleDataValue.eventDate.toString(),
                )
            }
        }
        return RuleVariableValue(fieldType)
    }
}
