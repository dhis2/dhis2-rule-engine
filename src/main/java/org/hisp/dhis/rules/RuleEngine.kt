package org.hisp.dhis.rules

import org.hisp.dhis.lib.expression.Expression
import org.hisp.dhis.lib.expression.spi.IllegalExpressionException
import org.hisp.dhis.lib.expression.spi.ParseException
import org.hisp.dhis.lib.expression.spi.ValueType
import org.hisp.dhis.rules.models.*
import java.util.Date
import java.util.concurrent.Callable

data class RuleEngine(
    val executionContext: RuleEngineContext,
    val events: List<RuleEvent> = emptyList(),
    val enrollment: RuleEnrollment? = null,
    val triggerEnvironment: TriggerEnvironment? = TriggerEnvironment.SERVER) {

    @Deprecated("use kotlin property")
    fun executionContext(): RuleEngineContext {
        return executionContext
    }

    @Deprecated("use kotlin property")
    fun events(): List<RuleEvent> {
        return events
    }

    fun evaluate(ruleEvent: RuleEvent): Callable<List<RuleEffect>> {
        return evaluate(ruleEvent, executionContext.rules)
    }

    fun evaluate(ruleEvent: RuleEvent, rulesToEvaluate: List<Rule>): Callable<List<RuleEffect>> {
        val valueMap = RuleVariableValueMapBuilder.target(ruleEvent)
                .ruleVariables(executionContext.ruleVariables)
                .ruleEnrollment(enrollment)
                .triggerEnvironment(triggerEnvironment)
                .ruleEvents(events)
                .constantValueMap(executionContext.constantsValues)
                .build()
        return RuleEngineExecution(ruleEvent, null, rulesToEvaluate, valueMap, executionContext.supplementaryData)
    }

    fun evaluate(ruleEnrollment: RuleEnrollment,
                 rulesToEvaluate: List<Rule>): Callable<List<RuleEffect>> {
        val valueMap = RuleVariableValueMapBuilder.target(ruleEnrollment)
                .ruleVariables(executionContext.ruleVariables)
                .triggerEnvironment(triggerEnvironment)
                .ruleEvents(events)
                .constantValueMap(executionContext.constantsValues)
                .build()
        return RuleEngineExecution(null, ruleEnrollment, rulesToEvaluate, valueMap, executionContext.supplementaryData)
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
                executionContext.supplementaryData).call()
    }

    fun evaluate( ruleEnrollment: RuleEnrollment): Callable<List<RuleEffect>> {
        return evaluate(ruleEnrollment, executionContext.rules)
    }

    fun evaluate(expression: String): RuleValidationResult {
        // Rule condition expression should be evaluated against Boolean
        return getExpressionDescription(expression, Expression.Mode.RULE_ENGINE_CONDITION)
    }

    fun evaluateDataFieldExpression(expression: String): RuleValidationResult {
        // Rule action data field field should be evaluated against all i.e Boolean, String, Date and Numerical value
        return getExpressionDescription(expression, Expression.Mode.RULE_ENGINE_ACTION)
    }

    private fun getExpressionDescription(expression: String, mode: Expression.Mode): RuleValidationResult {
        return try {
            val validationMap: MutableMap<String, ValueType> = HashMap()
            val dataItemStore = executionContext.dataItemStore
            for ((key, value) in dataItemStore!!) {
                validationMap[key] = value.valueType.toValueType()
            }
            Expression(expression, mode, false).validate(validationMap)
            val displayNames: MutableMap<String, String> = HashMap()
            for ((key, value) in dataItemStore) {
                displayNames[key] = value.displayName
            }
            val description = Expression(expression, mode, false).describe(displayNames)
            RuleValidationResult( valid=true, description = description)
        } catch (ex: IllegalExpressionException) {
            RuleValidationResult(valid = false, exception = RuleEngineValidationException(ex), errorMessage = ex.message)
        } catch (ex: ParseException) {
            RuleValidationResult(valid = false, exception = RuleEngineValidationException(ex), errorMessage = ex.message)
        }
    }

    @Deprecated("use data copy")
    class Builder internal constructor(private val ruleEngineContext: RuleEngineContext) {
        private var ruleEvents: List<RuleEvent> = emptyList()
        private var ruleEnrollment: RuleEnrollment? = null
        private var triggerEnvironment: TriggerEnvironment? = null
        fun events(ruleEvents: List<RuleEvent>): Builder {
            this.ruleEvents = ruleEvents
            return this
        }

        fun enrollment(ruleEnrollment: RuleEnrollment): Builder {
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
