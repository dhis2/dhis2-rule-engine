package org.hisp.dhis.rules.engine

import org.hisp.dhis.rules.api.RuleEngineContext
import org.hisp.dhis.rules.models.*

internal class RuleEngineMultipleExecution {
    fun execute(
        executionContext: RuleEngineContext,
        ruleVariableValueMap: RuleVariableValueMap,
    ): List<RuleEffects> {
        val supplementaryMap = RuleConditionEvaluator.convertSupplementaryData(executionContext.ruleSupplementaryData)
        val evaluator = RuleConditionEvaluator()
        val ruleEffects: MutableList<RuleEffects> = ArrayList()
        val enrollmentRules = executionContext.enrollmentRules
        val rulesByStage = executionContext.rulesByStage
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
