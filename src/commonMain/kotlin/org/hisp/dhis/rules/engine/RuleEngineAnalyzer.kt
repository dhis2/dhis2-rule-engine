package org.hisp.dhis.rules.engine

import org.hisp.dhis.lib.expression.Expression
import org.hisp.dhis.rules.api.RuleContextRequirements
import org.hisp.dhis.rules.models.Rule
import org.hisp.dhis.rules.models.RuleVariable
import org.hisp.dhis.rules.models.RuleVariableAttribute
import org.hisp.dhis.rules.models.RuleVariableCalculatedValue
import org.hisp.dhis.rules.models.RuleVariableCurrentEvent
import org.hisp.dhis.rules.models.RuleVariableNewestEvent
import org.hisp.dhis.rules.models.RuleVariableNewestStageEvent
import org.hisp.dhis.rules.models.RuleVariablePreviousEvent
import org.hisp.dhis.rules.utils.RuleEngineUtils.ENV_VAR_ENROLLMENT_COUNT
import org.hisp.dhis.rules.utils.RuleEngineUtils.ENV_VAR_ENROLLMENT_DATE
import org.hisp.dhis.rules.utils.RuleEngineUtils.ENV_VAR_ENROLLMENT_ID
import org.hisp.dhis.rules.utils.RuleEngineUtils.ENV_VAR_ENROLLMENT_STATUS
import org.hisp.dhis.rules.utils.RuleEngineUtils.ENV_VAR_EVENT_COUNT
import org.hisp.dhis.rules.utils.RuleEngineUtils.ENV_VAR_TEI_COUNT

internal object RuleEngineAnalyzer {
    private val MULTI_EVENT_ENV_VARS = setOf(ENV_VAR_EVENT_COUNT)
    private val ENROLLMENT_ENV_VARS = setOf(
        ENV_VAR_ENROLLMENT_COUNT,
        ENV_VAR_ENROLLMENT_DATE,
        ENV_VAR_ENROLLMENT_ID,
        ENV_VAR_ENROLLMENT_STATUS,
        ENV_VAR_TEI_COUNT,
    )

    fun analyzeContextRequirements(
        rules: List<Rule>,
        variables: List<RuleVariable>,
    ): RuleContextRequirements {
        val byName = variables.associateBy { it.name }
        val hasNonCalculatedVariables = variables.any { it !is RuleVariableCalculatedValue }
        val acc = RequirementsAccumulator(byName)

        for (rule in rules) {
            if (acc.isComplete) break
            for (expr in rule.expressions()) {
                acc.processEnvVars(expr)
                if (hasNonCalculatedVariables) acc.processVarNames(expr)
                acc.processOrgUnitGroups(expr)
            }
        }

        return acc.toRequirements()
    }

    private fun Rule.expressions(): List<Expression> = buildList {
        conditionExpression.getOrNull()?.let { add(it) }
        actions.forEach { action -> action.dataExpression.getOrNull()?.let { add(it) } }
    }

    private class RequirementsAccumulator(private val byName: Map<String, RuleVariable>) {
        var needsAllEvents = false
        var needsEnrollment = false
        var needsDataValues = false
        var needsAttributes = false
        var needsOrgUnitGroups = false

        val isComplete get() = needsAllEvents && needsEnrollment && needsDataValues && needsAttributes && needsOrgUnitGroups

        fun processEnvVars(expr: Expression) {
            if (needsAllEvents && needsEnrollment) return
            for (envVar in expr.collectProgramVariablesNames()) {
                needsAllEvents = needsAllEvents || envVar in MULTI_EVENT_ENV_VARS
                needsEnrollment = needsEnrollment || envVar in ENROLLMENT_ENV_VARS
                if (needsAllEvents && needsEnrollment) break
            }
        }

        fun processVarNames(expr: Expression) {
            if (needsAllEvents && needsDataValues && needsAttributes) return
            for (name in expr.collectProgramRuleVariableNames()) {
                val v = byName[name]
                val isMultiEvent = v is RuleVariableNewestEvent || v is RuleVariableNewestStageEvent || v is RuleVariablePreviousEvent
                needsAllEvents = needsAllEvents || isMultiEvent
                needsDataValues = needsDataValues || isMultiEvent || v is RuleVariableCurrentEvent
                needsAttributes = needsAttributes || v is RuleVariableAttribute
                needsEnrollment = needsEnrollment || needsAttributes
                if (needsAllEvents && needsDataValues && needsAttributes) break
            }
        }

        fun processOrgUnitGroups(expr: Expression) {
            if (!needsOrgUnitGroups) needsOrgUnitGroups = expr.collectInOrgUnitGroups().isNotEmpty()
        }

        fun toRequirements() = RuleContextRequirements(
            needsAllEvents, needsEnrollment, needsDataValues, needsAttributes, needsOrgUnitGroups,
        )
    }
}
