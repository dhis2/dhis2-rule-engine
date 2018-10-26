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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author Zubair Asghar.
 *
 * Returns the number of numeric zero and positive values among the given object arguments. Can be provided with any number of arguments.
 */
public class RuleFunctionZpvc extends RuleFunction
{
    public static final String D2_ZPVC = "d2:zpvc";

    @Nonnull
    @Override
    public String evaluate( @Nonnull List<String> arguments, Map<String, RuleVariableValue> valueMap, Map<String, List<String>> supplementaryData )
    {
        if ( arguments.isEmpty() )
        {
            throw new IllegalArgumentException( "At least one argument should be provided" );
        }

        List<Double> list = new ArrayList<>();

        try
        {
            list = arguments.stream().map( Double::new ).filter( v -> v >= 0 ).collect( Collectors.toList() );
        }
        catch ( NumberFormatException e )
        {
            throw new IllegalArgumentException( "Number has to be an integer" );
        }

        return String.valueOf( list.size() );
    }

    public static RuleFunctionZpvc create()
    {
        return new RuleFunctionZpvc();
    }
}
