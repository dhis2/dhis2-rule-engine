package org.hisp.dhis.rules.models

data class RuleEvaluationResult(
    val rule: Rule,
    val ruleEffects: List<RuleEffect>,
    val evaluatedAs: Boolean,
    val error: Boolean
) {
    companion object {
        fun evaluatedResult(rule: Rule, ruleEffects: List<RuleEffect>): RuleEvaluationResult {
            return RuleEvaluationResult(rule, ruleEffects, true, false)
        }

        fun notEvaluatedResult(rule: Rule): RuleEvaluationResult {
            return RuleEvaluationResult(rule, emptyList(), false, false)
        }

        fun errorRule(rule: Rule, errorMessage: String): RuleEvaluationResult {
            val effects = listOf(
                RuleEffect(
                    rule.uid,
                    RuleActionError(errorMessage),
                    errorMessage
                )
            )
            return RuleEvaluationResult(rule, effects, false, true)
        }
    }
}
