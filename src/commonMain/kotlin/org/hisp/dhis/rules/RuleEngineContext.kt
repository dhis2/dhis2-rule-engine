package org.hisp.dhis.rules

import org.hisp.dhis.rules.models.Rule
import org.hisp.dhis.rules.models.RuleVariable

data class RuleEngineContext(
    val rules: List<Rule>,
    val ruleVariables: List<RuleVariable>,
    val supplementaryData: Map<String, List<String>> = emptyMap(),
    val constantsValues: Map<String, String> = emptyMap(),
    val ruleEngineIntent: RuleEngineIntent = RuleEngineIntent.EVALUATION
)
