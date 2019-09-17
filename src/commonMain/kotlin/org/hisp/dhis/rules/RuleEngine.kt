package org.hisp.dhis.rules

import org.hisp.dhis.rules.models.*
import org.hisp.dhis.rules.utils.Callable

// ToDo: logging
expect class RuleEngine (executionContext: RuleEngineContext,
                         events: List<RuleEvent>,
                         ruleEnrollment: RuleEnrollment?,
                         triggerEnvironment: TriggerEnvironment?) {

    fun evaluate(ruleEvent: RuleEvent?): Callable<List<RuleEffect>>


    fun evaluate(ruleEvent: RuleEvent?, rulesToEvaluate: List<Rule>): Callable<List<RuleEffect>>

    fun evaluate(ruleEnrollment: RuleEnrollment?, rulesToEvaluate: List<Rule>): Callable<List<RuleEffect>>

    fun evaluate(ruleEnrollment: RuleEnrollment?): Callable<List<RuleEffect>>


    class Builder (executionContext: RuleEngineContext) {

        fun events(ruleEvents: List<RuleEvent>?): Builder

        fun enrollment(ruleEnrollment: RuleEnrollment?) : Builder

        fun triggerEnvironment(triggerEnvironment: TriggerEnvironment?) : Builder

        fun build() : RuleEngine
    }
}
