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
import org.dhis2.ruleengine.exprk.functions.Zing
import kotlin.test.Test
import kotlin.test.assertTrue

class RuleFunctionZingTest {

    @Test
    fun return_same_value_for_non_negative_argument() {
        assertZing("0", "0")
        assertZing("1", "1")
        assertZing("5", "5")
        assertZing("0.1", "0.1")
        assertZing("1.1", "1.1")
    }

    @Test
    fun return_zero_for_negative_argument() {
        assertZing("-0.1", "0")
        assertZing("-1", "0")
        assertZing("-10", "0")
        assertZing("-1.1", "0")
    }

    @Test
    fun throw_illegal_argument_exception_for_non_number_argument() {
        RuleEngineTestUtils.assertThrowsIllegalArgumentException {
            assertZing("non_number", null)

        }
    }

    private fun assertZing(value: String, expectedValue: String?) {
        assertTrue {
            Zing().call(listOf(value)) == expectedValue
        }
    }
}