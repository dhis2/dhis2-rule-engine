package org.dhis2.ruleengine.parser.expression.function

import org.dhis2.ruleengine.parser.expression.CommonExpressionVisitor
import org.dhis2.ruleengine.variables.ProgramRuleCustomVariable
import org.dhis2.ruleengine.variables.ProgramRuleVariable
import org.hisp.dhis.antlr.AntlrExprItem
import org.hisp.dhis.antlr.AntlrExpressionVisitor
import org.hisp.dhis.antlr.ParserExceptionWithoutContext
import org.hisp.dhis.parser.expression.antlr.ExpressionParser


/**
 * Scalar function that needs to be evaluated using the specific CommonExpressionVisitor.
 *
 * @author Enrico Colasante
 */
abstract class ScalarFunctionToEvaluate : AntlrExprItem {
    /**
     * Finds the value of an expression function, evaluating arguments only
     * when necessary.
     *
     * @param ctx     the expression context
     * @param visitor the specific tree visitor
     * @return the value of the function, evaluating necessary args
     */
    abstract fun evaluate(ctx: ExpressionParser.ExprContext, visitor: CommonExpressionVisitor): Any

    /**
     * Override the default evaluate method using the specific visitor CommonExpressionVisitor.
     *
     * @param ctx     the expression context
     * @param visitor the specific tree visitor
     * @return the value of the function, evaluating necessary args
     */
    override fun evaluate(ctx: ExpressionParser.ExprContext, visitor: AntlrExpressionVisitor): Any {
        return evaluate(ctx, visitor as CommonExpressionVisitor)
    }

    /**
     * Finds the description of an expression function.
     *
     * @param ctx     the expression context
     * @param visitor the specific tree visitor
     * @return expression description
     */
    abstract fun getDescription(ctx: ExpressionParser.ExprContext, visitor: CommonExpressionVisitor): Any
    protected fun getProgramArgType(ctx: ExpressionParser.ExprContext): ScalarFunctionToEvaluate {
        if (ctx.programVariable() != null) {
            return ProgramRuleVariable()
        }
        if (ctx.programRuleVariableName() != null) {
            return ProgramRuleCustomVariable()
        }
        if (ctx.programRuleStringVariableName() != null) {
            return ProgramRuleCustomVariable()
        }
        throw ParserExceptionWithoutContext("Illegal argument in program rule expression: " + ctx.getText())
    }
}