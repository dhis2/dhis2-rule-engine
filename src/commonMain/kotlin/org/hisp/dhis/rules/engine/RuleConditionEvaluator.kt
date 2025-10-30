package org.hisp.dhis.rules.engine

import org.hisp.dhis.lib.expression.Expression
import org.hisp.dhis.lib.expression.ExpressionMode
import org.hisp.dhis.lib.expression.spi.ExpressionData
import org.hisp.dhis.lib.expression.spi.IllegalExpressionException
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
        valueMap: Map<String, RuleVariableValue>,
        supplementaryData: Map<String, List<String>>,
        rules: List<Rule>,
    ): List<RuleEffect> {
        val ruleEvaluationResults = getRuleEvaluationResults(targetType, targetUid, valueMap, supplementaryData, rules)
        return ruleEvaluationResults
            .flatMap { result -> result.ruleEffects }
    }

    fun getRuleEffects(
        targetType: TrackerObjectType,
        targetUid: String,
        valueMap: Map<String, RuleVariableValue>,
        supplementaryData: Map<String, List<String>>,
        rules: List<Rule>,
    ): List<RuleEffect> {
        val ruleEvaluationResults =
            getRuleEvaluationResults(
                targetType,
                targetUid,
                valueMap,
                supplementaryData,
                rules,
            )
        return ruleEvaluationResults
            .filter { result -> !result.error }
            .flatMap { result -> result.ruleEffects }
    }

    fun getRuleEvaluationResults(
        targetType: TrackerObjectType,
        targetUid: String,
        valueMap: Map<String, RuleVariableValue>,
        supplementaryData: Map<String, List<String>>,
        rules: List<Rule>,
    ): List<RuleEvaluationResult> {
        val mutableValueMap = valueMap.toMutableMap()
        val ruleEvaluationResults: MutableList<RuleEvaluationResult> = ArrayList()
        for (rule in rules.sorted()) {
            log.fine("Evaluating programrule: " + rule.name)
            try {
                val ruleEffects: MutableList<RuleEffect> = ArrayList()
                if (process(
                        rule.condition,
                        mutableValueMap,
                        supplementaryData,
                        ExpressionMode.RULE_ENGINE_CONDITION,
                    ).toBoolean()
                ) {
                    for (action in rule.actions) {
                        try {
                            // Check if action is assigning value to calculated variable
                            if (isAssignToCalculatedValue(action)) {
                                updateValueMap(
                                    unwrapVariableName(action.content()!!),
                                    RuleVariableValue(
                                        RuleValueType.TEXT,
                                        process(action.data, mutableValueMap, supplementaryData, ExpressionMode.RULE_ENGINE_ACTION),
                                        listOf(),
                                        null,
                                    ),
                                    mutableValueMap,
                                )
                            } else {
                                ruleEffects.add(create(rule, action, mutableValueMap, supplementaryData))
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
        condition: String?,
        valueMap: Map<String, RuleVariableValue>,
        supplementaryData: Map<String, List<String>>,
        mode: ExpressionMode,
    ): String? {
        if (condition.isNullOrEmpty()) {
            return ""
        }
        val expression = Expression(condition, mode, false)

        val build =
            ExpressionData(
                valueMap.mapValues { v -> v.value.toVariableValue() },
                emptyMap(),
                supplementaryData,
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
        value: RuleVariableValue,
        valueMap: MutableMap<String, RuleVariableValue>,
    ) {
        valueMap[variable] = value
    }

    private fun create(
        rule: Rule,
        ruleAction: RuleAction,
        valueMap: MutableMap<String, RuleVariableValue>,
        supplementaryData: Map<String, List<String>>,
    ): RuleEffect {
        if (ruleAction.type == "ASSIGN") {
            val data = processRuleAction(rule, ruleAction, valueMap, supplementaryData)
            updateValueMap(ruleAction.field()!!, RuleVariableValue(RuleValueType.TEXT, data, listOf(), null), valueMap)
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
                    supplementaryData,
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
        valueMap: MutableMap<String, RuleVariableValue>,
        supplementaryData: Map<String, List<String>>,
    ): String? {
        val data = process(
            ruleAction.data,
            valueMap,
            supplementaryData,
            ExpressionMode.RULE_ENGINE_ACTION,
        )
        log.fine(
            "Action " + ruleAction::class.simpleName +
                " from rule " + rule.name + " with id " + rule.uid +
                " with condition (" + ruleAction.data + ")" +
                " was evaluated " + data
        )
        return data
    }

    companion object {
        private val log = createLogger("org.hisp.dhis.rules.engine.RuleConditionEvaluator")
    }
}
