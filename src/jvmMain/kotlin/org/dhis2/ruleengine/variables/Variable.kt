package org.dhis2.ruleengine.variables

import org.dhis2.ruleengine.RuleExpression.Companion.getProgramRuleVariable
import org.dhis2.ruleengine.parser.expression.CommonExpressionVisitor
import org.dhis2.ruleengine.parser.expression.function.ScalarFunctionToEvaluate
import org.hisp.dhis.antlr.ParserExceptionWithoutContext
import org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext

class Variable : ScalarFunctionToEvaluate() {
    override fun evaluate(ctx: ExprContext, visitor: CommonExpressionVisitor): Any {
        val variableValue =
            visitor.getValueMap()[getProgramRuleVariable(ctx)]
                ?: throw ParserExceptionWithoutContext(
                    "Variable " + getProgramRuleVariable(ctx) + " not present"
                )
        return variableValue.value ?: variableValue.ruleValueType.defaultValue
    }

    override fun getDescription(ctx: ExprContext, visitor: CommonExpressionVisitor): Any {
        val variable = getProgramRuleVariable(ctx)
        if (visitor.getItemStore().containsKey(variable)) {
            visitor.itemDescriptions[ctx.text] = visitor.getItemStore()[variable]!!.displayName
            return visitor.getItemStore()[getProgramRuleVariable(ctx)]!!.valueType.value
        }
        throw ParserExceptionWithoutContext(
            "Variable " + getProgramRuleVariable(ctx) + " does not exist"
        )
    }
}