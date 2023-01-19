package org.dhis2.ruleengine.models

import org.dhis2.ruleengine.RuleEffect

data class RuleEvaluationResult(
    val rule: Rule,
    val ruleEffects: List<RuleEffect>,
    val evaluatedAs: Boolean,
    val error: Boolean
)