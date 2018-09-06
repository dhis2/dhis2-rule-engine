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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author Zubair Asghar.
 * <p>
 * Counts the number of values that is zero or positive entered for the source field in the argument.
 * The source field parameter is the name of one of the defined source fields in the program.
 */
public class RuleFunctionCountIfZeroPos
    extends RuleFunction
{
        public static final String D2_COUNT_IF_ZERO_POS = "d2:countIfZeroPos";

        @Nonnull
        @Override
        public String evaluate( @Nonnull List<String> arguments, Map<String, RuleVariableValue> valueMap,
            Map<String, List<String>> supplementaryData )
        {
                if ( valueMap == null )
                {
                        throw new IllegalArgumentException( "valueMap is expected" );
                }

                if ( arguments.size() != 1 )
                {
                        throw new IllegalArgumentException( "One arguments were expected, " +
                            arguments.size() + " were supplied" );
                }

                RuleVariableValue value = valueMap.get( arguments.get( 0 ) );

                if ( value != null )
                {
                        List<String> candidates = value.candidates();

                        Integer count = candidates.stream().filter( this::isZeroPos ).collect( Collectors.toList() ).size();

                        return String.valueOf( count );
                }
                else
                {
                        return "0";
                }
        }

        public static RuleFunctionCountIfZeroPos create()
        {
                return new RuleFunctionCountIfZeroPos();
        }

        private boolean isZeroPos( String input )
        {
                Double value;

                try
                {
                        value = Double.parseDouble( input );
                }
                catch ( NumberFormatException e )
                {
                        throw new IllegalArgumentException( "Invalid number format" );
                }

                return value >= 0;
        }
}
