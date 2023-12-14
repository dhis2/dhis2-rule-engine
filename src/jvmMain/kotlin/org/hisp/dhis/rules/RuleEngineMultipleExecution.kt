package org.hisp.dhis.rules

import org.hisp.dhis.rules.models.Rule
import org.hisp.dhis.rules.models.RuleEffects
import org.hisp.dhis.rules.models.TrackerObjectType

internal data class RuleEngineMultipleExecution(
    val rules: List<Rule>,
    val ruleVariableValueMap: RuleVariableValueMap,
    val supplementaryData: Map<String, List<String>>
) {
    fun execute(): List<RuleEffects> {
        val ruleEffects: MutableList<RuleEffects> = ArrayList()
        for ((enrollment, value) in ruleVariableValueMap
            .enrollmentMap()) {
            val enrollmentRuleEffects = RuleConditionEvaluator()
                .getEvaluatedAndErrorRuleEffects(
                    TrackerObjectType.ENROLLMENT, enrollment.enrollment, value,
                    supplementaryData, filterRules(rules, enrollment)
                )
            ruleEffects.add(
                RuleEffects(
                    TrackerObjectType.ENROLLMENT, enrollment.enrollment,
                    enrollmentRuleEffects
                )
            )
        }
        for ((event, value) in ruleVariableValueMap
            .eventMap()) {
            ruleEffects.add(
                RuleEffects(
                    TrackerObjectType.EVENT, event.event(),
                    RuleConditionEvaluator().getEvaluatedAndErrorRuleEffects(
                        TrackerObjectType.EVENT, event.event(), value,
                        supplementaryData, filterRules(rules, event)
                    )
                )
            )
        }
        return ruleEffects
    }
}
