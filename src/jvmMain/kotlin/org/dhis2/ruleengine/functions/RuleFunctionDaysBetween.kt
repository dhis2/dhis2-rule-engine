package org.dhis2.ruleengine.functions

import org.dhis2.ruleengine.functions.RuleFunction.Companion.getTimeInterval
import org.dhis2.ruleengine.parser.expression.CommonExpressionVisitor
import org.dhis2.ruleengine.parser.expression.function.ScalarFunctionToEvaluate
import org.hisp.dhis.antlr.AntlrParserUtils
import org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext
import org.joda.time.Days

class RuleFunctionDaysBetween : ScalarFunctionToEvaluate() {
    /**
     * Function which will return the number of days between the two given dates.
     *
     * @param start the start date.
     * @param end   the end date.
     * @return number of days between dates.
     */
    private fun daysBetween(start: String, end: String): Int {
        val interval = getTimeInterval(start, end)
        return if (interval.isEmpty) {
            0
        } else Days.daysBetween(interval.startDate, interval.endDate).days
    }

    override fun evaluate(ctx: ExprContext, visitor: CommonExpressionVisitor): Any {
        return daysBetween(
            visitor.castStringVisit(ctx.expr(0)),
            visitor.castStringVisit(ctx.expr(1))
        ).toString()
    }

    override fun getDescription(ctx: ExprContext, visitor: CommonExpressionVisitor): Any {
        AntlrParserUtils.castDate(visitor.visit(ctx.expr(0)))
        AntlrParserUtils.castDate(visitor.visit(ctx.expr(1)))
        return CommonExpressionVisitor.DEFAULT_DOUBLE_VALUE
    }
}