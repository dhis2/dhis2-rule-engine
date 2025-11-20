package org.hisp.dhis.rules.models

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.hisp.dhis.rules.engine.RuleVariableValue


class RuleVariableCurrentEvent(
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
    ): RuleVariableValue =
        ruleEvent
            ?.dataValues
            ?.filter { d -> d.dataElement == field }
            ?.map {
                val optionValue = if (useCodeForOptionSet) it.value else getOptionName(it.value)
                RuleVariableValue(
                    fieldType,
                    optionValue,
                    listOf(optionValue),
                    ruleEvent.eventDate.toInstant()
                        .toLocalDateTime(TimeZone.currentSystemDefault())
                        .date
                        .toString(),
                )
            }?.firstOrNull() ?: RuleVariableValue(fieldType)
}
