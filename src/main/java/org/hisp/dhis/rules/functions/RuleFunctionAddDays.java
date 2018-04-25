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

import com.google.auto.value.AutoValue;
import org.hisp.dhis.rules.RuleVariableValue;

import javax.annotation.Nonnull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author Zubair Asghar.
 */

class RuleFunctionAddDays
        extends RuleFunction
{
        static final String D2_ADD_DAYS = "d2:addDays";

        @Nonnull
        @Override
        public String evaluate( @Nonnull List<String> arguments, @Nonnull Map<String, RuleVariableValue> valueMap )
        {
                if ( arguments.size() != 2 )
                {
                        throw new IllegalArgumentException( "Two arguments were expected, " +
                            arguments.size() + " were supplied" );
                }

                return wrap( addDays( arguments.get( 0 ), arguments.get( 1 ) ) );
        }

        @Nonnull
        public static RuleFunctionAddDays create()
        {
                return new RuleFunctionAddDays();
        }

        /**
         * Function which will return the the date after adding/subtracting number of days.
         *
         * @param inputDate the date to add/subtract from.
         * @param days  number of days to add/subtract.
         * @return date after adding/subtracting days.
         */
        @SuppressWarnings( "PMD.UnnecessaryWrapperObjectCreation" )
        static String addDays( String inputDate, String days )
        {
                Calendar calendar = Calendar.getInstance();

                SimpleDateFormat format = new SimpleDateFormat();
                format.applyPattern( DATE_PATTERN );

                try
                {
                        Date date = format.parse( inputDate );

                        calendar.setTime( date );
                        calendar.add( Calendar.DATE, Integer.parseInt( days ) );
                        Date calculatedDate = calendar.getTime();

                        return format.format( calculatedDate );
                }
                catch ( ParseException parseException )
                {
                        throw new RuntimeException( parseException );
                }
        }

        private String wrap( String input )
        {
                if( input == null )
                {
                        return "";
                }

                return "'"+input+"'";
        }
}
