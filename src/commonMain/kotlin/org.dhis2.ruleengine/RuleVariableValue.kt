package org.dhis2.ruleengine

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.dhis2.ruleengine.models.RuleValueType

class RuleVariableValue(
    private var variableValue: String? = null,
    val ruleValueType: RuleValueType,
    val candidates: List<String> = emptyList(),
    val eventDate: LocalDate? = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
) {
    val value get() = variableValue.takeIf { it != null }?:ruleValueType.defaultValue

    init {
        variableValue = variableValue?.replace("'", "")
    }
}
