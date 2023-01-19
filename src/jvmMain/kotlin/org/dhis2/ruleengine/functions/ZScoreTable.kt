package org.dhis2.ruleengine.functions

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
 */ /**
 * @author Zubair Asghar.
 */
object ZScoreTable {
    val zscoreWFATableGirl: Map<ZScoreTableKey, Map<Float, Int>>
        get() {
            val zscoreMap: MutableMap<ZScoreTableKey, Map<Float, Int>> = HashMap()
            zscoreMap[ZScoreTableKey(1.toByte(), 0f)] = createSDMap(2.0f, 2.4f, 2.8f, 3.2f, 3.7f, 4.2f, 4.8f)
            zscoreMap[ZScoreTableKey(1.toByte(), 1f)] = createSDMap(2.7f, 3.2f, 3.6f, 4.2f, 4.8f, 5.5f, 6.2f)
            zscoreMap[ZScoreTableKey(1.toByte(), 2f)] = createSDMap(3.4f, 3.9f, 4.5f, 5.1f, 5.8f, 6.6f, 7.5f)
            zscoreMap[ZScoreTableKey(1.toByte(), 3f)] = createSDMap(4.0f, 4.5f, 5.2f, 5.8f, 6.6f, 7.5f, 8.5f)
            zscoreMap[ZScoreTableKey(1.toByte(), 4f)] = createSDMap(4.4f, 5.0f, 5.7f, 6.4f, 7.3f, 8.2f, 9.3f)
            zscoreMap[ZScoreTableKey(1.toByte(), 5f)] = createSDMap(4.8f, 5.4f, 6.1f, 6.9f, 7.8f, 8.8f, 10.0f)
            zscoreMap[ZScoreTableKey(1.toByte(), 6f)] = createSDMap(5.1f, 5.7f, 6.5f, 7.3f, 8.2f, 9.3f, 10.6f)
            zscoreMap[ZScoreTableKey(1.toByte(), 7f)] = createSDMap(5.3f, 6.0f, 6.8f, 7.6f, 8.6f, 9.8f, 11.1f)
            zscoreMap[ZScoreTableKey(1.toByte(), 8f)] = createSDMap(5.6f, 6.3f, 7.0f, 7.9f, 9.0f, 10.2f, 11.6f)
            zscoreMap[ZScoreTableKey(1.toByte(), 9f)] = createSDMap(5.8f, 6.5f, 7.3f, 8.2f, 9.3f, 10.5f, 12.0f)
            zscoreMap[ZScoreTableKey(1.toByte(), 10f)] = createSDMap(5.9f, 6.7f, 7.5f, 8.5f, 9.6f, 10.9f, 12.4f)
            zscoreMap[ZScoreTableKey(1.toByte(), 11f)] = createSDMap(6.1f, 6.9f, 7.7f, 8.7f, 9.9f, 11.2f, 12.8f)
            zscoreMap[ZScoreTableKey(1.toByte(), 12f)] = createSDMap(6.3f, 7.0f, 7.9f, 8.9f, 10.1f, 11.5f, 13.1f)
            zscoreMap[ZScoreTableKey(1.toByte(), 13f)] = createSDMap(6.4f, 7.2f, 8.1f, 9.2f, 10.4f, 11.8f, 13.5f)
            zscoreMap[ZScoreTableKey(1.toByte(), 14f)] = createSDMap(6.6f, 7.4f, 8.3f, 9.4f, 10.6f, 12.1f, 13.8f)
            zscoreMap[ZScoreTableKey(1.toByte(), 15f)] = createSDMap(6.7f, 7.6f, 8.5f, 9.6f, 10.9f, 12.4f, 14.1f)
            zscoreMap[ZScoreTableKey(1.toByte(), 16f)] = createSDMap(6.9f, 7.7f, 8.7f, 9.8f, 11.1f, 12.6f, 14.5f)
            zscoreMap[ZScoreTableKey(1.toByte(), 17f)] = createSDMap(7.0f, 7.9f, 8.9f, 10.0f, 11.4f, 12.9f, 14.8f)
            zscoreMap[ZScoreTableKey(1.toByte(), 18f)] = createSDMap(7.2f, 8.1f, 9.1f, 10.2f, 11.6f, 13.2f, 15.1f)
            zscoreMap[ZScoreTableKey(1.toByte(), 19f)] = createSDMap(7.3f, 8.2f, 9.2f, 10.4f, 11.8f, 13.5f, 15.4f)
            zscoreMap[ZScoreTableKey(1.toByte(), 20f)] = createSDMap(7.5f, 8.4f, 9.4f, 10.6f, 12.1f, 13.7f, 15.7f)
            zscoreMap[ZScoreTableKey(1.toByte(), 21f)] = createSDMap(7.6f, 8.6f, 9.6f, 10.9f, 12.3f, 14.0f, 16.0f)
            zscoreMap[ZScoreTableKey(1.toByte(), 22f)] = createSDMap(7.8f, 8.7f, 9.8f, 11.1f, 12.5f, 14.3f, 16.4f)
            zscoreMap[ZScoreTableKey(1.toByte(), 23f)] = createSDMap(7.9f, 8.9f, 10.0f, 11.3f, 12.8f, 14.6f, 16.7f)
            zscoreMap[ZScoreTableKey(1.toByte(), 24f)] = createSDMap(8.1f, 9.0f, 10.2f, 11.5f, 13.0f, 14.8f, 17.0f)
            zscoreMap[ZScoreTableKey(1.toByte(), 25f)] = createSDMap(8.2f, 9.2f, 10.3f, 11.7f, 13.3f, 15.1f, 17.3f)
            zscoreMap[ZScoreTableKey(1.toByte(), 26f)] = createSDMap(8.4f, 9.4f, 10.5f, 11.9f, 13.5f, 15.4f, 17.7f)
            zscoreMap[ZScoreTableKey(1.toByte(), 27f)] = createSDMap(8.5f, 9.5f, 10.7f, 12.1f, 13.7f, 15.7f, 18.0f)
            zscoreMap[ZScoreTableKey(1.toByte(), 28f)] = createSDMap(8.6f, 9.7f, 10.9f, 12.3f, 14.0f, 16.0f, 18.3f)
            zscoreMap[ZScoreTableKey(1.toByte(), 29f)] = createSDMap(8.8f, 9.8f, 11.1f, 12.5f, 14.2f, 16.2f, 18.7f)
            zscoreMap[ZScoreTableKey(1.toByte(), 30f)] = createSDMap(8.9f, 10.0f, 11.2f, 12.7f, 14.4f, 16.5f, 19.0f)
            zscoreMap[ZScoreTableKey(1.toByte(), 31f)] = createSDMap(9.0f, 10.1f, 11.4f, 12.9f, 14.7f, 16.8f, 19.3f)
            zscoreMap[ZScoreTableKey(1.toByte(), 32f)] = createSDMap(9.1f, 10.3f, 11.6f, 13.1f, 14.9f, 17.1f, 19.6f)
            zscoreMap[ZScoreTableKey(1.toByte(), 33f)] = createSDMap(9.3f, 10.4f, 11.7f, 13.3f, 15.1f, 17.3f, 20.0f)
            zscoreMap[ZScoreTableKey(1.toByte(), 34f)] = createSDMap(9.4f, 10.5f, 11.9f, 13.5f, 15.4f, 17.6f, 20.3f)
            zscoreMap[ZScoreTableKey(1.toByte(), 35f)] = createSDMap(9.5f, 10.7f, 12.0f, 13.7f, 15.6f, 17.9f, 20.6f)
            zscoreMap[ZScoreTableKey(1.toByte(), 36f)] = createSDMap(9.6f, 10.8f, 12.2f, 13.9f, 15.8f, 18.1f, 20.9f)
            zscoreMap[ZScoreTableKey(1.toByte(), 37f)] = createSDMap(9.7f, 10.9f, 12.4f, 14.0f, 16.0f, 18.4f, 21.3f)
            zscoreMap[ZScoreTableKey(1.toByte(), 38f)] = createSDMap(9.8f, 11.1f, 12.5f, 14.2f, 16.3f, 18.7f, 21.6f)
            zscoreMap[ZScoreTableKey(1.toByte(), 39f)] = createSDMap(9.9f, 11.2f, 12.7f, 14.4f, 16.5f, 19.0f, 22.0f)
            zscoreMap[ZScoreTableKey(1.toByte(), 40f)] = createSDMap(10.1f, 11.3f, 12.8f, 14.6f, 16.7f, 19.2f, 22.3f)
            zscoreMap[ZScoreTableKey(1.toByte(), 41f)] = createSDMap(10.2f, 11.5f, 13.0f, 14.8f, 16.9f, 19.5f, 22.7f)
            zscoreMap[ZScoreTableKey(1.toByte(), 42f)] = createSDMap(10.3f, 11.6f, 13.1f, 15.0f, 17.2f, 19.8f, 23.0f)
            zscoreMap[ZScoreTableKey(1.toByte(), 43f)] = createSDMap(10.4f, 11.7f, 13.3f, 15.2f, 17.4f, 20.1f, 23.4f)
            zscoreMap[ZScoreTableKey(1.toByte(), 44f)] =
                createSDMap(10.5f, 11.8f, 13.4f, 15.3f, 17.6f, 20.4f, 23.7f)
            zscoreMap[ZScoreTableKey(1.toByte(), 45f)] = createSDMap(10.6f, 12.0f, 13.6f, 15.5f, 17.8f, 20.7f, 24.1f)
            zscoreMap[ZScoreTableKey(1.toByte(), 46f)] = createSDMap(10.7f, 12.1f, 13.7f, 15.7f, 18.1f, 20.9f, 24.5f)
            zscoreMap[ZScoreTableKey(1.toByte(), 47f)] = createSDMap(10.8f, 12.2f, 13.9f, 15.9f, 18.3f, 21.2f, 24.8f)
            zscoreMap[ZScoreTableKey(1.toByte(), 48f)] = createSDMap(10.9f, 12.3f, 14.0f, 16.1f, 18.5f, 21.5f, 25.2f)
            zscoreMap[ZScoreTableKey(1.toByte(), 49f)] =
                createSDMap(11.0f, 12.4f, 14.2f, 16.3f, 18.8f, 21.8f, 25.5f)
            zscoreMap[ZScoreTableKey(1.toByte(), 50f)] =
                createSDMap(11.1f, 12.6f, 14.3f, 16.4f, 19.0f, 22.1f, 25.9f)
            zscoreMap[ZScoreTableKey(1.toByte(), 51f)] = createSDMap(11.2f, 12.7f, 14.5f, 16.6f, 19.2f, 22.4f, 26.3f)
            zscoreMap[ZScoreTableKey(1.toByte(), 52f)] = createSDMap(11.3f, 12.8f, 14.6f, 16.8f, 19.4f, 22.6f, 26.6f)
            zscoreMap[ZScoreTableKey(1.toByte(), 53f)] =
                createSDMap(11.4f, 12.9f, 14.8f, 17.0f, 19.7f, 22.9f, 27.0f)
            zscoreMap[ZScoreTableKey(1.toByte(), 54f)] = createSDMap(11.5f, 13.0f, 14.9f, 17.2f, 19.9f, 23.2f, 27.4f)
            zscoreMap[ZScoreTableKey(1.toByte(), 55f)] = createSDMap(11.6f, 13.2f, 15.1f, 17.3f, 20.1f, 23.5f, 27.7f)
            zscoreMap[ZScoreTableKey(1.toByte(), 56f)] =
                createSDMap(11.7f, 13.3f, 15.2f, 17.5f, 20.3f, 23.8f, 28.1f)
            zscoreMap[ZScoreTableKey(1.toByte(), 57f)] =
                createSDMap(11.8f, 13.4f, 15.3f, 17.7f, 20.6f, 24.1f, 28.5f)
            zscoreMap[ZScoreTableKey(1.toByte(), 58f)] = createSDMap(11.9f, 13.5f, 15.5f, 17.9f, 20.8f, 24.4f, 28.8f)
            zscoreMap[ZScoreTableKey(1.toByte(), 59f)] =
                createSDMap(12.0f, 13.6f, 15.6f, 18.0f, 21.0f, 24.6f, 29.2f)
            zscoreMap[ZScoreTableKey(1.toByte(), 60f)] = createSDMap(12.1f, 13.7f, 15.8f, 18.2f, 21.2f, 24.9f, 29.5f)
            return zscoreMap
        }
    val zscoreWFATableBoy: Map<ZScoreTableKey, Map<Float, Int>>
        get() {
            val zscoreMap: MutableMap<ZScoreTableKey, Map<Float, Int>> = HashMap()
            zscoreMap[ZScoreTableKey(0.toByte(), 0f)] = createSDMap(2.1f, 2.5f, 2.9f, 3.3f, 3.9f, 4.4f, 5.0f)
            zscoreMap[ZScoreTableKey(0.toByte(), 1f)] = createSDMap(2.9f, 3.4f, 3.9f, 4.5f, 5.1f, 5.8f, 6.6f)
            zscoreMap[ZScoreTableKey(0.toByte(), 2f)] = createSDMap(3.8f, 4.3f, 4.9f, 5.6f, 6.3f, 7.1f, 8.0f)
            zscoreMap[ZScoreTableKey(0.toByte(), 3f)] = createSDMap(4.4f, 5.0f, 5.7f, 6.4f, 7.2f, 8.0f, 9.0f)
            zscoreMap[ZScoreTableKey(0.toByte(), 4f)] = createSDMap(4.9f, 5.6f, 6.2f, 7.0f, 7.8f, 8.7f, 9.7f)
            zscoreMap[ZScoreTableKey(0.toByte(), 5f)] = createSDMap(5.3f, 6.0f, 6.7f, 7.5f, 8.4f, 9.3f, 10.4f)
            zscoreMap[ZScoreTableKey(0.toByte(), 6f)] = createSDMap(5.7f, 6.4f, 7.1f, 7.9f, 8.8f, 9.8f, 10.9f)
            zscoreMap[ZScoreTableKey(0.toByte(), 7f)] = createSDMap(5.9f, 6.7f, 7.4f, 8.3f, 9.2f, 10.3f, 11.4f)
            zscoreMap[ZScoreTableKey(0.toByte(), 8f)] = createSDMap(6.2f, 6.9f, 7.7f, 8.6f, 9.6f, 10.7f, 11.9f)
            zscoreMap[ZScoreTableKey(0.toByte(), 9f)] = createSDMap(6.4f, 7.1f, 8.0f, 8.9f, 9.9f, 11.0f, 12.3f)
            zscoreMap[ZScoreTableKey(0.toByte(), 10f)] = createSDMap(6.6f, 7.4f, 8.2f, 9.2f, 10.2f, 11.4f, 12.7f)
            zscoreMap[ZScoreTableKey(0.toByte(), 11f)] = createSDMap(6.8f, 7.6f, 8.4f, 9.4f, 10.5f, 11.7f, 13.0f)
            zscoreMap[ZScoreTableKey(0.toByte(), 12f)] = createSDMap(6.9f, 7.7f, 8.6f, 9.6f, 10.8f, 12.0f, 13.3f)
            zscoreMap[ZScoreTableKey(0.toByte(), 13f)] = createSDMap(7.1f, 7.9f, 8.8f, 9.9f, 11.0f, 12.3f, 13.7f)
            zscoreMap[ZScoreTableKey(0.toByte(), 14f)] = createSDMap(7.2f, 8.1f, 9.0f, 10.1f, 11.3f, 12.6f, 14.0f)
            zscoreMap[ZScoreTableKey(0.toByte(), 15f)] = createSDMap(7.4f, 8.3f, 9.2f, 10.3f, 11.5f, 12.8f, 14.3f)
            zscoreMap[ZScoreTableKey(0.toByte(), 16f)] = createSDMap(7.5f, 8.4f, 9.4f, 10.5f, 11.7f, 13.1f, 14.6f)
            zscoreMap[ZScoreTableKey(0.toByte(), 17f)] = createSDMap(7.7f, 8.6f, 9.6f, 10.7f, 12.0f, 13.4f, 14.9f)
            zscoreMap[ZScoreTableKey(0.toByte(), 18f)] = createSDMap(7.8f, 8.8f, 9.8f, 10.9f, 12.2f, 13.7f, 15.3f)
            zscoreMap[ZScoreTableKey(0.toByte(), 19f)] = createSDMap(8.0f, 8.9f, 10.0f, 11.1f, 12.5f, 13.9f, 15.6f)
            zscoreMap[ZScoreTableKey(0.toByte(), 20f)] = createSDMap(8.1f, 9.1f, 10.1f, 11.3f, 12.7f, 14.2f, 15.9f)
            zscoreMap[ZScoreTableKey(0.toByte(), 21f)] = createSDMap(8.2f, 9.2f, 10.3f, 11.5f, 12.9f, 14.5f, 16.2f)
            zscoreMap[ZScoreTableKey(0.toByte(), 22f)] = createSDMap(8.4f, 9.4f, 10.5f, 11.8f, 13.2f, 14.7f, 16.5f)
            zscoreMap[ZScoreTableKey(0.toByte(), 23f)] = createSDMap(8.5f, 9.5f, 10.7f, 12.0f, 13.4f, 15.0f, 16.8f)
            zscoreMap[ZScoreTableKey(0.toByte(), 24f)] = createSDMap(8.6f, 9.7f, 10.8f, 12.2f, 13.6f, 15.3f, 17.1f)
            zscoreMap[ZScoreTableKey(0.toByte(), 25f)] = createSDMap(8.8f, 9.8f, 11.0f, 12.4f, 13.9f, 15.5f, 17.5f)
            zscoreMap[ZScoreTableKey(0.toByte(), 26f)] = createSDMap(8.9f, 10.0f, 11.2f, 12.5f, 14.1f, 15.8f, 17.8f)
            zscoreMap[ZScoreTableKey(0.toByte(), 27f)] = createSDMap(9.0f, 10.1f, 11.3f, 12.7f, 14.3f, 16.1f, 18.1f)
            zscoreMap[ZScoreTableKey(0.toByte(), 29f)] = createSDMap(9.2f, 10.4f, 11.7f, 13.1f, 14.8f, 16.6f, 18.7f)
            zscoreMap[ZScoreTableKey(0.toByte(), 30f)] = createSDMap(9.4f, 10.5f, 11.8f, 13.3f, 15.0f, 16.9f, 19.0f)
            zscoreMap[ZScoreTableKey(0.toByte(), 31f)] = createSDMap(9.5f, 10.7f, 12.0f, 13.5f, 15.2f, 17.1f, 19.3f)
            zscoreMap[ZScoreTableKey(0.toByte(), 32f)] = createSDMap(9.6f, 10.8f, 12.1f, 13.7f, 15.4f, 17.4f, 19.6f)
            zscoreMap[ZScoreTableKey(0.toByte(), 33f)] = createSDMap(9.7f, 10.9f, 12.3f, 13.8f, 15.6f, 17.6f, 19.9f)
            zscoreMap[ZScoreTableKey(0.toByte(), 34f)] = createSDMap(9.8f, 11.0f, 12.4f, 14.0f, 15.8f, 17.8f, 20.2f)
            zscoreMap[ZScoreTableKey(0.toByte(), 35f)] = createSDMap(9.9f, 11.2f, 12.6f, 14.2f, 16.0f, 18.1f, 20.4f)
            zscoreMap[ZScoreTableKey(0.toByte(), 36f)] =
                createSDMap(10.0f, 11.3f, 12.7f, 14.3f, 16.2f, 18.3f, 20.7f)
            zscoreMap[ZScoreTableKey(0.toByte(), 37f)] = createSDMap(10.1f, 11.4f, 12.9f, 14.5f, 16.4f, 18.6f, 21.0f)
            zscoreMap[ZScoreTableKey(0.toByte(), 38f)] = createSDMap(10.2f, 11.5f, 13.0f, 14.7f, 16.6f, 18.8f, 21.3f)
            zscoreMap[ZScoreTableKey(0.toByte(), 39f)] =
                createSDMap(10.3f, 11.6f, 13.1f, 14.8f, 16.8f, 19.0f, 21.6f)
            zscoreMap[ZScoreTableKey(0.toByte(), 40f)] =
                createSDMap(10.4f, 11.8f, 13.3f, 15.0f, 17.0f, 19.3f, 21.9f)
            zscoreMap[ZScoreTableKey(0.toByte(), 41f)] = createSDMap(10.5f, 11.9f, 13.4f, 15.2f, 17.2f, 19.5f, 22.1f)
            zscoreMap[ZScoreTableKey(0.toByte(), 42f)] = createSDMap(10.6f, 12.0f, 13.6f, 15.3f, 17.4f, 19.7f, 22.4f)
            zscoreMap[ZScoreTableKey(0.toByte(), 43f)] = createSDMap(10.7f, 12.1f, 13.7f, 15.5f, 17.6f, 20.0f, 22.7f)
            zscoreMap[ZScoreTableKey(0.toByte(), 44f)] = createSDMap(10.8f, 12.2f, 13.8f, 15.7f, 17.8f, 20.2f, 23.0f)
            zscoreMap[ZScoreTableKey(0.toByte(), 45f)] = createSDMap(10.9f, 12.4f, 14.0f, 15.8f, 18.0f, 20.5f, 23.3f)
            zscoreMap[ZScoreTableKey(0.toByte(), 46f)] =
                createSDMap(11.0f, 12.5f, 14.1f, 16.0f, 18.2f, 20.7f, 23.6f)
            zscoreMap[ZScoreTableKey(0.toByte(), 47f)] = createSDMap(11.1f, 12.6f, 14.3f, 16.2f, 18.4f, 20.9f, 23.9f)
            zscoreMap[ZScoreTableKey(0.toByte(), 48f)] =
                createSDMap(11.2f, 12.7f, 14.4f, 16.3f, 18.6f, 21.2f, 24.2f)
            zscoreMap[ZScoreTableKey(0.toByte(), 49f)] =
                createSDMap(11.3f, 12.8f, 14.5f, 16.5f, 18.8f, 21.4f, 24.5f)
            zscoreMap[ZScoreTableKey(0.toByte(), 50f)] = createSDMap(11.4f, 12.9f, 14.7f, 16.7f, 19.0f, 21.7f, 24.8f)
            zscoreMap[ZScoreTableKey(0.toByte(), 51f)] = createSDMap(11.5f, 13.1f, 14.8f, 16.8f, 19.2f, 21.9f, 25.1f)
            zscoreMap[ZScoreTableKey(0.toByte(), 52f)] = createSDMap(11.6f, 13.2f, 15.0f, 17.0f, 19.4f, 22.2f, 25.4f)
            zscoreMap[ZScoreTableKey(0.toByte(), 53f)] = createSDMap(11.7f, 13.3f, 15.1f, 17.2f, 19.6f, 22.4f, 25.7f)
            zscoreMap[ZScoreTableKey(0.toByte(), 54f)] = createSDMap(11.8f, 13.4f, 15.2f, 17.3f, 19.8f, 22.7f, 26.0f)
            zscoreMap[ZScoreTableKey(0.toByte(), 55f)] = createSDMap(11.9f, 13.5f, 15.4f, 17.5f, 20.0f, 22.9f, 26.3f)
            zscoreMap[ZScoreTableKey(0.toByte(), 56f)] =
                createSDMap(12.0f, 13.6f, 15.5f, 17.7f, 20.2f, 23.2f, 26.6f)
            zscoreMap[ZScoreTableKey(0.toByte(), 57f)] = createSDMap(12.1f, 13.7f, 15.6f, 17.8f, 20.4f, 23.4f, 26.9f)
            zscoreMap[ZScoreTableKey(0.toByte(), 58f)] = createSDMap(12.2f, 13.8f, 15.8f, 18.0f, 20.6f, 23.7f, 27.2f)
            zscoreMap[ZScoreTableKey(0.toByte(), 59f)] =
                createSDMap(12.3f, 14.0f, 15.9f, 18.2f, 20.8f, 23.9f, 27.6f)
            zscoreMap[ZScoreTableKey(0.toByte(), 60f)] =
                createSDMap(12.4f, 14.1f, 16.0f, 18.3f, 21.0f, 24.2f, 27.9f)
            return zscoreMap
        }
    val zscoreHFATableGirl: Map<ZScoreTableKey, Map<Float, Int>>
        get() {
            val zscoreMap: MutableMap<ZScoreTableKey, Map<Float, Int>> = HashMap()
            zscoreMap[ZScoreTableKey(1.toByte(), 0f)] = createSDMap(43.6f, 45.4f, 47.3f, 49.1f, 51.0f, 52.9f, 54.7f)
            zscoreMap[ZScoreTableKey(1.toByte(), 1f)] = createSDMap(47.8f, 49.8f, 51.7f, 53.7f, 55.6f, 57.6f, 59.5f)
            zscoreMap[ZScoreTableKey(1.toByte(), 2f)] = createSDMap(51.0f, 53.0f, 55.0f, 57.1f, 59.1f, 61.1f, 63.2f)
            zscoreMap[ZScoreTableKey(1.toByte(), 3f)] = createSDMap(53.5f, 55.6f, 57.7f, 59.8f, 61.9f, 64.0f, 66.1f)
            zscoreMap[ZScoreTableKey(1.toByte(), 4f)] = createSDMap(55.6f, 57.8f, 59.9f, 62.1f, 64.3f, 66.4f, 68.6f)
            zscoreMap[ZScoreTableKey(1.toByte(), 5f)] = createSDMap(57.4f, 59.6f, 61.8f, 64.0f, 66.2f, 68.5f, 70.7f)
            zscoreMap[ZScoreTableKey(1.toByte(), 6f)] = createSDMap(58.9f, 61.2f, 63.5f, 65.7f, 68.0f, 70.3f, 72.5f)
            zscoreMap[ZScoreTableKey(1.toByte(), 7f)] = createSDMap(60.3f, 62.7f, 65.0f, 67.3f, 69.6f, 71.9f, 74.2f)
            zscoreMap[ZScoreTableKey(1.toByte(), 8f)] = createSDMap(61.7f, 64.0f, 66.4f, 68.7f, 71.1f, 73.5f, 75.8f)
            zscoreMap[ZScoreTableKey(1.toByte(), 9f)] = createSDMap(62.9f, 65.3f, 67.7f, 70.1f, 72.6f, 75.0f, 77.4f)
            zscoreMap[ZScoreTableKey(1.toByte(), 10f)] = createSDMap(64.1f, 66.5f, 69.0f, 71.5f, 73.9f, 76.4f, 78.9f)
            zscoreMap[ZScoreTableKey(1.toByte(), 11f)] =
                createSDMap(65.2f, 67.7f, 70.3f, 72.8f, 75.3f, 77.8f, 80.3f)
            zscoreMap[ZScoreTableKey(1.toByte(), 12f)] =
                createSDMap(66.3f, 68.9f, 71.4f, 74.0f, 76.6f, 79.2f, 81.7f)
            zscoreMap[ZScoreTableKey(1.toByte(), 13f)] =
                createSDMap(67.3f, 70.0f, 72.6f, 75.2f, 77.8f, 80.5f, 83.1f)
            zscoreMap[ZScoreTableKey(1.toByte(), 14f)] = createSDMap(68.3f, 71.0f, 73.7f, 76.4f, 79.1f, 81.7f, 84.4f)
            zscoreMap[ZScoreTableKey(1.toByte(), 15f)] = createSDMap(69.3f, 72.0f, 74.8f, 77.5f, 80.2f, 83.0f, 85.7f)
            zscoreMap[ZScoreTableKey(1.toByte(), 16f)] = createSDMap(70.2f, 73.0f, 75.8f, 78.6f, 81.4f, 84.2f, 87.0f)
            zscoreMap[ZScoreTableKey(1.toByte(), 17f)] =
                createSDMap(71.1f, 74.0f, 76.8f, 79.7f, 82.5f, 85.4f, 88.2f)
            zscoreMap[ZScoreTableKey(1.toByte(), 18f)] = createSDMap(72.0f, 74.9f, 77.8f, 80.7f, 83.6f, 86.5f, 89.4f)
            zscoreMap[ZScoreTableKey(1.toByte(), 19f)] = createSDMap(72.8f, 75.8f, 78.8f, 81.7f, 84.7f, 87.6f, 90.6f)
            zscoreMap[ZScoreTableKey(1.toByte(), 20f)] = createSDMap(73.7f, 76.7f, 79.7f, 82.7f, 85.7f, 88.7f, 91.7f)
            zscoreMap[ZScoreTableKey(1.toByte(), 21f)] =
                createSDMap(74.5f, 77.5f, 80.6f, 83.7f, 86.7f, 89.8f, 92.9f)
            zscoreMap[ZScoreTableKey(1.toByte(), 22f)] =
                createSDMap(75.2f, 78.4f, 81.5f, 84.6f, 87.7f, 90.8f, 94.0f)
            zscoreMap[ZScoreTableKey(1.toByte(), 23f)] =
                createSDMap(76.0f, 79.2f, 82.3f, 85.5f, 88.7f, 91.9f, 95.0f)
            zscoreMap[ZScoreTableKey(1.toByte(), 24f)] = createSDMap(76.7f, 80.0f, 83.2f, 86.4f, 89.6f, 92.9f, 96.1f)
            zscoreMap[ZScoreTableKey(1.toByte(), 25f)] = createSDMap(76.8f, 80.0f, 83.3f, 86.6f, 89.9f, 93.1f, 96.4f)
            zscoreMap[ZScoreTableKey(1.toByte(), 26f)] = createSDMap(77.5f, 80.8f, 84.1f, 87.4f, 90.8f, 94.1f, 97.4f)
            zscoreMap[ZScoreTableKey(1.toByte(), 27f)] = createSDMap(78.1f, 81.5f, 84.9f, 88.3f, 91.7f, 95.0f, 98.4f)
            zscoreMap[ZScoreTableKey(1.toByte(), 28f)] = createSDMap(78.8f, 82.2f, 85.7f, 89.1f, 92.5f, 96.0f, 99.4f)
            zscoreMap[ZScoreTableKey(1.toByte(), 29f)] = createSDMap(79.5f, 82.9f, 86.4f, 89.9f, 93.4f, 96.9f, 100.3f)
            zscoreMap[ZScoreTableKey(1.toByte(), 30f)] = createSDMap(80.1f, 83.6f, 87.1f, 90.7f, 94.2f, 97.7f, 101.3f)
            zscoreMap[ZScoreTableKey(1.toByte(), 31f)] =
                createSDMap(80.7f, 84.3f, 87.9f, 91.4f, 95.0f, 98.6f, 102.2f)
            zscoreMap[ZScoreTableKey(1.toByte(), 32f)] =
                createSDMap(81.3f, 84.9f, 88.6f, 92.2f, 95.8f, 99.4f, 103.1f)
            zscoreMap[ZScoreTableKey(1.toByte(), 33f)] = createSDMap(81.9f, 85.6f, 89.3f, 92.9f, 96.6f, 100.3f, 103.9f)
            zscoreMap[ZScoreTableKey(1.toByte(), 34f)] =
                createSDMap(82.5f, 86.2f, 89.9f, 93.6f, 97.4f, 101.1f, 104.8f)
            zscoreMap[ZScoreTableKey(1.toByte(), 35f)] = createSDMap(83.1f, 86.8f, 90.6f, 94.4f, 98.1f, 101.9f, 105.6f)
            zscoreMap[ZScoreTableKey(1.toByte(), 36f)] = createSDMap(83.6f, 87.4f, 91.2f, 95.1f, 98.9f, 102.7f, 106.5f)
            zscoreMap[ZScoreTableKey(1.toByte(), 37f)] = createSDMap(84.2f, 88.0f, 91.9f, 95.7f, 99.6f, 103.4f, 107.3f)
            zscoreMap[ZScoreTableKey(1.toByte(), 38f)] =
                createSDMap(84.7f, 88.6f, 92.5f, 96.4f, 100.3f, 104.2f, 108.1f)
            zscoreMap[ZScoreTableKey(1.toByte(), 39f)] =
                createSDMap(85.3f, 89.2f, 93.1f, 97.1f, 101.0f, 105.0f, 108.9f)
            zscoreMap[ZScoreTableKey(1.toByte(), 40f)] =
                createSDMap(85.8f, 89.8f, 93.8f, 97.7f, 101.7f, 105.7f, 109.7f)
            zscoreMap[ZScoreTableKey(1.toByte(), 41f)] = createSDMap(86.3f, 90.4f, 94.4f, 98.4f, 102.4f, 106.4f, 110.5f)
            zscoreMap[ZScoreTableKey(1.toByte(), 42f)] =
                createSDMap(86.8f, 90.9f, 95.0f, 99.0f, 103.1f, 107.2f, 111.2f)
            zscoreMap[ZScoreTableKey(1.toByte(), 43f)] = createSDMap(87.4f, 91.5f, 95.6f, 99.7f, 103.8f, 107.9f, 112.0f)
            zscoreMap[ZScoreTableKey(1.toByte(), 44f)] =
                createSDMap(87.9f, 92.0f, 96.2f, 100.3f, 104.5f, 108.6f, 112.7f)
            zscoreMap[ZScoreTableKey(1.toByte(), 45f)] =
                createSDMap(88.4f, 92.5f, 96.7f, 100.9f, 105.1f, 109.3f, 113.5f)
            zscoreMap[ZScoreTableKey(1.toByte(), 46f)] =
                createSDMap(88.9f, 93.1f, 97.3f, 101.5f, 105.8f, 110.0f, 114.2f)
            zscoreMap[ZScoreTableKey(1.toByte(), 47f)] =
                createSDMap(89.3f, 93.6f, 97.9f, 102.1f, 106.4f, 110.7f, 114.9f)
            zscoreMap[ZScoreTableKey(1.toByte(), 48f)] =
                createSDMap(89.8f, 94.1f, 98.4f, 102.7f, 107.0f, 111.3f, 115.7f)
            zscoreMap[ZScoreTableKey(1.toByte(), 49f)] =
                createSDMap(90.3f, 94.6f, 99.0f, 103.3f, 107.7f, 112.0f, 116.4f)
            zscoreMap[ZScoreTableKey(1.toByte(), 50f)] =
                createSDMap(90.7f, 95.1f, 99.5f, 103.9f, 108.3f, 112.7f, 117.1f)
            zscoreMap[ZScoreTableKey(1.toByte(), 51f)] =
                createSDMap(91.2f, 95.6f, 100.1f, 104.5f, 108.9f, 113.3f, 117.7f)
            zscoreMap[ZScoreTableKey(1.toByte(), 52f)] =
                createSDMap(91.7f, 96.1f, 100.6f, 105.0f, 109.5f, 114.0f, 118.4f)
            zscoreMap[ZScoreTableKey(1.toByte(), 53f)] =
                createSDMap(92.1f, 96.6f, 101.1f, 105.6f, 110.1f, 114.6f, 119.1f)
            zscoreMap[ZScoreTableKey(1.toByte(), 54f)] =
                createSDMap(92.6f, 97.1f, 101.6f, 106.2f, 110.7f, 115.2f, 119.8f)
            zscoreMap[ZScoreTableKey(1.toByte(), 55f)] =
                createSDMap(93.0f, 97.6f, 102.2f, 106.7f, 111.3f, 115.9f, 120.4f)
            zscoreMap[ZScoreTableKey(1.toByte(), 56f)] =
                createSDMap(93.4f, 98.1f, 102.7f, 107.3f, 111.9f, 116.5f, 121.1f)
            zscoreMap[ZScoreTableKey(1.toByte(), 57f)] =
                createSDMap(93.9f, 98.5f, 103.2f, 107.8f, 112.5f, 117.1f, 121.8f)
            zscoreMap[ZScoreTableKey(1.toByte(), 58f)] =
                createSDMap(94.3f, 99.0f, 103.7f, 108.4f, 113.0f, 117.7f, 122.4f)
            zscoreMap[ZScoreTableKey(1.toByte(), 59f)] =
                createSDMap(94.7f, 99.5f, 104.2f, 108.9f, 113.6f, 118.3f, 123.1f)
            zscoreMap[ZScoreTableKey(1.toByte(), 60f)] =
                createSDMap(95.2f, 99.9f, 104.7f, 109.4f, 114.2f, 118.9f, 123.7f)
            return zscoreMap
        }
    val zscoreHFATableBoy: Map<ZScoreTableKey, Map<Float, Int>>
        get() {
            val zscoreMap: MutableMap<ZScoreTableKey, Map<Float, Int>> = HashMap()
            zscoreMap[ZScoreTableKey(0.toByte(), 0f)] = createSDMap(44.2f, 46.1f, 48.0f, 49.9f, 51.8f, 53.7f, 55.6f)
            zscoreMap[ZScoreTableKey(0.toByte(), 1f)] = createSDMap(48.9f, 50.8f, 52.8f, 54.7f, 56.7f, 58.6f, 60.6f)
            zscoreMap[ZScoreTableKey(0.toByte(), 2f)] = createSDMap(52.4f, 54.4f, 56.4f, 58.4f, 60.4f, 62.4f, 64.4f)
            zscoreMap[ZScoreTableKey(0.toByte(), 3f)] = createSDMap(55.3f, 57.3f, 59.4f, 61.4f, 63.5f, 65.5f, 67.6f)
            zscoreMap[ZScoreTableKey(0.toByte(), 4f)] = createSDMap(57.6f, 59.7f, 61.8f, 63.9f, 66.0f, 68.0f, 70.1f)
            zscoreMap[ZScoreTableKey(0.toByte(), 5f)] = createSDMap(59.6f, 61.7f, 63.8f, 65.9f, 68.0f, 70.1f, 72.2f)
            zscoreMap[ZScoreTableKey(0.toByte(), 6f)] = createSDMap(61.2f, 63.3f, 65.5f, 67.6f, 69.8f, 71.9f, 74.0f)
            zscoreMap[ZScoreTableKey(0.toByte(), 7f)] = createSDMap(62.7f, 64.8f, 67.0f, 69.2f, 71.3f, 73.5f, 75.7f)
            zscoreMap[ZScoreTableKey(0.toByte(), 8f)] = createSDMap(64.0f, 66.2f, 68.4f, 70.6f, 72.8f, 75.0f, 77.2f)
            zscoreMap[ZScoreTableKey(0.toByte(), 9f)] = createSDMap(65.2f, 67.5f, 69.7f, 72.0f, 74.2f, 76.5f, 78.7f)
            zscoreMap[ZScoreTableKey(0.toByte(), 10f)] = createSDMap(66.4f, 68.7f, 71.0f, 73.3f, 75.6f, 77.9f, 80.1f)
            zscoreMap[ZScoreTableKey(0.toByte(), 11f)] =
                createSDMap(67.6f, 69.9f, 72.2f, 74.5f, 76.9f, 79.2f, 81.5f)
            zscoreMap[ZScoreTableKey(0.toByte(), 12f)] =
                createSDMap(68.6f, 71.0f, 73.4f, 75.7f, 78.1f, 80.5f, 82.9f)
            zscoreMap[ZScoreTableKey(0.toByte(), 13f)] =
                createSDMap(69.6f, 72.1f, 74.5f, 76.9f, 79.3f, 81.8f, 84.2f)
            zscoreMap[ZScoreTableKey(0.toByte(), 14f)] =
                createSDMap(70.6f, 73.1f, 75.6f, 78.0f, 80.5f, 83.0f, 85.5f)
            zscoreMap[ZScoreTableKey(0.toByte(), 15f)] =
                createSDMap(71.6f, 74.1f, 76.6f, 79.1f, 81.7f, 84.2f, 86.7f)
            zscoreMap[ZScoreTableKey(0.toByte(), 16f)] =
                createSDMap(72.5f, 75.0f, 77.6f, 80.2f, 82.8f, 85.4f, 88.0f)
            zscoreMap[ZScoreTableKey(0.toByte(), 17f)] = createSDMap(73.3f, 76.0f, 78.6f, 81.2f, 83.9f, 86.5f, 89.2f)
            zscoreMap[ZScoreTableKey(0.toByte(), 18f)] = createSDMap(74.2f, 76.9f, 79.6f, 82.3f, 85.0f, 87.7f, 90.4f)
            zscoreMap[ZScoreTableKey(0.toByte(), 19f)] =
                createSDMap(75.0f, 77.7f, 80.5f, 83.2f, 86.0f, 88.8f, 91.5f)
            zscoreMap[ZScoreTableKey(0.toByte(), 20f)] = createSDMap(75.8f, 78.6f, 81.4f, 84.2f, 87.0f, 89.8f, 92.6f)
            zscoreMap[ZScoreTableKey(0.toByte(), 21f)] =
                createSDMap(76.5f, 79.4f, 82.3f, 85.1f, 88.0f, 90.9f, 93.8f)
            zscoreMap[ZScoreTableKey(0.toByte(), 22f)] =
                createSDMap(77.2f, 80.2f, 83.1f, 86.0f, 89.0f, 91.9f, 94.9f)
            zscoreMap[ZScoreTableKey(0.toByte(), 23f)] = createSDMap(78.0f, 81.0f, 83.9f, 86.9f, 89.9f, 92.9f, 95.9f)
            zscoreMap[ZScoreTableKey(0.toByte(), 24f)] = createSDMap(78.0f, 81.0f, 84.1f, 87.1f, 90.2f, 93.2f, 96.3f)
            zscoreMap[ZScoreTableKey(0.toByte(), 25f)] = createSDMap(78.6f, 81.7f, 84.9f, 88.0f, 91.1f, 94.2f, 97.3f)
            zscoreMap[ZScoreTableKey(0.toByte(), 26f)] =
                createSDMap(79.3f, 82.5f, 85.6f, 88.8f, 92.0f, 95.2f, 98.3f)
            zscoreMap[ZScoreTableKey(0.toByte(), 27f)] = createSDMap(79.9f, 83.1f, 86.4f, 89.6f, 92.9f, 96.1f, 99.3f)
            zscoreMap[ZScoreTableKey(0.toByte(), 28f)] = createSDMap(80.5f, 83.8f, 87.1f, 90.4f, 93.7f, 97.0f, 100.3f)
            zscoreMap[ZScoreTableKey(0.toByte(), 29f)] = createSDMap(81.1f, 84.5f, 87.8f, 91.2f, 94.5f, 97.9f, 101.2f)
            zscoreMap[ZScoreTableKey(0.toByte(), 30f)] = createSDMap(81.7f, 85.1f, 88.5f, 91.9f, 95.3f, 98.7f, 102.1f)
            zscoreMap[ZScoreTableKey(0.toByte(), 31f)] = createSDMap(82.3f, 85.7f, 89.2f, 92.7f, 96.1f, 99.6f, 103.0f)
            zscoreMap[ZScoreTableKey(0.toByte(), 32f)] = createSDMap(82.8f, 86.4f, 89.9f, 93.4f, 96.9f, 100.4f, 103.9f)
            zscoreMap[ZScoreTableKey(0.toByte(), 33f)] = createSDMap(83.4f, 86.9f, 90.5f, 94.1f, 97.6f, 101.2f, 104.8f)
            zscoreMap[ZScoreTableKey(0.toByte(), 34f)] = createSDMap(83.9f, 87.5f, 91.1f, 94.8f, 98.4f, 102.0f, 105.6f)
            zscoreMap[ZScoreTableKey(0.toByte(), 35f)] =
                createSDMap(84.4f, 88.1f, 91.8f, 95.4f, 99.1f, 102.7f, 106.4f)
            zscoreMap[ZScoreTableKey(0.toByte(), 36f)] =
                createSDMap(85.0f, 88.7f, 92.4f, 96.1f, 99.8f, 103.5f, 107.2f)
            zscoreMap[ZScoreTableKey(0.toByte(), 37f)] = createSDMap(85.5f, 89.2f, 93.0f, 96.7f, 100.5f, 104.2f, 108.0f)
            zscoreMap[ZScoreTableKey(0.toByte(), 38f)] =
                createSDMap(86.0f, 89.8f, 93.6f, 97.4f, 101.2f, 105.0f, 108.8f)
            zscoreMap[ZScoreTableKey(0.toByte(), 39f)] =
                createSDMap(86.5f, 90.3f, 94.2f, 98.0f, 101.8f, 105.7f, 109.5f)
            zscoreMap[ZScoreTableKey(0.toByte(), 40f)] =
                createSDMap(87.0f, 90.9f, 94.7f, 98.6f, 102.5f, 106.4f, 110.3f)
            zscoreMap[ZScoreTableKey(0.toByte(), 41f)] = createSDMap(87.5f, 91.4f, 95.3f, 99.2f, 103.2f, 107.1f, 111.0f)
            zscoreMap[ZScoreTableKey(0.toByte(), 42f)] = createSDMap(88.0f, 91.9f, 95.9f, 99.9f, 103.8f, 107.8f, 111.7f)
            zscoreMap[ZScoreTableKey(0.toByte(), 43f)] =
                createSDMap(88.4f, 92.4f, 96.4f, 100.4f, 104.5f, 108.5f, 112.5f)
            zscoreMap[ZScoreTableKey(0.toByte(), 44f)] =
                createSDMap(88.9f, 93.0f, 97.0f, 101.0f, 105.1f, 109.1f, 113.2f)
            zscoreMap[ZScoreTableKey(0.toByte(), 45f)] =
                createSDMap(89.4f, 93.5f, 97.5f, 101.6f, 105.7f, 109.8f, 113.9f)
            zscoreMap[ZScoreTableKey(0.toByte(), 46f)] =
                createSDMap(89.8f, 94.0f, 98.1f, 102.2f, 106.3f, 110.4f, 114.6f)
            zscoreMap[ZScoreTableKey(0.toByte(), 47f)] =
                createSDMap(90.3f, 94.4f, 98.6f, 102.8f, 106.9f, 111.1f, 115.2f)
            zscoreMap[ZScoreTableKey(0.toByte(), 48f)] =
                createSDMap(90.7f, 94.9f, 99.1f, 103.3f, 107.5f, 111.7f, 115.9f)
            zscoreMap[ZScoreTableKey(0.toByte(), 49f)] =
                createSDMap(91.2f, 95.4f, 99.7f, 103.9f, 108.1f, 112.4f, 116.6f)
            zscoreMap[ZScoreTableKey(0.toByte(), 50f)] =
                createSDMap(91.6f, 95.9f, 100.2f, 104.4f, 108.7f, 113.0f, 117.3f)
            zscoreMap[ZScoreTableKey(0.toByte(), 51f)] =
                createSDMap(92.1f, 96.4f, 100.7f, 105.0f, 109.3f, 113.6f, 117.9f)
            zscoreMap[ZScoreTableKey(0.toByte(), 52f)] =
                createSDMap(92.5f, 96.9f, 101.2f, 105.6f, 109.9f, 114.2f, 118.6f)
            zscoreMap[ZScoreTableKey(0.toByte(), 53f)] =
                createSDMap(93.0f, 97.4f, 101.7f, 106.1f, 110.5f, 114.9f, 119.2f)
            zscoreMap[ZScoreTableKey(0.toByte(), 54f)] =
                createSDMap(93.4f, 97.8f, 102.3f, 106.7f, 111.1f, 115.5f, 119.9f)
            zscoreMap[ZScoreTableKey(0.toByte(), 55f)] =
                createSDMap(93.9f, 98.3f, 102.8f, 107.2f, 111.7f, 116.1f, 120.6f)
            zscoreMap[ZScoreTableKey(0.toByte(), 56f)] =
                createSDMap(94.3f, 98.8f, 103.3f, 107.8f, 112.3f, 116.7f, 121.2f)
            zscoreMap[ZScoreTableKey(0.toByte(), 57f)] =
                createSDMap(94.7f, 99.3f, 103.8f, 108.3f, 112.8f, 117.4f, 121.9f)
            zscoreMap[ZScoreTableKey(0.toByte(), 58f)] =
                createSDMap(95.2f, 99.7f, 104.3f, 108.9f, 113.4f, 118.0f, 122.6f)
            zscoreMap[ZScoreTableKey(0.toByte(), 59f)] =
                createSDMap(95.6f, 100.2f, 104.8f, 109.4f, 114.0f, 118.6f, 123.2f)
            zscoreMap[ZScoreTableKey(0.toByte(), 60f)] =
                createSDMap(96.1f, 100.7f, 105.3f, 110.0f, 114.6f, 119.2f, 123.9f)
            return zscoreMap
        }
    val zscoreWFHTableGirl: Map<ZScoreTableKey, Map<Float, Int>>
        get() {
            val zscoreMap: MutableMap<ZScoreTableKey, Map<Float, Int>> = HashMap()
            zscoreMap[ZScoreTableKey(1.toByte(), 45f)] = createSDMap(1.9f, 2.1f, 2.3f, 2.5f, 2.7f, 3.0f, 3.3f)
            zscoreMap[ZScoreTableKey(1.toByte(), 45.5.toFloat())] =
                createSDMap(2.0f, 2.1f, 2.3f, 2.5f, 2.8f, 3.1f, 3.4f)
            zscoreMap[ZScoreTableKey(1.toByte(), 46f)] = createSDMap(2.0f, 2.2f, 2.4f, 2.6f, 2.9f, 3.2f, 3.5f)
            zscoreMap[ZScoreTableKey(1.toByte(), 46.5.toFloat())] =
                createSDMap(2.1f, 2.3f, 2.5f, 2.7f, 3.0f, 3.3f, 3.6f)
            zscoreMap[ZScoreTableKey(1.toByte(), 47f)] = createSDMap(2.2f, 2.4f, 2.6f, 2.8f, 3.1f, 3.4f, 3.7f)
            zscoreMap[ZScoreTableKey(1.toByte(), 47.5.toFloat())] =
                createSDMap(2.2f, 2.4f, 2.6f, 2.9f, 3.2f, 3.5f, 3.8f)
            zscoreMap[ZScoreTableKey(1.toByte(), 48f)] = createSDMap(2.3f, 2.5f, 2.7f, 3.0f, 3.3f, 3.6f, 4.0f)
            zscoreMap[ZScoreTableKey(1.toByte(), 48.5.toFloat())] =
                createSDMap(2.4f, 2.6f, 2.8f, 3.1f, 3.4f, 3.7f, 4.1f)
            zscoreMap[ZScoreTableKey(1.toByte(), 49f)] = createSDMap(2.4f, 2.6f, 2.9f, 3.2f, 3.5f, 3.8f, 4.2f)
            zscoreMap[ZScoreTableKey(1.toByte(), 49.5.toFloat())] =
                createSDMap(2.5f, 2.7f, 3.0f, 3.3f, 3.6f, 3.9f, 4.3f)
            zscoreMap[ZScoreTableKey(1.toByte(), 50f)] = createSDMap(2.6f, 2.8f, 3.1f, 3.4f, 3.7f, 4.0f, 4.5f)
            zscoreMap[ZScoreTableKey(1.toByte(), 50.5.toFloat())] =
                createSDMap(2.7f, 2.9f, 3.2f, 3.5f, 3.8f, 4.2f, 4.6f)
            zscoreMap[ZScoreTableKey(1.toByte(), 51f)] = createSDMap(2.8f, 3.0f, 3.3f, 3.6f, 3.9f, 4.3f, 4.8f)
            zscoreMap[ZScoreTableKey(1.toByte(), 51.5.toFloat())] =
                createSDMap(2.8f, 3.1f, 3.4f, 3.7f, 4.0f, 4.4f, 4.9f)
            zscoreMap[ZScoreTableKey(1.toByte(), 52f)] = createSDMap(2.9f, 3.2f, 3.5f, 3.8f, 4.2f, 4.6f, 5.1f)
            zscoreMap[ZScoreTableKey(1.toByte(), 52.5.toFloat())] =
                createSDMap(3.0f, 3.3f, 3.6f, 3.9f, 4.3f, 4.7f, 5.2f)
            zscoreMap[ZScoreTableKey(1.toByte(), 53f)] = createSDMap(3.1f, 3.4f, 3.7f, 4.0f, 4.4f, 4.9f, 5.4f)
            zscoreMap[ZScoreTableKey(1.toByte(), 53.5.toFloat())] =
                createSDMap(3.2f, 3.5f, 3.8f, 4.2f, 4.6f, 5.0f, 5.5f)
            zscoreMap[ZScoreTableKey(1.toByte(), 54f)] = createSDMap(3.3f, 3.6f, 3.9f, 4.3f, 4.7f, 5.2f, 5.7f)
            zscoreMap[ZScoreTableKey(1.toByte(), 54.5.toFloat())] =
                createSDMap(3.4f, 3.7f, 4.0f, 4.4f, 4.8f, 5.3f, 5.9f)
            zscoreMap[ZScoreTableKey(1.toByte(), 55f)] = createSDMap(3.5f, 3.8f, 4.2f, 4.5f, 5.0f, 5.5f, 6.1f)
            zscoreMap[ZScoreTableKey(1.toByte(), 55.5.toFloat())] =
                createSDMap(3.6f, 3.9f, 4.3f, 4.7f, 5.1f, 5.7f, 6.3f)
            zscoreMap[ZScoreTableKey(1.toByte(), 56f)] = createSDMap(3.7f, 4.0f, 4.4f, 4.8f, 5.3f, 5.8f, 6.4f)
            zscoreMap[ZScoreTableKey(1.toByte(), 56.5.toFloat())] =
                createSDMap(3.8f, 4.1f, 4.5f, 5.0f, 5.4f, 6.0f, 6.6f)
            zscoreMap[ZScoreTableKey(1.toByte(), 57f)] = createSDMap(3.9f, 4.3f, 4.6f, 5.1f, 5.6f, 6.1f, 6.8f)
            zscoreMap[ZScoreTableKey(1.toByte(), 57.5.toFloat())] =
                createSDMap(4.0f, 4.4f, 4.8f, 5.2f, 5.7f, 6.3f, 7.0f)
            zscoreMap[ZScoreTableKey(1.toByte(), 58f)] = createSDMap(4.1f, 4.5f, 4.9f, 5.4f, 5.9f, 6.5f, 7.1f)
            zscoreMap[ZScoreTableKey(1.toByte(), 58.5.toFloat())] =
                createSDMap(4.2f, 4.6f, 5.0f, 5.5f, 6.0f, 6.6f, 7.3f)
            zscoreMap[ZScoreTableKey(1.toByte(), 59f)] = createSDMap(4.3f, 4.7f, 5.1f, 5.6f, 6.2f, 6.8f, 7.5f)
            zscoreMap[ZScoreTableKey(1.toByte(), 59.5.toFloat())] =
                createSDMap(4.4f, 4.8f, 5.3f, 5.7f, 6.3f, 6.9f, 7.7f)
            zscoreMap[ZScoreTableKey(1.toByte(), 60f)] = createSDMap(4.5f, 4.9f, 5.4f, 5.9f, 6.4f, 7.1f, 7.8f)
            zscoreMap[ZScoreTableKey(1.toByte(), 60.5.toFloat())] =
                createSDMap(4.6f, 5.0f, 5.5f, 6.0f, 6.6f, 7.3f, 8.0f)
            zscoreMap[ZScoreTableKey(1.toByte(), 61f)] = createSDMap(4.7f, 5.1f, 5.6f, 6.1f, 6.7f, 7.4f, 8.2f)
            zscoreMap[ZScoreTableKey(1.toByte(), 61.5.toFloat())] =
                createSDMap(4.8f, 5.2f, 5.7f, 6.3f, 6.9f, 7.6f, 8.4f)
            zscoreMap[ZScoreTableKey(1.toByte(), 62f)] = createSDMap(4.9f, 5.3f, 5.8f, 6.4f, 7.0f, 7.7f, 8.5f)
            zscoreMap[ZScoreTableKey(1.toByte(), 62.5.toFloat())] =
                createSDMap(5.0f, 5.4f, 5.9f, 6.5f, 7.1f, 7.8f, 8.7f)
            zscoreMap[ZScoreTableKey(1.toByte(), 63f)] = createSDMap(5.1f, 5.5f, 6.0f, 6.6f, 7.3f, 8.0f, 8.8f)
            zscoreMap[ZScoreTableKey(1.toByte(), 63.5.toFloat())] =
                createSDMap(5.2f, 5.6f, 6.2f, 6.7f, 7.4f, 8.1f, 9.0f)
            zscoreMap[ZScoreTableKey(1.toByte(), 64f)] = createSDMap(5.3f, 5.7f, 6.3f, 6.9f, 7.5f, 8.3f, 9.1f)
            zscoreMap[ZScoreTableKey(1.toByte(), 64.5.toFloat())] =
                createSDMap(5.4f, 5.8f, 6.4f, 7.0f, 7.6f, 8.4f, 9.3f)
            zscoreMap[ZScoreTableKey(1.toByte(), 65f)] = createSDMap(5.5f, 5.9f, 6.5f, 7.1f, 7.8f, 8.6f, 9.5f)
            zscoreMap[ZScoreTableKey(1.toByte(), 65.5.toFloat())] =
                createSDMap(5.5f, 6.0f, 6.6f, 7.2f, 7.9f, 8.7f, 9.6f)
            zscoreMap[ZScoreTableKey(1.toByte(), 66f)] = createSDMap(5.6f, 6.1f, 6.7f, 7.3f, 8.0f, 8.8f, 9.8f)
            zscoreMap[ZScoreTableKey(1.toByte(), 66.5.toFloat())] =
                createSDMap(5.7f, 6.2f, 6.8f, 7.4f, 8.1f, 9.0f, 9.9f)
            zscoreMap[ZScoreTableKey(1.toByte(), 67f)] = createSDMap(5.8f, 6.3f, 6.9f, 7.5f, 8.3f, 9.1f, 10.0f)
            zscoreMap[ZScoreTableKey(1.toByte(), 67.5.toFloat())] =
                createSDMap(5.9f, 6.4f, 7.0f, 7.6f, 8.4f, 9.2f, 10.2f)
            zscoreMap[ZScoreTableKey(1.toByte(), 68f)] = createSDMap(6.0f, 6.5f, 7.1f, 7.7f, 8.5f, 9.4f, 10.3f)
            zscoreMap[ZScoreTableKey(1.toByte(), 68.5.toFloat())] =
                createSDMap(6.1f, 6.6f, 7.2f, 7.9f, 8.6f, 9.5f, 10.5f)
            zscoreMap[ZScoreTableKey(1.toByte(), 69f)] = createSDMap(6.1f, 6.7f, 7.3f, 8.0f, 8.7f, 9.6f, 10.6f)
            zscoreMap[ZScoreTableKey(1.toByte(), 69.5.toFloat())] =
                createSDMap(6.2f, 6.8f, 7.4f, 8.1f, 8.8f, 9.7f, 10.7f)
            zscoreMap[ZScoreTableKey(1.toByte(), 70f)] = createSDMap(6.3f, 6.9f, 7.5f, 8.2f, 9.0f, 9.9f, 10.9f)
            zscoreMap[ZScoreTableKey(1.toByte(), 70.5.toFloat())] =
                createSDMap(6.4f, 6.9f, 7.6f, 8.3f, 9.1f, 10.0f, 11.0f)
            zscoreMap[ZScoreTableKey(1.toByte(), 71f)] = createSDMap(6.5f, 7.0f, 7.7f, 8.4f, 9.2f, 10.1f, 11.1f)
            zscoreMap[ZScoreTableKey(1.toByte(), 71.5.toFloat())] =
                createSDMap(6.5f, 7.1f, 7.7f, 8.5f, 9.3f, 10.2f, 11.3f)
            zscoreMap[ZScoreTableKey(1.toByte(), 72f)] = createSDMap(6.6f, 7.2f, 7.8f, 8.6f, 9.4f, 10.3f, 11.4f)
            zscoreMap[ZScoreTableKey(1.toByte(), 72.5.toFloat())] =
                createSDMap(6.7f, 7.3f, 7.9f, 8.7f, 9.5f, 10.5f, 11.5f)
            zscoreMap[ZScoreTableKey(1.toByte(), 73f)] = createSDMap(6.8f, 7.4f, 8.0f, 8.8f, 9.6f, 10.6f, 11.7f)
            zscoreMap[ZScoreTableKey(1.toByte(), 73.5.toFloat())] =
                createSDMap(6.9f, 7.4f, 8.1f, 8.9f, 9.7f, 10.7f, 11.8f)
            zscoreMap[ZScoreTableKey(1.toByte(), 74f)] = createSDMap(6.9f, 7.5f, 8.2f, 9.0f, 9.8f, 10.8f, 11.9f)
            zscoreMap[ZScoreTableKey(1.toByte(), 74.5.toFloat())] =
                createSDMap(7.0f, 7.6f, 8.3f, 9.1f, 9.9f, 10.9f, 12.0f)
            zscoreMap[ZScoreTableKey(1.toByte(), 75f)] = createSDMap(7.1f, 7.7f, 8.4f, 9.1f, 10.0f, 11.0f, 12.2f)
            zscoreMap[ZScoreTableKey(1.toByte(), 75.5.toFloat())] =
                createSDMap(7.1f, 7.8f, 8.5f, 9.2f, 10.1f, 11.1f, 12.3f)
            zscoreMap[ZScoreTableKey(1.toByte(), 76f)] = createSDMap(7.2f, 7.8f, 8.5f, 9.3f, 10.2f, 11.2f, 12.4f)
            zscoreMap[ZScoreTableKey(1.toByte(), 76.5.toFloat())] =
                createSDMap(7.3f, 7.9f, 8.6f, 9.4f, 10.3f, 11.4f, 12.5f)
            zscoreMap[ZScoreTableKey(1.toByte(), 77f)] = createSDMap(7.4f, 8.0f, 8.7f, 9.5f, 10.4f, 11.5f, 12.6f)
            zscoreMap[ZScoreTableKey(1.toByte(), 77.5.toFloat())] =
                createSDMap(7.4f, 8.1f, 8.8f, 9.6f, 10.5f, 11.6f, 12.8f)
            zscoreMap[ZScoreTableKey(1.toByte(), 78f)] = createSDMap(7.5f, 8.2f, 8.9f, 9.7f, 10.6f, 11.7f, 12.9f)
            zscoreMap[ZScoreTableKey(1.toByte(), 78.5.toFloat())] =
                createSDMap(7.6f, 8.2f, 9.0f, 9.8f, 10.7f, 11.8f, 13.0f)
            zscoreMap[ZScoreTableKey(1.toByte(), 79f)] = createSDMap(7.7f, 8.3f, 9.1f, 9.9f, 10.8f, 11.9f, 13.1f)
            zscoreMap[ZScoreTableKey(1.toByte(), 79.5.toFloat())] =
                createSDMap(7.7f, 8.4f, 9.1f, 10.0f, 10.9f, 12.0f, 13.3f)
            zscoreMap[ZScoreTableKey(1.toByte(), 80f)] = createSDMap(7.8f, 8.5f, 9.2f, 10.1f, 11.0f, 12.1f, 13.4f)
            zscoreMap[ZScoreTableKey(1.toByte(), 80.5.toFloat())] =
                createSDMap(7.9f, 8.6f, 9.3f, 10.2f, 11.2f, 12.3f, 13.5f)
            zscoreMap[ZScoreTableKey(1.toByte(), 81f)] = createSDMap(8.0f, 8.7f, 9.4f, 10.3f, 11.3f, 12.4f, 13.7f)
            zscoreMap[ZScoreTableKey(1.toByte(), 81.5.toFloat())] =
                createSDMap(8.1f, 8.8f, 9.5f, 10.4f, 11.4f, 12.5f, 13.8f)
            zscoreMap[ZScoreTableKey(1.toByte(), 82f)] = createSDMap(8.1f, 8.8f, 9.6f, 10.5f, 11.5f, 12.6f, 13.9f)
            zscoreMap[ZScoreTableKey(1.toByte(), 82.5.toFloat())] =
                createSDMap(8.2f, 8.9f, 9.7f, 10.6f, 11.6f, 12.8f, 14.1f)
            zscoreMap[ZScoreTableKey(1.toByte(), 83f)] = createSDMap(8.3f, 9.0f, 9.8f, 10.7f, 11.8f, 12.9f, 14.2f)
            zscoreMap[ZScoreTableKey(1.toByte(), 83.5.toFloat())] =
                createSDMap(8.4f, 9.1f, 9.9f, 10.9f, 11.9f, 13.1f, 14.4f)
            zscoreMap[ZScoreTableKey(1.toByte(), 84f)] = createSDMap(8.5f, 9.2f, 10.1f, 11.0f, 12.0f, 13.2f, 14.5f)
            zscoreMap[ZScoreTableKey(1.toByte(), 84.5.toFloat())] =
                createSDMap(8.6f, 9.3f, 10.2f, 11.1f, 12.1f, 13.3f, 14.7f)
            zscoreMap[ZScoreTableKey(1.toByte(), 85f)] = createSDMap(8.7f, 9.4f, 10.3f, 11.2f, 12.3f, 13.5f, 14.9f)
            zscoreMap[ZScoreTableKey(1.toByte(), 85.5.toFloat())] =
                createSDMap(8.8f, 9.5f, 10.4f, 11.3f, 12.4f, 13.6f, 15.0f)
            zscoreMap[ZScoreTableKey(1.toByte(), 86f)] = createSDMap(8.9f, 9.7f, 10.5f, 11.5f, 12.6f, 13.8f, 15.2f)
            zscoreMap[ZScoreTableKey(1.toByte(), 86.5.toFloat())] =
                createSDMap(9.0f, 9.8f, 10.6f, 11.6f, 12.7f, 13.9f, 15.4f)
            zscoreMap[ZScoreTableKey(1.toByte(), 87f)] = createSDMap(9.1f, 9.9f, 10.7f, 11.7f, 12.8f, 14.1f, 15.5f)
            zscoreMap[ZScoreTableKey(1.toByte(), 87.5.toFloat())] =
                createSDMap(9.2f, 10.0f, 10.9f, 11.8f, 13.0f, 14.2f, 15.7f)
            zscoreMap[ZScoreTableKey(1.toByte(), 88f)] = createSDMap(9.3f, 10.1f, 11.0f, 12.0f, 13.1f, 14.4f, 15.9f)
            zscoreMap[ZScoreTableKey(1.toByte(), 88.5.toFloat())] =
                createSDMap(9.4f, 10.2f, 11.1f, 12.1f, 13.2f, 14.5f, 16.0f)
            zscoreMap[ZScoreTableKey(1.toByte(), 89f)] = createSDMap(9.5f, 10.3f, 11.2f, 12.2f, 13.4f, 14.7f, 16.2f)
            zscoreMap[ZScoreTableKey(1.toByte(), 89.5.toFloat())] =
                createSDMap(9.6f, 10.4f, 11.3f, 12.3f, 13.5f, 14.8f, 16.4f)
            zscoreMap[ZScoreTableKey(1.toByte(), 90f)] = createSDMap(9.7f, 10.5f, 11.4f, 12.5f, 13.7f, 15.0f, 16.5f)
            zscoreMap[ZScoreTableKey(1.toByte(), 90.5.toFloat())] =
                createSDMap(9.8f, 10.6f, 11.5f, 12.6f, 13.8f, 15.1f, 16.7f)
            zscoreMap[ZScoreTableKey(1.toByte(), 91f)] = createSDMap(9.9f, 10.7f, 11.7f, 12.7f, 13.9f, 15.3f, 16.9f)
            zscoreMap[ZScoreTableKey(1.toByte(), 91.5.toFloat())] =
                createSDMap(10.0f, 10.8f, 11.8f, 12.8f, 14.1f, 15.5f, 17.0f)
            zscoreMap[ZScoreTableKey(1.toByte(), 92f)] = createSDMap(10.1f, 10.9f, 11.9f, 13.0f, 14.2f, 15.6f, 17.2f)
            zscoreMap[ZScoreTableKey(1.toByte(), 92.5.toFloat())] =
                createSDMap(10.1f, 11.0f, 12.0f, 13.1f, 14.3f, 15.8f, 17.4f)
            zscoreMap[ZScoreTableKey(1.toByte(), 93f)] =
                createSDMap(10.2f, 11.1f, 12.1f, 13.2f, 14.5f, 15.9f, 17.5f)
            zscoreMap[ZScoreTableKey(1.toByte(), 93.5.toFloat())] =
                createSDMap(10.3f, 11.2f, 12.2f, 13.3f, 14.6f, 16.1f, 17.7f)
            zscoreMap[ZScoreTableKey(1.toByte(), 94f)] = createSDMap(10.4f, 11.3f, 12.3f, 13.5f, 14.7f, 16.2f, 17.9f)
            zscoreMap[ZScoreTableKey(1.toByte(), 94.5.toFloat())] =
                createSDMap(10.5f, 11.4f, 12.4f, 13.6f, 14.9f, 16.4f, 18.0f)
            zscoreMap[ZScoreTableKey(1.toByte(), 95f)] = createSDMap(10.6f, 11.5f, 12.6f, 13.7f, 15.0f, 16.5f, 18.2f)
            zscoreMap[ZScoreTableKey(1.toByte(), 95.5.toFloat())] =
                createSDMap(10.7f, 11.6f, 12.7f, 13.8f, 15.2f, 16.7f, 18.4f)
            zscoreMap[ZScoreTableKey(1.toByte(), 96f)] = createSDMap(10.8f, 11.7f, 12.8f, 14.0f, 15.3f, 16.8f, 18.6f)
            zscoreMap[ZScoreTableKey(1.toByte(), 96.5.toFloat())] =
                createSDMap(10.9f, 11.8f, 12.9f, 14.1f, 15.4f, 17.0f, 18.7f)
            zscoreMap[ZScoreTableKey(1.toByte(), 97f)] = createSDMap(11.0f, 12.0f, 13.0f, 14.2f, 15.6f, 17.1f, 18.9f)
            zscoreMap[ZScoreTableKey(1.toByte(), 97.5.toFloat())] =
                createSDMap(11.1f, 12.1f, 13.1f, 14.4f, 15.7f, 17.3f, 19.1f)
            zscoreMap[ZScoreTableKey(1.toByte(), 98f)] = createSDMap(11.2f, 12.2f, 13.3f, 14.5f, 15.9f, 17.5f, 19.3f)
            zscoreMap[ZScoreTableKey(1.toByte(), 98.5.toFloat())] =
                createSDMap(11.3f, 12.3f, 13.4f, 14.6f, 16.0f, 17.6f, 19.5f)
            zscoreMap[ZScoreTableKey(1.toByte(), 99f)] = createSDMap(11.4f, 12.4f, 13.5f, 14.8f, 16.2f, 17.8f, 19.6f)
            zscoreMap[ZScoreTableKey(1.toByte(), 99.5.toFloat())] =
                createSDMap(11.5f, 12.5f, 13.6f, 14.9f, 16.3f, 18.0f, 19.8f)
            zscoreMap[ZScoreTableKey(1.toByte(), 100f)] = createSDMap(11.6f, 12.6f, 13.7f, 15.0f, 16.5f, 18.1f, 20.0f)
            zscoreMap[ZScoreTableKey(1.toByte(), 100.5.toFloat())] =
                createSDMap(11.7f, 12.7f, 13.9f, 15.2f, 16.6f, 18.3f, 20.2f)
            zscoreMap[ZScoreTableKey(1.toByte(), 101f)] =
                createSDMap(11.8f, 12.8f, 14.0f, 15.3f, 16.8f, 18.5f, 20.4f)
            zscoreMap[ZScoreTableKey(1.toByte(), 101.5.toFloat())] =
                createSDMap(11.9f, 13.0f, 14.1f, 15.5f, 17.0f, 18.7f, 20.6f)
            zscoreMap[ZScoreTableKey(1.toByte(), 102f)] =
                createSDMap(12.0f, 13.1f, 14.3f, 15.6f, 17.1f, 18.9f, 20.8f)
            zscoreMap[ZScoreTableKey(1.toByte(), 102.5.toFloat())] =
                createSDMap(12.1f, 13.2f, 14.4f, 15.8f, 17.3f, 19.0f, 21.0f)
            zscoreMap[ZScoreTableKey(1.toByte(), 103f)] = createSDMap(12.3f, 13.3f, 14.5f, 15.9f, 17.5f, 19.2f, 21.3f)
            zscoreMap[ZScoreTableKey(1.toByte(), 103.5.toFloat())] =
                createSDMap(12.4f, 13.5f, 14.7f, 16.1f, 17.6f, 19.4f, 21.5f)
            zscoreMap[ZScoreTableKey(1.toByte(), 104f)] = createSDMap(12.5f, 13.6f, 14.8f, 16.2f, 17.8f, 19.6f, 21.7f)
            zscoreMap[ZScoreTableKey(1.toByte(), 104.5.toFloat())] =
                createSDMap(12.6f, 13.7f, 15.0f, 16.4f, 18.0f, 19.8f, 21.9f)
            zscoreMap[ZScoreTableKey(1.toByte(), 105f)] = createSDMap(12.7f, 13.8f, 15.1f, 16.5f, 18.2f, 20.0f, 22.2f)
            zscoreMap[ZScoreTableKey(1.toByte(), 105.5.toFloat())] =
                createSDMap(12.8f, 14.0f, 15.3f, 16.7f, 18.4f, 20.2f, 22.4f)
            zscoreMap[ZScoreTableKey(1.toByte(), 106f)] = createSDMap(13.0f, 14.1f, 15.4f, 16.9f, 18.5f, 20.5f, 22.6f)
            zscoreMap[ZScoreTableKey(1.toByte(), 106.5.toFloat())] =
                createSDMap(13.1f, 14.3f, 15.6f, 17.1f, 18.7f, 20.7f, 22.9f)
            zscoreMap[ZScoreTableKey(1.toByte(), 107f)] =
                createSDMap(13.2f, 14.4f, 15.7f, 17.2f, 18.9f, 20.9f, 23.1f)
            zscoreMap[ZScoreTableKey(1.toByte(), 107.5.toFloat())] =
                createSDMap(13.3f, 14.5f, 15.9f, 17.4f, 19.1f, 21.1f, 23.4f)
            zscoreMap[ZScoreTableKey(1.toByte(), 108f)] = createSDMap(13.5f, 14.7f, 16.0f, 17.6f, 19.3f, 21.3f, 23.6f)
            zscoreMap[ZScoreTableKey(1.toByte(), 108.5.toFloat())] =
                createSDMap(13.6f, 14.8f, 16.2f, 17.8f, 19.5f, 21.6f, 23.9f)
            zscoreMap[ZScoreTableKey(1.toByte(), 109f)] = createSDMap(13.7f, 15.0f, 16.4f, 18.0f, 19.7f, 21.8f, 24.2f)
            zscoreMap[ZScoreTableKey(1.toByte(), 109.5.toFloat())] =
                createSDMap(13.9f, 15.1f, 16.5f, 18.1f, 20.0f, 22.0f, 24.4f)
            zscoreMap[ZScoreTableKey(1.toByte(), 110f)] = createSDMap(14.0f, 15.3f, 16.7f, 18.3f, 20.2f, 22.3f, 24.7f)
            zscoreMap[ZScoreTableKey(1.toByte(), 110.5.toFloat())] =
                createSDMap(14.4f, 15.7f, 17.1f, 18.8f, 20.7f, 22.9f, 25.4f)
            zscoreMap[ZScoreTableKey(1.toByte(), 111f)] = createSDMap(14.5f, 15.8f, 17.3f, 19.0f, 20.9f, 23.1f, 25.7f)
            zscoreMap[ZScoreTableKey(1.toByte(), 111.5.toFloat())] =
                createSDMap(14.7f, 16.0f, 17.5f, 19.2f, 21.2f, 23.4f, 26.0f)
            zscoreMap[ZScoreTableKey(1.toByte(), 112f)] =
                createSDMap(0.09507f, 14.8f, 16.2f, 17.7f, 19.4f, 21.4f, 23.6f)
            zscoreMap[ZScoreTableKey(1.toByte(), 112.5.toFloat())] =
                createSDMap(15.0f, 16.3f, 17.9f, 19.6f, 21.6f, 23.9f, 26.5f)
            zscoreMap[ZScoreTableKey(1.toByte(), 113f)] =
                createSDMap(15.1f, 16.5f, 18.0f, 19.8f, 21.8f, 24.2f, 26.8f)
            zscoreMap[ZScoreTableKey(1.toByte(), 113.5.toFloat())] =
                createSDMap(15.3f, 16.7f, 18.2f, 20.0f, 22.1f, 24.4f, 27.1f)
            zscoreMap[ZScoreTableKey(1.toByte(), 114f)] =
                createSDMap(15.4f, 16.8f, 18.4f, 20.2f, 22.3f, 24.7f, 27.4f)
            zscoreMap[ZScoreTableKey(1.toByte(), 114.5.toFloat())] =
                createSDMap(15.6f, 17.0f, 18.6f, 20.5f, 22.6f, 25.0f, 27.8f)
            zscoreMap[ZScoreTableKey(1.toByte(), 115f)] = createSDMap(15.7f, 17.2f, 18.8f, 20.7f, 22.8f, 25.2f, 28.1f)
            zscoreMap[ZScoreTableKey(1.toByte(), 115.5.toFloat())] =
                createSDMap(15.9f, 17.3f, 19.0f, 20.9f, 23.0f, 25.5f, 28.4f)
            zscoreMap[ZScoreTableKey(1.toByte(), 116f)] =
                createSDMap(16.0f, 17.5f, 19.2f, 21.1f, 23.3f, 25.8f, 28.7f)
            zscoreMap[ZScoreTableKey(1.toByte(), 116.5.toFloat())] =
                createSDMap(16.2f, 17.7f, 19.4f, 21.3f, 23.5f, 26.1f, 29.0f)
            zscoreMap[ZScoreTableKey(1.toByte(), 117f)] =
                createSDMap(16.3f, 17.8f, 19.6f, 21.5f, 23.8f, 26.3f, 29.3f)
            zscoreMap[ZScoreTableKey(1.toByte(), 117.5.toFloat())] =
                createSDMap(16.5f, 18.0f, 19.8f, 21.7f, 24.0f, 26.6f, 29.6f)
            zscoreMap[ZScoreTableKey(1.toByte(), 118f)] = createSDMap(16.6f, 18.2f, 19.9f, 22.0f, 24.2f, 26.9f, 29.9f)
            zscoreMap[ZScoreTableKey(1.toByte(), 118.5.toFloat())] =
                createSDMap(16.8f, 18.4f, 20.1f, 22.2f, 24.5f, 27.2f, 30.3f)
            zscoreMap[ZScoreTableKey(1.toByte(), 119f)] = createSDMap(16.9f, 18.5f, 20.3f, 22.4f, 24.7f, 27.4f, 30.6f)
            zscoreMap[ZScoreTableKey(1.toByte(), 119.5.toFloat())] =
                createSDMap(17.1f, 18.7f, 20.5f, 22.6f, 25.0f, 27.7f, 30.9f)
            zscoreMap[ZScoreTableKey(1.toByte(), 120f)] = createSDMap(17.3f, 18.9f, 20.7f, 22.8f, 25.2f, 28.0f, 31.2f)
            return zscoreMap
        }
    val zscoreWFHTableBoy: Map<ZScoreTableKey, Map<Float, Int>>
        get() {
            val zscoreMap: MutableMap<ZScoreTableKey, Map<Float, Int>> = HashMap()
            zscoreMap[ZScoreTableKey(0.toByte(), 45f)] = createSDMap(1.9f, 2f, 2.2f, 2.4f, 2.7f, 3f, 3.3f)
            zscoreMap[ZScoreTableKey(0.toByte(), 45.5.toFloat())] =
                createSDMap(1.9f, 2.1f, 2.3f, 2.5f, 2.8f, 3.1f, 3.4f)
            zscoreMap[ZScoreTableKey(0.toByte(), 46f)] = createSDMap(2f, 2.2f, 2.4f, 2.6f, 2.9f, 3.1f, 3.5f)
            zscoreMap[ZScoreTableKey(0.toByte(), 46.5.toFloat())] =
                createSDMap(2.1f, 2.3f, 2.5f, 2.7f, 3f, 3.2f, 3.6f)
            zscoreMap[ZScoreTableKey(0.toByte(), 47f)] = createSDMap(2.1f, 2.3f, 2.5f, 2.8f, 3f, 3.3f, 3.7f)
            zscoreMap[ZScoreTableKey(0.toByte(), 47.5.toFloat())] =
                createSDMap(2.2f, 2.4f, 2.6f, 2.9f, 3.1f, 3.4f, 3.8f)
            zscoreMap[ZScoreTableKey(0.toByte(), 48f)] = createSDMap(2.3f, 2.5f, 2.7f, 2.9f, 3.2f, 3.6f, 3.9f)
            zscoreMap[ZScoreTableKey(0.toByte(), 48.5.toFloat())] =
                createSDMap(2.3f, 2.6f, 2.8f, 3f, 3.3f, 3.7f, 4f)
            zscoreMap[ZScoreTableKey(0.toByte(), 49f)] = createSDMap(2.4f, 2.6f, 2.9f, 3.1f, 3.4f, 3.8f, 4.2f)
            zscoreMap[ZScoreTableKey(0.toByte(), 49.5.toFloat())] =
                createSDMap(2.5f, 2.7f, 3f, 3.2f, 3.5f, 3.9f, 4.3f)
            zscoreMap[ZScoreTableKey(0.toByte(), 50f)] = createSDMap(2.6f, 2.8f, 3f, 3.3f, 3.6f, 4f, 4.4f)
            zscoreMap[ZScoreTableKey(0.toByte(), 50.5.toFloat())] =
                createSDMap(2.7f, 2.9f, 3.1f, 3.4f, 3.8f, 4.1f, 4.5f)
            zscoreMap[ZScoreTableKey(0.toByte(), 51f)] = createSDMap(2.7f, 3f, 3.2f, 3.5f, 3.9f, 4.2f, 4.7f)
            zscoreMap[ZScoreTableKey(0.toByte(), 51.5.toFloat())] = createSDMap(2.8f, 3.1f, 3.3f, 3.6f, 4f, 4.4f, 4.8f)
            zscoreMap[ZScoreTableKey(0.toByte(), 52f)] = createSDMap(2.9f, 3.2f, 3.5f, 3.8f, 4.1f, 4.5f, 5f)
            zscoreMap[ZScoreTableKey(0.toByte(), 52.5.toFloat())] = createSDMap(3f, 3.3f, 3.6f, 3.9f, 4.2f, 4.6f, 5.1f)
            zscoreMap[ZScoreTableKey(0.toByte(), 53f)] = createSDMap(3.1f, 3.4f, 3.7f, 4f, 4.4f, 4.8f, 5.3f)
            zscoreMap[ZScoreTableKey(0.toByte(), 53.5.toFloat())] =
                createSDMap(3.2f, 3.5f, 3.8f, 4.1f, 4.5f, 4.9f, 5.4f)
            zscoreMap[ZScoreTableKey(0.toByte(), 54f)] = createSDMap(3.3f, 3.6f, 3.9f, 4.3f, 4.7f, 5.1f, 5.6f)
            zscoreMap[ZScoreTableKey(0.toByte(), 54.5.toFloat())] = createSDMap(3.4f, 3.7f, 4f, 4.4f, 4.8f, 5.3f, 5.8f)
            zscoreMap[ZScoreTableKey(0.toByte(), 55f)] = createSDMap(3.6f, 3.8f, 4.2f, 4.5f, 5f, 5.4f, 6f)
            zscoreMap[ZScoreTableKey(0.toByte(), 55.5.toFloat())] = createSDMap(3.7f, 4f, 4.3f, 4.7f, 5.1f, 5.6f, 6.1f)
            zscoreMap[ZScoreTableKey(0.toByte(), 56f)] = createSDMap(3.8f, 4.1f, 4.4f, 4.8f, 5.3f, 5.8f, 6.3f)
            zscoreMap[ZScoreTableKey(0.toByte(), 56.5.toFloat())] =
                createSDMap(3.9f, 4.2f, 4.6f, 5f, 5.4f, 5.9f, 6.5f)
            zscoreMap[ZScoreTableKey(0.toByte(), 57f)] = createSDMap(4f, 4.3f, 4.7f, 5.1f, 5.6f, 6.1f, 6.7f)
            zscoreMap[ZScoreTableKey(0.toByte(), 57.5.toFloat())] =
                createSDMap(4.1f, 4.5f, 4.9f, 5.3f, 5.7f, 6.3f, 6.9f)
            zscoreMap[ZScoreTableKey(0.toByte(), 58f)] = createSDMap(4.3f, 4.6f, 5f, 5.4f, 5.9f, 6.4f, 7.1f)
            zscoreMap[ZScoreTableKey(0.toByte(), 58.5.toFloat())] =
                createSDMap(4.4f, 4.7f, 5.1f, 5.6f, 6.1f, 6.6f, 7.2f)
            zscoreMap[ZScoreTableKey(0.toByte(), 59f)] = createSDMap(4.5f, 4.8f, 5.3f, 5.7f, 6.2f, 6.8f, 7.4f)
            zscoreMap[ZScoreTableKey(0.toByte(), 59.5.toFloat())] =
                createSDMap(4.6f, 5f, 5.4f, 5.9f, 6.4f, 7f, 7.6f)
            zscoreMap[ZScoreTableKey(0.toByte(), 60f)] = createSDMap(4.7f, 5.1f, 5.5f, 6f, 6.5f, 7.1f, 7.8f)
            zscoreMap[ZScoreTableKey(0.toByte(), 60.5.toFloat())] = createSDMap(4.8f, 5.2f, 5.6f, 6.1f, 6.7f, 7.3f, 8f)
            zscoreMap[ZScoreTableKey(0.toByte(), 61f)] = createSDMap(4.9f, 5.3f, 5.8f, 6.3f, 6.8f, 7.4f, 8.1f)
            zscoreMap[ZScoreTableKey(0.toByte(), 61.5.toFloat())] =
                createSDMap(5f, 5.4f, 5.9f, 6.4f, 7f, 7.6f, 8.3f)
            zscoreMap[ZScoreTableKey(0.toByte(), 62f)] = createSDMap(5.1f, 5.6f, 6f, 6.5f, 7.1f, 7.7f, 8.5f)
            zscoreMap[ZScoreTableKey(0.toByte(), 62.5.toFloat())] =
                createSDMap(5.2f, 5.7f, 6.1f, 6.7f, 7.2f, 7.9f, 8.6f)
            zscoreMap[ZScoreTableKey(0.toByte(), 63f)] = createSDMap(5.3f, 5.8f, 6.2f, 6.8f, 7.4f, 8f, 8.8f)
            zscoreMap[ZScoreTableKey(0.toByte(), 63.5.toFloat())] =
                createSDMap(5.4f, 5.9f, 6.4f, 6.9f, 7.5f, 8.2f, 8.9f)
            zscoreMap[ZScoreTableKey(0.toByte(), 64f)] = createSDMap(5.5f, 6f, 6.5f, 7f, 7.6f, 8.3f, 9.1f)
            zscoreMap[ZScoreTableKey(0.toByte(), 64.5.toFloat())] =
                createSDMap(5.6f, 6.1f, 6.6f, 7.1f, 7.8f, 8.5f, 9.3f)
            zscoreMap[ZScoreTableKey(0.toByte(), 65f)] = createSDMap(5.7f, 6.2f, 6.7f, 7.3f, 7.9f, 8.6f, 9.4f)
            zscoreMap[ZScoreTableKey(0.toByte(), 65.5.toFloat())] = createSDMap(5.8f, 6.3f, 6.8f, 7.4f, 8f, 8.7f, 9.6f)
            zscoreMap[ZScoreTableKey(0.toByte(), 66f)] = createSDMap(5.9f, 6.4f, 6.9f, 7.5f, 8.2f, 8.9f, 9.7f)
            zscoreMap[ZScoreTableKey(0.toByte(), 66.5.toFloat())] = createSDMap(6f, 6.5f, 7f, 7.6f, 8.3f, 9f, 9.9f)
            zscoreMap[ZScoreTableKey(0.toByte(), 67f)] = createSDMap(6.1f, 6.6f, 7.1f, 7.7f, 8.4f, 9.2f, 10f)
            zscoreMap[ZScoreTableKey(0.toByte(), 67.5.toFloat())] =
                createSDMap(6.2f, 6.7f, 7.2f, 7.9f, 8.5f, 9.3f, 10.2f)
            zscoreMap[ZScoreTableKey(0.toByte(), 68f)] = createSDMap(6.3f, 6.8f, 7.3f, 8f, 8.7f, 9.4f, 10.3f)
            zscoreMap[ZScoreTableKey(0.toByte(), 68.5.toFloat())] =
                createSDMap(6.4f, 6.9f, 7.5f, 8.1f, 8.8f, 9.6f, 10.5f)
            zscoreMap[ZScoreTableKey(0.toByte(), 69f)] = createSDMap(6.5f, 7f, 7.6f, 8.2f, 8.9f, 9.7f, 10.6f)
            zscoreMap[ZScoreTableKey(0.toByte(), 69.5.toFloat())] = createSDMap(6.6f, 7.1f, 7.7f, 8.3f, 9f, 9.8f, 10.8f)
            zscoreMap[ZScoreTableKey(0.toByte(), 70f)] = createSDMap(6.6f, 7.2f, 7.8f, 8.4f, 9.2f, 10f, 10.9f)
            zscoreMap[ZScoreTableKey(0.toByte(), 70.5.toFloat())] =
                createSDMap(6.7f, 7.3f, 7.9f, 8.5f, 9.3f, 10.1f, 11.1f)
            zscoreMap[ZScoreTableKey(0.toByte(), 71f)] = createSDMap(6.8f, 7.4f, 8f, 8.6f, 9.4f, 10.2f, 11.2f)
            zscoreMap[ZScoreTableKey(0.toByte(), 71.5.toFloat())] =
                createSDMap(6.9f, 7.5f, 8.1f, 8.8f, 9.5f, 10.4f, 11.3f)
            zscoreMap[ZScoreTableKey(0.toByte(), 72f)] = createSDMap(7f, 7.6f, 8.2f, 8.9f, 9.6f, 10.5f, 11.5f)
            zscoreMap[ZScoreTableKey(0.toByte(), 72.5.toFloat())] =
                createSDMap(7.1f, 7.6f, 8.3f, 9f, 9.8f, 10.6f, 11.6f)
            zscoreMap[ZScoreTableKey(0.toByte(), 73f)] = createSDMap(7.2f, 7.7f, 8.4f, 9.1f, 9.9f, 10.8f, 11.8f)
            zscoreMap[ZScoreTableKey(0.toByte(), 73.5.toFloat())] =
                createSDMap(7.2f, 7.8f, 8.5f, 9.2f, 10f, 10.9f, 11.9f)
            zscoreMap[ZScoreTableKey(0.toByte(), 74f)] = createSDMap(7.3f, 7.9f, 8.6f, 9.3f, 10.1f, 11f, 12.1f)
            zscoreMap[ZScoreTableKey(0.toByte(), 74.5.toFloat())] =
                createSDMap(7.4f, 8f, 8.7f, 9.4f, 10.2f, 11.2f, 12.2f)
            zscoreMap[ZScoreTableKey(0.toByte(), 75f)] = createSDMap(7.5f, 8.1f, 8.8f, 9.5f, 10.3f, 11.3f, 12.3f)
            zscoreMap[ZScoreTableKey(0.toByte(), 75.5.toFloat())] =
                createSDMap(7.6f, 8.2f, 8.8f, 9.6f, 10.4f, 11.4f, 12.5f)
            zscoreMap[ZScoreTableKey(0.toByte(), 76f)] = createSDMap(7.6f, 8.3f, 8.9f, 9.7f, 10.6f, 11.5f, 12.6f)
            zscoreMap[ZScoreTableKey(0.toByte(), 76.5.toFloat())] =
                createSDMap(7.7f, 8.3f, 9f, 9.8f, 10.7f, 11.6f, 12.7f)
            zscoreMap[ZScoreTableKey(0.toByte(), 77f)] = createSDMap(7.8f, 8.4f, 9.1f, 9.9f, 10.8f, 11.7f, 12.8f)
            zscoreMap[ZScoreTableKey(0.toByte(), 77.5.toFloat())] =
                createSDMap(7.9f, 8.5f, 9.2f, 10f, 10.9f, 11.9f, 13f)
            zscoreMap[ZScoreTableKey(0.toByte(), 78f)] = createSDMap(7.9f, 8.6f, 9.3f, 10.1f, 11f, 12f, 13.1f)
            zscoreMap[ZScoreTableKey(0.toByte(), 78.5.toFloat())] =
                createSDMap(8f, 8.7f, 9.4f, 10.2f, 11.1f, 12.1f, 13.2f)
            zscoreMap[ZScoreTableKey(0.toByte(), 79f)] = createSDMap(8.1f, 8.7f, 9.5f, 10.3f, 11.2f, 12.2f, 13.3f)
            zscoreMap[ZScoreTableKey(0.toByte(), 79.5.toFloat())] =
                createSDMap(8.2f, 8.8f, 9.5f, 10.4f, 11.3f, 12.3f, 13.4f)
            zscoreMap[ZScoreTableKey(0.toByte(), 80f)] = createSDMap(8.2f, 8.9f, 9.6f, 10.4f, 11.4f, 12.4f, 13.6f)
            zscoreMap[ZScoreTableKey(0.toByte(), 80.5.toFloat())] =
                createSDMap(8.3f, 9f, 9.7f, 10.5f, 11.5f, 12.5f, 13.7f)
            zscoreMap[ZScoreTableKey(0.toByte(), 81f)] = createSDMap(8.4f, 9.1f, 9.8f, 10.6f, 11.6f, 12.6f, 13.8f)
            zscoreMap[ZScoreTableKey(0.toByte(), 81.5.toFloat())] =
                createSDMap(8.5f, 9.1f, 9.9f, 10.7f, 11.7f, 12.7f, 13.9f)
            zscoreMap[ZScoreTableKey(0.toByte(), 82f)] = createSDMap(8.5f, 9.2f, 10f, 10.8f, 11.8f, 12.8f, 14f)
            zscoreMap[ZScoreTableKey(0.toByte(), 82.5.toFloat())] =
                createSDMap(8.6f, 9.3f, 10.1f, 10.9f, 11.9f, 13f, 14.2f)
            zscoreMap[ZScoreTableKey(0.toByte(), 83f)] = createSDMap(8.7f, 9.4f, 10.2f, 11f, 12f, 13.1f, 14.3f)
            zscoreMap[ZScoreTableKey(0.toByte(), 83.5.toFloat())] =
                createSDMap(8.8f, 9.5f, 10.3f, 11.2f, 12.1f, 13.2f, 14.4f)
            zscoreMap[ZScoreTableKey(0.toByte(), 84f)] = createSDMap(8.9f, 9.6f, 10.4f, 11.3f, 12.2f, 13.3f, 14.6f)
            zscoreMap[ZScoreTableKey(0.toByte(), 84.5.toFloat())] =
                createSDMap(9f, 9.7f, 10.5f, 11.4f, 12.4f, 13.5f, 14.7f)
            zscoreMap[ZScoreTableKey(0.toByte(), 85f)] = createSDMap(9.1f, 9.8f, 10.6f, 11.5f, 12.5f, 13.6f, 14.9f)
            zscoreMap[ZScoreTableKey(0.toByte(), 85.5.toFloat())] =
                createSDMap(9.2f, 9.9f, 10.7f, 11.6f, 12.6f, 13.7f, 15f)
            zscoreMap[ZScoreTableKey(0.toByte(), 86f)] = createSDMap(9.3f, 10f, 10.8f, 11.7f, 12.8f, 13.9f, 15.2f)
            zscoreMap[ZScoreTableKey(0.toByte(), 86.5.toFloat())] =
                createSDMap(9.4f, 10.1f, 11f, 11.9f, 12.9f, 14f, 15.3f)
            zscoreMap[ZScoreTableKey(0.toByte(), 87f)] = createSDMap(9.5f, 10.2f, 11.1f, 12f, 13f, 14.2f, 15.5f)
            zscoreMap[ZScoreTableKey(0.toByte(), 87.5.toFloat())] =
                createSDMap(9.6f, 10.4f, 11.2f, 12.1f, 13.2f, 14.3f, 15.6f)
            zscoreMap[ZScoreTableKey(0.toByte(), 88f)] = createSDMap(9.7f, 10.5f, 11.3f, 12.2f, 13.3f, 14.5f, 15.8f)
            zscoreMap[ZScoreTableKey(0.toByte(), 88.5.toFloat())] =
                createSDMap(9.8f, 10.6f, 11.4f, 12.4f, 13.4f, 14.6f, 15.9f)
            zscoreMap[ZScoreTableKey(0.toByte(), 89f)] = createSDMap(9.9f, 10.7f, 11.5f, 12.5f, 13.5f, 14.7f, 16.1f)
            zscoreMap[ZScoreTableKey(0.toByte(), 89.5.toFloat())] =
                createSDMap(10f, 10.8f, 11.6f, 12.6f, 13.7f, 14.9f, 16.2f)
            zscoreMap[ZScoreTableKey(0.toByte(), 90f)] = createSDMap(10.1f, 10.9f, 11.8f, 12.7f, 13.8f, 15f, 16.4f)
            zscoreMap[ZScoreTableKey(0.toByte(), 90.5.toFloat())] =
                createSDMap(10.2f, 11f, 11.9f, 12.8f, 13.9f, 15.1f, 16.5f)
            zscoreMap[ZScoreTableKey(0.toByte(), 91f)] = createSDMap(10.3f, 11.1f, 12f, 13f, 14.1f, 15.3f, 16.7f)
            zscoreMap[ZScoreTableKey(0.toByte(), 91.5.toFloat())] =
                createSDMap(10.4f, 11.2f, 12.1f, 13.1f, 14.2f, 15.4f, 16.8f)
            zscoreMap[ZScoreTableKey(0.toByte(), 92f)] = createSDMap(10.5f, 11.3f, 12.2f, 13.2f, 14.3f, 15.6f, 17f)
            zscoreMap[ZScoreTableKey(0.toByte(), 92.5.toFloat())] =
                createSDMap(10.6f, 11.4f, 12.3f, 13.3f, 14.4f, 15.7f, 17.1f)
            zscoreMap[ZScoreTableKey(0.toByte(), 93f)] =
                createSDMap(10.7f, 11.5f, 12.4f, 13.4f, 14.6f, 15.8f, 17.3f)
            zscoreMap[ZScoreTableKey(0.toByte(), 93.5.toFloat())] =
                createSDMap(10.7f, 11.6f, 12.5f, 13.5f, 14.7f, 16f, 17.4f)
            zscoreMap[ZScoreTableKey(0.toByte(), 94f)] =
                createSDMap(10.8f, 11.7f, 12.6f, 13.7f, 14.8f, 16.1f, 17.6f)
            zscoreMap[ZScoreTableKey(0.toByte(), 94.5.toFloat())] =
                createSDMap(10.9f, 11.8f, 12.7f, 13.8f, 14.9f, 16.3f, 17.7f)
            zscoreMap[ZScoreTableKey(0.toByte(), 95f)] = createSDMap(11f, 11.9f, 12.8f, 13.9f, 15.1f, 16.4f, 17.9f)
            zscoreMap[ZScoreTableKey(0.toByte(), 95.5.toFloat())] =
                createSDMap(11.1f, 12f, 12.9f, 14f, 15.2f, 16.5f, 18f)
            zscoreMap[ZScoreTableKey(0.toByte(), 96f)] = createSDMap(11.2f, 12.1f, 13.1f, 14.1f, 15.3f, 16.7f, 18.2f)
            zscoreMap[ZScoreTableKey(0.toByte(), 96.5.toFloat())] =
                createSDMap(11.3f, 12.2f, 13.2f, 14.3f, 15.5f, 16.8f, 18.4f)
            zscoreMap[ZScoreTableKey(0.toByte(), 97f)] = createSDMap(11.4f, 12.3f, 13.3f, 14.4f, 15.6f, 17f, 18.5f)
            zscoreMap[ZScoreTableKey(0.toByte(), 97.5.toFloat())] =
                createSDMap(11.5f, 12.4f, 13.4f, 14.5f, 15.7f, 17.1f, 18.7f)
            zscoreMap[ZScoreTableKey(0.toByte(), 98f)] = createSDMap(11.6f, 12.5f, 13.5f, 14.6f, 15.9f, 17.3f, 18.9f)
            zscoreMap[ZScoreTableKey(0.toByte(), 98.5.toFloat())] =
                createSDMap(11.7f, 12.6f, 13.6f, 14.8f, 16f, 17.5f, 19.1f)
            zscoreMap[ZScoreTableKey(0.toByte(), 99f)] = createSDMap(11.8f, 12.7f, 13.7f, 14.9f, 16.2f, 17.6f, 19.2f)
            zscoreMap[ZScoreTableKey(0.toByte(), 99.5.toFloat())] =
                createSDMap(11.9f, 12.8f, 13.9f, 15f, 16.3f, 17.8f, 19.4f)
            zscoreMap[ZScoreTableKey(0.toByte(), 100f)] = createSDMap(12f, 12.9f, 14f, 15.2f, 16.5f, 18f, 19.6f)
            zscoreMap[ZScoreTableKey(0.toByte(), 100.5.toFloat())] =
                createSDMap(12.1f, 13f, 14.1f, 15.3f, 16.6f, 18.1f, 19.8f)
            zscoreMap[ZScoreTableKey(0.toByte(), 101f)] = createSDMap(12.2f, 13.2f, 14.2f, 15.4f, 16.8f, 18.3f, 20f)
            zscoreMap[ZScoreTableKey(0.toByte(), 101.5.toFloat())] =
                createSDMap(12.3f, 13.3f, 14.4f, 15.6f, 16.9f, 18.5f, 20.2f)
            zscoreMap[ZScoreTableKey(0.toByte(), 102f)] =
                createSDMap(12.4f, 13.4f, 14.5f, 15.7f, 17.1f, 18.7f, 20.4f)
            zscoreMap[ZScoreTableKey(0.toByte(), 102.5.toFloat())] =
                createSDMap(12.5f, 13.5f, 14.6f, 15.9f, 17.3f, 18.8f, 20.6f)
            zscoreMap[ZScoreTableKey(0.toByte(), 103f)] = createSDMap(12.6f, 13.6f, 14.8f, 16f, 17.4f, 19f, 20.8f)
            zscoreMap[ZScoreTableKey(0.toByte(), 103.5.toFloat())] =
                createSDMap(12.7f, 13.7f, 14.9f, 16.2f, 17.6f, 19.2f, 21f)
            zscoreMap[ZScoreTableKey(0.toByte(), 104f)] = createSDMap(12.8f, 13.9f, 15f, 16.3f, 17.8f, 19.4f, 21.2f)
            zscoreMap[ZScoreTableKey(0.toByte(), 104.5.toFloat())] =
                createSDMap(12.9f, 14f, 15.2f, 16.5f, 17.9f, 19.6f, 21.5f)
            zscoreMap[ZScoreTableKey(0.toByte(), 105f)] = createSDMap(13f, 14.1f, 15.3f, 16.6f, 18.1f, 19.8f, 21.7f)
            zscoreMap[ZScoreTableKey(0.toByte(), 105.5.toFloat())] =
                createSDMap(13.2f, 14.2f, 15.4f, 16.8f, 18.3f, 20f, 21.9f)
            zscoreMap[ZScoreTableKey(0.toByte(), 106f)] =
                createSDMap(13.3f, 14.4f, 15.6f, 16.9f, 18.5f, 20.2f, 22.1f)
            zscoreMap[ZScoreTableKey(0.toByte(), 106.5.toFloat())] =
                createSDMap(13.4f, 14.5f, 15.7f, 17.1f, 18.6f, 20.4f, 22.4f)
            zscoreMap[ZScoreTableKey(0.toByte(), 107f)] =
                createSDMap(13.5f, 14.6f, 15.9f, 17.3f, 18.8f, 20.6f, 22.6f)
            zscoreMap[ZScoreTableKey(0.toByte(), 107.5.toFloat())] =
                createSDMap(13.6f, 14.7f, 16f, 17.4f, 19f, 20.8f, 22.8f)
            zscoreMap[ZScoreTableKey(0.toByte(), 108f)] = createSDMap(13.7f, 14.9f, 16.2f, 17.6f, 19.2f, 21f, 23.1f)
            zscoreMap[ZScoreTableKey(0.toByte(), 108.5.toFloat())] =
                createSDMap(13.8f, 15f, 16.3f, 17.8f, 19.4f, 21.2f, 23.3f)
            zscoreMap[ZScoreTableKey(0.toByte(), 109f)] = createSDMap(14f, 15.1f, 16.5f, 17.9f, 19.6f, 21.4f, 23.6f)
            zscoreMap[ZScoreTableKey(0.toByte(), 109.5.toFloat())] =
                createSDMap(14.1f, 15.3f, 16.6f, 18.1f, 19.8f, 21.7f, 23.8f)
            zscoreMap[ZScoreTableKey(0.toByte(), 110f)] = createSDMap(14.2f, 15.4f, 16.8f, 18.3f, 20f, 21.9f, 24.1f)
            zscoreMap[ZScoreTableKey(0.toByte(), 110.5.toFloat())] =
                createSDMap(14.5f, 15.8f, 17.1f, 18.7f, 20.4f, 22.4f, 24.7f)
            zscoreMap[ZScoreTableKey(0.toByte(), 111f)] =
                createSDMap(14.6f, 15.9f, 17.3f, 18.9f, 20.7f, 22.7f, 25.0f)
            zscoreMap[ZScoreTableKey(0.toByte(), 111.5.toFloat())] =
                createSDMap(14.8f, 16.0f, 17.5f, 19.1f, 20.9f, 22.9f, 25.2f)
            zscoreMap[ZScoreTableKey(0.toByte(), 112f)] =
                createSDMap(14.9f, 16.2f, 17.6f, 19.2f, 21.1f, 23.1f, 25.5f)
            zscoreMap[ZScoreTableKey(0.toByte(), 112.5.toFloat())] =
                createSDMap(15.0f, 16.3f, 17.8f, 19.4f, 21.3f, 23.4f, 25.8f)
            zscoreMap[ZScoreTableKey(0.toByte(), 113f)] =
                createSDMap(15.2f, 16.5f, 18.0f, 19.6f, 21.5f, 23.6f, 26.0f)
            zscoreMap[ZScoreTableKey(0.toByte(), 113.5.toFloat())] =
                createSDMap(15.3f, 16.6f, 18.1f, 19.8f, 21.7f, 23.9f, 26.3f)
            zscoreMap[ZScoreTableKey(0.toByte(), 114f)] =
                createSDMap(15.4f, 16.8f, 18.3f, 20.0f, 21.9f, 24.1f, 26.6f)
            zscoreMap[ZScoreTableKey(0.toByte(), 114.5.toFloat())] =
                createSDMap(15.6f, 16.9f, 18.5f, 20.2f, 22.1f, 24.4f, 26.9f)
            zscoreMap[ZScoreTableKey(0.toByte(), 115f)] = createSDMap(15.7f, 17.1f, 18.6f, 20.4f, 22.4f, 24.6f, 27.2f)
            zscoreMap[ZScoreTableKey(0.toByte(), 115.5.toFloat())] =
                createSDMap(15.8f, 17.2f, 18.8f, 20.6f, 22.6f, 24.9f, 27.5f)
            zscoreMap[ZScoreTableKey(0.toByte(), 116f)] =
                createSDMap(16.0f, 17.4f, 19.0f, 20.8f, 22.8f, 25.1f, 27.8f)
            zscoreMap[ZScoreTableKey(0.toByte(), 116.5.toFloat())] =
                createSDMap(16.1f, 17.5f, 19.2f, 21.0f, 23.0f, 25.4f, 28.0f)
            zscoreMap[ZScoreTableKey(0.toByte(), 117f)] =
                createSDMap(16.2f, 17.7f, 19.3f, 21.2f, 23.3f, 25.6f, 28.3f)
            zscoreMap[ZScoreTableKey(0.toByte(), 117.5.toFloat())] =
                createSDMap(16.4f, 17.9f, 19.5f, 21.4f, 23.5f, 25.9f, 28.6f)
            zscoreMap[ZScoreTableKey(0.toByte(), 118f)] =
                createSDMap(16.5f, 18.0f, 19.7f, 21.6f, 23.7f, 26.1f, 28.9f)
            zscoreMap[ZScoreTableKey(0.toByte(), 118.5.toFloat())] =
                createSDMap(16.7f, 18.2f, 19.9f, 21.8f, 23.9f, 26.4f, 29.2f)
            zscoreMap[ZScoreTableKey(0.toByte(), 119f)] = createSDMap(16.8f, 18.3f, 20.0f, 22.0f, 24.1f, 26.6f, 29.5f)
            zscoreMap[ZScoreTableKey(0.toByte(), 119.5.toFloat())] =
                createSDMap(16.9f, 18.5f, 20.2f, 22.2f, 24.4f, 26.9f, 29.8f)
            zscoreMap[ZScoreTableKey(0.toByte(), 120f)] =
                createSDMap(17.1f, 18.6f, 20.4f, 22.4f, 24.6f, 27.2f, 30.1f)
            return zscoreMap
        }

    private fun createSDMap(
        SD3neg: Float, SD2neg: Float, SD1neg: Float, SD0: Float, SD1: Float,
        SD2: Float, SD3: Float
    ): Map<Float, Int> {
        val sdMap: MutableMap<Float, Int> = HashMap()
        sdMap[SD3neg] = 3
        sdMap[SD2neg] = 2
        sdMap[SD1neg] = 1
        sdMap[SD0] = 0
        sdMap[SD1] = 1
        sdMap[SD2] = 2
        sdMap[SD3] = 3
        return sdMap
    }
}