package org.hisp.dhis.rules.api

import org.hisp.dhis.rules.engine.DefaultRuleEngine
import org.hisp.dhis.rules.models.*

interface RuleEngine {
    fun validate(expression: String, dataItemStore: Map<String, DataItem>): RuleValidationResult
    fun validateDataFieldExpression(expression: String, dataItemStore: Map<String, DataItem>): RuleValidationResult
    fun evaluateAll(enrollmentTarget: RuleEnrollment?, eventsTarget: List<RuleEvent>, executionContext: RuleEngineContext): List<RuleEffects>
    fun evaluate(target: RuleEnrollment, ruleEvents: List<RuleEvent>, executionContext: RuleEngineContext): List<RuleEffect>
    fun evaluate(target: RuleEvent, ruleEnrollment: RuleEnrollment?, ruleEvents: List<RuleEvent>, executionContext: RuleEngineContext): List<RuleEffect>

    companion object {
        fun getInstance(): RuleEngine {
            return DefaultRuleEngine()
        }
    }
}