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

        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)0 ), createSDMap( 1.8627f,43.6f,45.4f,47.3f,49.1f,51.0f,52.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)1 ), createSDMap( 1.9542f,47.8f,49.8f,51.7f,53.7f,55.6f,57.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)2 ), createSDMap( 2.0362f,51.0f,53.0f,55.0f,57.1f,59.1f,61.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)3 ), createSDMap( 2.1051f,53.5f,55.6f,57.7f,59.8f,61.9f,64.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)4 ), createSDMap( 2.1645f,55.6f,57.8f,59.9f,62.1f,64.3f,66.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)5 ), createSDMap( 2.2174f,57.4f,59.6f,61.8f,64.0f,66.2f,68.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)6 ), createSDMap( 2.2664f,58.9f,61.2f,63.5f,65.7f,68.0f,70.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)7 ), createSDMap( 2.3154f,60.3f,62.7f,65.0f,67.3f,69.6f,71.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)8 ), createSDMap( 2.3650f,61.7f,64.0f,66.4f,68.7f,71.1f,73.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)9 ), createSDMap( 2.4157f,62.9f,65.3f,67.7f,70.1f,72.6f,75.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)10 ), createSDMap( 2.4676f,64.1f,66.5f,69.0f,71.5f,73.9f,76.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)11 ), createSDMap( 2.5208f,65.2f,67.7f,70.3f,72.8f,75.3f,77.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)12 ), createSDMap( 2.5750f,66.3f,68.9f,71.4f,74.0f,76.6f,79.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)13 ), createSDMap( 2.6296f,67.3f,70.0f,72.6f,75.2f,77.8f,80.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)14 ), createSDMap( 2.6841f,68.3f,71.0f,73.7f,76.4f,79.1f,81.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)15 ), createSDMap( 2.7392f,69.3f,72.0f,74.8f,77.5f,80.2f,83.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)16 ), createSDMap( 2.7944f,70.2f,73.0f,75.8f,78.6f,81.4f,84.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)17 ), createSDMap( 2.8490f,71.1f,74.0f,76.8f,79.7f,82.5f,85.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)18 ), createSDMap( 2.9039f,72.0f,74.9f,77.8f,80.7f,83.6f,86.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)19 ), createSDMap( 2.9582f,72.8f,75.8f,78.8f,81.7f,84.7f,87.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)20 ), createSDMap( 3.0129f,73.7f,76.7f,79.7f,82.7f,85.7f,88.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)21 ), createSDMap( 3.0672f,74.5f,77.5f,80.6f,83.7f,86.7f,89.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)22 ), createSDMap( 3.1202f,75.2f,78.4f,81.5f,84.6f,87.7f,90.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)23 ), createSDMap( 3.1737f,76.0f,79.2f,82.3f,85.5f,88.7f,91.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)24 ), createSDMap( 3.2267f,76.7f,80.0f,83.2f,86.4f,89.6f,92.9f ) );

        return zscoreMap;
    }

    public static Map<ZScoreTableKey, Map<Float, Integer>> getZscoreHFATableBoy()
    {
        Map<ZScoreTableKey, Map<Float, Integer>> zscoreMap = new HashMap<>();

        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)0 ), createSDMap( 1.8931f,44.2f,46.1f,48.0f,49.9f,51.8f,53.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)1 ), createSDMap( 1.9465f,48.9f,50.8f,52.8f,54.7f,56.7f,58.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)2 ), createSDMap( 2.0005f,52.4f,54.4f,56.4f,58.4f,60.4f,62.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)3 ), createSDMap( 2.0444f,55.3f,57.3f,59.4f,61.4f,63.5f,65.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)4 ), createSDMap( 2.0808f,57.6f,59.7f,61.8f,63.9f,66.0f,68.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)5 ), createSDMap( 2.1115f,59.6f,61.7f,63.8f,65.9f,68.0f,70.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)6 ), createSDMap( 2.1403f,61.2f,63.3f,65.5f,67.6f,69.8f,71.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)7 ), createSDMap( 2.1711f,62.7f,64.8f,67.0f,69.2f,71.3f,73.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)8 ), createSDMap( 2.2055f,64.0f,66.2f,68.4f,70.6f,72.8f,75.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)9 ), createSDMap( 2.2433f,65.2f,67.5f,69.7f,72.0f,74.2f,76.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)10 ), createSDMap( 2.2849f,66.4f,68.7f,71.0f,73.3f,75.6f,77.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)11 ), createSDMap( 2.3293f,67.6f,69.9f,72.2f,74.5f,76.9f,79.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)12 ), createSDMap( 2.3762f,68.6f,71.0f,73.4f,75.7f,78.1f,80.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)13 ), createSDMap( 2.4260f,69.6f,72.1f,74.5f,76.9f,79.3f,81.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)14 ), createSDMap( 2.4773f,70.6f,73.1f,75.6f,78.0f,80.5f,83.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)15 ), createSDMap( 2.5303f,71.6f,74.1f,76.6f,79.1f,81.7f,84.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)16 ), createSDMap( 2.5844f,72.5f,75.0f,77.6f,80.2f,82.8f,85.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)17 ), createSDMap( 2.6406f,73.3f,76.0f,78.6f,81.2f,83.9f,86.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)18 ), createSDMap( 2.6973f,74.2f,76.9f,79.6f,82.3f,85.0f,87.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)19 ), createSDMap( 2.7553f,75.0f,77.7f,80.5f,83.2f,86.0f,88.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)20 ), createSDMap( 2.8140f,75.8f,78.6f,81.4f,84.2f,87.0f,89.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)21 ), createSDMap( 2.8742f,76.5f,79.4f,82.3f,85.1f,88.0f,90.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)22 ), createSDMap( 2.9342f,77.2f,80.2f,83.1f,86.0f,89.0f,91.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)23 ), createSDMap( 2.9951f,78.0f,81.0f,83.9f,86.9f,89.9f,92.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)24 ), createSDMap( 3.0551f,78.7f,81.7f,84.8f,87.8f,90.9f,93.9f ) );

        return zscoreMap;
    }

    public static Map<ZScoreTableKey, Map<Float, Integer>> getZscoreWFHTableGirl()
    {
        Map<ZScoreTableKey, Map<Float, Integer>> zscoreMap = new HashMap<>();

        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)45 ), createSDMap( 1.9f,2.1f,2.3f,2.5f,2.7f,3.0f,3.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)45.5 ), createSDMap( 2.0f,2.1f,2.3f,2.5f,2.8f,3.1f,3.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)46 ), createSDMap( 2.0f,2.2f,2.4f,2.6f,2.9f,3.2f,3.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)46.5 ), createSDMap( 2.1f,2.3f,2.5f,2.7f,3.0f,3.3f,3.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)47 ), createSDMap( 2.2f,2.4f,2.6f,2.8f,3.1f,3.4f,3.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)47.5 ), createSDMap( 2.2f,2.4f,2.6f,2.9f,3.2f,3.5f,3.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)48 ), createSDMap( 2.3f,2.5f,2.7f,3.0f,3.3f,3.6f,4.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)48.5 ), createSDMap( 2.4f,2.6f,2.8f,3.1f,3.4f,3.7f,4.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)49 ), createSDMap( 2.4f,2.6f,2.9f,3.2f,3.5f,3.8f,4.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)49.5 ), createSDMap( 2.5f,2.7f,3.0f,3.3f,3.6f,3.9f,4.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)50 ), createSDMap( 2.6f,2.8f,3.1f,3.4f,3.7f,4.0f,4.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)50.5 ), createSDMap( 2.7f,2.9f,3.2f,3.5f,3.8f,4.2f,4.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)51 ), createSDMap( 2.8f,3.0f,3.3f,3.6f,3.9f,4.3f,4.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)51.5 ), createSDMap( 2.8f,3.1f,3.4f,3.7f,4.0f,4.4f,4.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)52 ), createSDMap( 2.9f,3.2f,3.5f,3.8f,4.2f,4.6f,5.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)52.5 ), createSDMap( 3.0f,3.3f,3.6f,3.9f,4.3f,4.7f,5.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)53 ), createSDMap( 3.1f,3.4f,3.7f,4.0f,4.4f,4.9f,5.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)53.5 ), createSDMap( 3.2f,3.5f,3.8f,4.2f,4.6f,5.0f,5.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)54 ), createSDMap( 3.3f,3.6f,3.9f,4.3f,4.7f,5.2f,5.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)54.5 ), createSDMap( 3.4f,3.7f,4.0f,4.4f,4.8f,5.3f,5.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)55 ), createSDMap( 3.5f,3.8f,4.2f,4.5f,5.0f,5.5f,6.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)55.5 ), createSDMap( 3.6f,3.9f,4.3f,4.7f,5.1f,5.7f,6.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)56 ), createSDMap( 3.7f,4.0f,4.4f,4.8f,5.3f,5.8f,6.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)56.5 ), createSDMap( 3.8f,4.1f,4.5f,5.0f,5.4f,6.0f,6.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)57 ), createSDMap( 3.9f,4.3f,4.6f,5.1f,5.6f,6.1f,6.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)57.5 ), createSDMap( 4.0f,4.4f,4.8f,5.2f,5.7f,6.3f,7.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)58 ), createSDMap( 4.1f,4.5f,4.9f,5.4f,5.9f,6.5f,7.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)58.5 ), createSDMap( 4.2f,4.6f,5.0f,5.5f,6.0f,6.6f,7.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)59 ), createSDMap( 4.3f,4.7f,5.1f,5.6f,6.2f,6.8f,7.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)59.5 ), createSDMap( 4.4f,4.8f,5.3f,5.7f,6.3f,6.9f,7.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)60 ), createSDMap( 4.5f,4.9f,5.4f,5.9f,6.4f,7.1f,7.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)60.5 ), createSDMap( 4.6f,5.0f,5.5f,6.0f,6.6f,7.3f,8.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)61 ), createSDMap( 4.7f,5.1f,5.6f,6.1f,6.7f,7.4f,8.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)61.5 ), createSDMap( 4.8f,5.2f,5.7f,6.3f,6.9f,7.6f,8.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)62 ), createSDMap( 4.9f,5.3f,5.8f,6.4f,7.0f,7.7f,8.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)62.5 ), createSDMap( 5.0f,5.4f,5.9f,6.5f,7.1f,7.8f,8.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)63 ), createSDMap( 5.1f,5.5f,6.0f,6.6f,7.3f,8.0f,8.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)63.5 ), createSDMap( 5.2f,5.6f,6.2f,6.7f,7.4f,8.1f,9.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)64 ), createSDMap( 5.3f,5.7f,6.3f,6.9f,7.5f,8.3f,9.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)64.5 ), createSDMap( 5.4f,5.8f,6.4f,7.0f,7.6f,8.4f,9.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)65 ), createSDMap( 5.5f,5.9f,6.5f,7.1f,7.8f,8.6f,9.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)65.5 ), createSDMap( 5.5f,6.0f,6.6f,7.2f,7.9f,8.7f,9.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)66 ), createSDMap( 5.6f,6.1f,6.7f,7.3f,8.0f,8.8f,9.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)66.5 ), createSDMap( 5.7f,6.2f,6.8f,7.4f,8.1f,9.0f,9.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)67 ), createSDMap( 5.8f,6.3f,6.9f,7.5f,8.3f,9.1f,10.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)67.5 ), createSDMap( 5.9f,6.4f,7.0f,7.6f,8.4f,9.2f,10.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)68 ), createSDMap( 6.0f,6.5f,7.1f,7.7f,8.5f,9.4f,10.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)68.5 ), createSDMap( 6.1f,6.6f,7.2f,7.9f,8.6f,9.5f,10.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)69 ), createSDMap( 6.1f,6.7f,7.3f,8.0f,8.7f,9.6f,10.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)69.5 ), createSDMap( 6.2f,6.8f,7.4f,8.1f,8.8f,9.7f,10.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)70 ), createSDMap( 6.3f,6.9f,7.5f,8.2f,9.0f,9.9f,10.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)70.5 ), createSDMap( 6.4f,6.9f,7.6f,8.3f,9.1f,10.0f,11.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)71 ), createSDMap( 6.5f,7.0f,7.7f,8.4f,9.2f,10.1f,11.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)71.5 ), createSDMap( 6.5f,7.1f,7.7f,8.5f,9.3f,10.2f,11.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)72 ), createSDMap( 6.6f,7.2f,7.8f,8.6f,9.4f,10.3f,11.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)72.5 ), createSDMap( 6.7f,7.3f,7.9f,8.7f,9.5f,10.5f,11.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)73 ), createSDMap( 6.8f,7.4f,8.0f,8.8f,9.6f,10.6f,11.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)73.5 ), createSDMap( 6.9f,7.4f,8.1f,8.9f,9.7f,10.7f,11.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)74 ), createSDMap( 6.9f,7.5f,8.2f,9.0f,9.8f,10.8f,11.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)74.5 ), createSDMap( 7.0f,7.6f,8.3f,9.1f,9.9f,10.9f,12.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)75 ), createSDMap( 7.1f,7.7f,8.4f,9.1f,10.0f,11.0f,12.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)75.5 ), createSDMap( 7.1f,7.8f,8.5f,9.2f,10.1f,11.1f,12.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)76 ), createSDMap( 7.2f,7.8f,8.5f,9.3f,10.2f,11.2f,12.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)76.5 ), createSDMap( 7.3f,7.9f,8.6f,9.4f,10.3f,11.4f,12.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)77 ), createSDMap( 7.4f,8.0f,8.7f,9.5f,10.4f,11.5f,12.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)77.5 ), createSDMap( 7.4f,8.1f,8.8f,9.6f,10.5f,11.6f,12.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)78 ), createSDMap( 7.5f,8.2f,8.9f,9.7f,10.6f,11.7f,12.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)78.5 ), createSDMap( 7.6f,8.2f,9.0f,9.8f,10.7f,11.8f,13.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)79 ), createSDMap( 7.7f,8.3f,9.1f,9.9f,10.8f,11.9f,13.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)79.5 ), createSDMap( 7.7f,8.4f,9.1f,10.0f,10.9f,12.0f,13.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)80 ), createSDMap( 7.8f,8.5f,9.2f,10.1f,11.0f,12.1f,13.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)80.5 ), createSDMap( 7.9f,8.6f,9.3f,10.2f,11.2f,12.3f,13.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)81 ), createSDMap( 8.0f,8.7f,9.4f,10.3f,11.3f,12.4f,13.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)81.5 ), createSDMap( 8.1f,8.8f,9.5f,10.4f,11.4f,12.5f,13.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)82 ), createSDMap( 8.1f,8.8f,9.6f,10.5f,11.5f,12.6f,13.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)82.5 ), createSDMap( 8.2f,8.9f,9.7f,10.6f,11.6f,12.8f,14.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)83 ), createSDMap( 8.3f,9.0f,9.8f,10.7f,11.8f,12.9f,14.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)83.5 ), createSDMap( 8.4f,9.1f,9.9f,10.9f,11.9f,13.1f,14.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)84 ), createSDMap( 8.5f,9.2f,10.1f,11.0f,12.0f,13.2f,14.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)84.5 ), createSDMap( 8.6f,9.3f,10.2f,11.1f,12.1f,13.3f,14.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)85 ), createSDMap( 8.7f,9.4f,10.3f,11.2f,12.3f,13.5f,14.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)85.5 ), createSDMap( 8.8f,9.5f,10.4f,11.3f,12.4f,13.6f,15.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)86 ), createSDMap( 8.9f,9.7f,10.5f,11.5f,12.6f,13.8f,15.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)86.5 ), createSDMap( 9.0f,9.8f,10.6f,11.6f,12.7f,13.9f,15.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)87 ), createSDMap( 9.1f,9.9f,10.7f,11.7f,12.8f,14.1f,15.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)87.5 ), createSDMap( 9.2f,10.0f,10.9f,11.8f,13.0f,14.2f,15.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)88 ), createSDMap( 9.3f,10.1f,11.0f,12.0f,13.1f,14.4f,15.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)88.5 ), createSDMap( 9.4f,10.2f,11.1f,12.1f,13.2f,14.5f,16.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)89 ), createSDMap( 9.5f,10.3f,11.2f,12.2f,13.4f,14.7f,16.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)89.5 ), createSDMap( 9.6f,10.4f,11.3f,12.3f,13.5f,14.8f,16.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)90 ), createSDMap( 9.7f,10.5f,11.4f,12.5f,13.7f,15.0f,16.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)90.5 ), createSDMap( 9.8f,10.6f,11.5f,12.6f,13.8f,15.1f,16.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)91 ), createSDMap( 9.9f,10.7f,11.7f,12.7f,13.9f,15.3f,16.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)91.5 ), createSDMap( 10.0f,10.8f,11.8f,12.8f,14.1f,15.5f,17.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)92 ), createSDMap( 10.1f,10.9f,11.9f,13.0f,14.2f,15.6f,17.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)92.5 ), createSDMap( 10.1f,11.0f,12.0f,13.1f,14.3f,15.8f,17.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)93 ), createSDMap( 10.2f,11.1f,12.1f,13.2f,14.5f,15.9f,17.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)93.5 ), createSDMap( 10.3f,11.2f,12.2f,13.3f,14.6f,16.1f,17.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)94 ), createSDMap( 10.4f,11.3f,12.3f,13.5f,14.7f,16.2f,17.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)94.5 ), createSDMap( 10.5f,11.4f,12.4f,13.6f,14.9f,16.4f,18.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)95 ), createSDMap( 10.6f,11.5f,12.6f,13.7f,15.0f,16.5f,18.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)95.5 ), createSDMap( 10.7f,11.6f,12.7f,13.8f,15.2f,16.7f,18.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)96 ), createSDMap( 10.8f,11.7f,12.8f,14.0f,15.3f,16.8f,18.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)96.5 ), createSDMap( 10.9f,11.8f,12.9f,14.1f,15.4f,17.0f,18.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)97 ), createSDMap( 11.0f,12.0f,13.0f,14.2f,15.6f,17.1f,18.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)97.5 ), createSDMap( 11.1f,12.1f,13.1f,14.4f,15.7f,17.3f,19.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)98 ), createSDMap( 11.2f,12.2f,13.3f,14.5f,15.9f,17.5f,19.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)98.5 ), createSDMap( 11.3f,12.3f,13.4f,14.6f,16.0f,17.6f,19.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)99 ), createSDMap( 11.4f,12.4f,13.5f,14.8f,16.2f,17.8f,19.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)99.5 ), createSDMap( 11.5f,12.5f,13.6f,14.9f,16.3f,18.0f,19.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)100 ), createSDMap( 11.6f,12.6f,13.7f,15.0f,16.5f,18.1f,20.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)100.5 ), createSDMap( 11.7f,12.7f,13.9f,15.2f,16.6f,18.3f,20.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)101 ), createSDMap( 11.8f,12.8f,14.0f,15.3f,16.8f,18.5f,20.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)101.5 ), createSDMap( 11.9f,13.0f,14.1f,15.5f,17.0f,18.7f,20.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)102 ), createSDMap( 12.0f,13.1f,14.3f,15.6f,17.1f,18.9f,20.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)102.5 ), createSDMap( 12.1f,13.2f,14.4f,15.8f,17.3f,19.0f,21.0f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)103 ), createSDMap( 12.3f,13.3f,14.5f,15.9f,17.5f,19.2f,21.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)103.5 ), createSDMap( 12.4f,13.5f,14.7f,16.1f,17.6f,19.4f,21.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)104 ), createSDMap( 12.5f,13.6f,14.8f,16.2f,17.8f,19.6f,21.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)104.5 ), createSDMap( 12.6f,13.7f,15.0f,16.4f,18.0f,19.8f,21.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)105 ), createSDMap( 12.7f,13.8f,15.1f,16.5f,18.2f,20.0f,22.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)105.5 ), createSDMap( 12.8f,14.0f,15.3f,16.7f,18.4f,20.2f,22.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)106 ), createSDMap( 13.0f,14.1f,15.4f,16.9f,18.5f,20.5f,22.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)106.5 ), createSDMap( 13.1f,14.3f,15.6f,17.1f,18.7f,20.7f,22.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)107 ), createSDMap( 13.2f,14.4f,15.7f,17.2f,18.9f,20.9f,23.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)107.5 ), createSDMap( 13.3f,14.5f,15.9f,17.4f,19.1f,21.1f,23.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)108 ), createSDMap( 13.5f,14.7f,16.0f,17.6f,19.3f,21.3f,23.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)108.5 ), createSDMap( 13.6f,14.8f,16.2f,17.8f,19.5f,21.6f,23.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)109 ), createSDMap( 13.7f,15.0f,16.4f,18.0f,19.7f,21.8f,24.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)109.5 ), createSDMap( 13.9f,15.1f,16.5f,18.1f,20.0f,22.0f,24.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)1, (byte)110 ), createSDMap( 14.0f,15.3f,16.7f,18.3f,20.2f,22.3f,24.7f ) );

        return zscoreMap;
    }

    public static Map<ZScoreTableKey, Map<Float, Integer>> getZscoreWFHTableBoy()
    {
        Map<ZScoreTableKey, Map<Float, Integer>> zscoreMap = new HashMap<>();

        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)45 ), createSDMap( 1.9f,2f,2.2f,2.4f,2.7f,3f,3.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)45.5 ), createSDMap( 1.9f,2.1f,2.3f,2.5f,2.8f,3.1f,3.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)46 ), createSDMap( 2f,2.2f,2.4f,2.6f,2.9f,3.1f,3.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)46.5 ), createSDMap( 2.1f,2.3f,2.5f,2.7f,3f,3.2f,3.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)47 ), createSDMap( 2.1f,2.3f,2.5f,2.8f,3f,3.3f,3.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)47.5 ), createSDMap( 2.2f,2.4f,2.6f,2.9f,3.1f,3.4f,3.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)48 ), createSDMap( 2.3f,2.5f,2.7f,2.9f,3.2f,3.6f,3.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)48.5 ), createSDMap( 2.3f,2.6f,2.8f,3f,3.3f,3.7f,4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)49 ), createSDMap( 2.4f,2.6f,2.9f,3.1f,3.4f,3.8f,4.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)49.5 ), createSDMap( 2.5f,2.7f,3f,3.2f,3.5f,3.9f,4.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)50 ), createSDMap( 2.6f,2.8f,3f,3.3f,3.6f,4f,4.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)50.5 ), createSDMap( 2.7f,2.9f,3.1f,3.4f,3.8f,4.1f,4.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)51 ), createSDMap( 2.7f,3f,3.2f,3.5f,3.9f,4.2f,4.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)51.5 ), createSDMap( 2.8f,3.1f,3.3f,3.6f,4f,4.4f,4.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)52 ), createSDMap( 2.9f,3.2f,3.5f,3.8f,4.1f,4.5f,5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)52.5 ), createSDMap( 3f,3.3f,3.6f,3.9f,4.2f,4.6f,5.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)53 ), createSDMap( 3.1f,3.4f,3.7f,4f,4.4f,4.8f,5.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)53.5 ), createSDMap( 3.2f,3.5f,3.8f,4.1f,4.5f,4.9f,5.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)54 ), createSDMap( 3.3f,3.6f,3.9f,4.3f,4.7f,5.1f,5.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)54.5 ), createSDMap( 3.4f,3.7f,4f,4.4f,4.8f,5.3f,5.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)55 ), createSDMap( 3.6f,3.8f,4.2f,4.5f,5f,5.4f,6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)55.5 ), createSDMap( 3.7f,4f,4.3f,4.7f,5.1f,5.6f,6.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)56 ), createSDMap( 3.8f,4.1f,4.4f,4.8f,5.3f,5.8f,6.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)56.5 ), createSDMap( 3.9f,4.2f,4.6f,5f,5.4f,5.9f,6.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)57 ), createSDMap( 4f,4.3f,4.7f,5.1f,5.6f,6.1f,6.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)57.5 ), createSDMap( 4.1f,4.5f,4.9f,5.3f,5.7f,6.3f,6.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)58 ), createSDMap( 4.3f,4.6f,5f,5.4f,5.9f,6.4f,7.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)58.5 ), createSDMap( 4.4f,4.7f,5.1f,5.6f,6.1f,6.6f,7.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)59 ), createSDMap( 4.5f,4.8f,5.3f,5.7f,6.2f,6.8f,7.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)59.5 ), createSDMap( 4.6f,5f,5.4f,5.9f,6.4f,7f,7.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)60 ), createSDMap( 4.7f,5.1f,5.5f,6f,6.5f,7.1f,7.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)60.5 ), createSDMap( 4.8f,5.2f,5.6f,6.1f,6.7f,7.3f,8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)61 ), createSDMap( 4.9f,5.3f,5.8f,6.3f,6.8f,7.4f,8.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)61.5 ), createSDMap( 5f,5.4f,5.9f,6.4f,7f,7.6f,8.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)62 ), createSDMap( 5.1f,5.6f,6f,6.5f,7.1f,7.7f,8.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)62.5 ), createSDMap( 5.2f,5.7f,6.1f,6.7f,7.2f,7.9f,8.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)63 ), createSDMap( 5.3f,5.8f,6.2f,6.8f,7.4f,8f,8.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)63.5 ), createSDMap( 5.4f,5.9f,6.4f,6.9f,7.5f,8.2f,8.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)64 ), createSDMap( 5.5f,6f,6.5f,7f,7.6f,8.3f,9.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)64.5 ), createSDMap( 5.6f,6.1f,6.6f,7.1f,7.8f,8.5f,9.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)65 ), createSDMap( 5.7f,6.2f,6.7f,7.3f,7.9f,8.6f,9.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)65.5 ), createSDMap( 5.8f,6.3f,6.8f,7.4f,8f,8.7f,9.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)66 ), createSDMap( 5.9f,6.4f,6.9f,7.5f,8.2f,8.9f,9.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)66.5 ), createSDMap( 6f,6.5f,7f,7.6f,8.3f,9f,9.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)67 ), createSDMap( 6.1f,6.6f,7.1f,7.7f,8.4f,9.2f,10f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)67.5 ), createSDMap( 6.2f,6.7f,7.2f,7.9f,8.5f,9.3f,10.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)68 ), createSDMap( 6.3f,6.8f,7.3f,8f,8.7f,9.4f,10.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)68.5 ), createSDMap( 6.4f,6.9f,7.5f,8.1f,8.8f,9.6f,10.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)69 ), createSDMap( 6.5f,7f,7.6f,8.2f,8.9f,9.7f,10.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)69.5 ), createSDMap( 6.6f,7.1f,7.7f,8.3f,9f,9.8f,10.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)70 ), createSDMap( 6.6f,7.2f,7.8f,8.4f,9.2f,10f,10.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)70.5 ), createSDMap( 6.7f,7.3f,7.9f,8.5f,9.3f,10.1f,11.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)71 ), createSDMap( 6.8f,7.4f,8f,8.6f,9.4f,10.2f,11.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)71.5 ), createSDMap( 6.9f,7.5f,8.1f,8.8f,9.5f,10.4f,11.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)72 ), createSDMap( 7f,7.6f,8.2f,8.9f,9.6f,10.5f,11.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)72.5 ), createSDMap( 7.1f,7.6f,8.3f,9f,9.8f,10.6f,11.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)73 ), createSDMap( 7.2f,7.7f,8.4f,9.1f,9.9f,10.8f,11.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)73.5 ), createSDMap( 7.2f,7.8f,8.5f,9.2f,10f,10.9f,11.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)74 ), createSDMap( 7.3f,7.9f,8.6f,9.3f,10.1f,11f,12.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)74.5 ), createSDMap( 7.4f,8f,8.7f,9.4f,10.2f,11.2f,12.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)75 ), createSDMap( 7.5f,8.1f,8.8f,9.5f,10.3f,11.3f,12.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)75.5 ), createSDMap( 7.6f,8.2f,8.8f,9.6f,10.4f,11.4f,12.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)76 ), createSDMap( 7.6f,8.3f,8.9f,9.7f,10.6f,11.5f,12.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)76.5 ), createSDMap( 7.7f,8.3f,9f,9.8f,10.7f,11.6f,12.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)77 ), createSDMap( 7.8f,8.4f,9.1f,9.9f,10.8f,11.7f,12.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)77.5 ), createSDMap( 7.9f,8.5f,9.2f,10f,10.9f,11.9f,13f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)78 ), createSDMap( 7.9f,8.6f,9.3f,10.1f,11f,12f,13.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)78.5 ), createSDMap( 8f,8.7f,9.4f,10.2f,11.1f,12.1f,13.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)79 ), createSDMap( 8.1f,8.7f,9.5f,10.3f,11.2f,12.2f,13.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)79.5 ), createSDMap( 8.2f,8.8f,9.5f,10.4f,11.3f,12.3f,13.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)80 ), createSDMap( 8.2f,8.9f,9.6f,10.4f,11.4f,12.4f,13.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)80.5 ), createSDMap( 8.3f,9f,9.7f,10.5f,11.5f,12.5f,13.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)81 ), createSDMap( 8.4f,9.1f,9.8f,10.6f,11.6f,12.6f,13.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)81.5 ), createSDMap( 8.5f,9.1f,9.9f,10.7f,11.7f,12.7f,13.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)82 ), createSDMap( 8.5f,9.2f,10f,10.8f,11.8f,12.8f,14f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)82.5 ), createSDMap( 8.6f,9.3f,10.1f,10.9f,11.9f,13f,14.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)83 ), createSDMap( 8.7f,9.4f,10.2f,11f,12f,13.1f,14.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)83.5 ), createSDMap( 8.8f,9.5f,10.3f,11.2f,12.1f,13.2f,14.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)84 ), createSDMap( 8.9f,9.6f,10.4f,11.3f,12.2f,13.3f,14.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)84.5 ), createSDMap( 9f,9.7f,10.5f,11.4f,12.4f,13.5f,14.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)85 ), createSDMap( 9.1f,9.8f,10.6f,11.5f,12.5f,13.6f,14.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)85.5 ), createSDMap( 9.2f,9.9f,10.7f,11.6f,12.6f,13.7f,15f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)86 ), createSDMap( 9.3f,10f,10.8f,11.7f,12.8f,13.9f,15.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)86.5 ), createSDMap( 9.4f,10.1f,11f,11.9f,12.9f,14f,15.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)87 ), createSDMap( 9.5f,10.2f,11.1f,12f,13f,14.2f,15.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)87.5 ), createSDMap( 9.6f,10.4f,11.2f,12.1f,13.2f,14.3f,15.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)88 ), createSDMap( 9.7f,10.5f,11.3f,12.2f,13.3f,14.5f,15.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)88.5 ), createSDMap( 9.8f,10.6f,11.4f,12.4f,13.4f,14.6f,15.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)89 ), createSDMap( 9.9f,10.7f,11.5f,12.5f,13.5f,14.7f,16.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)89.5 ), createSDMap( 10f,10.8f,11.6f,12.6f,13.7f,14.9f,16.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)90 ), createSDMap( 10.1f,10.9f,11.8f,12.7f,13.8f,15f,16.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)90.5 ), createSDMap( 10.2f,11f,11.9f,12.8f,13.9f,15.1f,16.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)91 ), createSDMap( 10.3f,11.1f,12f,13f,14.1f,15.3f,16.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)91.5 ), createSDMap( 10.4f,11.2f,12.1f,13.1f,14.2f,15.4f,16.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)92 ), createSDMap( 10.5f,11.3f,12.2f,13.2f,14.3f,15.6f,17f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)92.5 ), createSDMap( 10.6f,11.4f,12.3f,13.3f,14.4f,15.7f,17.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)93 ), createSDMap( 10.7f,11.5f,12.4f,13.4f,14.6f,15.8f,17.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)93.5 ), createSDMap( 10.7f,11.6f,12.5f,13.5f,14.7f,16f,17.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)94 ), createSDMap( 10.8f,11.7f,12.6f,13.7f,14.8f,16.1f,17.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)94.5 ), createSDMap( 10.9f,11.8f,12.7f,13.8f,14.9f,16.3f,17.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)95 ), createSDMap( 11f,11.9f,12.8f,13.9f,15.1f,16.4f,17.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)95.5 ), createSDMap( 11.1f,12f,12.9f,14f,15.2f,16.5f,18f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)96 ), createSDMap( 11.2f,12.1f,13.1f,14.1f,15.3f,16.7f,18.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)96.5 ), createSDMap( 11.3f,12.2f,13.2f,14.3f,15.5f,16.8f,18.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)97 ), createSDMap( 11.4f,12.3f,13.3f,14.4f,15.6f,17f,18.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)97.5 ), createSDMap( 11.5f,12.4f,13.4f,14.5f,15.7f,17.1f,18.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)98 ), createSDMap( 11.6f,12.5f,13.5f,14.6f,15.9f,17.3f,18.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)98.5 ), createSDMap( 11.7f,12.6f,13.6f,14.8f,16f,17.5f,19.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)99 ), createSDMap( 11.8f,12.7f,13.7f,14.9f,16.2f,17.6f,19.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)99.5 ), createSDMap( 11.9f,12.8f,13.9f,15f,16.3f,17.8f,19.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)100 ), createSDMap( 12f,12.9f,14f,15.2f,16.5f,18f,19.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)100.5 ), createSDMap( 12.1f,13f,14.1f,15.3f,16.6f,18.1f,19.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)101 ), createSDMap( 12.2f,13.2f,14.2f,15.4f,16.8f,18.3f,20f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)101.5 ), createSDMap( 12.3f,13.3f,14.4f,15.6f,16.9f,18.5f,20.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)102 ), createSDMap( 12.4f,13.4f,14.5f,15.7f,17.1f,18.7f,20.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)102.5 ), createSDMap( 12.5f,13.5f,14.6f,15.9f,17.3f,18.8f,20.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)103 ), createSDMap( 12.6f,13.6f,14.8f,16f,17.4f,19f,20.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)103.5 ), createSDMap( 12.7f,13.7f,14.9f,16.2f,17.6f,19.2f,21f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)104 ), createSDMap( 12.8f,13.9f,15f,16.3f,17.8f,19.4f,21.2f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)104.5 ), createSDMap( 12.9f,14f,15.2f,16.5f,17.9f,19.6f,21.5f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)105 ), createSDMap( 13f,14.1f,15.3f,16.6f,18.1f,19.8f,21.7f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)105.5 ), createSDMap( 13.2f,14.2f,15.4f,16.8f,18.3f,20f,21.9f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)106 ), createSDMap( 13.3f,14.4f,15.6f,16.9f,18.5f,20.2f,22.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)106.5 ), createSDMap( 13.4f,14.5f,15.7f,17.1f,18.6f,20.4f,22.4f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)107 ), createSDMap( 13.5f,14.6f,15.9f,17.3f,18.8f,20.6f,22.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)107.5 ), createSDMap( 13.6f,14.7f,16f,17.4f,19f,20.8f,22.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)108 ), createSDMap( 13.7f,14.9f,16.2f,17.6f,19.2f,21f,23.1f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)108.5 ), createSDMap( 13.8f,15f,16.3f,17.8f,19.4f,21.2f,23.3f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)109 ), createSDMap( 14f,15.1f,16.5f,17.9f,19.6f,21.4f,23.6f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)109.5 ), createSDMap( 14.1f,15.3f,16.6f,18.1f,19.8f,21.7f,23.8f ) );
        zscoreMap.put( new ZScoreTableKey( (byte)0, (byte)110 ), createSDMap( 14.2f,15.4f,16.8f,18.3f,20f,21.9f,24.1f ) );

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
