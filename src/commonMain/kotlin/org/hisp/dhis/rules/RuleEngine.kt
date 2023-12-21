package org.hisp.dhis.rules

import org.hisp.dhis.lib.expression.Expression
import org.hisp.dhis.lib.expression.spi.IllegalExpressionException
import org.hisp.dhis.lib.expression.spi.ParseException
import org.hisp.dhis.lib.expression.spi.ValueType
import org.hisp.dhis.rules.models.*

class RuleEngine: RuleEngineAPI {
    override fun evaluate(ruleEvent: RuleEvent, executionContext: RuleEngineContext): List<RuleEffect> {
        val valueMap = RuleVariableValueMapBuilder.target(ruleEvent)
            .ruleVariables(executionContext.ruleVariables)
            .ruleEnrollment(executionContext.enrollment)
            .triggerEnvironment(TriggerEnvironment.SERVER)
            .ruleEvents(executionContext.events)
            .constantValueMap(executionContext.constantsValues)
            .build()
        return RuleConditionEvaluator().getRuleEffects(
            TrackerObjectType.EVENT, ruleEvent.event, valueMap,
            executionContext.supplementaryData, executionContext.rules
        )
    }

    override fun evaluate(ruleEnrollment: RuleEnrollment, executionContext: RuleEngineContext): List<RuleEffect> {
        val valueMap = RuleVariableValueMapBuilder.target(ruleEnrollment)
                .ruleVariables(executionContext.ruleVariables)
                .triggerEnvironment(TriggerEnvironment.SERVER)
                .ruleEvents(executionContext.events)
                .constantValueMap(executionContext.constantsValues)
                .build()
        return RuleConditionEvaluator().getRuleEffects(
            TrackerObjectType.ENROLLMENT, ruleEnrollment.enrollment, valueMap,
            executionContext.supplementaryData, executionContext.rules
        )
    }

    override fun evaluate(executionContext: RuleEngineContext): List<RuleEffects> {
        val valueMap = RuleVariableValueMapBuilder.target()
                .ruleVariables(executionContext.ruleVariables)
                .ruleEnrollment(executionContext.enrollment)
                .triggerEnvironment(TriggerEnvironment.SERVER)
                .ruleEvents(executionContext.events)
                .constantValueMap(executionContext.constantsValues)
                .multipleBuild()
        return RuleEngineMultipleExecution().execute(executionContext.rules, valueMap,
                executionContext.supplementaryData)
    }

    override fun validate(expression: String, dataItemStore: Map<String, DataItem>): RuleValidationResult {
        // Rule condition expression should be evaluated against Boolean
        return getExpressionDescription(expression, Expression.Mode.RULE_ENGINE_CONDITION, dataItemStore)
    }

    override fun validateDataFieldExpression(expression: String, dataItemStore: Map<String, DataItem>): RuleValidationResult {
        // Rule action data field should be evaluated against all i.e Boolean, String, Date and Numerical value
        return getExpressionDescription(expression, Expression.Mode.RULE_ENGINE_ACTION, dataItemStore)
    }

    private fun getExpressionDescription(expression: String, mode: Expression.Mode, dataItemStore: Map<String, DataItem>): RuleValidationResult {
        return try {
            val validationMap: Map<String, ValueType> = dataItemStore.mapValues { e -> e.value.valueType.toValueType() }
            Expression(expression, mode, false).validate(validationMap)
            val displayNames: Map<String, String> = dataItemStore.mapValues { e -> e.value.displayName }
            val description = Expression(expression, mode, false).describe(displayNames)
            RuleValidationResult(valid = true, description = description)
        } catch (ex: IllegalExpressionException) {
            RuleValidationResult(
                valid = false,
                exception = RuleEngineValidationException(ex),
                errorMessage = ex.message
            )
        } catch (ex: ParseException) {
            RuleValidationResult(
                valid = false,
                exception = RuleEngineValidationException(ex),
                errorMessage = ex.message
            )
        }
    }
}
