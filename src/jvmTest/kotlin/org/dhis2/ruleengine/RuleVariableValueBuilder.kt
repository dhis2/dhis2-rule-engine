package org.dhis2.ruleengine

import kotlinx.datetime.toLocalDate
import kotlinx.datetime.toLocalDateTime
import org.dhis2.ruleengine.models.RuleValueType

class RuleVariableValueBuilder {
    private var value: String? = null
    private var type: RuleValueType = RuleValueType.TEXT
    private var candidates: List<String> = ArrayList()
    private var eventDate: String? = null
    fun withValue(value: String): RuleVariableValueBuilder {
        this.value = value
        return this
    }

    fun withCandidates(candidates: List<String>): RuleVariableValueBuilder {
        this.candidates = candidates
        return this
    }

    fun withEventDate(eventDate: String?): RuleVariableValueBuilder {
        this.eventDate = eventDate
        return this
    }

    fun withType(valueType: RuleValueType): RuleVariableValueBuilder {
        type = valueType
        return this
    }

    fun build(): RuleVariableValue {
        return RuleVariableValue(
            variableValue = value,
            ruleValueType = type,
            candidates = candidates,
            eventDate = eventDate?.toLocalDate()
        )
    }

    companion object {
        fun create(): RuleVariableValueBuilder {
            return RuleVariableValueBuilder()
        }
    }
}