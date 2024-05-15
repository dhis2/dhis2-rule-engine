package org.hisp.dhis.rules.api

import org.hisp.dhis.rules.engine.DefaultRuleEngine
import org.hisp.dhis.rules.getEnvironment
import org.hisp.dhis.rules.models.*
import org.hisp.dhis.rules.models.TriggerEnvironment.SERVER
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic

interface RuleEngine {
    fun validate(expression: String, dataItemStore: Map<String, DataItem>): RuleValidationResult
    fun validateDataFieldExpression(expression: String, dataItemStore: Map<String, DataItem>): RuleValidationResult
    fun evaluateAll(enrollmentTarget: RuleEnrollment?, eventsTarget: List<RuleEvent>, executionContext: RuleEngineContext, triggerEnvironment: TriggerEnvironment = getEnvironment()): List<RuleEffects>
    fun evaluate(target: RuleEnrollment, ruleEvents: List<RuleEvent>, executionContext: RuleEngineContext, triggerEnvironment: TriggerEnvironment = getEnvironment()): List<RuleEffect>
    fun evaluate(target: RuleEvent, ruleEnrollment: RuleEnrollment?, ruleEvents: List<RuleEvent>, executionContext: RuleEngineContext, triggerEnvironment: TriggerEnvironment = getEnvironment()): List<RuleEffect>

    companion object {
        @JvmStatic
        fun getInstance(): RuleEngine {
            return DefaultRuleEngine()
        }
    }
}