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

import org.hisp.dhis.rules.RuleVariableValue;

import javax.annotation.Nonnull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

/**
 * @Author Zubair Asghar.
 */
public class RuleFunctionYearsBetween extends RuleFunction
{
    public static final String D2_YEARS_BETWEEN = "d2:yearsBetween";

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "yyyy-MM-dd" );

    @Nonnull
    @Override
    public String evaluate( @Nonnull List<String> arguments, Map<String, RuleVariableValue> valueMap, Map<String, List<String>> supplementaryData )
    {
        if ( arguments.size() != 2 )
        {
            throw new IllegalArgumentException( "Two arguments were expected, " + arguments.size() + " were supplied" );
        }

        LocalDate startDate = null;
        LocalDate endDate = null;

        try
        {
            startDate = LocalDate.parse( arguments.get( 0 ), formatter );
            endDate = LocalDate.parse( arguments.get( 1 ), formatter );
        }
        catch ( DateTimeParseException e )
        {
            throw  new IllegalArgumentException( "Date cannot be parsed" );
        }

        return wrap( String.valueOf( ChronoUnit.YEARS.between( startDate, endDate ) ) );
    }

    public static RuleFunctionYearsBetween create()
    {
        return new RuleFunctionYearsBetween();
    }
}
