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

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import org.hisp.dhis.rules.RuleVariableValue;
import org.hisp.dhis.rules.models.RuleValueType;

/**
 * @Author Zubair Asghar.
 *         <p>
 *         Counts the number of matching values that is entered for the source
 *         field in the first argument. Only occurrences that matches the second
 *         argument is counted. The source field parameter is the name of one of
 *         the defined source fields in the program.
 */
public class RuleFunctionCountIfValue
    extends
    RuleFunction
{
    static final String D2_COUNT_IF_VALUE = "d2:countIfValue";

    @Nonnull
    @Override
    public String evaluate( @Nonnull List<String> arguments, Map<String, RuleVariableValue> valueMap,
        Map<String, List<String>> supplementaryData )
    {
        if ( valueMap == null )
        {
            throw new IllegalArgumentException( "valueMap is expected" );
        }

        if ( arguments.size() != 2 )
        {
            throw new IllegalArgumentException( "Two arguments were expected, " + arguments.size() + " were supplied" );
        }

        return countIfValue( arguments, valueMap );
    }

    @Nonnull
    public static RuleFunctionCountIfValue create()
    {
        return new RuleFunctionCountIfValue();
    }

    /**
     * Function which will return the count of argument[0]. Program rule variable at
     * argument[0] will only be counted if it satisfy to condition which is at
     * argument[1]
     *
     * @param arguments arguments for this function. First is the name of program
     *        rule variable and second is the condition
     * @param valueMap key value pair containing values for each variable
     * @return count of program rule variable
     */
    private String countIfValue( List<String> arguments, Map<String, RuleVariableValue> valueMap )
    {
        String ruleVariableName = arguments.get( 0 );

        RuleVariableValue variableValue = valueMap.get( ruleVariableName );

        String valueToFind = arguments.get( 1 );

        if ( variableValue != null )
        {
            if ( variableValue.type() == RuleValueType.BOOLEAN )
            {
                if ( valueToFind.equals( "1" ) )
                {
                    valueToFind = "true";
                }
                else if ( valueToFind.equals( "0" ) )
                {
                    valueToFind = "false";
                }
            }

            return Integer.toString( Collections.frequency( variableValue.candidates(), valueToFind ) );
        }
        else
        {
            return "0";
        }
    }
}
