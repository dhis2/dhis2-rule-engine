package org.hisp.dhis.rules.engine

import org.hisp.dhis.rules.models.Rule
import org.hisp.dhis.rules.models.RuleAction
import org.hisp.dhis.rules.models.RuleEffect

internal data class RuleEvaluationResult(
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
                    RuleAction(errorMessage, "ERROR"),
                    errorMessage
                )
            )
            return RuleEvaluationResult(rule, effects, false, true)
        }
    }
}
