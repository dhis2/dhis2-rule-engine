package org.hisp.dhis.rules.engine

import org.hisp.dhis.rules.api.RuleSupplementaryData
import org.hisp.dhis.rules.models.*
import org.hisp.dhis.rules.utils.filterRules

internal class RuleEngineMultipleExecution {
    fun execute(
        rules: List<Rule>,
        ruleVariableValueMap: RuleVariableValueMap,
        ruleSupplementaryData: RuleSupplementaryData,
    ): List<RuleEffects> {
        val supplementaryMap = RuleConditionEvaluator.convertSupplementaryData(ruleSupplementaryData)
        val evaluator = RuleConditionEvaluator()
        val ruleEffects: MutableList<RuleEffects> = ArrayList()
        for ((enrollment, valueMap) in ruleVariableValueMap.enrollmentMap) {
            val enrollmentRuleEffects =
                evaluator.getEvaluatedAndErrorRuleEffects(
                    TrackerObjectType.ENROLLMENT,
                    enrollment.enrollment,
                    valueMap,
                    supplementaryMap,
                    filterRules(rules),
                    AttributeType.TRACKED_ENTITY_ATTRIBUTE,
                )
            ruleEffects.add(
                RuleEffects(
                    TrackerObjectType.ENROLLMENT,
                    enrollment.enrollment,
                    enrollmentRuleEffects,
                ),
            )
        }
        for ((event, valueMap) in ruleVariableValueMap.eventMap) {
            ruleEffects.add(
                RuleEffects(
                    TrackerObjectType.EVENT,
                    event.event,
                    evaluator.getEvaluatedAndErrorRuleEffects(
                        TrackerObjectType.EVENT,
                        event.event,
                        valueMap,
                        supplementaryMap,
                        filterRules(rules, event),
                        AttributeType.DATA_ELEMENT,
                    ),
                ),
            )
        }
        return ruleEffects
    }
}
