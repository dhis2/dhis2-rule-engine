package org.hisp.dhis.rules.engine

import org.hisp.dhis.rules.models.Rule
import org.hisp.dhis.rules.models.*

internal class RuleEngineMultipleExecution {
    fun execute(rules: List<Rule>, ruleVariableValueMap: RuleVariableValueMap,
                supplementaryData: Map<String, List<String>>): List<RuleEffects> {
        val ruleEffects: MutableList<RuleEffects> = ArrayList()
        for ((enrollment, value) in ruleVariableValueMap.enrollmentMap) {
            val enrollmentRuleEffects = RuleConditionEvaluator()
                .getEvaluatedAndErrorRuleEffects(
                    TrackerObjectType.ENROLLMENT, enrollment.enrollment, value,
                    supplementaryData, filterRules(rules)
                )
            ruleEffects.add(
                RuleEffects(
                    TrackerObjectType.ENROLLMENT, enrollment.enrollment,
                    enrollmentRuleEffects
                )
            )
        }
        for ((event, value) in ruleVariableValueMap.eventMap) {
            ruleEffects.add(
                RuleEffects(
                    TrackerObjectType.EVENT, event.event,
                    RuleConditionEvaluator().getEvaluatedAndErrorRuleEffects(
                        TrackerObjectType.EVENT, event.event, value,
                        supplementaryData, filterRules(rules, event)
                    )
                )
            )
        }
        return ruleEffects
    }

    private fun filterRules(rules: List<Rule>): List<Rule> {
        val filteredRules: MutableList<Rule> = mutableListOf()
        for (rule in rules) {
            val programStage: String? = rule.programStage
            if (programStage.isNullOrEmpty()) {
                val ruleActions = filterActionRules(
                    rule.actions,
                    AttributeType.TRACKED_ENTITY_ATTRIBUTE
                )
                filteredRules.add(rule.copy(actions = ruleActions))
            }
        }
        return filteredRules
    }

    private fun filterRules(rules: List<Rule>, ruleEvent: RuleEvent): List<Rule> {
        val filteredRules: MutableList<Rule> = mutableListOf()
        for (rule in rules) {
            val programStage: String? = rule.programStage
            if (programStage.isNullOrEmpty() || programStage == ruleEvent.programStage) {
                val ruleActions = filterActionRules(
                    rule.actions,
                    AttributeType.DATA_ELEMENT
                )
                filteredRules.add(rule.copy(actions = ruleActions))
            }
        }
        return filteredRules
    }

    private fun filterActionRules(ruleActions: List<RuleAction>, attributeType: AttributeType): List<RuleAction> {
        val filteredRuleActions: MutableList<RuleAction> = mutableListOf()
        for (ruleAction in ruleActions) {
            if (ruleAction.attributeType() == null || ruleAction.attributeType() == attributeType.name || ruleAction.attributeType() == AttributeType.UNKNOWN.name) {
                filteredRuleActions.add(ruleAction)
            }
        }
        return filteredRuleActions
    }
}
