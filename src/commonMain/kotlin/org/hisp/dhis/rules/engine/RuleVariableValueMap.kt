package org.hisp.dhis.rules.engine

import org.hisp.dhis.rules.models.RuleEvent

internal data class RuleVariableValueMap(
    val enrollmentMap: Map<org.hisp.dhis.rules.models.RuleEnrollment, MutableMap<String, RuleVariableValue>>,
    val eventMap: Map<RuleEvent, MutableMap<String, RuleVariableValue>>
)