package org.dhis2.ruleengine.functions

import org.dhis2.ruleengine.RuleEngineTestUtils
import org.dhis2.ruleengine.exprk.functions.Split
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

class RuleFunctionSplitTest {

    @Test
    fun return_empty_string_for_null_inputs() {
        assertSplit(null, null, "0", "")
        assertSplit("", null, "0", "")
        assertSplit(null, "", "0", "")
    }

    @Test
    fun return_the_nth_field_of_the_splited_first_argument() {
        assertSplit("a,b,c", ",", "0", "a")
        assertSplit("a,b,c", ",", "2", "c")
        assertSplit("a,;b,;c", ",;", "1", "b")
    }

    @Test
    fun return_empty_string_if_field_index_is_out_of_bounds() {
        assertSplit("a,b,c", ",", "10", "")
        assertSplit("a,b,c", ",", "-1", "")
    }

    @Test
    fun throw_parser_exception_without_context_if_position_is_a_text() {
        RuleEngineTestUtils.assertThrowsIllegalArgumentException {
            assertSplit("test_variable_one", "variable", "text", null)
        }
    }

    private fun assertSplit(input: String?, delimiter: String?, index: String, expectedValue: String?) {
        assertTrue {
            Split().call(listOf(input, delimiter, index)) == expectedValue
        }
    }
}