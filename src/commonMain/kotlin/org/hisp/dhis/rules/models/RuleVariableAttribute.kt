package org.hisp.dhis.rules.models

import kotlinx.datetime.LocalDate
import org.hisp.dhis.rules.engine.RuleVariableValue
import org.hisp.dhis.rules.utils.currentDate

data class RuleVariableAttribute(
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
        val currentDate = currentDate()
        return currentEnrollmentValues[field]?.let {
            val optionValue = if (useCodeForOptionSet) it.value else getOptionName(it.value)
            RuleVariableValue(
                fieldType,
                optionValue,
                listOf(optionValue),
                currentDate.toString(),
            )
        } ?: RuleVariableValue(fieldType)
    }
}
