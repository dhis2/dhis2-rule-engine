package org.dhis2.ruleengine

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.dhis2.ruleengine.models.*
import org.dhis2.ruleengine.utils.VariableNameUnwrapper

class RuleConditionEvaluator {

    val expressionParserEvaluator = expressionEvaluator()

    fun getRuleEffects(
        trackerObjectType: TrackerObjectType,
        targetUid: String,
        valueMap: MutableMap<String, RuleVariableValue>,
        supplementaryData: Map<String, List<String>>,
        rules: List<Rule>,
        includeError: Boolean = true
    ): List<RuleEffect> {
        return getRuleEvaluationResults(
            trackerObjectType,
            targetUid,
            valueMap,
            supplementaryData,
            rules
        )
            .filter { ruleEvaluationResult ->
                includeError || !ruleEvaluationResult.error
            }
            .map { ruleEvaluationResult ->
                ruleEvaluationResult.ruleEffects
            }.flatten()
    }

    private fun getRuleEvaluationResults(
        trackerObjectType: TrackerObjectType,
        targetUid: String,
        valueMap: MutableMap<String, RuleVariableValue>,
        supplementaryData: Map<String, List<String>>,
        rules: List<Rule>
    ): List<RuleEvaluationResult> {
        return rules.sortedBy {
            it.priority
        }.map { rule ->
            try {
                val ruleEffects = mutableListOf<RuleEffect>()
                if (process(rule.condition, valueMap, supplementaryData).toBoolean()) {
                    rule.actions.forEach { action ->
                        try {
                            if (action.isAssignToCalculatedValue()) {
                                updateValueMap(
                                    VariableNameUnwrapper.unwrap((action as RuleAction.Assign).content, null),
                                    RuleVariableValue(
                                        variableValue = process(
                                            condition = action.data,
                                            valueMap = valueMap,
                                            supplementaryData = supplementaryData
                                        ),
                                        ruleValueType = RuleValueType.TEXT,
                                        candidates = emptyList(),
                                        eventDate = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
                                    ),
                                    valueMap = valueMap
                                )
                            } else {
                                createRuleEffect(
                                    rule = rule,
                                    ruleAction = action,
                                    valueMap = valueMap,
                                    supplementaryData = supplementaryData
                                ).also {
                                    ruleEffects.add(it)
                                }
                            }
                        } catch (exception: Exception) {
                            //TODO: HANDLE EXCEPTION
                        }
                    }
                    RuleEvaluationResult(
                        rule = rule,
                        ruleEffects = ruleEffects,
                        evaluatedAs = true,
                        error = false
                    )
                } else {
                    RuleEvaluationResult(
                        rule = rule,
                        ruleEffects = emptyList(),
                        evaluatedAs = false,
                        error = false
                    )
                }
            } catch (e: Exception) {
                RuleEvaluationResult(
                    rule = rule,
                    ruleEffects = emptyList(), //TODO: HANDLE EXCEPTION
                    evaluatedAs = false,
                    error = true
                )
            }

        }
    }

    private fun process(
        condition: String,
        valueMap: Map<String, RuleVariableValue>,
        supplementaryData: Map<String, List<String>>
    ): String {
        return expressionParserEvaluator.evaluate(
            condition,
            valueMap,
            supplementaryData
        )
    }

    private fun updateValueMap(
        variable: String,
        value: RuleVariableValue,
        valueMap: MutableMap<String, RuleVariableValue>
    ) {
        valueMap[variable] = value
    }

    private fun createRuleEffect(
        rule: Rule,
        ruleAction: RuleAction,
        valueMap: MutableMap<String, RuleVariableValue>,
        supplementaryData: Map<String, List<String>>
    ): RuleEffect {
        return when (ruleAction) {
            is RuleAction.Assign -> {
                val data = process(
                    condition = ruleAction.data,
                    valueMap = valueMap,
                    supplementaryData = supplementaryData
                )
                updateValueMap(
                    variable = ruleAction.field!!,
                    value = RuleVariableValue(
                        variableValue = data,
                        ruleValueType = RuleValueType.TEXT,
                        candidates = emptyList(),
                        eventDate = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
                    ),
                    valueMap = valueMap
                )
                val dataInRuleEffect = if (data.isEmpty() && ruleAction.data.isEmpty()) {
                    ruleAction.data
                } else {
                    data
                }
                RuleEffect(
                    ruleId = rule.uid,
                    ruleAction = ruleAction,
                    data = dataInRuleEffect
                )
            }

            else ->
                RuleEffect(
                    ruleId = rule.uid,
                    ruleAction = ruleAction,
                    data = process(condition = ruleAction.data, valueMap = valueMap, supplementaryData = supplementaryData)
                )
        }
    }
}