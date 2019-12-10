package org.hisp.dhis.rules.functions;

import org.hisp.dhis.rules.RuleVariableValue;
import org.hisp.dhis.rules.models.TimeInterval;
import org.joda.time.Weeks;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

final class RuleFunctionWeeksBetween
    extends RuleFunction
{
    static final String D2_WEEKS_BETWEEN = "d2:weeksBetween";

    @Nonnull
    public static RuleFunctionWeeksBetween create()
    {
        return new RuleFunctionWeeksBetween();
    }

    @Nonnull
    @Override
    public String evaluate( @Nonnull List<String> arguments,
        Map<String, RuleVariableValue> valueMap, Map<String, List<String>> supplementaryData )
    {
        if ( arguments.size() != 2 )
        {
            throw new IllegalArgumentException( "Two arguments were expected, " +
                arguments.size() + " were supplied" );
        }

        return String.valueOf( weeksBetween( arguments.get( 0 ), arguments.get( 1 ) ) );
    }

    /**
     * Function which will return the number of weeks between the two given dates.
     *
     * @param start the start date.
     * @param end   the end date.
     * @return number of weeks between dates.
     */
    private Integer weeksBetween( String start, String end )
    {
        TimeInterval interval = getTimeInterval( start, end );

        if ( interval.isEmpty() )
        {
            return 0;
        }

        return Weeks.weeksBetween( interval.getStartDate(), interval.getEndDate() ).getWeeks();
    }
}
