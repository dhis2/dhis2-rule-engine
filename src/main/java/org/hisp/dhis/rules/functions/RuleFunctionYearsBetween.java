package org.hisp.dhis.rules.functions;

/*
 * Copyright (c) 2004-2018, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the HISP project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;

import org.hisp.dhis.rules.RuleVariableValue;

/**
 * @Author Zubair Asghar.
 */
public class RuleFunctionYearsBetween
    extends
    RuleFunction
{
    public static final String D2_YEARS_BETWEEN = "d2:yearsBetween";

    // private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
    // "yyyy-MM-dd" );
    private final SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd", Locale.getDefault() );

    @Nonnull
    @Override
    public String evaluate( @Nonnull List<String> arguments, Map<String, RuleVariableValue> valueMap,
        Map<String, List<String>> supplementaryData )
    {

        if ( arguments.size() != 2 )
        {
            throw new IllegalArgumentException( "Two arguments were expected, " + arguments.size() + " were supplied" );
        }

        String startDateString = arguments.get( 0 );
        String endDateString = arguments.get( 1 );

        if ( isEmpty( startDateString ) || isEmpty( endDateString ) )
        {
            return "";
        }

        Date startDate;
        Date endDate;

        try
        {
            startDate = formatter.parse( arguments.get( 0 ) );
            endDate = formatter.parse( arguments.get( 1 ) );
        }
        catch ( ParseException e )
        {
            throw new IllegalArgumentException( "Date cannot be parsed" );
        }

        long yearsBetween = yearsBetween( startDate, endDate );

        return String.valueOf( yearsBetween );
    }

    public static RuleFunctionYearsBetween create()
    {
        return new RuleFunctionYearsBetween();
    }

    private Long yearsBetween( Date startDate, Date endDate )
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( startDate );
        int startYear = calendar.get( Calendar.YEAR );
        int startMonth = calendar.get( Calendar.MONTH );
        int startDay = calendar.get( Calendar.DAY_OF_MONTH );
        calendar.setTime( endDate );
        int endYear = calendar.get( Calendar.YEAR );
        int endMonth = calendar.get( Calendar.MONTH );
        int endDay = calendar.get( Calendar.DAY_OF_MONTH );

        long diffYear = endYear - startYear;

        if ( diffYear > 0 )
        {
            if ( endMonth > startMonth )
            {
                return diffYear;
            }
            else if ( endMonth < startMonth )
            {
                return --diffYear;
            }
            else if ( endDay > startDay )
            {
                return diffYear;
            }
            else if ( endDay < startDay )
            {
                return --diffYear;
            }
            else
            {
                return diffYear;
            }
        }
        else if ( diffYear < 0 )
        {
            return -yearsBetween( endDate, startDate );
        }
        else
        {
            return diffYear;
        }
    }
}
