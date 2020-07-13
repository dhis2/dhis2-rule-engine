package org.hisp.dhis.rules.functions;

import org.hisp.dhis.rules.models.TimeInterval;
import org.hisp.dhis.rules.parser.expression.CommonExpressionVisitor;
import org.hisp.dhis.rules.parser.expression.function.ScalarFunctionToEvaluate;
import org.joda.time.Weeks;

import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

public class RuleFunctionWeeksBetween
    extends ScalarFunctionToEvaluate
{
    /**
     * Function which will return the number of weeks between the two given dates.
     *
     * @param start the start date.
     * @param end   the end date.
     * @return number of weeks between dates.
     */
    private Integer weeksBetween( String start, String end )
    {
        TimeInterval interval = RuleFunction.getTimeInterval( start, end );

        if ( interval.isEmpty() )
        {
            return 0;
        }

        return Weeks.weeksBetween( interval.getStartDate(), interval.getEndDate() ).getWeeks();
    }

    @Override
    public Object evaluate( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        return String.valueOf(
            weeksBetween( visitor.castStringVisit( ctx.expr( 0 ) ),
                visitor.castStringVisit( ctx.expr( 1 ) ) ) );
    }

    @Override
    public Object getDescription( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        return null;
    }
}
