package org.hisp.dhis.rules.models

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.hisp.dhis.rules.engine.RuleVariableValue


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
            for (ruleDataValue in dataValues) {
                if (ruleEvent.eventDate.toInstant() > ruleDataValue.eventDate ||
                    (ruleEvent.eventDate.toInstant() == ruleDataValue.eventDate && ruleEvent.createdDate.toInstant() > ruleDataValue.createdDate)
                ) {
                    val optionValue =
                        if (useCodeForOptionSet) ruleDataValue.value else getOptionName(ruleDataValue.value)
                    return RuleVariableValue(
                        fieldType,
                        optionValue,
                        dataValues.map { it.value },
                        ruleDataValue.eventDate
                            .toLocalDateTime(TimeZone.currentSystemDefault())
                            .date
                            .toString(),
                    )
                }
            }
        }

        return RuleVariableValue(fieldType)
    }
}
