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
import org.dhis2.ruleengine.exprk.functions.ZScoreWFH
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RuleFunctionZScoreWFHTest {

    @Test
    fun testZscoreAtExactSDValue() {
        assertZScore("45.5", "2.8", "1", "1")
        assertZScore("45.5", "2.1", "1", "-2")
        assertZScore("108.5", "13.6", "1", "-3")
        assertZScore("108.5", "15.08", "1", "-1.80")
    }

    @Test
    fun testZscoreBeyond3SD() {
        assertZScore("45.5", "3.5", "1", "3.5")
    }

    @Test
    fun testZscoreBeyondNegative3SD() {
        assertZScore("45.5", "1.9", "1", "-3.5")
    }

    @Test
    fun testZscoreAboveSD0() {
        assertZScore("45.5", "2.94", "1", "1.47")
        assertZScore("59", "6.91", "1", "2.16")
        assertZScore("45.5", "3.29", "1", "2.63")
    }

    @Test
    fun testZscoreBelowSD0() {
        assertZScore("45.5", "2.03", "1", "-2.70")
        assertZScore("89", "11.0", "1", "-1.22")
        assertZScore("45.5", "2.02", "1", "-2.80")
    }

    @Test
    fun testFractionAgeParameter() {
        assertZScore("45.8", "2.03", "1", "-2.70")
        assertZScore("89.3", "11.0", "1", "-1.22")
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
            assertZScore("42", "2.9", "1", "1")
        }
    }

    private fun assertZScore(parameter: String, weight: String, gender: String?, zScore: String) {
        assertEquals(
            zScore,
            ZScoreWFH().call(listOf(parameter, weight, gender))
        )
    }
}