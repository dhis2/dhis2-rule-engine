package org.dhis2.ruleengine.functions

import org.dhis2.ruleengine.exprk.functions.HasUserRole
import kotlin.test.Ignore
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

class RuleFunctionHasUserRoleTest {


    @Test
    fun throwExceptionWhenSupplementaryDataIsNull() {
        try {
            assertHasUserRole("uid1", mutableMapOf(), "true")
        } catch (e: Exception) {
            assertTrue { e is IllegalArgumentException }
        }
    }

    @Test
    fun returnTrueIfUserHasRole() {
        val supplementaryData: MutableMap<String, List<String>> = mutableMapOf()
        supplementaryData["USER"] = listOf("uid1")
        assertHasUserRole("uid1", supplementaryData, "true")
    }

    @Test
    fun returnFalseIfUserHasNoRole() {
        val supplementaryData: MutableMap<String, List<String>> = mutableMapOf()
        supplementaryData["USER"] = listOf("uid1")
        assertHasUserRole("uid2", supplementaryData, "false")
    }

    @Ignore
    @Test
    fun returnFalseIfRoleListIsNull() {
    }

    @Test
    fun returnFalseIfRoleListIsEmpty() {
        val supplementaryData: MutableMap<String, List<String>> = mutableMapOf()
        supplementaryData["USER"] = emptyList()
        assertHasUserRole("uid2", supplementaryData, "false")
    }

    private fun assertHasUserRole(value: String, supplementaryData: Map<String, List<String>>, hasUserRole: String) {
        assertTrue {
            HasUserRole{supplementaryData}.call(listOf(value)) == hasUserRole
        }
    }
}