package org.hisp.dhis.rules.functions;

import org.hisp.dhis.rules.RuleVariableValue;

import javax.annotation.Nonnull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

                return String.valueOf( daysBetween( arguments.get( 0 ), arguments.get( 1 ) ) / 7 );
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
                if ( isEmpty( start ) || isEmpty( end ) )
                {
                        return 0;
                }

                SimpleDateFormat format = new SimpleDateFormat();
                format.applyPattern( DATE_PATTERN );

                try
                {
                        Date startDate = format.parse( start );
                        Date endDate = format.parse( end );

                        return Long.valueOf( (endDate.getTime() - startDate.getTime()) / 86400000 ).intValue();
                }
                catch ( ParseException parseException )
                {
                        throw new RuntimeException( parseException );
                }
        }
}
