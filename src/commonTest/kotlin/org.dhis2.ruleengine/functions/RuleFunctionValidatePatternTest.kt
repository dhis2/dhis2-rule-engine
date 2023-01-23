package org.dhis2.ruleengine.functions

import org.dhis2.ruleengine.exprk.functions.ValidatePattern
import kotlin.test.Test
import kotlin.test.assertTrue

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

class RuleFunctionValidatePatternTest {

    @Test
    fun return_true_if_pattern_match() {
        assertValidatePattern("123", "123", "true")
        assertValidatePattern("27123456789", "27\\d{2}\\d{3}\\d{4}", "true")
        assertValidatePattern("27123456789", "27\\d{9}", "true")
        assertValidatePattern("abc123", "abc123", "true")
        assertValidatePattern("9999/99/9", "\\d{4}/\\d{2}/\\d", "true")
        assertValidatePattern("9999/99/9", "[0-9]{4}/[0-9]{2}/[0-9]", "true")
    }

    @Test
    fun return_false_for_non_matching_pairs() {
        assertValidatePattern("1999/99/9", "[9]{4}/\\d{2}/\\d", "false")
        assertValidatePattern("9999/99/", "[0-9]{4}/[0-9]{2}/[0-9]", "false")
        assertValidatePattern("abc123", "xyz", "false")
        assertValidatePattern("abc123", "^bc", "false")
        assertValidatePattern("abc123", "abc12345", "false")
        assertValidatePattern("123", "567", "false")
    }

    private fun assertValidatePattern(input: String, regex: String, isPatternValid: String) {
        assertTrue {
            ValidatePattern().call(listOf(input, regex)) == isPatternValid
        }
    }
}