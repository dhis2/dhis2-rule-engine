package org.dhis2.ruleengine.functions

import org.dhis2.ruleengine.RuleExpression.Companion.getProgramRuleVariable
import org.dhis2.ruleengine.parser.expression.CommonExpressionVisitor
import org.dhis2.ruleengine.parser.expression.function.ScalarFunctionToEvaluate
import org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext

class RuleFunctionHasValue : ScalarFunctionToEvaluate() {
    override fun evaluate(ctx: ExprContext, visitor: CommonExpressionVisitor): Any {
        val valueMap = visitor.getValueMap()
        val variableName = getProgramRuleVariable(ctx)
        val variableValue = valueMap[variableName] ?: return false.toString()
        return (valueMap[variableName]!!.value != null).toString()
    }

    override fun getDescription(ctx: ExprContext, visitor: CommonExpressionVisitor): Any {
        getProgramArgType(ctx).getDescription(ctx, visitor)
        return CommonExpressionVisitor.DEFAULT_BOOLEAN_VALUE
    }
}