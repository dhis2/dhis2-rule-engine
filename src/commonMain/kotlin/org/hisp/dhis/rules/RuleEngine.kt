package org.hisp.dhis.rules

import org.hisp.dhis.lib.expression.Expression
import org.hisp.dhis.lib.expression.spi.IllegalExpressionException
import org.hisp.dhis.lib.expression.spi.ParseException
import org.hisp.dhis.lib.expression.spi.ValueType
import org.hisp.dhis.rules.models.*

data class RuleEngine(
    val executionContext: RuleEngineContext,
    val events: List<RuleEvent> = emptyList(),
    val enrollment: RuleEnrollment? = null,
    val triggerEnvironment: TriggerEnvironment? = TriggerEnvironment.SERVER) {

    fun evaluate(ruleEvent: RuleEvent): List<RuleEffect> {
        return evaluate(ruleEvent, executionContext.rules)
    }

    fun evaluate(ruleEvent: RuleEvent, rulesToEvaluate: List<Rule>): List<RuleEffect> {
        val valueMap = RuleVariableValueMapBuilder.target(ruleEvent)
                .ruleVariables(executionContext.ruleVariables)
                .ruleEnrollment(enrollment)
                .triggerEnvironment(triggerEnvironment)
                .ruleEvents(events)
                .constantValueMap(executionContext.constantsValues)
                .build()
        return RuleEngineExecution(ruleEvent, null, rulesToEvaluate, valueMap, executionContext.supplementaryData).execute()
    }

    fun evaluate(ruleEnrollment: RuleEnrollment,
                 rulesToEvaluate: List<Rule>): List<RuleEffect> {
        val valueMap = RuleVariableValueMapBuilder.target(ruleEnrollment)
                .ruleVariables(executionContext.ruleVariables)
                .triggerEnvironment(triggerEnvironment)
                .ruleEvents(events)
                .constantValueMap(executionContext.constantsValues)
                .build()
        return RuleEngineExecution(null, ruleEnrollment, rulesToEvaluate, valueMap, executionContext.supplementaryData).execute()
    }

    fun evaluate(): List<RuleEffects> {
        val valueMap = RuleVariableValueMapBuilder.target()
                .ruleVariables(executionContext.ruleVariables)
                .ruleEnrollment(enrollment)
                .triggerEnvironment(triggerEnvironment)
                .ruleEvents(events)
                .constantValueMap(executionContext.constantsValues)
                .multipleBuild()
        return RuleEngineMultipleExecution(executionContext.rules, valueMap,
                executionContext.supplementaryData).execute()
    }

    fun evaluate( ruleEnrollment: RuleEnrollment): List<RuleEffect> {
        return evaluate(ruleEnrollment, executionContext.rules)
    }

    @Deprecated("use data copy")
    class Builder internal constructor(private val ruleEngineContext: RuleEngineContext) {
        private var ruleEvents: List<RuleEvent> = emptyList()
        private var ruleEnrollment: org.hisp.dhis.rules.models.RuleEnrollment? = null
        private var triggerEnvironment: TriggerEnvironment? = null
        fun events(ruleEvents: List<RuleEvent>): Builder {
            this.ruleEvents = ruleEvents
            return this
        }

        fun enrollment(ruleEnrollment: org.hisp.dhis.rules.models.RuleEnrollment): Builder {
            this.ruleEnrollment = ruleEnrollment
            return this
        }

        fun triggerEnvironment(triggerEnvironment: TriggerEnvironment): Builder {
            this.triggerEnvironment = triggerEnvironment
            return this
        }

        fun build(): RuleEngine {
            return RuleEngine(ruleEngineContext, ruleEvents, ruleEnrollment, triggerEnvironment)
        }
    }
}
