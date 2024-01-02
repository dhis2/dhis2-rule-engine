package org.hisp.dhis.rules.api

import org.hisp.dhis.rules.models.Rule
import org.hisp.dhis.rules.models.RuleEnrollment
import org.hisp.dhis.rules.models.RuleEvent
import org.hisp.dhis.rules.models.RuleVariable

data class RuleEngineContext(
    val rules: List<Rule>,
    val ruleVariables: List<RuleVariable> = emptyList(),
    val events: List<RuleEvent> = emptyList(),
    val enrollment: RuleEnrollment? = null,
    val supplementaryData: Map<String, List<String>> = emptyMap(),
    val constantsValues: Map<String, String> = emptyMap()
)
