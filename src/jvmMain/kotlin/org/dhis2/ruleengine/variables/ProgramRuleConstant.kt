package org.dhis2.ruleengine.variables

import org.dhis2.ruleengine.parser.expression.CommonExpressionVisitor
import org.dhis2.ruleengine.parser.expression.function.ScalarFunctionToEvaluate
import org.hisp.dhis.antlr.ParserExceptionWithoutContext
import org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext

class ProgramRuleConstant : ScalarFunctionToEvaluate() {
    override fun evaluate(ctx: ExprContext, visitor: CommonExpressionVisitor): Any {
        val variableValue = visitor.getValueMap()[ctx.uid0.text]
            ?: throw ParserExceptionWithoutContext("Variable " + ctx.uid0.text + " not present")
        return if (variableValue.value == null) variableValue.ruleValueType.defaultValue else variableValue.value!!
    }

    override fun getDescription(ctx: ExprContext, visitor: CommonExpressionVisitor): Any {
        val variable = ctx.uid0.text
        if (visitor.getItemStore().containsKey(variable)) {
            visitor.itemDescriptions[ctx.text] = visitor.getItemStore()[variable]!!.displayName
            return visitor.getItemStore()[ctx.uid0.text]!!.valueType.value
        }
        throw ParserExceptionWithoutContext(
            "Variable " + ctx.uid0.text + " does not exist"
        )
    }
}