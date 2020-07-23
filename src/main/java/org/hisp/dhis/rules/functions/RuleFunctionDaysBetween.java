package org.hisp.dhis.rules.functions;

import org.hisp.dhis.rules.models.TimeInterval;
import org.hisp.dhis.rules.parser.expression.CommonExpressionVisitor;
import org.hisp.dhis.rules.parser.expression.function.ScalarFunctionToEvaluate;
import org.joda.time.Days;

import static org.hisp.dhis.antlr.AntlrParserUtils.castDate;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

public class RuleFunctionDaysBetween
    extends ScalarFunctionToEvaluate
{
    /**
     * Function which will return the number of days between the two given dates.
     *
     * @param start the start date.
     * @param end   the end date.
     * @return number of days between dates.
     */
    private Integer daysBetween( String start, String end )
    {

        TimeInterval interval = RuleFunction.getTimeInterval( start, end );

        if ( interval.isEmpty() )
        {
            return 0;
        }

        return Days.daysBetween( interval.getStartDate(), interval.getEndDate() ).getDays();
    }

    @Override
    public Object evaluate( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        return String.valueOf( daysBetween(
            visitor.castStringVisit( ctx.expr( 0 ) ),
            visitor.castStringVisit( ctx.expr( 1 ) ) ) );
    }

    @Override
    public Object getDescription( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        castDate( visitor.visit( ctx.expr( 0 ) ) );
        castDate( visitor.visit( ctx.expr( 1 ) ) );

        return CommonExpressionVisitor.DEFAULT_DATE_VALUE;
    }
}
