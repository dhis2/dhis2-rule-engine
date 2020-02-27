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

import java.util.Map;

/**
 * @Author Zubair Asghar.
 */
public class RuleFunctionZScoreHFA
    extends RuleFunctionZScore
{
    public static final String D2_ZSCOREHFA = "d2:zScoreHFA";

    private static final Map<ZScoreTableKey, Map<Float, Integer>> ZSCORE_TABLE_GIRL = ZScoreTable
        .getZscoreHFATableGirl();

    private static final Map<ZScoreTableKey, Map<Float, Integer>> ZSCORE_TABLE_BOY = ZScoreTable.getZscoreHFATableBoy();

    public static RuleFunctionZScoreHFA create()
    {
        return new RuleFunctionZScoreHFA();
    }

    @Override
    public Map<ZScoreTableKey, Map<Float, Integer>> getTableForGirl()
    {
        return ZSCORE_TABLE_GIRL;
    }

    @Override
    public Map<ZScoreTableKey, Map<Float, Integer>> getTableForBoy()
    {
        return ZSCORE_TABLE_BOY;
    }
}
