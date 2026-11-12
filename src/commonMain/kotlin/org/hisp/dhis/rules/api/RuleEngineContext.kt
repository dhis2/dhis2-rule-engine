package org.hisp.dhis.rules.api

import org.hisp.dhis.rules.models.Rule
import org.hisp.dhis.rules.models.RuleVariable
import org.hisp.dhis.rules.models.TriggerEnvironment

data class RuleEngineContext(
    val rules: List<Rule>,
    val ruleVariables: List<RuleVariable> = emptyList(),
    val ruleSupplementaryData: RuleSupplementaryData = RuleSupplementaryData(),
    val constantsValues: Map<String, String> = emptyMap(),
    val environment: TriggerEnvironment,
)
