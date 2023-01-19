package org.dhis2.ruleengine.functions

import org.dhis2.ruleengine.exprk.functions.ExtractDataMatrixValue
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

class RuleFunctionExtractDataMatrixValueTest {

    private val functionToTest = ExtractDataMatrixValue()

    @Test
    fun throw_argument_exception_if_value_is_not_gs1() {
        val arguments = listOf("serial number","testingvalue")
        try {
            functionToTest.call(arguments)
        } catch (e: Exception) {
            e is IllegalArgumentException
        }
    }

    @Test
    fun throw_argument_exception_if_key_is_not_valid() {
        val testValue = "]d2\u001D01084700069915412110081996195256\u001D10DXB2005\u001D17220228"
        val gs1Key = "serial numb"
        val arguments = listOf(gs1Key, testValue)
        try {
            functionToTest.call(arguments)
        } catch (e: Exception) {
            e is IllegalArgumentException
        }
    }

    @Test
    fun return_gs1_value_if_named_key_is_in_value() {
        val testValue = "]d2\u001D01084700069915412110081996195256\u001D10DXB2005\u001D17220228"
        val gs1Key = "serial number"
        val arguments = listOf(gs1Key, testValue)
        assertTrue {
            functionToTest.call(arguments) == "10081996195256"
        }
    }

    @Test
    fun return_gs1_value_if_numeric_key_is_in_value() {
        val testValue = "]d2\u001D01084700069915412110081996195256\u001D10DXB2005\u001D17220228"
        val gs1Key = "21"
        val arguments = listOf(gs1Key, testValue)
        assertTrue {
            functionToTest.call(arguments) == "10081996195256"
        }
    }

    @Test
    fun return_gs1_value_if_key_is_in_value() {
        val testValue = "]d201084700069915412110081996195256\u001D10DXB2005\u001D17220228"
        val gs1Key = "serial number"
        val arguments = listOf(gs1Key, testValue)
        assertTrue {
            functionToTest.call(arguments) == "10081996195256"
        }
    }

    @Test
    fun throw_exception_if_key_is_not_in_value() {
        val testValue = "]d2\u001D01084700069915412110081996195256\u001D10DXB2005\u001D17220228"
        val gs1Key = "production date"
        val arguments = listOf(gs1Key, testValue)
        try {
            functionToTest.call(arguments)
        } catch (e: Exception) {
            e is IllegalArgumentException
        }
    }
}