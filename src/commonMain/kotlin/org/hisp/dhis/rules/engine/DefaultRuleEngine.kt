package org.hisp.dhis.rules.engine

import org.hisp.dhis.lib.expression.Expression
import org.hisp.dhis.lib.expression.ExpressionMode
import org.hisp.dhis.lib.expression.spi.IllegalExpressionException
import org.hisp.dhis.lib.expression.spi.ParseException
import org.hisp.dhis.lib.expression.spi.ValueType
import org.hisp.dhis.lib.expression.ast.BinaryOperator
import org.hisp.dhis.lib.expression.ast.FunctionSignature
import org.hisp.dhis.lib.expression.ast.UnaryOperator
import org.hisp.dhis.rules.api.DataItem
import org.hisp.dhis.rules.api.EnvironmentVariable
import org.hisp.dhis.rules.api.EnvironmentVariables
import org.hisp.dhis.rules.api.ExpressionFunction
import org.hisp.dhis.rules.api.ExpressionFunctionParameter
import org.hisp.dhis.rules.api.ExpressionOperator
import org.hisp.dhis.rules.api.ExpressionValueType
import org.hisp.dhis.rules.api.RuleContextRequirements
import org.hisp.dhis.rules.api.RuleEngine
import org.hisp.dhis.rules.api.RuleEngineContext
import org.hisp.dhis.rules.api.RuleExpressionSyntax
import org.hisp.dhis.rules.models.*
import org.hisp.dhis.rules.utils.filterRules
import org.hisp.dhis.rules.utils.orderEvents

internal class DefaultRuleEngine : RuleEngine {
    override fun evaluate(
        target: RuleEvent,
        ruleEnrollment: RuleEnrollment?,
        ruleEvents: List<RuleEvent>,
        executionContext: RuleEngineContext,
    ): List<RuleEffect> {
        val valueMap =
            RuleVariableValueMapBuilder().build(
                executionContext.constantsValues,
                executionContext.ruleVariables,
                ruleEvents.toSet(),
                ruleEnrollment,
                target,
            )
        return RuleConditionEvaluator(executionContext.ruleVariables).getRuleEffects(
            TrackerObjectType.EVENT,
            target.event,
            valueMap,
            RuleConditionEvaluator.convertSupplementaryData(executionContext.ruleSupplementaryData),
            filterRules(executionContext.rules, target).sorted(),
            AttributeType.DATA_ELEMENT,
        )
    }

    override fun evaluate(
        target: RuleEnrollment,
        ruleEvents: List<RuleEvent>,
        executionContext: RuleEngineContext,
    ): List<RuleEffect> {
        val valueMap =
            RuleVariableValueMapBuilder().build(
                executionContext.constantsValues,
                executionContext.ruleVariables,
                ruleEvents.toSet(),
                target,
            )
        return RuleConditionEvaluator(executionContext.ruleVariables).getRuleEffects(
            TrackerObjectType.ENROLLMENT,
            target.enrollment,
            valueMap,
            RuleConditionEvaluator.convertSupplementaryData(executionContext.ruleSupplementaryData),
            filterRules(executionContext.rules).sorted(),
            AttributeType.TRACKED_ENTITY_ATTRIBUTE,
        )
    }

    override fun evaluateAll(
        enrollmentTarget: RuleEnrollment?,
        eventsTarget: List<RuleEvent>,
        executionContext: RuleEngineContext,
    ): List<RuleEffects> {
        val valueMap =
            RuleVariableValueMapBuilder()
                .multipleBuild(executionContext.constantsValues, executionContext.ruleVariables, eventsTarget.toSet(), enrollmentTarget)
        return RuleEngineMultipleExecution().execute(
            executionContext.rules,
            executionContext.ruleVariables,
            valueMap,
            executionContext.ruleSupplementaryData,
        )
    }

    override fun analyzeContextRequirements(rules: List<Rule>, variables: List<RuleVariable>): RuleContextRequirements {
        return RuleEngineAnalyzer.analyzeContextRequirements(rules, variables)
    }

    override fun validate(
        expression: String,
        dataItemStore: Map<String, DataItem>,
    ): RuleValidationResult {
        // Rule condition expression should be evaluated against Boolean
        return getExpressionDescription(expression, ExpressionMode.RULE_ENGINE_CONDITION, dataItemStore)
    }

    override fun validateDataFieldExpression(
        expression: String,
        dataItemStore: Map<String, DataItem>,
    ): RuleValidationResult {
        // Rule action data field should be evaluated against all i.e Boolean, String, Date and Numerical value
        return getExpressionDescription(expression, ExpressionMode.RULE_ENGINE_ACTION, dataItemStore)
    }

    private fun getExpressionDescription(
        expression: String,
        mode: ExpressionMode,
        dataItemStore: Map<String, DataItem>,
    ): RuleValidationResult =
        try {
            val validationMap: Map<String, ValueType> = dataItemStore.mapValues { e -> e.value.valueType.toValueType() }
            Expression(expression, mode, false).validate(validationMap)
            val displayNames: Map<String, String> = dataItemStore.mapValues { e -> e.value.displayName }
            val description = Expression(expression, mode, false).describe(displayNames)
            RuleValidationResult(valid = true, description = description)
        } catch (ex: IllegalExpressionException) {
            val errors = ex.validationErrors()
            val warnings = ex.validationWarnings()
            RuleValidationResult(
                valid = false,
                exception = RuleEngineValidationException(ex),
                errorMessage = describeIssues(errors, warnings),
                errors = errors,
                warnings = warnings,
            )
        } catch (ex: ParseException) {
            RuleValidationResult(
                valid = false,
                exception = RuleEngineValidationException(ex),
                errorMessage = ex.message,
                errors = listOf(RuleValidationIssue(ex.message.orEmpty())),
            )
        }

    override fun order(
        ruleEvents: List<RuleEvent>,
    ): List<RuleEvent> {
        return orderEvents(ruleEvents)
    }

    // Conditions and data fields share the same syntax, so the available functions and operators are the same
    override fun getExpressionSyntax(): RuleExpressionSyntax =
        RuleExpressionSyntax(
            functions = Expression.getFunctions(ExpressionMode.RULE_ENGINE_CONDITION).map { it.toExpressionFunction() },
            binaryOperators = Expression.getBinaryOperators().map { it.toExpressionOperator() },
            unaryOperators =
                Expression.getUnaryOperators(ExpressionMode.RULE_ENGINE_CONDITION).map { it.toExpressionOperator() },
            environmentVariables =
                EnvironmentVariables.ENV_VARIABLES.map { (name, type) -> EnvironmentVariable(name, type) },
        )

    private fun FunctionSignature.toExpressionFunction() =
        ExpressionFunction(
            name = getName(),
            returnType = ExpressionValueType.from(getReturnType()),
            parameters =
                parameters.map {
                    ExpressionFunctionParameter(
                        type = ExpressionValueType.from(it.type),
                        isOptional = it.isOptional,
                        isVarargs = it.isVarargs,
                        name = it.name,
                    )
                },
        )

    private fun BinaryOperator.toExpressionOperator() =
        ExpressionOperator(
            symbol = symbol,
            aliases = aliases,
            returnType = ExpressionValueType.from(getValueType()),
            operandsType = ExpressionValueType.from(operandsType),
        )

    // unary operators expect an operand of the type they return
    private fun UnaryOperator.toExpressionOperator() =
        ExpressionOperator(
            symbol = symbol,
            aliases = aliases,
            returnType = ExpressionValueType.from(getValueType()),
            operandsType = ExpressionValueType.from(getValueType()),
        )
}
