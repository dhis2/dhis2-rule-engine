package org.hisp.dhis.rules.functions;

import org.hisp.dhis.rules.models.TimeInterval;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class RuleFunction
{
    static final String DATE_PATTERN = "yyyy-MM-dd";

    static public TimeInterval getTimeInterval( String start, String end )
    {
        if ( isEmpty( start ) || isEmpty( end ) )
        {
            return TimeInterval.empty();
        }

        LocalDate startDate = LocalDate.parse( start, DateTimeFormat.forPattern( DATE_PATTERN ) );
        LocalDate endDate = LocalDate.parse( end, DateTimeFormat.forPattern( DATE_PATTERN ) );

        return TimeInterval.fromTo( startDate, endDate );
    }

    @Nonnull
    public double toDouble( @Nullable final String str, final double defaultValue )
    {
        if ( str == null )
        {
            return defaultValue;
        }

        try
        {
            return Double.parseDouble( str );
        }
        catch ( final NumberFormatException nfe )
        {
            return defaultValue;
        }
    }

    static public String wrap( String input )
    {
            if( input == null )
            {
                    return "";
            }
            return input;
    }

    static boolean isEmpty( String input )
    {
        return input == null || input.length() == 0;
    }
}
