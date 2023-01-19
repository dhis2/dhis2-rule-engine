package org.dhis2.ruleengine.functions

import org.dhis2.ruleengine.exprk.functions.Left
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

class RuleFunctionLeftTest {

    @Test
    fun return_empty_string_for_null_input() {
        assertLeftValue(null, "0", "")
        assertLeftValue(null, "10", "")
        assertLeftValue(null, "-10", "")
    }

    @Test
    fun return_substring_of_first_argument_from_the_beginning() {
        assertLeftValue("000", "2", "00")
        assertLeftValue("abcdef", "0", "")
        assertLeftValue("abcdef", "-5", "a")
        assertLeftValue("abcdef", "2", "ab")
        assertLeftValue("abcdef", "30", "abcdef")
    }

    @Test
    fun throw_parser_exception_without_context_if_position_is_a_text() {
        try {
            assertLeftValue("", "text", "")
            assertTrue { false }
        } catch (e: Exception) {
            assertTrue { e is IllegalArgumentException }
        }
    }

    @Test
    fun throw_illegal_argument_when_number_not_an_integer() {
        try {
            assertLeftValue("", "6.8", "")
            assertTrue { false }
        } catch (e: Exception) {
            assertTrue { e is IllegalArgumentException }
        }
    }

    private fun assertLeftValue(currentValue: String?, count: String, expectedValue: String) {
        assertTrue { Left().call(listOf(currentValue, count)) == expectedValue }
    }
}