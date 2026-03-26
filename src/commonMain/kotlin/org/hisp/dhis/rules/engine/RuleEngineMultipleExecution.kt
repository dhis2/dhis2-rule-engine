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
        val enrollmentRules: List<Rule> = filterRules(rules).sorted()
        val rulesByStage: Map<String, List<Rule>> = rules
            .filter { !it.programStage.isNullOrEmpty() }
            .groupBy { it.programStage!! }
            .mapValues { (_, stageRules) -> (enrollmentRules + stageRules).sorted() }
        for ((enrollment, valueMap) in ruleVariableValueMap.enrollmentMap) {
            val enrollmentRuleEffects =
                evaluator.getEvaluatedAndErrorRuleEffects(
                    TrackerObjectType.ENROLLMENT,
                    enrollment.enrollment,
                    valueMap,
                    supplementaryMap,
                    enrollmentRules,
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
            val eventRules = rulesByStage[event.programStage] ?: enrollmentRules
            ruleEffects.add(
                RuleEffects(
                    TrackerObjectType.EVENT,
                    event.event,
                    evaluator.getEvaluatedAndErrorRuleEffects(
                        TrackerObjectType.EVENT,
                        event.event,
                        valueMap,
                        supplementaryMap,
                        eventRules,
                        AttributeType.DATA_ELEMENT,
                    ),
                ),
            )
        }
        return ruleEffects
    }
}
