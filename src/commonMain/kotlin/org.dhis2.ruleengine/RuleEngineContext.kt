package org.dhis2.ruleengine

import org.dhis2.ruleengine.models.Rule
import org.dhis2.ruleengine.models.RuleEnrollment
import org.dhis2.ruleengine.models.RuleEvent
import org.dhis2.ruleengine.models.RuleVariable

class RuleEngineContext(
    val ruleEngineIntent: RuleEngineIntent = RuleEngineIntent.EVALUATION,
    val rules: List<Rule>,
    val ruleVariables: List<RuleVariable>,
    val supplementaryData: Map<String, List<String>>,
    val constantsValues: Map<String, String>,
    val dataItemStore: Map<String, DataItem>
)

fun RuleEngineContext.toRuleEngine(
    ruleEvents: List<RuleEvent>,
    ruleEnrollment: RuleEnrollment?
): RuleEngine {
    return RuleEngine(
        ruleEngineContext = this,
        ruleEvents = ruleEvents,
        ruleEnrollment = ruleEnrollment
    )
}
