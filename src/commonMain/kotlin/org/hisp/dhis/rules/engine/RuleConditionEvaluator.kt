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
import org.hisp.dhis.rules.utils.unwrapVariableName

internal class RuleConditionEvaluator {
    fun getEvaluatedAndErrorRuleEffects(
        targetType: TrackerObjectType,
        targetUid: String,
        valueMap: MutableMap<String, VariableValue>,
        supplementaryMap: Map<String, List<String>>,
        rules: List<Rule>,
        attributeType: AttributeType,
    ): List<RuleEffect> {
        val ruleEvaluationResults = getRuleEvaluationResults(targetType, targetUid, valueMap, supplementaryMap, rules, attributeType)
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

    fun getRuleEvaluationResults(
        targetType: TrackerObjectType,
        targetUid: String,
        valueMap: MutableMap<String, VariableValue>,
        supplementaryMap: Map<String, List<String>>,
        rules: List<Rule>,
        attributeType: AttributeType,
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
                            // Check if action is assigning value to calculated variable
                            if (isAssignToCalculatedValue(action)) {
                                updateValueMap(
                                    unwrapVariableName(action.content()!!),
                                    VariableValue(
                                        ValueType.STRING,
                                        process(action.dataExpression, valueMap, supplementaryMap),
                                        listOf(),
                                        null,
                                    ),
                                    valueMap,
                                )
                            } else {
                                ruleEffects.add(create(rule, action, valueMap, supplementaryMap))
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
        val errorMessage: String
        errorMessage =
            if (ruleAction != null && e is IllegalExpressionException) {
                "Action " + ruleAction::class.simpleName +
                    " from rule " + rule.name + " with id " + rule.uid +
                    " executed for " + targetType.name + "(" + targetUid + ")" +
                    " with condition (" + rule.condition + ")" +
                    " raised an error: " + e.message
            } else if (ruleAction != null) {
                "Action " + ruleAction::class.simpleName +
                    " from rule " + rule.name + " with id " + rule.uid +
                    " executed for " + targetType.name + "(" + targetUid + ")" +
                    " with condition (" + rule.condition + ")" +
                    " raised an unexpected exception: " + e.message
            } else if (e is IllegalExpressionException) {
                "Rule " + rule.name + " with id " + rule.uid +
                    " executed for " + targetType.name + "(" + targetUid + ")" +
                    " with condition (" + rule.condition + ")" +
                    " raised an error: " + e.message
            } else {
                "Rule " + rule.name + " with id " + rule.uid +
                    " executed for " + targetType.name + "(" + targetUid + ")" +
                    " with condition (" + rule.condition + ")" +
                    " raised an unexpected exception: " + e.message
            }
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

    private fun isAssignToCalculatedValue(ruleAction: RuleAction): Boolean = ruleAction.type == "ASSIGN" && ruleAction.field().isNullOrEmpty()

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
        if (ruleAction.type == "ASSIGN") {
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
