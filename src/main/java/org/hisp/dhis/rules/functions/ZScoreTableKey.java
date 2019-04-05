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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author Zubair Asghar.
 */
public class ZScoreTableKey
{
    private final String gender;
    private final String age;

    public ZScoreTableKey( String gender, String age )
    {
        this.gender = gender;
        this.age = age;
    }

    public static Map<ZScoreTableKey, Set<Float>> getZscoreTableGirl()
    {
        Map<ZScoreTableKey, Set<Float>> zscoreMap = new HashMap<>();

        zscoreMap.put( new ZScoreTableKey( "female", "1" ), Sets.newHashSet( 2.0f, 2.4f, 2.8f, 3.2f, 3.7f, 4.2f, 4.8f ) );

        return zscoreMap;
    }

    public static Map<ZScoreTableKey, Set<Float>> getZscoreTableBoy()
    {
        Map<ZScoreTableKey, Set<Float>> zscoreMap = new HashMap<>();

        zscoreMap.put( new ZScoreTableKey( "male", "1" ), Sets.newHashSet( 2.0f, 2.4f, 2.8f, 3.2f, 3.7f, 4.2f, 4.8f ) );

        return zscoreMap;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;

        int result = 1;

        result = prime * result + ( (gender == null) ? 0 : gender.hashCode() );
        result = prime * result + ( (age == null) ? 0 : age.hashCode());

        return result;
    }

    @Override
    public boolean equals( Object obj )
    {
        if ( this == obj )
        {
            return true;
        }

        if ( !( obj instanceof ZScoreTableKey) )
        {
            return false;
        }

        ZScoreTableKey other = (ZScoreTableKey) obj;

        return this.age.equals( other.age ) && this.gender.equals( other.gender );
    }
}
