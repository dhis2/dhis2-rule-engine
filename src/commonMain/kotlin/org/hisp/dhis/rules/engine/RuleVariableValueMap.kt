package org.hisp.dhis.rules.engine

import org.hisp.dhis.rules.models.RuleEnrollment
import org.hisp.dhis.rules.models.RuleEvent

internal data class RuleVariableValueMap(
    val enrollmentMap: Map<RuleEnrollment, Map<String, RuleVariableValue>>,
    val eventMap: Map<RuleEvent, Map<String, RuleVariableValue>>,
)
