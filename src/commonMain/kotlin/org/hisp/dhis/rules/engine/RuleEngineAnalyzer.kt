package org.hisp.dhis.rules.engine

import org.hisp.dhis.rules.api.RuleContextRequirements
import org.hisp.dhis.rules.models.Rule
import org.hisp.dhis.rules.models.RuleVariable
import org.hisp.dhis.rules.models.RuleVariableAttribute
import org.hisp.dhis.rules.models.RuleVariableCurrentEvent
import org.hisp.dhis.rules.models.RuleVariableNewestEvent
import org.hisp.dhis.rules.models.RuleVariableNewestStageEvent
import org.hisp.dhis.rules.models.RuleVariablePreviousEvent
import org.hisp.dhis.rules.utils.RuleEngineUtils.ENV_VAR_ENROLLMENT_COUNT
import org.hisp.dhis.rules.utils.RuleEngineUtils.ENV_VAR_ENROLLMENT_DATE
import org.hisp.dhis.rules.utils.RuleEngineUtils.ENV_VAR_ENROLLMENT_ID
import org.hisp.dhis.rules.utils.RuleEngineUtils.ENV_VAR_ENROLLMENT_STATUS
import org.hisp.dhis.rules.utils.RuleEngineUtils.ENV_VAR_EVENT_COUNT
import org.hisp.dhis.rules.utils.RuleEngineUtils.ENV_VAR_INCIDENT_DATE
import org.hisp.dhis.rules.utils.RuleEngineUtils.ENV_VAR_TEI_COUNT

internal object RuleEngineAnalyzer {
    private val MULTI_EVENT_ENV_VARS = setOf(ENV_VAR_EVENT_COUNT)
    private val ENROLLMENT_ENV_VARS = setOf(
        ENV_VAR_ENROLLMENT_COUNT,
        ENV_VAR_ENROLLMENT_DATE,
        ENV_VAR_ENROLLMENT_ID,
        ENV_VAR_ENROLLMENT_STATUS,
        ENV_VAR_INCIDENT_DATE,
        ENV_VAR_TEI_COUNT,
    )

    fun analyzeContextRequirements(
        rules: List<Rule>,
        variables: List<RuleVariable>,
    ): RuleContextRequirements {
        val envVars = mutableSetOf<String>()
        val referencedVarNames = mutableSetOf<String>()
        val orgUnitGroups = mutableSetOf<String>()

        for (rule in rules) {
            rule.conditionExpression.getOrNull()?.let {
                envVars += it.collectProgramVariablesNames()
                referencedVarNames += it.collectProgramRuleVariableNames()
                orgUnitGroups += it.collectInOrgUnitGroups()
            }
            for (action in rule.actions) {
                action.dataExpression.getOrNull()?.let {
                    envVars += it.collectProgramVariablesNames()
                    referencedVarNames += it.collectProgramRuleVariableNames()
                    orgUnitGroups += it.collectInOrgUnitGroups()
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

        val needsDataValues = referencedVarNames.any { name ->
            val v = byName[name]
            v is RuleVariableNewestEvent ||
                v is RuleVariableNewestStageEvent ||
                v is RuleVariablePreviousEvent ||
                v is RuleVariableCurrentEvent
        }

        val needsAttributes = referencedVarNames.any { name ->
            byName[name] is RuleVariableAttribute
        }

        val needsEnrollment = envVars.any { it in ENROLLMENT_ENV_VARS } || needsAttributes

        return RuleContextRequirements(needsAllEvents, needsEnrollment, needsDataValues, needsAttributes, orgUnitGroups)
    }
}
