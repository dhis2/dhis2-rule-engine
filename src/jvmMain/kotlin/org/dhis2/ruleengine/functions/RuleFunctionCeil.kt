package org.dhis2.ruleengine.functions

import org.apache.commons.lang3.math.NumberUtils
import org.dhis2.ruleengine.parser.expression.CommonExpressionVisitor
import org.dhis2.ruleengine.parser.expression.function.ScalarFunctionToEvaluate
import org.hisp.dhis.antlr.AntlrParserUtils
import org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext
import kotlin.math.ceil

class RuleFunctionCeil : ScalarFunctionToEvaluate() {
    override fun evaluate(ctx: ExprContext, visitor: CommonExpressionVisitor): Any {
        return ceil(NumberUtils.toDouble(visitor.castStringVisit(ctx.expr(0)), 0.0)).toLong().toString()
    }

    override fun getDescription(ctx: ExprContext, visitor: CommonExpressionVisitor): Any {
        AntlrParserUtils.castDouble(visitor.visit(ctx.expr(0)))
        return CommonExpressionVisitor.DEFAULT_DOUBLE_VALUE
    }
}