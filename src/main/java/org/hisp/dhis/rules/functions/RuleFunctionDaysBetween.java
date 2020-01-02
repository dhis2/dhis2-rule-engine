package org.hisp.dhis.rules.functions;

import org.hisp.dhis.parser.expression.CommonExpressionVisitor;
import org.hisp.dhis.parser.expression.function.SimpleNoSqlFunction;
import org.hisp.dhis.parser.expression.antlr.ExpressionParser;
import org.hisp.dhis.rules.models.TimeInterval;
import org.joda.time.Days;

public class RuleFunctionDaysBetween
    extends SimpleNoSqlFunction
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
    public Object evaluate( ExpressionParser.ExprContext ctx, CommonExpressionVisitor visitor )
    {
        return String.valueOf( daysBetween(
            visitor.castStringVisit( ctx.compareDate( 0 ) ),
            visitor.castStringVisit( ctx.compareDate( 1 ) ) ) );
    }
}
