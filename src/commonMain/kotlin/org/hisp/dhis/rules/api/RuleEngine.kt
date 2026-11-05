package org.hisp.dhis.rules.api

import org.hisp.dhis.rules.engine.DefaultRuleEngine
import org.hisp.dhis.rules.models.*
import kotlin.jvm.JvmStatic

interface RuleEngine {
    fun analyzeContextRequirements(rules: List<Rule>, variables: List<RuleVariable>): RuleContextRequirements

    fun validate(
        expression: String,
        dataItemStore: Map<String, DataItem>,
    ): RuleValidationResult

    fun validateDataFieldExpression(
        expression: String,
        dataItemStore: Map<String, DataItem>,
    ): RuleValidationResult

    fun evaluateAll(
        enrollmentTarget: RuleEnrollment?,
        eventsTarget: List<RuleEvent>,
        executionContext: RuleEngineContext,
    ): List<RuleEffects>

    fun evaluate(
        target: RuleEnrollment,
        ruleEvents: List<RuleEvent>,
        executionContext: RuleEngineContext,
    ): List<RuleEffect>

    fun evaluate(
        target: RuleEvent,
        ruleEnrollment: RuleEnrollment?,
        ruleEvents: List<RuleEvent>,
        executionContext: RuleEngineContext,
    ): List<RuleEffect>

    fun order(
        ruleEvents: List<RuleEvent>,
    ): List<RuleEvent>

    /**
     * @return the functions, operators and environment variables that can be used in program rule expressions
     * (conditions and data field expressions)
     */
    fun getExpressionSyntax(): RuleExpressionSyntax

    companion object {
        @JvmStatic
        fun getInstance(): RuleEngine = DefaultRuleEngine()
    }
}
