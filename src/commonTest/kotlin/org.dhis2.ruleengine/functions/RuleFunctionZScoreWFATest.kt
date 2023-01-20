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
 */
import org.dhis2.ruleengine.RuleEngineTestUtils
import org.dhis2.ruleengine.exprk.functions.ZScoreWFA
import kotlin.test.Test
import kotlin.test.assertTrue

class RuleFunctionZScoreWFATest {
   
    @Test
    fun testZscoreAtExactSDValue() {
        assertZScore("1", "4.8", "1", "1")
        assertZScore("1", "3.2", "1", "-2")
        assertZScore("39", "9.9", "1", "-3")
        assertZScore("39", "11.5", "1", "-1.80")
    }

    @Test
    fun testZscoreBeyond3SD() {
        assertZScore("1", "7.5", "1", "3.5")
    }

    @Test
    fun testZscoreBeyondNegative3SD() {
        assertZScore("1", "4.8", "1", "1")
    }

    @Test
    fun testZscoreAboveSD0() {
        assertZScore("1", "5.2", "1", "1.57")
        assertZScore("6", "9.5", "1", "2.15")
        assertZScore("1", "6.0", "1", "2.71")
    }

    @Test
    fun testZscoreBelowSD0() {
        assertZScore("1", "2.9", "1", "-2.60")
        assertZScore("12", "7.5", "1", "-1.44")
        assertZScore("1", "2.8", "1", "-2.80")
    }

    @Test
    fun testFractionAgeParameter() {
        assertZScore("1.2", "2.9", "1", "-2.60")
        assertZScore("1.3", "5.2", "1", "1.57")
        assertZScore("1.4", "4.8", "1", "1")
    }

    @Test
    fun testExceptionIfInvalidArgument() {
        RuleEngineTestUtils.assertThrowsIllegalArgumentException {
            assertZScore("1", "2.9", null, "0")
        }
    }

    @Test
    fun testExceptionWeightIsInvalid() {
        RuleEngineTestUtils.assertThrowsIllegalArgumentException {
            assertZScore("1", "abc", "1", "2.40")
        }
    }

    @Test
    fun testExceptionIfParameterOutOfRange() {
        RuleEngineTestUtils.assertThrowsIllegalArgumentException {
            assertZScore("61", "2.9", "1", "1")
        }
    }

    private fun assertZScore(parameter: String, weight: String, gender: String?, zScore: String) {
        assertTrue {
            ZScoreWFA().call(listOf(parameter, weight, gender)) == zScore
        }
    }
}