package org.hisp.dhis.rules.models

data class RuleEvaluationResult(
    val rule: Rule,
    val ruleEffects: List<RuleEffect>,
    val evaluatedAs: Boolean,
    val error: Boolean
) {

    fun rule(): Rule {
        return rule
    }

    fun ruleEffects(): List<RuleEffect> {
        return ruleEffects
    }

    fun evaluatedAs(): Boolean {
        return evaluatedAs
    }

    fun error(): Boolean {
        return error
    }

    companion object {
        fun evaluatedResult(rule: Rule, ruleEffects: List<RuleEffect>): RuleEvaluationResult {
            return RuleEvaluationResult(rule, ruleEffects, true, false)
        }

        fun notEvaluatedResult(rule: Rule): RuleEvaluationResult {
            return RuleEvaluationResult(rule, emptyList(), false, false)
        }

        fun errorRule(rule: Rule, errorMessage: String): RuleEvaluationResult {
            val effects = listOf(RuleEffect(rule.uid, RuleActionError.create(errorMessage), errorMessage))
            return RuleEvaluationResult(rule, effects, false, true)
        }
    }
}
