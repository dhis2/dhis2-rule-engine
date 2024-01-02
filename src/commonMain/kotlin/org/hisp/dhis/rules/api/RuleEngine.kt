package org.hisp.dhis.rules.api

import org.hisp.dhis.rules.models.*

interface RuleEngine {
    fun validate(expression: String, dataItemStore: Map<String, DataItem>): RuleValidationResult
    fun validateDataFieldExpression(expression: String, dataItemStore: Map<String, DataItem>): RuleValidationResult
    fun evaluate(executionContext: RuleEngineContext): List<RuleEffects>
    fun evaluate(ruleEnrollment: RuleEnrollment, executionContext: RuleEngineContext): List<RuleEffect>
    fun evaluate(ruleEvent: RuleEvent, executionContext: RuleEngineContext): List<RuleEffect>
}