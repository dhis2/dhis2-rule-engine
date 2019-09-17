package org.hisp.dhis.rules

import org.hisp.dhis.rules.models.RuleValueType

class RuleVariableValueBuilder {

    private var value: String? = null

    private val type = RuleValueType.TEXT

    private var candidates: List<String> = ArrayList()

    private var eventDate: String? = null

    fun withValue(value: String) = apply { this.value = value }

    fun withCandidates(candidates: List<String>) = apply { this.candidates = candidates }

    fun withEventDate(eventDate: String?) = apply { this.eventDate = eventDate }

    fun build() =  RuleVariableValue.create(value, type, candidates, eventDate)

    companion object {

        @JvmStatic
        fun create() = RuleVariableValueBuilder()

    }
}
