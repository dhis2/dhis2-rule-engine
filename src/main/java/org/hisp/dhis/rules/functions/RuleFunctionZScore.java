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

import com.google.common.collect.Sets;
import org.hisp.dhis.rules.RuleVariableValue;

import javax.annotation.Nonnull;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author Zubair Asghar.
 */
public abstract class RuleFunctionZScore extends RuleFunction
{
    private static final Set<String> GENDER_CODES = Sets.newHashSet( "male", "MALE", "Male","ma", "m", "M", "0", "false" );
    protected static final DecimalFormat format = new DecimalFormat( "###.00" );

    @Nonnull
    @Override
    public String evaluate( @Nonnull List<String> arguments, Map<String, RuleVariableValue> valueMap, Map<String, List<String>> supplementaryData )
    {
        if ( arguments.size() < 3 )
        {
            throw new IllegalArgumentException( "At least three arguments required but found: " + arguments.size() );
        }

        // 1 = female, 0 = male
        byte age;
        float weight;
        byte gender = GENDER_CODES.contains( arguments.get( 2 ) ) ? (byte) 0 : (byte) 1;

        String zScore = "";

        try
        {
            age = Byte.parseByte( arguments.get( 0 ) );
            weight = Float.parseFloat( arguments.get( 1 ) );
        }
        catch ( NumberFormatException ex )
        {
            throw new IllegalArgumentException( "Byte parsing failed" );
        }

        zScore = getZScore( age, weight, gender );

        return zScore;
    }

    public abstract Map<ZScoreTableKey, Map<Float, Integer>> getTableForGirl();

    public abstract Map<ZScoreTableKey, Map<Float, Integer>> getTableForBoy();

    private String getZScore( byte age, float weight, byte gender )
    {
        ZScoreTableKey key = new ZScoreTableKey( gender, age );

        Map<Float, Integer> sdMap = new HashMap<>();

        // Female
        if ( gender == 1 )
        {
            sdMap = getTableForGirl().get( key );

        }
        else
        {
            sdMap = getTableForBoy().get( key );
        }

        int multiplicationFactor = getMultiplicationFactor( sdMap, weight );

        // weight exactly matches with any of the SD values
        if ( sdMap.keySet().contains( weight ) )
        {
            int sd = sdMap.get( weight );

            return String.valueOf( sd * multiplicationFactor );
        }

        // weight is beyond -3SD or 3SD
        if ( weight > Collections.max( sdMap.keySet() ) )
        {
            return String.valueOf( 3.5 );
        }
        else if ( weight < Collections.min( sdMap.keySet() ) )
        {
            return String.valueOf( -3.5 );
        }

        float lowerLimitX = 0, higherLimitY = 0;

        // find the interval
        for ( float f : sortKeySet( sdMap ) )
        {
            if (  weight > f )
            {
                lowerLimitX = f;
                continue;
            }

            higherLimitY = f;
            break;
        }

        float distance = higherLimitY - lowerLimitX;

        float gap;

        float decimalAddition;

        float result;

        if ( weight > findMedian( sdMap ) )
        {
            gap = weight - lowerLimitX;
            decimalAddition = gap / distance;
            result = sdMap.get( lowerLimitX ) + decimalAddition;
        }
        else
        {
            gap = higherLimitY - weight;
            decimalAddition = gap / distance;
            result = sdMap.get( higherLimitY ) + decimalAddition;
        }

        result = result * multiplicationFactor;

        return String.valueOf( format.format( result ) );
    }

    private int getMultiplicationFactor( Map<Float, Integer> sdMap, float weight )
    {
        float median = findMedian( sdMap );

        return Float.compare( weight, median );
    }

    private float findMedian( Map<Float, Integer> sdMap )
    {
        List<Float> sortedList = sortKeySet( sdMap );

        return sortedList.get( 3 );
    }

    private List<Float> sortKeySet( Map<Float, Integer> sdMap )
    {
        Set<Float> keySet = sdMap.keySet();

        List<Float> list = new ArrayList<>( keySet );

        Collections.sort( list );

        return list;
    }
}
