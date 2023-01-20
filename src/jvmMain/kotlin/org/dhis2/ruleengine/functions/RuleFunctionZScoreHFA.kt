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
class RuleFunctionZScoreHFA : RuleFunctionZScore() {
    override val tableForGirl: Map<ZScoreTableKey, Map<Float, Int>>
        get() = ZSCORE_TABLE_GIRL
    override val tableForBoy: Map<ZScoreTableKey, Map<Float, Int>>
        get() = ZSCORE_TABLE_BOY
    override fun validateParameter(parameter: Float) {
        require(!(parameter < 0 || parameter > 60)) { "age parameter should be between 0 and 60" }
    }

    override fun parameterCorrection(parameter: Float): Float {
        return Math.floor(parameter.toDouble()).toFloat()
    }

    companion object {
        const val D2_ZSCOREHFA = "d2:zScoreHFA"
        private val ZSCORE_TABLE_GIRL = ZScoreTable.zscoreHFATableGirl
        private val ZSCORE_TABLE_BOY = ZScoreTable.zscoreHFATableBoy
        fun create(): RuleFunctionZScoreHFA {
            return RuleFunctionZScoreHFA()
        }
    }
}