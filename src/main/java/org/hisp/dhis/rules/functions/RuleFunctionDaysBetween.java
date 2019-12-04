package org.hisp.dhis.rules.functions;

import com.google.auto.value.AutoValue;
import org.hisp.dhis.rules.RuleVariableValue;
import org.hisp.dhis.rules.models.TimeInterval;
import org.joda.time.Days;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

@AutoValue
abstract class RuleFunctionDaysBetween
    extends RuleFunction
{
        static final String D2_DAYS_BETWEEN = "d2:daysBetween";

        @Nonnull
        @Override
        public String evaluate( @Nonnull List<String> arguments,
            @Nonnull Map<String, RuleVariableValue> valueMap, Map<String, List<String>> supplementaryData )
        {
                if ( arguments.size() != 2 )
                {
                        throw new IllegalArgumentException( "Two arguments were expected, " +
                            arguments.size() + " were supplied" );
                }

                return String.valueOf( daysBetween( arguments.get( 0 ), arguments.get( 1 ) ) );
        }

        @Nonnull
        public static RuleFunctionDaysBetween create()
        {
                return new AutoValue_RuleFunctionDaysBetween();
        }

        /**
         * Function which will return the number of days between the two given dates.
         *
         * @param start the start date.
         * @param end   the end date.
         * @return number of days between dates.
         */
        private Integer daysBetween( String start, String end )
        {

                TimeInterval interval = getTimeInterval( start, end );

                if ( interval.isEmpty() )
                {
                        return 0;
                }

                return Days.daysBetween( interval.getStartDate(), interval.getEndDate() ).getDays();
        }
}
