package org.hisp.dhis.rules

import org.hisp.dhis.rules.models.RuleEnrollment
import org.hisp.dhis.rules.models.RuleEvent

data class RuleVariableValueMap(
    val enrollmentMap: Map<RuleEnrollment, Map<String, RuleVariableValue>>,
    val eventMap: Map<RuleEvent, Map<String, RuleVariableValue>>
) {
    fun enrollmentMap(): Map<RuleEnrollment, Map<String, RuleVariableValue>> {
        return enrollmentMap
    }
    fun eventMap(): Map<RuleEvent, Map<String, RuleVariableValue>> {
        return eventMap
    }
}