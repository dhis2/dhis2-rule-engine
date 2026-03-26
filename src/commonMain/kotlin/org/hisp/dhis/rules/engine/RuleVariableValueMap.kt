package org.hisp.dhis.rules.engine

import org.hisp.dhis.lib.expression.spi.VariableValue
import org.hisp.dhis.rules.models.RuleEnrollment
import org.hisp.dhis.rules.models.RuleEvent

internal data class RuleVariableValueMap(
    val enrollmentMap: Map<RuleEnrollment, MutableMap<String, VariableValue>>,
    val eventMap: Map<RuleEvent, MutableMap<String, VariableValue>>,
)
