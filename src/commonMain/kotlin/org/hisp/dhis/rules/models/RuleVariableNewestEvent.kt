package org.hisp.dhis.rules.models

import org.hisp.dhis.rules.engine.RuleVariableValue
import org.hisp.dhis.rules.utils.getLastUpdateDate

class RuleVariableNewestEvent(
    override val name: String,
    override val useCodeForOptionSet: Boolean,
    override val options: List<Option>,
    override val field: String,
    override val fieldType: RuleValueType
) : RuleVariable {
    override fun createValues(
        ruleEvent: RuleEvent?,
        allEventValues: Map<String, List<RuleDataValueHistory>>,
        currentEnrollmentValues: Map<String, RuleAttributeValue>
    ): RuleVariableValue {
        val ruleDataValues = allEventValues[field]
        return if (ruleDataValues.isNullOrEmpty()) {
             RuleVariableValue(fieldType)
        } else {
            val value = ruleDataValues[0]
            val optionValue = if (useCodeForOptionSet) value.value else getOptionName(value.value)
            RuleVariableValue(
                fieldType, optionValue,
                ruleDataValues.map { it.value }, getLastUpdateDate(ruleDataValues)
            )
        }
    }
}
