package org.hisp.dhis.rules.models

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.hisp.dhis.rules.engine.RuleVariableValue
import org.hisp.dhis.rules.utils.getLastUpdateDate

class RuleVariableCurrentEvent(
    override val name: String,
    override val useCodeForOptionSet: Boolean,
    override val options: List<Option>,
    override val field: String,
    override val fieldType: RuleValueType
) : RuleVariable {
    override fun createValues(
        ruleEvent: RuleEvent?,
        allEventValues: Map<String, List<RuleDataValueHistory>>,
        currentEnrollmentValues: Map<String, RuleAttributeValue>,
        currentEventValues: Map<String, RuleDataValue>
    ): RuleVariableValue {
        val variableValue = if (currentEventValues.containsKey(field)) {
            val value = currentEventValues[field]
            val optionValue = if (useCodeForOptionSet) value!!.value else getOptionName(value!!.value)!!
            RuleVariableValue(
                fieldType, optionValue,
                listOf(optionValue), ruleEvent!!.eventDate.toLocalDateTime(TimeZone.currentSystemDefault()).date.toString())
        } else {
            RuleVariableValue(fieldType)
        }
        return variableValue
    }
}
