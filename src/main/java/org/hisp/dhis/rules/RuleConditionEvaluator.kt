package org.hisp.dhis.rules

import org.hisp.dhis.lib.expression.Expression
import org.hisp.dhis.lib.expression.spi.ExpressionData
import org.hisp.dhis.lib.expression.spi.IllegalExpressionException
import org.hisp.dhis.rules.models.*
import org.hisp.dhis.rules.models.RuleEvaluationResult.Companion.errorRule
import org.hisp.dhis.rules.models.RuleEvaluationResult.Companion.evaluatedResult
import org.hisp.dhis.rules.models.RuleEvaluationResult.Companion.notEvaluatedResult
import java.util.logging.Level
import java.util.logging.Logger

class RuleConditionEvaluator {
    fun getEvaluatedAndErrorRuleEffects(
        targetType: TrackerObjectType,
        targetUid: String,
        valueMap: MutableMap<String, RuleVariableValue>,
        supplementaryData: Map<String, List<String>>,
        rules: List<Rule>
    ): List<RuleEffect> {
        val ruleEffects: MutableList<RuleEffect> = ArrayList()
        for ((_, ruleEffects1) in getRuleEvaluationResults(targetType, targetUid, valueMap, supplementaryData, rules)) {
            ruleEffects.addAll(ruleEffects1)
        }
        return ruleEffects
    }

    fun getRuleEffects(
        targetType: TrackerObjectType,
        targetUid: String,
        valueMap: MutableMap<String, RuleVariableValue>,
        supplementaryData: Map<String, List<String>>,
        rules: List<Rule>
    ): List<RuleEffect> {
        val ruleEffects: MutableList<RuleEffect> = ArrayList()
        for ((_, ruleEffects1, _, error) in getRuleEvaluationResults(
            targetType,
            targetUid,
            valueMap,
            supplementaryData,
            rules
        )) {
            if (!error) {
                ruleEffects.addAll(ruleEffects1)
            }
        }
        return ruleEffects
    }

    fun getRuleEvaluationResults(
        targetType: TrackerObjectType, targetUid: String,
        valueMap: MutableMap<String, RuleVariableValue>,
        supplementaryData: Map<String, List<String>>,
        rules: List<Rule>
    ): List<RuleEvaluationResult> {
        val ruleEvaluationResults: MutableList<RuleEvaluationResult> = ArrayList()
        for (rule in rules.sorted()) {
            log.fine("Evaluating programrule: " + rule.name())
            try {
                val ruleEffects: MutableList<RuleEffect> = ArrayList()
                if (process(
                        rule.condition(),
                        valueMap,
                        supplementaryData,
                        Expression.Mode.RULE_ENGINE_CONDITION
                    ).toBoolean()
                ) {
                    for (action in rule.actions()) {
                        try {
                            //Check if action is assigning value to calculated variable
                            if (isAssignToCalculatedValue(action)) {
                                val (_, content, _, data) = action as RuleActionAssign
                                updateValueMap(
                                    unwrapVariableName(content),
                                    RuleVariableValue(
                                        RuleValueType.TEXT,
                                        process(data, valueMap, supplementaryData, Expression.Mode.RULE_ENGINE_ACTION),
                                        listOf(),
                                        null
                                    ),
                                    valueMap
                                )
                            } else {
                                ruleEffects.add(create(rule, action, valueMap, supplementaryData))
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
                "Rule " + rule.name() + " with id " + rule.uid() +
                        " executed for " + targetType.name + "(" + targetUid + ")" +
                        " with condition (" + rule.condition() + ")" +
                        " was evaluated " + evaluatedAs
            )
        }
        return ruleEvaluationResults
    }

    private fun addRuleErrorResult(
        rule: Rule, ruleAction: RuleAction?, e: Exception, targetType: TrackerObjectType,
        targetUid: String, ruleEvaluationResults: MutableList<RuleEvaluationResult>
    ) {
        val errorMessage: String
        errorMessage = if (ruleAction != null && e is IllegalExpressionException) {
            "Action " + ruleAction.javaClass.name +
                    " from rule " + rule.name() + " with id " + rule.uid() +
                    " executed for " + targetType.name + "(" + targetUid + ")" +
                    " with condition (" + rule.condition() + ")" +
                    " raised an error: " + e.message
        } else if (ruleAction != null) {
            "Action " + ruleAction.javaClass.name +
                    " from rule " + rule.name() + " with id " + rule.uid() +
                    " executed for " + targetType.name + "(" + targetUid + ")" +
                    " with condition (" + rule.condition() + ")" +
                    " raised an unexpected exception: " + e.message
        } else if (e is IllegalExpressionException) {
            "Rule " + rule.name() + " with id " + rule.uid() +
                    " executed for " + targetType.name + "(" + targetUid + ")" +
                    " with condition (" + rule.condition() + ")" +
                    " raised an error: " + e.message
        } else {
            "Rule " + rule.name() + " with id " + rule.uid() +
                    " executed for " + targetType.name + "(" + targetUid + ")" +
                    " with condition (" + rule.condition() + ")" +
                    " raised an unexpected exception: " + e.message
        }
        log.log(Level.SEVERE, errorMessage)
        ruleEvaluationResults.add(errorRule(rule, errorMessage))
    }

    private fun process(
        condition: String?, valueMap: Map<String, RuleVariableValue>,
        supplementaryData: Map<String, List<String>>, mode: Expression.Mode
    ): String {
        if (condition==null) {
            return ""
        }
        if (condition.isEmpty()) {
            return ""
        }
        val expression = Expression(condition, mode, false)

        val build = ExpressionData(
            valueMap.mapValues { v -> v.value.toVariableValue() },
            java.util.Map.of(),
            supplementaryData,
            java.util.Map.of(),
            java.util.Map.of()
        )
        return convertInteger(expression.evaluate({ name: String ->
            throw UnsupportedOperationException(
                "function not supported: $name"
            )
        }, build)).toString()
    }

    private fun convertInteger(result: Any?): Any? {
        return if (result is Double && result % 1 == 0.0) {
            result.toInt()
        } else result
    }

    private fun isAssignToCalculatedValue(ruleAction: RuleAction): Boolean {
        return ruleAction is RuleActionAssign && ruleAction.field.isEmpty()
    }

    private fun updateValueMap(
        variable: String,
        value: RuleVariableValue,
        valueMap: MutableMap<String, RuleVariableValue>
    ) {
        valueMap[variable] = value
    }

    private fun create(
        rule: Rule,
        ruleAction: RuleAction,
        valueMap: MutableMap<String, RuleVariableValue>,
        supplementaryData: Map<String, List<String>>
    ): RuleEffect {
        if (ruleAction is RuleActionAssign) {
            val data = process(ruleAction.data(), valueMap, supplementaryData, Expression.Mode.RULE_ENGINE_ACTION)
            updateValueMap(ruleAction.field, RuleVariableValue(RuleValueType.TEXT, data, listOf(), null), valueMap)
            return if (data == null || data.isEmpty()) {
                RuleEffect(rule.uid(), ruleAction, null)
            } else {
                RuleEffect(rule.uid(), ruleAction, data)
            }
        }
        return RuleEffect(
            rule.uid(),
            ruleAction,
            process(ruleAction.data(), valueMap, supplementaryData, Expression.Mode.RULE_ENGINE_ACTION)
        )
    }

    companion object {
        private val log = Logger.getLogger(RuleConditionEvaluator::class.java.name)
    }
}
