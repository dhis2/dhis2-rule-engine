package org.hisp.dhis.rules.functions;

import org.hisp.dhis.parser.expression.CommonExpressionVisitor;
import org.hisp.dhis.parser.expression.antlr.ExpressionParser;
import org.hisp.dhis.parser.expression.function.SimpleNoSqlFunction;
import org.hisp.dhis.rules.models.TimeInterval;
import org.joda.time.Weeks;

public class RuleFunctionWeeksBetween
    extends SimpleNoSqlFunction
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
    public Object evaluate( ExpressionParser.ExprContext ctx, CommonExpressionVisitor visitor )
    {
        return String.valueOf(
            weeksBetween( visitor.castStringVisit( ctx.compareDate( 0 ) ),
                visitor.castStringVisit( ctx.compareDate( 1 ) ) ) );
    }
}
