package org.hisp.dhis.rules.engine

import org.hisp.dhis.lib.expression.Expression
import org.hisp.dhis.lib.expression.spi.ExpressionData
import org.hisp.dhis.lib.expression.spi.IllegalExpressionException
import org.hisp.dhis.lib.expression.spi.ValueType
import org.hisp.dhis.lib.expression.spi.VariableValue
import org.hisp.dhis.rules.api.RuleSupplementaryData
import org.hisp.dhis.rules.createLogger
import org.hisp.dhis.rules.engine.RuleEvaluationResult.Companion.errorRule
import org.hisp.dhis.rules.engine.RuleEvaluationResult.Companion.evaluatedResult
import org.hisp.dhis.rules.engine.RuleEvaluationResult.Companion.notEvaluatedResult
import org.hisp.dhis.rules.models.*

internal class RuleConditionEvaluator(
    ruleVariables: List<RuleVariable> = emptyList(),
) {
    private val ruleVariablesByName: Map<String, RuleVariable> = ruleVariables.associateBy { it.name }

    fun getEvaluatedAndErrorRuleEffects(
        targetType: TrackerObjectType,
        targetUid: String,
        valueMap: MutableMap<String, VariableValue>,
        supplementaryMap: Map<String, List<String>>,
        rules: List<Rule>,
        attributeType: AttributeType,
        hasEnrollmentPass: Boolean = false,
    ): List<RuleEffect> {
        val ruleEvaluationResults =
            getRuleEvaluationResults(targetType, targetUid, valueMap, supplementaryMap, rules, attributeType, hasEnrollmentPass)
        return ruleEvaluationResults
            .flatMap { result -> result.ruleEffects }
    }

    fun getRuleEffects(
        targetType: TrackerObjectType,
        targetUid: String,
        valueMap: MutableMap<String, VariableValue>,
        supplementaryMap: Map<String, List<String>>,
        rules: List<Rule>,
        attributeType: AttributeType,
    ): List<RuleEffect> {
        val ruleEvaluationResults =
            getRuleEvaluationResults(
                targetType,
                targetUid,
                valueMap,
                supplementaryMap,
                rules,
                attributeType,
            )
        return ruleEvaluationResults
            .filter { result -> !result.error }
            .flatMap { result -> result.ruleEffects }
    }

    /**
     * [hasEnrollmentPass] tells this pass whether the surrounding execution also evaluates
     * the enrollment, which owns the effects for assignments to attribute-backed variables;
     * when it does not (single-target evaluation, executions without an enrollment), the
     * event pass emits them instead.
     */
    fun getRuleEvaluationResults(
        targetType: TrackerObjectType,
        targetUid: String,
        valueMap: MutableMap<String, VariableValue>,
        supplementaryMap: Map<String, List<String>>,
        rules: List<Rule>,
        attributeType: AttributeType,
        hasEnrollmentPass: Boolean = false,
    ): List<RuleEvaluationResult> {
        val ruleEvaluationResults: MutableList<RuleEvaluationResult> = ArrayList()
        for (rule in rules) {
            log.fine("Evaluating programrule: " + rule.name)
            try {
                val ruleEffects: MutableList<RuleEffect> = ArrayList()
                if (process(
                        rule.conditionExpression,
                        valueMap,
                        supplementaryMap,
                    ).toBoolean()
                ) {
                    for (action in if (attributeType == AttributeType.DATA_ELEMENT) rule.actionsForDataElement else rule.actionsForEnrollment) {
                        try {
                            // Assignments to program rule variables mutate the value map for
                            // subsequent rules; when the variable is backed by a data element or
                            // attribute, an effect targeting the backing field is emitted as well
                            when (val target = action.assignTarget()) {
                                is AssignTarget.Variable -> {
                                    // Assigning to a name without a program rule variable is
                                    // allowed: the value is visible to subsequent rules through
                                    // the value map, but there is no backing field to emit an
                                    // effect for, which is worth a warning as it may be a typo
                                    val variable = ruleVariablesByName[target.name]
                                    if (variable == null) {
                                        log.warning(
                                            "ASSIGN action in rule " + rule.uid + " assigns to '" + target.name +
                                                "' which is not a defined program rule variable; the value is" +
                                                " visible to subsequent rules but no effect is emitted",
                                        )
                                    }
                                    val data = processRuleAction(rule, action, valueMap, supplementaryMap)
                                    updateValueMap(
                                        target.name,
                                        VariableValue(ValueType.STRING, data, listOf(), null),
                                        valueMap,
                                    )
                                    variable
                                        ?.let { createVariableAssignEffect(rule, action, it, data, attributeType, hasEnrollmentPass) }
                                        ?.let { ruleEffects.add(it) }
                                }
                                is AssignTarget.Invalid -> throw RuleConfigurationException(target.reason)
                                else -> ruleEffects.add(create(rule, action, valueMap, supplementaryMap))
                            }
                        } catch (e: Exception) {
                            addRuleErrorResult(rule, action, e, targetType, targetUid, ruleEvaluationResults)
                        }
                    }
                    ruleEvaluationResults.add(evaluatedResult(rule, ruleEffects))
                } else {
                    ruleEvaluationResults.add(notEvaluatedResult(rule))
                }
            } catch (e: Exception) {
                addRuleErrorResult(rule, null, e, targetType, targetUid, ruleEvaluationResults)
            }
        }
        for ((rule, _, evaluatedAs) in ruleEvaluationResults) {
            log.fine(
                "Rule " + rule.name + " with id " + rule.uid +
                    " executed for " + targetType.name + "(" + targetUid + ")" +
                    " with condition (" + rule.condition + ")" +
                    " was evaluated " + evaluatedAs,
            )
        }
        return ruleEvaluationResults
    }

    private fun addRuleErrorResult(
        rule: Rule,
        ruleAction: RuleAction?,
        e: Exception,
        targetType: TrackerObjectType,
        targetUid: String,
        ruleEvaluationResults: MutableList<RuleEvaluationResult>,
    ) {
        // Expression and rule-configuration errors are reported as errors; anything else as
        // an unexpected exception
        val isConfigurationError = e is IllegalExpressionException || e is RuleConfigurationException
        val subject =
            if (ruleAction != null) {
                "Action " + ruleAction::class.simpleName + " from rule "
            } else {
                "Rule "
            }
        val failure = if (isConfigurationError) "an error" else "an unexpected exception"
        val errorMessage =
            subject + rule.name + " with id " + rule.uid +
                " executed for " + targetType.name + "(" + targetUid + ")" +
                " with condition (" + rule.condition + ")" +
                " raised " + failure + ": " + e.message
        log.severe(errorMessage)
        ruleEvaluationResults.add(errorRule(rule, errorMessage))
    }

    private fun process(
        expressionResult: Result<Expression?>,
        valueMap: Map<String, VariableValue>,
        supplementaryMap: Map<String, List<String>>,
    ): String? {
        val expression = expressionResult.getOrThrow() ?: return ""
        val build =
            ExpressionData(
                valueMap,
                emptyMap(),
                supplementaryMap,
                emptyMap(),
                emptyMap(),
            )
        return convertInteger(
            expression.evaluate({ name: String ->
                throw UnsupportedOperationException(
                    "function not supported: $name",
                )
            }, build),
        )?.toString()
    }

    private fun convertInteger(result: Any?): Any? =
        if (result is Double && result % 1 == 0.0) {
            result.toInt()
        } else {
            result
        }

    /**
     * Builds the effect for an assignment to a program rule variable, so the caller can apply
     * the value to the data element or attribute the variable is backed by. The backing field
     * and its attribute type are injected into a copy of the action, making the effect
     * self-contained for consumers that read `field`; the copy resolves to
     * [AssignTarget.Field] and compares unequal to the configured action.
     *
     * Returns null when this pass does not emit the effect: calculated values are not backed
     * by a field (their `field` is the variable's own uid), data-element backed effects are
     * only emitted during event evaluation, and attribute backed effects are emitted during
     * enrollment evaluation when an enrollment pass evaluates the rule — otherwise
     * (single-event evaluation, executions without an enrollment, rules scoped to a program
     * stage and thus excluded from the enrollment pass) the event pass emits them, so the
     * assignment is never silently dropped.
     */
    private fun createVariableAssignEffect(
        rule: Rule,
        ruleAction: RuleAction,
        variable: RuleVariable,
        data: String?,
        attributeType: AttributeType,
        hasEnrollmentPass: Boolean,
    ): RuleEffect? {
        if (variable is RuleVariableCalculatedValue || variable.field.isEmpty()) return null
        val backingType =
            if (variable is RuleVariableAttribute) {
                AttributeType.TRACKED_ENTITY_ATTRIBUTE
            } else {
                AttributeType.DATA_ELEMENT
            }
        val emitted =
            when (backingType) {
                attributeType -> true
                AttributeType.TRACKED_ENTITY_ATTRIBUTE ->
                    !(hasEnrollmentPass && rule.programStage.isNullOrEmpty())

                else -> false
            }
        if (!emitted) return null
        val effectAction = ruleAction.withAssignBacking(variable.field, backingType.name)
        return RuleEffect(rule.uid, effectAction, if (data.isNullOrEmpty()) null else data)
    }

    private fun updateValueMap(
        variable: String,
        value: VariableValue,
        valueMap: MutableMap<String, VariableValue>,
    ) {
        valueMap[variable] = value
    }

    private fun create(
        rule: Rule,
        ruleAction: RuleAction,
        valueMap: MutableMap<String, VariableValue>,
        supplementaryMap: Map<String, List<String>>,
    ): RuleEffect {
        if (ruleAction.type == RuleAction.ASSIGN) {
            val data = processRuleAction(rule, ruleAction, valueMap, supplementaryMap)
            updateValueMap(ruleAction.field()!!, VariableValue(ValueType.STRING, data, listOf(), null), valueMap)
            return if (data.isNullOrEmpty()) {
                RuleEffect(rule.uid, ruleAction, null)
            } else {
                RuleEffect(rule.uid, ruleAction, data)
            }
        }
        val data =
            if (!ruleAction.data.isNullOrEmpty()) {
                processRuleAction(
                    rule,
                    ruleAction,
                    valueMap,
                    supplementaryMap,
                )
            } else {
                ""
            }
        return RuleEffect(
            rule.uid,
            ruleAction,
            data,
        )
    }

    private fun processRuleAction(
        rule: Rule,
        ruleAction: RuleAction,
        valueMap: MutableMap<String, VariableValue>,
        supplementaryMap: Map<String, List<String>>,
    ): String? {
        val data = process(
            ruleAction.dataExpression,
            valueMap,
            supplementaryMap,
        )
        log.fine(
            "Action " + ruleAction.type +
                " from rule " + rule.name + " with id " + rule.uid +
                " with expression (" + ruleAction.data + ")" +
                " was evaluated " + data
        )
        return data
    }

    companion object {
        private val log = createLogger("org.hisp.dhis.rules.engine.RuleConditionEvaluator")

        fun convertSupplementaryData(ruleSupplementaryData: RuleSupplementaryData): Map<String, List<String>> {
            val supplementaryValues: MutableMap<String, List<String>> = HashMap()
            supplementaryValues["USER_GROUPS"] = ruleSupplementaryData.userGroups
            supplementaryValues["USER_ROLES"] = ruleSupplementaryData.userRoles
            supplementaryValues.putAll(ruleSupplementaryData.orgUnitGroups)
            return supplementaryValues
        }
    }
}
