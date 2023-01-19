package org.dhis2.ruleengine.variables

import org.dhis2.ruleengine.parser.expression.CommonExpressionVisitor
import org.dhis2.ruleengine.parser.expression.function.ScalarFunctionToEvaluate
import org.hisp.dhis.antlr.ParserExceptionWithoutContext
import org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext

class ProgramRuleCustomVariable : ScalarFunctionToEvaluate() {
    override fun evaluate(ctx: ExprContext, visitor: CommonExpressionVisitor): Any {
        val variableValue = visitor.getValueMap()[ctx.programRuleVariableName().text]
            ?: throw ParserExceptionWithoutContext(
                "Variable " + ctx.programRuleVariableName().text + " not present"
            )
        return variableValue.value ?: variableValue.ruleValueType.defaultValue
    }

    override fun getDescription(ctx: ExprContext, visitor: CommonExpressionVisitor): Any {
        var variable: String? = null
        if (ctx.programRuleVariableName() != null) {
            variable = ctx.programRuleVariableName().text
        } else if (ctx.programRuleStringVariableName() != null) {
            variable = ctx.programRuleStringVariableName().text.replace("\'", "")
        }
        if (visitor.getItemStore().containsKey(variable)) {
            visitor.itemDescriptions[ctx.text] = visitor.getItemStore()[variable]!!.displayName
            return visitor.getItemStore()[variable]!!.valueType.value
        }
        throw ParserExceptionWithoutContext(
            "Variable $variable does not exist"
        )
    }
}