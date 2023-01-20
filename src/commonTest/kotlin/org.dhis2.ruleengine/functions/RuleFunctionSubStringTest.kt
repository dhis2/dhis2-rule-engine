package org.dhis2.ruleengine.functions

import org.dhis2.ruleengine.RuleEngineTestUtils
import org.dhis2.ruleengine.exprk.functions.SubString
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

class RuleFunctionSubStringTest {

    @Test
    fun return_empty_string_for_null_inputs() {
        assertSubstring(null, "0", "0", "")
        assertSubstring(null, "0", "10", "")
    }

    @Test
    fun return_substring_from_start_index_to_end_index_of_input_string() {
        assertSubstring("abcdef", "0", "0", "")
        assertSubstring("abcdef", "0", "1", "a")
        assertSubstring("abcdef", "-10", "1", "a")
        assertSubstring("abcdef", "2", "4", "cd")
        assertSubstring("abcdef", "2", "10", "cdef")
    }

    @Test
    fun throw_parser_exception_without_context_if_start_index_is_a_text() {
        RuleEngineTestUtils.assertThrowsIllegalArgumentException {
            assertSubstring("test_variable_one", "variable", "2", "")
        }
    }

    @Test
    fun throw_parser_exception_without_context_if_end_index_is_a_text() {
        RuleEngineTestUtils.assertThrowsIllegalArgumentException {
            assertSubstring("test_variable_one", "2", "variable", "")
        }
    }

    private fun assertSubstring(input: String?, fromIndex: String?, toIndex: String?, expectedValue: String?) {
        assertTrue {
            SubString().call(listOf(input, fromIndex, toIndex)) == expectedValue
        }
    }
}