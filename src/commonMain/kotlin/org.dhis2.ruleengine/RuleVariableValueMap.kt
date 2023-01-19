package org.dhis2.ruleengine

import org.dhis2.ruleengine.models.RuleEnrollment
import org.dhis2.ruleengine.models.RuleEvent

data class RuleVariableValueMap internal constructor(
    val enrollmentMap: Map<RuleEnrollment, Map<String, RuleVariableValue>>,
    val eventMap: Map<RuleEvent, Map<String, RuleVariableValue>>
)