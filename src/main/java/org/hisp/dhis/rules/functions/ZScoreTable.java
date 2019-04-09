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

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Zubair Asghar.
 */
public class ZScoreTable
{
    public static Map<ZScoreTableKey, Map<Float, Integer>> getZscoreTableGirl()
    {
        Map<ZScoreTableKey, Map<Float, Integer>> zscoreMap = new HashMap<>();

        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)0 ),  createSDMap( 2.0f,2.4f,2.8f,3.2f,3.7f,4.2f,4.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)1 ),  createSDMap( 2.7f,3.2f,3.6f,4.2f,4.8f,5.5f,6.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)2 ),  createSDMap( 3.4f,3.9f,4.5f,5.1f,5.8f,6.6f,7.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)3 ),  createSDMap( 4.0f,4.5f,5.2f,5.8f,6.6f,7.5f,8.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)4 ),  createSDMap( 4.4f,5.0f,5.7f,6.4f,7.3f,8.2f,9.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)5 ),  createSDMap( 4.8f,5.4f,6.1f,6.9f,7.8f,8.8f,10.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)6 ),  createSDMap( 5.1f,5.7f,6.5f,7.3f,8.2f,9.3f,10.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)7 ),  createSDMap( 5.3f,6.0f,6.8f,7.6f,8.6f,9.8f,11.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)8 ),  createSDMap( 5.6f,6.3f,7.0f,7.9f,9.0f,10.2f,11.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)9 ),  createSDMap( 5.8f,6.5f,7.3f,8.2f,9.3f,10.5f,12.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)10 ),  createSDMap( 5.9f,6.7f,7.5f,8.5f,9.6f,10.9f,12.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)11 ),  createSDMap( 6.1f,6.9f,7.7f,8.7f,9.9f,11.2f,12.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)12 ),  createSDMap( 6.3f,7.0f,7.9f,8.9f,10.1f,11.5f,13.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)13 ),  createSDMap( 6.4f,7.2f,8.1f,9.2f,10.4f,11.8f,13.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)14 ),  createSDMap( 6.6f,7.4f,8.3f,9.4f,10.6f,12.1f,13.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)15 ),  createSDMap( 6.7f,7.6f,8.5f,9.6f,10.9f,12.4f,14.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)16 ),  createSDMap( 6.9f,7.7f,8.7f,9.8f,11.1f,12.6f,14.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)17 ),  createSDMap( 7.0f,7.9f,8.9f,10.0f,11.4f,12.9f,14.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)18 ),  createSDMap( 7.2f,8.1f,9.1f,10.2f,11.6f,13.2f,15.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)19 ),  createSDMap( 7.3f,8.2f,9.2f,10.4f,11.8f,13.5f,15.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)20 ),  createSDMap( 7.5f,8.4f,9.4f,10.6f,12.1f,13.7f,15.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)21 ),  createSDMap( 7.6f,8.6f,9.6f,10.9f,12.3f,14.0f,16.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)22 ),  createSDMap( 7.8f,8.7f,9.8f,11.1f,12.5f,14.3f,16.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)23 ),  createSDMap( 7.9f,8.9f,10.0f,11.3f,12.8f,14.6f,16.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)24 ),  createSDMap( 8.1f,9.0f,10.2f,11.5f,13.0f,14.8f,17.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)25 ),  createSDMap( 8.2f,9.2f,10.3f,11.7f,13.3f,15.1f,17.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)26 ),  createSDMap( 8.4f,9.4f,10.5f,11.9f,13.5f,15.4f,17.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)27 ),  createSDMap( 8.5f,9.5f,10.7f,12.1f,13.7f,15.7f,18.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)28 ),  createSDMap( 8.6f,9.7f,10.9f,12.3f,14.0f,16.0f,18.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)29 ),  createSDMap( 8.8f,9.8f,11.1f,12.5f,14.2f,16.2f,18.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)30 ),  createSDMap( 8.9f,10.0f,11.2f,12.7f,14.4f,16.5f,19.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)31 ),  createSDMap( 9.0f,10.1f,11.4f,12.9f,14.7f,16.8f,19.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)32 ),  createSDMap( 9.1f,10.3f,11.6f,13.1f,14.9f,17.1f,19.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)33 ),  createSDMap( 9.3f,10.4f,11.7f,13.3f,15.1f,17.3f,20.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)34 ),  createSDMap( 9.4f,10.5f,11.9f,13.5f,15.4f,17.6f,20.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)35 ),  createSDMap( 9.5f,10.7f,12.0f,13.7f,15.6f,17.9f,20.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)36 ),  createSDMap( 9.6f,10.8f,12.2f,13.9f,15.8f,18.1f,20.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)37 ),  createSDMap( 9.7f,10.9f,12.4f,14.0f,16.0f,18.4f,21.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)38 ),  createSDMap( 9.8f,11.1f,12.5f,14.2f,16.3f,18.7f,21.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)39 ),  createSDMap( 9.9f,11.2f,12.7f,14.4f,16.5f,19.0f,22.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)40 ),  createSDMap( 10.1f,11.3f,12.8f,14.6f,16.7f,19.2f,22.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)41 ),  createSDMap( 10.2f,11.5f,13.0f,14.8f,16.9f,19.5f,22.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)42 ),  createSDMap( 10.3f,11.6f,13.1f,15.0f,17.2f,19.8f,23.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)43 ),  createSDMap( 10.4f,11.7f,13.3f,15.2f,17.4f,20.1f,23.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)44 ),  createSDMap( 10.5f,11.8f,13.4f,15.3f,17.6f,20.4f,23.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)45 ),  createSDMap( 10.6f,12.0f,13.6f,15.5f,17.8f,20.7f,24.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)46 ),  createSDMap( 10.7f,12.1f,13.7f,15.7f,18.1f,20.9f,24.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)47 ),  createSDMap( 10.8f,12.2f,13.9f,15.9f,18.3f,21.2f,24.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)48 ),  createSDMap( 10.9f,12.3f,14.0f,16.1f,18.5f,21.5f,25.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)49 ),  createSDMap( 11.0f,12.4f,14.2f,16.3f,18.8f,21.8f,25.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)50 ),  createSDMap( 11.1f,12.6f,14.3f,16.4f,19.0f,22.1f,25.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)51 ),  createSDMap( 11.2f,12.7f,14.5f,16.6f,19.2f,22.4f,26.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)52 ),  createSDMap( 11.3f,12.8f,14.6f,16.8f,19.4f,22.6f,26.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)53 ),  createSDMap( 11.4f,12.9f,14.8f,17.0f,19.7f,22.9f,27.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)54 ),  createSDMap( 11.5f,13.0f,14.9f,17.2f,19.9f,23.2f,27.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)55 ),  createSDMap( 11.6f,13.2f,15.1f,17.3f,20.1f,23.5f,27.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)56 ),  createSDMap( 11.7f,13.3f,15.2f,17.5f,20.3f,23.8f,28.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)57 ),  createSDMap( 11.8f,13.4f,15.3f,17.7f,20.6f,24.1f,28.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)58 ),  createSDMap( 11.9f,13.5f,15.5f,17.9f,20.8f,24.4f,28.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)59 ),  createSDMap( 12.0f,13.6f,15.6f,18.0f,21.0f,24.6f,29.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)60 ),  createSDMap( 12.1f,13.7f,15.8f,18.2f,21.2f,24.9f,29.5f ) );

        return zscoreMap;
    }

    public static Map<ZScoreTableKey, Map<Float, Integer>> getZscoreTableBoy()
    {
        Map<ZScoreTableKey, Map<Float, Integer>> zscoreMap = new HashMap<>();

        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)0 ),  createSDMap( 2.1f,2.5f,2.9f,3.3f,3.9f,4.4f,5.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)1 ),  createSDMap( 2.9f,3.4f,3.9f,4.5f,5.1f,5.8f,6.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)2 ),  createSDMap( 3.8f,4.3f,4.9f,5.6f,6.3f,7.1f,8.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)3 ),  createSDMap( 4.4f,5.0f,5.7f,6.4f,7.2f,8.0f,9.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)4 ),  createSDMap( 4.9f,5.6f,6.2f,7.0f,7.8f,8.7f,9.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)5 ),  createSDMap( 5.3f,6.0f,6.7f,7.5f,8.4f,9.3f,10.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)6 ),  createSDMap( 5.7f,6.4f,7.1f,7.9f,8.8f,9.8f,10.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)7 ),  createSDMap( 5.9f,6.7f,7.4f,8.3f,9.2f,10.3f,11.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)8 ),  createSDMap( 6.2f,6.9f,7.7f,8.6f,9.6f,10.7f,11.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)9 ),  createSDMap( 6.4f,7.1f,8.0f,8.9f,9.9f,11.0f,12.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)10 ),  createSDMap( 6.6f,7.4f,8.2f,9.2f,10.2f,11.4f,12.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)11 ),  createSDMap( 6.8f,7.6f,8.4f,9.4f,10.5f,11.7f,13.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)12 ),  createSDMap( 6.9f,7.7f,8.6f,9.6f,10.8f,12.0f,13.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)13 ),  createSDMap( 7.1f,7.9f,8.8f,9.9f,11.0f,12.3f,13.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)14 ),  createSDMap( 7.2f,8.1f,9.0f,10.1f,11.3f,12.6f,14.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)15 ),  createSDMap( 7.4f,8.3f,9.2f,10.3f,11.5f,12.8f,14.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)16 ),  createSDMap( 7.5f,8.4f,9.4f,10.5f,11.7f,13.1f,14.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)17 ),  createSDMap( 7.7f,8.6f,9.6f,10.7f,12.0f,13.4f,14.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)18 ),  createSDMap( 7.8f,8.8f,9.8f,10.9f,12.2f,13.7f,15.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)19 ),  createSDMap( 8.0f,8.9f,10.0f,11.1f,12.5f,13.9f,15.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)20 ),  createSDMap( 8.1f,9.1f,10.1f,11.3f,12.7f,14.2f,15.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)21 ),  createSDMap( 8.2f,9.2f,10.3f,11.5f,12.9f,14.5f,16.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)22 ),  createSDMap( 8.4f,9.4f,10.5f,11.8f,13.2f,14.7f,16.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)23 ),  createSDMap( 8.5f,9.5f,10.7f,12.0f,13.4f,15.0f,16.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)24 ),  createSDMap( 8.6f,9.7f,10.8f,12.2f,13.6f,15.3f,17.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)25 ),  createSDMap( 8.8f,9.8f,11.0f,12.4f,13.9f,15.5f,17.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)26 ),  createSDMap( 8.9f,10.0f,11.2f,12.5f,14.1f,15.8f,17.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)27 ),  createSDMap( 9.0f,10.1f,11.3f,12.7f,14.3f,16.1f,18.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)28 ),  createSDMap( 9.1f,10.2f,11.5f,12.9f,14.5f,16.3f,18.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)29 ),  createSDMap( 9.2f,10.4f,11.7f,13.1f,14.8f,16.6f,18.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)30 ),  createSDMap( 9.4f,10.5f,11.8f,13.3f,15.0f,16.9f,19.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)31 ),  createSDMap( 9.5f,10.7f,12.0f,13.5f,15.2f,17.1f,19.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)32 ),  createSDMap( 9.6f,10.8f,12.1f,13.7f,15.4f,17.4f,19.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)33 ),  createSDMap( 9.7f,10.9f,12.3f,13.8f,15.6f,17.6f,19.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)34 ),  createSDMap( 9.8f,11.0f,12.4f,14.0f,15.8f,17.8f,20.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)35 ),  createSDMap( 9.9f,11.2f,12.6f,14.2f,16.0f,18.1f,20.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)36 ),  createSDMap( 10.0f,11.3f,12.7f,14.3f,16.2f,18.3f,20.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)37 ),  createSDMap( 10.1f,11.4f,12.9f,14.5f,16.4f,18.6f,21.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)38 ),  createSDMap( 10.2f,11.5f,13.0f,14.7f,16.6f,18.8f,21.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)39 ),  createSDMap( 10.3f,11.6f,13.1f,14.8f,16.8f,19.0f,21.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)40 ),  createSDMap( 10.4f,11.8f,13.3f,15.0f,17.0f,19.3f,21.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)41 ),  createSDMap( 10.5f,11.9f,13.4f,15.2f,17.2f,19.5f,22.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)42 ),  createSDMap( 10.6f,12.0f,13.6f,15.3f,17.4f,19.7f,22.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)43 ),  createSDMap( 10.7f,12.1f,13.7f,15.5f,17.6f,20.0f,22.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)44 ),  createSDMap( 10.8f,12.2f,13.8f,15.7f,17.8f,20.2f,23.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)45 ),  createSDMap( 10.9f,12.4f,14.0f,15.8f,18.0f,20.5f,23.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)46 ),  createSDMap( 11.0f,12.5f,14.1f,16.0f,18.2f,20.7f,23.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)47 ),  createSDMap( 11.1f,12.6f,14.3f,16.2f,18.4f,20.9f,23.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)48 ),  createSDMap( 11.2f,12.7f,14.4f,16.3f,18.6f,21.2f,24.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)49 ),  createSDMap( 11.3f,12.8f,14.5f,16.5f,18.8f,21.4f,24.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)50 ),  createSDMap( 11.4f,12.9f,14.7f,16.7f,19.0f,21.7f,24.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)51 ),  createSDMap( 11.5f,13.1f,14.8f,16.8f,19.2f,21.9f,25.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)52 ),  createSDMap( 11.6f,13.2f,15.0f,17.0f,19.4f,22.2f,25.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)53 ),  createSDMap( 11.7f,13.3f,15.1f,17.2f,19.6f,22.4f,25.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)54 ),  createSDMap( 11.8f,13.4f,15.2f,17.3f,19.8f,22.7f,26.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)55 ),  createSDMap( 11.9f,13.5f,15.4f,17.5f,20.0f,22.9f,26.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)56 ),  createSDMap( 12.0f,13.6f,15.5f,17.7f,20.2f,23.2f,26.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)57 ),  createSDMap( 12.1f,13.7f,15.6f,17.8f,20.4f,23.4f,26.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)58 ),  createSDMap( 12.2f,13.8f,15.8f,18.0f,20.6f,23.7f,27.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)59 ),  createSDMap( 12.3f,14.0f,15.9f,18.2f,20.8f,23.9f,27.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)60 ),  createSDMap( 12.4f,14.1f,16.0f,18.3f,21.0f,24.2f,27.9f ) );

        return zscoreMap;
    }

    private static Map<Float, Integer> createSDMap( Float SD3neg, Float SD2neg, Float	SD1neg, Float SD0, Float SD1, Float	SD2, Float SD3 )
    {
        Map<Float, Integer> sdMap = new HashMap<>();

        sdMap.put( SD3neg, 3 );
        sdMap.put( SD2neg, 2 );
        sdMap.put( SD1neg, 1 );
        sdMap.put( SD0, 0 );
        sdMap.put( SD1, 1 );
        sdMap.put( SD2, 2 );
        sdMap.put( SD3, 3 );

        return sdMap;
    }
}
