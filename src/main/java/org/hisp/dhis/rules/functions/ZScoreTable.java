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
    public static Map<ZScoreTableKey, Map<Float, Integer>> getZscoreWFATableGirl()
    {
        Map<ZScoreTableKey, Map<Float, Integer>> zscoreMap = new HashMap<>();

        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)0 ),  createSDMap( 2.0f,2.4f,2.8f,3.2f,3.7f,4.2f,4.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)1 ),  createSDMap( 2.7f,3.2f,3.6f,4.2f,4.8f,5.5f,6.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)2 ),  createSDMap( 3.4f,3.9f,4.5f,5.1f,5.8f,6.6f,7.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)3 ),  createSDMap( 4.0f,4.5f,5.2f,5.8f,6.6f,7.5f,8.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)4 ),  createSDMap( 4.4f,5.0f,5.7f,6.4f,7.3f,8.2f,9.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)5 ),  createSDMap( 4.8f,5.4f,6.1f,6.9f,7.8f,8.8f,10.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)6 ),  createSDMap( 5.1f,5.7f,6.5f,7.3f,8.2f,9.3f,10.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)7 ),  createSDMap( 5.3f,6.0f,6.8f,7.6f,8.6f,9.8f,11.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)8 ),  createSDMap( 5.6f,6.3f,7.0f,7.9f,9.0f,10.2f,11.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)9 ),  createSDMap( 5.8f,6.5f,7.3f,8.2f,9.3f,10.5f,12.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)10 ),  createSDMap( 5.9f,6.7f,7.5f,8.5f,9.6f,10.9f,12.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)11 ),  createSDMap( 6.1f,6.9f,7.7f,8.7f,9.9f,11.2f,12.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)12 ),  createSDMap( 6.3f,7.0f,7.9f,8.9f,10.1f,11.5f,13.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)13 ),  createSDMap( 6.4f,7.2f,8.1f,9.2f,10.4f,11.8f,13.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)14 ),  createSDMap( 6.6f,7.4f,8.3f,9.4f,10.6f,12.1f,13.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)15 ),  createSDMap( 6.7f,7.6f,8.5f,9.6f,10.9f,12.4f,14.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)16 ),  createSDMap( 6.9f,7.7f,8.7f,9.8f,11.1f,12.6f,14.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)17 ),  createSDMap( 7.0f,7.9f,8.9f,10.0f,11.4f,12.9f,14.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)18 ),  createSDMap( 7.2f,8.1f,9.1f,10.2f,11.6f,13.2f,15.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)19 ),  createSDMap( 7.3f,8.2f,9.2f,10.4f,11.8f,13.5f,15.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)20 ),  createSDMap( 7.5f,8.4f,9.4f,10.6f,12.1f,13.7f,15.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)21 ),  createSDMap( 7.6f,8.6f,9.6f,10.9f,12.3f,14.0f,16.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)22 ),  createSDMap( 7.8f,8.7f,9.8f,11.1f,12.5f,14.3f,16.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)23 ),  createSDMap( 7.9f,8.9f,10.0f,11.3f,12.8f,14.6f,16.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)24 ),  createSDMap( 8.1f,9.0f,10.2f,11.5f,13.0f,14.8f,17.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)25 ),  createSDMap( 8.2f,9.2f,10.3f,11.7f,13.3f,15.1f,17.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)26 ),  createSDMap( 8.4f,9.4f,10.5f,11.9f,13.5f,15.4f,17.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)27 ),  createSDMap( 8.5f,9.5f,10.7f,12.1f,13.7f,15.7f,18.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)28 ),  createSDMap( 8.6f,9.7f,10.9f,12.3f,14.0f,16.0f,18.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)29 ),  createSDMap( 8.8f,9.8f,11.1f,12.5f,14.2f,16.2f,18.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)30 ),  createSDMap( 8.9f,10.0f,11.2f,12.7f,14.4f,16.5f,19.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)31 ),  createSDMap( 9.0f,10.1f,11.4f,12.9f,14.7f,16.8f,19.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)32 ),  createSDMap( 9.1f,10.3f,11.6f,13.1f,14.9f,17.1f,19.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)33 ),  createSDMap( 9.3f,10.4f,11.7f,13.3f,15.1f,17.3f,20.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)34 ),  createSDMap( 9.4f,10.5f,11.9f,13.5f,15.4f,17.6f,20.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)35 ),  createSDMap( 9.5f,10.7f,12.0f,13.7f,15.6f,17.9f,20.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)36 ),  createSDMap( 9.6f,10.8f,12.2f,13.9f,15.8f,18.1f,20.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)37 ),  createSDMap( 9.7f,10.9f,12.4f,14.0f,16.0f,18.4f,21.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)38 ),  createSDMap( 9.8f,11.1f,12.5f,14.2f,16.3f,18.7f,21.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)39 ),  createSDMap( 9.9f,11.2f,12.7f,14.4f,16.5f,19.0f,22.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)40 ),  createSDMap( 10.1f,11.3f,12.8f,14.6f,16.7f,19.2f,22.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)41 ),  createSDMap( 10.2f,11.5f,13.0f,14.8f,16.9f,19.5f,22.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)42 ),  createSDMap( 10.3f,11.6f,13.1f,15.0f,17.2f,19.8f,23.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)43 ),  createSDMap( 10.4f,11.7f,13.3f,15.2f,17.4f,20.1f,23.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)44 ),  createSDMap( 10.5f,11.8f,13.4f,15.3f,17.6f,20.4f,23.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)45 ),  createSDMap( 10.6f,12.0f,13.6f,15.5f,17.8f,20.7f,24.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)46 ),  createSDMap( 10.7f,12.1f,13.7f,15.7f,18.1f,20.9f,24.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)47 ),  createSDMap( 10.8f,12.2f,13.9f,15.9f,18.3f,21.2f,24.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)48 ),  createSDMap( 10.9f,12.3f,14.0f,16.1f,18.5f,21.5f,25.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)49 ),  createSDMap( 11.0f,12.4f,14.2f,16.3f,18.8f,21.8f,25.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)50 ),  createSDMap( 11.1f,12.6f,14.3f,16.4f,19.0f,22.1f,25.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)51 ),  createSDMap( 11.2f,12.7f,14.5f,16.6f,19.2f,22.4f,26.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)52 ),  createSDMap( 11.3f,12.8f,14.6f,16.8f,19.4f,22.6f,26.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)53 ),  createSDMap( 11.4f,12.9f,14.8f,17.0f,19.7f,22.9f,27.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)54 ),  createSDMap( 11.5f,13.0f,14.9f,17.2f,19.9f,23.2f,27.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)55 ),  createSDMap( 11.6f,13.2f,15.1f,17.3f,20.1f,23.5f,27.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)56 ),  createSDMap( 11.7f,13.3f,15.2f,17.5f,20.3f,23.8f,28.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)57 ),  createSDMap( 11.8f,13.4f,15.3f,17.7f,20.6f,24.1f,28.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)58 ),  createSDMap( 11.9f,13.5f,15.5f,17.9f,20.8f,24.4f,28.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)59 ),  createSDMap( 12.0f,13.6f,15.6f,18.0f,21.0f,24.6f,29.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)60 ),  createSDMap( 12.1f,13.7f,15.8f,18.2f,21.2f,24.9f,29.5f ) );

        return zscoreMap;
    }

    public static Map<ZScoreTableKey, Map<Float, Integer>> getZscoreWFATableBoy()
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

    public static Map<ZScoreTableKey, Map<Float, Integer>> getZscoreHFATableGirl()
    {
        Map<ZScoreTableKey, Map<Float, Integer>> zscoreMap = new HashMap<>();

        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)24 ), createSDMap( 3.2267f,76.0f,79.3f,82.5f,85.7f,88.9f,92.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)25 ), createSDMap( 3.2783f,76.8f,80.0f,83.3f,86.6f,89.9f,93.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)26 ), createSDMap( 3.3300f,77.5f,80.8f,84.1f,87.4f,90.8f,94.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)27 ), createSDMap( 3.3812f,78.1f,81.5f,84.9f,88.3f,91.7f,95.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)28 ), createSDMap( 3.4313f,78.8f,82.2f,85.7f,89.1f,92.5f,96.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)29 ), createSDMap( 3.4809f,79.5f,82.9f,86.4f,89.9f,93.4f,96.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)30 ), createSDMap( 3.5302f,80.1f,83.6f,87.1f,90.7f,94.2f,97.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)31 ), createSDMap( 3.5782f,80.7f,84.3f,87.9f,91.4f,95.0f,98.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)32 ), createSDMap( 3.6259f,81.3f,84.9f,88.6f,92.2f,95.8f,99.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)33 ), createSDMap( 3.6724f,81.9f,85.6f,89.3f,92.9f,96.6f,100.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)34 ), createSDMap( 3.7186f,82.5f,86.2f,89.9f,93.6f,97.4f,101.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)35 ), createSDMap( 3.7638f,83.1f,86.8f,90.6f,94.4f,98.1f,101.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)36 ), createSDMap( 3.8078f,83.6f,87.4f,91.2f,95.1f,98.9f,102.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)37 ), createSDMap( 3.8526f,84.2f,88.0f,91.9f,95.7f,99.6f,103.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)38 ), createSDMap( 3.8963f,84.7f,88.6f,92.5f,96.4f,100.3f,104.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)39 ), createSDMap( 3.9389f,85.3f,89.2f,93.1f,97.1f,101.0f,105.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)40 ), createSDMap( 3.9813f,85.8f,89.8f,93.8f,97.7f,101.7f,105.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)41 ), createSDMap( 4.0236f,86.3f,90.4f,94.4f,98.4f,102.4f,106.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)42 ), createSDMap( 4.0658f,86.8f,90.9f,95.0f,99.0f,103.1f,107.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)43 ), createSDMap( 4.1068f,87.4f,91.5f,95.6f,99.7f,103.8f,107.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)44 ), createSDMap( 4.1476f,87.9f,92.0f,96.2f,100.3f,104.5f,108.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)45 ), createSDMap( 4.1883f,88.4f,92.5f,96.7f,100.9f,105.1f,109.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)46 ), createSDMap( 4.2279f,88.9f,93.1f,97.3f,101.5f,105.8f,110.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)47 ), createSDMap( 4.2683f,89.3f,93.6f,97.9f,102.1f,106.4f,110.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)48 ), createSDMap( 4.3075f,89.8f,94.1f,98.4f,102.7f,107.0f,111.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)49 ), createSDMap( 4.3456f,90.3f,94.6f,99.0f,103.3f,107.7f,112.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)50 ), createSDMap( 4.3847f,90.7f,95.1f,99.5f,103.9f,108.3f,112.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)51 ), createSDMap( 4.4226f,91.2f,95.6f,100.1f,104.5f,108.9f,113.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)52 ), createSDMap( 4.4604f,91.7f,96.1f,100.6f,105.0f,109.5f,114.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)53 ), createSDMap( 4.4981f,92.1f,96.6f,101.1f,105.6f,110.1f,114.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)54 ), createSDMap( 4.5358f,92.6f,97.1f,101.6f,106.2f,110.7f,115.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)55 ), createSDMap( 4.5734f,93.0f,97.6f,102.2f,106.7f,111.3f,115.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)56 ), createSDMap( 4.6108f,93.4f,98.1f,102.7f,107.3f,111.9f,116.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)57 ), createSDMap( 4.6472f,93.9f,98.5f,103.2f,107.8f,112.5f,117.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)58 ), createSDMap( 4.6834f,94.3f,99.0f,103.7f,108.4f,113.0f,117.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)59 ), createSDMap( 4.7195f,94.7f,99.5f,104.2f,108.9f,113.6f,118.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)60 ), createSDMap( 4.7566f,95.2f,99.9f,104.7f,109.4f,114.2f,118.9f ) );


        return zscoreMap;
    }

    public static Map<ZScoreTableKey, Map<Float, Integer>> getZscoreHFATableBoy()
    {
        Map<ZScoreTableKey, Map<Float, Integer>> zscoreMap = new HashMap<>();

        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)24 ), createSDMap( 3.0551f,78.0f,81.0f,84.1f,87.1f,90.2f,93.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)25 ), createSDMap( 3.1160f,78.6f,81.7f,84.9f,88.0f,91.1f,94.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)26 ), createSDMap( 3.1757f,79.3f,82.5f,85.6f,88.8f,92.0f,95.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)27 ), createSDMap( 3.2353f,79.9f,83.1f,86.4f,89.6f,92.9f,96.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)28 ), createSDMap( 3.2928f,80.5f,83.8f,87.1f,90.4f,93.7f,97.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)29 ), createSDMap( 3.3501f,81.1f,84.5f,87.8f,91.2f,94.5f,97.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)30 ), createSDMap( 3.4052f,81.7f,85.1f,88.5f,91.9f,95.3f,98.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)31 ), createSDMap( 3.4591f,82.3f,85.7f,89.2f,92.7f,96.1f,99.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)32 ), createSDMap( 3.5118f,82.8f,86.4f,89.9f,93.4f,96.9f,100.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)33 ), createSDMap( 3.5625f,83.4f,86.9f,90.5f,94.1f,97.6f,101.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)34 ), createSDMap( 3.6120f,83.9f,87.5f,91.1f,94.8f,98.4f,102.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)35 ), createSDMap( 3.6604f,84.4f,88.1f,91.8f,95.4f,99.1f,102.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)36 ), createSDMap( 3.7069f,85.0f,88.7f,92.4f,96.1f,99.8f,103.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)37 ), createSDMap( 3.7523f,85.5f,89.2f,93.0f,96.7f,100.5f,104.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)38 ), createSDMap( 3.7976f,86.0f,89.8f,93.6f,97.4f,101.2f,105.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)39 ), createSDMap( 3.8409f,86.5f,90.3f,94.2f,98.0f,101.8f,105.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)40 ), createSDMap( 3.8831f,87.0f,90.9f,94.7f,98.6f,102.5f,106.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)41 ), createSDMap( 3.9242f,87.5f,91.4f,95.3f,99.2f,103.2f,107.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)42 ), createSDMap( 3.9651f,88.0f,91.9f,95.9f,99.9f,103.8f,107.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)43 ), createSDMap( 4.0039f,88.4f,92.4f,96.4f,100.4f,104.5f,108.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)44 ), createSDMap( 4.0435f,88.9f,93.0f,97.0f,101.0f,105.1f,109.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)45 ), createSDMap( 4.0810f,89.4f,93.5f,97.5f,101.6f,105.7f,109.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)46 ), createSDMap( 4.1194f,89.8f,94.0f,98.1f,102.2f,106.3f,110.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)47 ), createSDMap( 4.1567f,90.3f,94.4f,98.6f,102.8f,106.9f,111.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)48 ), createSDMap( 4.1941f,90.7f,94.9f,99.1f,103.3f,107.5f,111.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)49 ), createSDMap( 4.2314f,91.2f,95.4f,99.7f,103.9f,108.1f,112.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)50 ), createSDMap( 4.2677f,91.6f,95.9f,100.2f,104.4f,108.7f,113.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)51 ), createSDMap( 4.3052f,92.1f,96.4f,100.7f,105.0f,109.3f,113.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)52 ), createSDMap( 4.3417f,92.5f,96.9f,101.2f,105.6f,109.9f,114.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)53 ), createSDMap( 4.3783f,93.0f,97.4f,101.7f,106.1f,110.5f,114.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)54 ), createSDMap( 4.4149f,93.4f,97.8f,102.3f,106.7f,111.1f,115.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)55 ), createSDMap( 4.4517f,93.9f,98.3f,102.8f,107.2f,111.7f,116.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)56 ), createSDMap( 4.4886f,94.3f,98.8f,103.3f,107.8f,112.3f,116.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)57 ), createSDMap( 4.5245f,94.7f,99.3f,103.8f,108.3f,112.8f,117.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)58 ), createSDMap( 4.5616f,95.2f,99.7f,104.3f,108.9f,113.4f,118.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)59 ), createSDMap( 4.5977f,95.6f,100.2f,104.8f,109.4f,114.0f,118.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)60 ), createSDMap( 4.6339f,96.1f,100.7f,105.3f,110.0f,114.6f,119.2f ) );

        return zscoreMap;
    }

    public static Map<ZScoreTableKey, Map<Float, Integer>> getZscoreWFHTableGirl()
    {
        Map<ZScoreTableKey, Map<Float, Integer>> zscoreMap = new HashMap<>();

        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)65 ), createSDMap( 5.6f,6.1f,6.6f,7.2f,7.9f,8.7f,9.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)65.5 ), createSDMap( 5.7f,6.2f,6.7f,7.4f,8.1f,8.9f,9.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)66 ), createSDMap( 5.8f,6.3f,6.8f,7.5f,8.2f,9.0f,10.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)66.5 ), createSDMap( 5.8f,6.4f,6.9f,7.6f,8.3f,9.1f,10.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)67 ), createSDMap( 5.9f,6.4f,7.0f,7.7f,8.4f,9.3f,10.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)67.5 ), createSDMap( 6.0f,6.5f,7.1f,7.8f,8.5f,9.4f,10.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)68 ), createSDMap( 6.1f,6.6f,7.2f,7.9f,8.7f,9.5f,10.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)68.5 ), createSDMap( 6.2f,6.7f,7.3f,8.0f,8.8f,9.7f,10.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)69 ), createSDMap( 6.3f,6.8f,7.4f,8.1f,8.9f,9.8f,10.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)69.5 ), createSDMap( 6.3f,6.9f,7.5f,8.2f,9.0f,9.9f,10.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)70 ), createSDMap( 6.4f,7.0f,7.6f,8.3f,9.1f,10.0f,11.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)70.5 ), createSDMap( 6.5f,7.1f,7.7f,8.4f,9.2f,10.1f,11.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)71 ), createSDMap( 6.6f,7.1f,7.8f,8.5f,9.3f,10.3f,11.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)71.5 ), createSDMap( 6.7f,7.2f,7.9f,8.6f,9.4f,10.4f,11.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)72 ), createSDMap( 6.7f,7.3f,8.0f,8.7f,9.5f,10.5f,11.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)72.5 ), createSDMap( 6.8f,7.4f,8.1f,8.8f,9.7f,10.6f,11.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)73 ), createSDMap( 6.9f,7.5f,8.1f,8.9f,9.8f,10.7f,11.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)73.5 ), createSDMap( 7.0f,7.6f,8.2f,9.0f,9.9f,10.8f,12.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)74 ), createSDMap( 7.0f,7.6f,8.3f,9.1f,10.0f,11.0f,12.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)74.5 ), createSDMap( 7.1f,7.7f,8.4f,9.2f,10.1f,11.1f,12.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)75 ), createSDMap( 7.2f,7.8f,8.5f,9.3f,10.2f,11.2f,12.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)75.5 ), createSDMap( 7.2f,7.9f,8.6f,9.4f,10.3f,11.3f,12.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)76 ), createSDMap( 7.3f,8.0f,8.7f,9.5f,10.4f,11.4f,12.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)76.5 ), createSDMap( 7.4f,8.0f,8.7f,9.6f,10.5f,11.5f,12.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)77 ), createSDMap( 7.5f,8.1f,8.8f,9.6f,10.6f,11.6f,12.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)77.5 ), createSDMap( 7.5f,8.2f,8.9f,9.7f,10.7f,11.7f,12.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)78 ), createSDMap( 7.6f,8.3f,9.0f,9.8f,10.8f,11.8f,13.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)78.5 ), createSDMap( 7.7f,8.4f,9.1f,9.9f,10.9f,12.0f,13.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)79 ), createSDMap( 7.8f,8.4f,9.2f,10.0f,11.0f,12.1f,13.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)79.5 ), createSDMap( 7.8f,8.5f,9.3f,10.1f,11.1f,12.2f,13.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)80 ), createSDMap( 7.9f,8.6f,9.4f,10.2f,11.2f,12.3f,13.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)80.5 ), createSDMap( 8.0f,8.7f,9.5f,10.3f,11.3f,12.4f,13.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)81 ), createSDMap( 8.1f,8.8f,9.6f,10.4f,11.4f,12.6f,13.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)81.5 ), createSDMap( 8.2f,8.9f,9.7f,10.6f,11.6f,12.7f,14.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)82 ), createSDMap( 8.3f,9.0f,9.8f,10.7f,11.7f,12.8f,14.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)82.5 ), createSDMap( 8.4f,9.1f,9.9f,10.8f,11.8f,13.0f,14.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)83 ), createSDMap( 8.5f,9.2f,10.0f,10.9f,11.9f,13.1f,14.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)83.5 ), createSDMap( 8.5f,9.3f,10.1f,11.0f,12.1f,13.3f,14.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)84 ), createSDMap( 8.6f,9.4f,10.2f,11.1f,12.2f,13.4f,14.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)84.5 ), createSDMap( 8.7f,9.5f,10.3f,11.3f,12.3f,13.5f,14.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)85 ), createSDMap( 8.8f,9.6f,10.4f,11.4f,12.5f,13.7f,15.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)85.5 ), createSDMap( 8.9f,9.7f,10.6f,11.5f,12.6f,13.8f,15.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)86 ), createSDMap( 9.0f,9.8f,10.7f,11.6f,12.7f,14.0f,15.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)86.5 ), createSDMap( 9.1f,9.9f,10.8f,11.8f,12.9f,14.2f,15.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)87 ), createSDMap( 9.2f,10.0f,10.9f,11.9f,13.0f,14.3f,15.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)87.5 ), createSDMap( 9.3f,10.1f,11.0f,12.0f,13.2f,14.5f,15.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)88 ), createSDMap( 9.4f,10.2f,11.1f,12.1f,13.3f,14.6f,16.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)88.5 ), createSDMap( 9.5f,10.3f,11.2f,12.3f,13.4f,14.8f,16.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)89 ), createSDMap( 9.6f,10.4f,11.4f,12.4f,13.6f,14.9f,16.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)89.5 ), createSDMap( 9.7f,10.5f,11.5f,12.5f,13.7f,15.1f,16.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)90 ), createSDMap( 9.8f,10.6f,11.6f,12.6f,13.8f,15.2f,16.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)90.5 ), createSDMap( 9.9f,10.7f,11.7f,12.8f,14.0f,15.4f,16.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)91 ), createSDMap( 10.0f,10.9f,11.8f,12.9f,14.1f,15.5f,17.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)91.5 ), createSDMap( 10.1f,11.0f,11.9f,13.0f,14.3f,15.7f,17.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)92 ), createSDMap( 10.2f,11.1f,12.0f,13.1f,14.4f,15.8f,17.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)92.5 ), createSDMap( 10.3f,11.2f,12.1f,13.3f,14.5f,16.0f,17.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)93 ), createSDMap( 10.4f,11.3f,12.3f,13.4f,14.7f,16.1f,17.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)93.5 ), createSDMap( 10.5f,11.4f,12.4f,13.5f,14.8f,16.3f,17.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)94 ), createSDMap( 10.6f,11.5f,12.5f,13.6f,14.9f,16.4f,18.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)94.5 ), createSDMap( 10.7f,11.6f,12.6f,13.8f,15.1f,16.6f,18.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)95 ), createSDMap( 10.8f,11.7f,12.7f,13.9f,15.2f,16.7f,18.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)95.5 ), createSDMap( 10.8f,11.8f,12.8f,14.0f,15.4f,16.9f,18.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)96 ), createSDMap( 10.9f,11.9f,12.9f,14.1f,15.5f,17.0f,18.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)96.5 ), createSDMap( 11.0f,12.0f,13.1f,14.3f,15.6f,17.2f,19.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)97 ), createSDMap( 11.1f,12.1f,13.2f,14.4f,15.8f,17.4f,19.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)97.5 ), createSDMap( 11.2f,12.2f,13.3f,14.5f,15.9f,17.5f,19.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)98 ), createSDMap( 11.3f,12.3f,13.4f,14.7f,16.1f,17.7f,19.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)98.5 ), createSDMap( 11.4f,12.4f,13.5f,14.8f,16.2f,17.9f,19.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)99 ), createSDMap( 11.5f,12.5f,13.7f,14.9f,16.4f,18.0f,19.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)99.5 ), createSDMap( 11.6f,12.7f,13.8f,15.1f,16.5f,18.2f,20.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)100 ), createSDMap( 11.7f,12.8f,13.9f,15.2f,16.7f,18.4f,20.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)100.5 ), createSDMap( 11.9f,12.9f,14.1f,15.4f,16.9f,18.6f,20.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)101 ), createSDMap( 12.0f,13.0f,14.2f,15.5f,17.0f,18.7f,20.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)101.5 ), createSDMap( 12.1f,13.1f,14.3f,15.7f,17.2f,18.9f,20.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)102 ), createSDMap( 12.2f,13.3f,14.5f,15.8f,17.4f,19.1f,21.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)102.5 ), createSDMap( 12.3f,13.4f,14.6f,16.0f,17.5f,19.3f,21.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)103 ), createSDMap( 12.4f,13.5f,14.7f,16.1f,17.7f,19.5f,21.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)103.5 ), createSDMap( 12.5f,13.6f,14.9f,16.3f,17.9f,19.7f,21.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)104 ), createSDMap( 12.6f,13.8f,15.0f,16.4f,18.1f,19.9f,22.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)104.5 ), createSDMap( 12.8f,13.9f,15.2f,16.6f,18.2f,20.1f,22.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)105 ), createSDMap( 12.9f,14.0f,15.3f,16.8f,18.4f,20.3f,22.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)105.5 ), createSDMap( 13.0f,14.2f,15.5f,16.9f,18.6f,20.5f,22.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)106 ), createSDMap( 13.1f,14.3f,15.6f,17.1f,18.8f,20.8f,23.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)106.5 ), createSDMap( 13.3f,14.5f,15.8f,17.3f,19.0f,21.0f,23.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)107 ), createSDMap( 13.4f,14.6f,15.9f,17.5f,19.2f,21.2f,23.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)107.5 ), createSDMap( 13.5f,14.7f,16.1f,17.7f,19.4f,21.4f,23.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)108 ), createSDMap( 13.7f,14.9f,16.3f,17.8f,19.6f,21.7f,24.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)108.5 ), createSDMap( 13.8f,15.0f,16.4f,18.0f,19.8f,21.9f,24.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)109 ), createSDMap( 13.9f,15.2f,16.6f,18.2f,20.0f,22.1f,24.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)109.5 ), createSDMap( 14.1f,15.4f,16.8f,18.4f,20.3f,22.4f,24.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)110 ), createSDMap( 14.2f,15.5f,17.0f,18.6f,20.5f,22.6f,25.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)110.5 ), createSDMap( 14.4f,15.7f,17.1f,18.8f,20.7f,22.9f,25.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)111 ), createSDMap( 14.5f,15.8f,17.3f,19.0f,20.9f,23.1f,25.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)111.5 ), createSDMap( 14.7f,16.0f,17.5f,19.2f,21.2f,23.4f,26.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)112 ), createSDMap( 14.8f,16.2f,17.7f,19.4f,21.4f,23.6f,26.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)112.5 ), createSDMap( 15.0f,16.3f,17.9f,19.6f,21.6f,23.9f,26.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)113 ), createSDMap( 15.1f,16.5f,18.0f,19.8f,21.8f,24.2f,26.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)113.5 ), createSDMap( 15.3f,16.7f,18.2f,20.0f,22.1f,24.4f,27.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)114 ), createSDMap( 15.4f,16.8f,18.4f,20.2f,22.3f,24.7f,27.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)114.5 ), createSDMap( 15.6f,17.0f,18.6f,20.5f,22.6f,25.0f,27.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)115 ), createSDMap( 15.7f,17.2f,18.8f,20.7f,22.8f,25.2f,28.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)115.5 ), createSDMap( 15.9f,17.3f,19.0f,20.9f,23.0f,25.5f,28.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)116 ), createSDMap( 16.0f,17.5f,19.2f,21.1f,23.3f,25.8f,28.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)116.5 ), createSDMap( 16.2f,17.7f,19.4f,21.3f,23.5f,26.1f,29.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)117 ), createSDMap( 16.3f,17.8f,19.6f,21.5f,23.8f,26.3f,29.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)117.5 ), createSDMap( 16.5f,18.0f,19.8f,21.7f,24.0f,26.6f,29.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)118 ), createSDMap( 16.6f,18.2f,19.9f,22.0f,24.2f,26.9f,29.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)118.5 ), createSDMap( 16.8f,18.4f,20.1f,22.2f,24.5f,27.2f,30.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)119 ), createSDMap( 16.9f,18.5f,20.3f,22.4f,24.7f,27.4f,30.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)119.5 ), createSDMap( 17.1f,18.7f,20.5f,22.6f,25.0f,27.7f,30.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)120 ), createSDMap( 17.3f,18.9f,20.7f,22.8f,25.2f,28.0f,31.2f ) );

        return zscoreMap;
    }

    public static Map<ZScoreTableKey, Map<Float, Integer>> getZscoreWFHTableBoy()
    {
        Map<ZScoreTableKey, Map<Float, Integer>> zscoreMap = new HashMap<>();

        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)65 ), createSDMap( 5.9f,6.3f,6.9f,7.4f,8.1f,8.8f,9.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)65.5 ), createSDMap( 6.0f,6.4f,7.0f,7.6f,8.2f,8.9f,9.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)66 ), createSDMap( 6.1f,6.5f,7.1f,7.7f,8.3f,9.1f,9.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)66.5 ), createSDMap( 6.1f,6.6f,7.2f,7.8f,8.5f,9.2f,10.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)67 ), createSDMap( 6.2f,6.7f,7.3f,7.9f,8.6f,9.4f,10.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)67.5 ), createSDMap( 6.3f,6.8f,7.4f,8.0f,8.7f,9.5f,10.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)68 ), createSDMap( 6.4f,6.9f,7.5f,8.1f,8.8f,9.6f,10.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)68.5 ), createSDMap( 6.5f,7.0f,7.6f,8.2f,9.0f,9.8f,10.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)69 ), createSDMap( 6.6f,7.1f,7.7f,8.4f,9.1f,9.9f,10.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)69.5 ), createSDMap( 6.7f,7.2f,7.8f,8.5f,9.2f,10.0f,11.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)70 ), createSDMap( 6.8f,7.3f,7.9f,8.6f,9.3f,10.2f,11.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)70.5 ), createSDMap( 6.9f,7.4f,8.0f,8.7f,9.5f,10.3f,11.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)71 ), createSDMap( 6.9f,7.5f,8.1f,8.8f,9.6f,10.4f,11.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)71.5 ), createSDMap( 7.0f,7.6f,8.2f,8.9f,9.7f,10.6f,11.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)72 ), createSDMap( 7.1f,7.7f,8.3f,9.0f,9.8f,10.7f,11.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)72.5 ), createSDMap( 7.2f,7.8f,8.4f,9.1f,9.9f,10.8f,11.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)73 ), createSDMap( 7.3f,7.9f,8.5f,9.2f,10.0f,11.0f,12.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)73.5 ), createSDMap( 7.4f,7.9f,8.6f,9.3f,10.2f,11.1f,12.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)74 ), createSDMap( 7.4f,8.0f,8.7f,9.4f,10.3f,11.2f,12.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)74.5 ), createSDMap( 7.5f,8.1f,8.8f,9.5f,10.4f,11.3f,12.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)75 ), createSDMap( 7.6f,8.2f,8.9f,9.6f,10.5f,11.4f,12.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)75.5 ), createSDMap( 7.7f,8.3f,9.0f,9.7f,10.6f,11.6f,12.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)76 ), createSDMap( 7.7f,8.4f,9.1f,9.8f,10.7f,11.7f,12.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)76.5 ), createSDMap( 7.8f,8.5f,9.2f,9.9f,10.8f,11.8f,12.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)77 ), createSDMap( 7.9f,8.5f,9.2f,10.0f,10.9f,11.9f,13.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)77.5 ), createSDMap( 8.0f,8.6f,9.3f,10.1f,11.0f,12.0f,13.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)78 ), createSDMap( 8.0f,8.7f,9.4f,10.2f,11.1f,12.1f,13.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)78.5 ), createSDMap( 8.1f,8.8f,9.5f,10.3f,11.2f,12.2f,13.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)79 ), createSDMap( 8.2f,8.8f,9.6f,10.4f,11.3f,12.3f,13.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)79.5 ), createSDMap( 8.3f,8.9f,9.7f,10.5f,11.4f,12.4f,13.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)80 ), createSDMap( 8.3f,9.0f,9.7f,10.6f,11.5f,12.6f,13.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)80.5 ), createSDMap( 8.4f,9.1f,9.8f,10.7f,11.6f,12.7f,13.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)81 ), createSDMap( 8.5f,9.2f,9.9f,10.8f,11.7f,12.8f,14.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)81.5 ), createSDMap( 8.6f,9.3f,10.0f,10.9f,11.8f,12.9f,14.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)82 ), createSDMap( 8.7f,9.3f,10.1f,11.0f,11.9f,13.0f,14.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)82.5 ), createSDMap( 8.7f,9.4f,10.2f,11.1f,12.1f,13.1f,14.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)83 ), createSDMap( 8.8f,9.5f,10.3f,11.2f,12.2f,13.3f,14.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)83.5 ), createSDMap( 8.9f,9.6f,10.4f,11.3f,12.3f,13.4f,14.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)84 ), createSDMap( 9.0f,9.7f,10.5f,11.4f,12.4f,13.5f,14.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)84.5 ), createSDMap( 9.1f,9.9f,10.7f,11.5f,12.5f,13.7f,14.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)85 ), createSDMap( 9.2f,10.0f,10.8f,11.7f,12.7f,13.8f,15.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)85.5 ), createSDMap( 9.3f,10.1f,10.9f,11.8f,12.8f,13.9f,15.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)86 ), createSDMap( 9.4f,10.2f,11.0f,11.9f,12.9f,14.1f,15.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)86.5 ), createSDMap( 9.5f,10.3f,11.1f,12.0f,13.1f,14.2f,15.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)87 ), createSDMap( 9.6f,10.4f,11.2f,12.2f,13.2f,14.4f,15.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)87.5 ), createSDMap( 9.7f,10.5f,11.3f,12.3f,13.3f,14.5f,15.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)88 ), createSDMap( 9.8f,10.6f,11.5f,12.4f,13.5f,14.7f,16.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)88.5 ), createSDMap( 9.9f,10.7f,11.6f,12.5f,13.6f,14.8f,16.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)89 ), createSDMap( 10.0f,10.8f,11.7f,12.6f,13.7f,14.9f,16.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)89.5 ), createSDMap( 10.1f,10.9f,11.8f,12.8f,13.9f,15.1f,16.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)90 ), createSDMap( 10.2f,11.0f,11.9f,12.9f,14.0f,15.2f,16.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)90.5 ), createSDMap( 10.3f,11.1f,12.0f,13.0f,14.1f,15.3f,16.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)91 ), createSDMap( 10.4f,11.2f,12.1f,13.1f,14.2f,15.5f,16.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)91.5 ), createSDMap( 10.5f,11.3f,12.2f,13.2f,14.4f,15.6f,17.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)92 ), createSDMap( 10.6f,11.4f,12.3f,13.4f,14.5f,15.8f,17.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)92.5 ), createSDMap( 10.7f,11.5f,12.4f,13.5f,14.6f,15.9f,17.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)93 ), createSDMap( 10.8f,11.6f,12.6f,13.6f,14.7f,16.0f,17.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)93.5 ), createSDMap( 10.9f,11.7f,12.7f,13.7f,14.9f,16.2f,17.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)94 ), createSDMap( 11.0f,11.8f,12.8f,13.8f,15.0f,16.3f,17.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)94.5 ), createSDMap( 11.1f,11.9f,12.9f,13.9f,15.1f,16.5f,17.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)95 ), createSDMap( 11.1f,12.0f,13.0f,14.1f,15.3f,16.6f,18.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)95.5 ), createSDMap( 11.2f,12.1f,13.1f,14.2f,15.4f,16.7f,18.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)96 ), createSDMap( 11.3f,12.2f,13.2f,14.3f,15.5f,16.9f,18.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)96.5 ), createSDMap( 11.4f,12.3f,13.3f,14.4f,15.7f,17.0f,18.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)97 ), createSDMap( 11.5f,12.4f,13.4f,14.6f,15.8f,17.2f,18.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)97.5 ), createSDMap( 11.6f,12.5f,13.6f,14.7f,15.9f,17.4f,18.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)98 ), createSDMap( 11.7f,12.6f,13.7f,14.8f,16.1f,17.5f,19.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)98.5 ), createSDMap( 11.8f,12.8f,13.8f,14.9f,16.2f,17.7f,19.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)99 ), createSDMap( 11.9f,12.9f,13.9f,15.1f,16.4f,17.9f,19.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)99.5 ), createSDMap( 12.0f,13.0f,14.0f,15.2f,16.5f,18.0f,19.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)100 ), createSDMap( 12.1f,13.1f,14.2f,15.4f,16.7f,18.2f,19.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)100.5 ), createSDMap( 12.2f,13.2f,14.3f,15.5f,16.9f,18.4f,20.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)101 ), createSDMap( 12.3f,13.3f,14.4f,15.6f,17.0f,18.5f,20.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)101.5 ), createSDMap( 12.4f,13.4f,14.5f,15.8f,17.2f,18.7f,20.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)102 ), createSDMap( 12.5f,13.6f,14.7f,15.9f,17.3f,18.9f,20.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)102.5 ), createSDMap( 12.6f,13.7f,14.8f,16.1f,17.5f,19.1f,20.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)103 ), createSDMap( 12.8f,13.8f,14.9f,16.2f,17.7f,19.3f,21.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)103.5 ), createSDMap( 12.9f,13.9f,15.1f,16.4f,17.8f,19.5f,21.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)104 ), createSDMap( 13.0f,14.0f,15.2f,16.5f,18.0f,19.7f,21.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)104.5 ), createSDMap( 13.1f,14.2f,15.4f,16.7f,18.2f,19.9f,21.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)105 ), createSDMap( 13.2f,14.3f,15.5f,16.8f,18.4f,20.1f,22.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)105.5 ), createSDMap( 13.3f,14.4f,15.6f,17.0f,18.5f,20.3f,22.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)106 ), createSDMap( 13.4f,14.5f,15.8f,17.2f,18.7f,20.5f,22.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)106.5 ), createSDMap( 13.5f,14.7f,15.9f,17.3f,18.9f,20.7f,22.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)107 ), createSDMap( 13.7f,14.8f,16.1f,17.5f,19.1f,20.9f,22.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)107.5 ), createSDMap( 13.8f,14.9f,16.2f,17.7f,19.3f,21.1f,23.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)108 ), createSDMap( 13.9f,15.1f,16.4f,17.8f,19.5f,21.3f,23.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)108.5 ), createSDMap( 14.0f,15.2f,16.5f,18.0f,19.7f,21.5f,23.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)109 ), createSDMap( 14.1f,15.3f,16.7f,18.2f,19.8f,21.8f,23.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)109.5 ), createSDMap( 14.3f,15.5f,16.8f,18.3f,20.0f,22.0f,24.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)110 ), createSDMap( 14.4f,15.6f,17.0f,18.5f,20.2f,22.2f,24.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)110.5 ), createSDMap( 14.5f,15.8f,17.1f,18.7f,20.4f,22.4f,24.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)111 ), createSDMap( 14.6f,15.9f,17.3f,18.9f,20.7f,22.7f,25.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)111.5 ), createSDMap( 14.8f,16.0f,17.5f,19.1f,20.9f,22.9f,25.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)112 ), createSDMap( 14.9f,16.2f,17.6f,19.2f,21.1f,23.1f,25.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)112.5 ), createSDMap( 15.0f,16.3f,17.8f,19.4f,21.3f,23.4f,25.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)113 ), createSDMap( 15.2f,16.5f,18.0f,19.6f,21.5f,23.6f,26.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)113.5 ), createSDMap( 15.3f,16.6f,18.1f,19.8f,21.7f,23.9f,26.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)114 ), createSDMap( 15.4f,16.8f,18.3f,20.0f,21.9f,24.1f,26.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)114.5 ), createSDMap( 15.6f,16.9f,18.5f,20.2f,22.1f,24.4f,26.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)115 ), createSDMap( 15.7f,17.1f,18.6f,20.4f,22.4f,24.6f,27.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)115.5 ), createSDMap( 15.8f,17.2f,18.8f,20.6f,22.6f,24.9f,27.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)116 ), createSDMap( 16.0f,17.4f,19.0f,20.8f,22.8f,25.1f,27.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)116.5 ), createSDMap( 16.1f,17.5f,19.2f,21.0f,23.0f,25.4f,28.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)117 ), createSDMap( 16.2f,17.7f,19.3f,21.2f,23.3f,25.6f,28.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)117.5 ), createSDMap( 16.4f,17.9f,19.5f,21.4f,23.5f,25.9f,28.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)118 ), createSDMap( 16.5f,18.0f,19.7f,21.6f,23.7f,26.1f,28.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)118.5 ), createSDMap( 16.7f,18.2f,19.9f,21.8f,23.9f,26.4f,29.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)119 ), createSDMap( 16.8f,18.3f,20.0f,22.0f,24.1f,26.6f,29.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)119.5 ), createSDMap( 16.9f,18.5f,20.2f,22.2f,24.4f,26.9f,29.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)120 ), createSDMap( 17.1f,18.6f,20.4f,22.4f,24.6f,27.2f,30.1f ) );

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
