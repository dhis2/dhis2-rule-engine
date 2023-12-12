package org.hisp.dhis.rules

import org.hisp.dhis.rules.models.*
import java.util.concurrent.Callable


internal data class RuleEngineExecution(
    val event: RuleEvent?,
    val enrollment: RuleEnrollment?,
    val rules: List<Rule>,
    val valueMap: MutableMap<String, RuleVariableValue>,
    val supplementaryData: Map<String, List<String>>
) : Callable<List<RuleEffect>> {
    override fun call(): List<RuleEffect> {
        if (event != null) {
            return RuleConditionEvaluator().getRuleEffects(
                TrackerObjectType.EVENT, event.event(), valueMap,
                supplementaryData, rules
            )
        }
        if (enrollment != null) {
            return RuleConditionEvaluator().getRuleEffects(
                TrackerObjectType.ENROLLMENT, enrollment.enrollment, valueMap,
                supplementaryData, rules
            )
        }
        throw IllegalStateException("event and enrollment were null")
    }
}
