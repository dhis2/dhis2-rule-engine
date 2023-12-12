package org.hisp.dhis.rules

import org.hisp.dhis.rules.models.RuleEnrollment
import org.hisp.dhis.rules.models.RuleEvent

data class RuleVariableValueMap(
    val enrollmentMap: Map<RuleEnrollment, MutableMap<String, RuleVariableValue>>,
    val eventMap: Map<RuleEvent, MutableMap<String, RuleVariableValue>>
) {
    fun enrollmentMap(): Map<RuleEnrollment, MutableMap<String, RuleVariableValue>> {
        return enrollmentMap
    }
    fun eventMap(): Map<RuleEvent, MutableMap<String, RuleVariableValue>> {
        return eventMap
    }
}