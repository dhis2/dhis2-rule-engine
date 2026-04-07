package org.hisp.dhis.rules.engine

import org.hisp.dhis.rules.api.RuleContextRequirements
import org.hisp.dhis.rules.models.Rule
import org.hisp.dhis.rules.models.RuleVariable
import org.hisp.dhis.rules.models.RuleVariableNewestEvent
import org.hisp.dhis.rules.models.RuleVariableNewestStageEvent
import org.hisp.dhis.rules.models.RuleVariablePreviousEvent

internal object RuleEngineAnalyzer {
    private val MULTI_EVENT_ENV_VARS = setOf("event_count")

    fun analyzeContextRequirements(
        rules: List<Rule>,
        variables: List<RuleVariable>,
    ): RuleContextRequirements {
        val envVars = mutableSetOf<String>()
        val referencedVarNames = mutableSetOf<String>()

        for (rule in rules) {
            rule.conditionExpression.getOrNull()?.let {
                envVars += it.collectProgramVariablesNames()
                referencedVarNames += it.collectProgramRuleVariableNames()
            }
            for (action in rule.actions) {
                action.dataExpression.getOrNull()?.let {
                    envVars += it.collectProgramVariablesNames()
                    referencedVarNames += it.collectProgramRuleVariableNames()
                }
            }
        }

        val byName = variables.associateBy { it.name }
        val needsAllEvents = envVars.any { it in MULTI_EVENT_ENV_VARS } ||
            referencedVarNames.any { name ->
                val v = byName[name]
                v is RuleVariableNewestEvent ||
                    v is RuleVariableNewestStageEvent ||
                    v is RuleVariablePreviousEvent
            }

        return RuleContextRequirements(needsAllEvents)
    }
}
