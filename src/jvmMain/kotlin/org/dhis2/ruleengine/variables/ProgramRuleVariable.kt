package org.dhis2.ruleengine.variables

import org.dhis2.ruleengine.parser.expression.CommonExpressionVisitor
import org.dhis2.ruleengine.parser.expression.function.ScalarFunctionToEvaluate
import org.hisp.dhis.antlr.ParserExceptionWithoutContext
import org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext

class ProgramRuleVariable : ScalarFunctionToEvaluate() {
    override fun evaluate(ctx: ExprContext, visitor: CommonExpressionVisitor): Any {
        val variableValue = visitor.getValueMap()[ctx.programVariable().text]
            ?: throw ParserExceptionWithoutContext("Variable " + ctx.programVariable().text + " not present")
        return variableValue.value ?: variableValue.ruleValueType.defaultValue
    }

    override fun getDescription(ctx: ExprContext, visitor: CommonExpressionVisitor): Any {
        val variable = ctx.programVariable().text
        if (visitor.getItemStore().containsKey(variable)) {
            visitor.itemDescriptions[ctx.text] = visitor.getItemStore()[variable]!!.displayName
            return visitor.getItemStore()[ctx.programVariable().text]!!.valueType.value
        }
        throw ParserExceptionWithoutContext(
            "Variable " + ctx.programVariable().text + " does not exist"
        )
    }
}