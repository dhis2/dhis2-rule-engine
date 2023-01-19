package org.hisp.dhis.rules.functions

import org.dhis2.ruleengine.RuleVariableValue
import org.dhis2.ruleengine.exprk.functions.InOrgUnitGroup
import org.dhis2.ruleengine.models.RuleValueType
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

class RuleFunctionInOrgUnitGroupTest {

    @Test
    fun returnFalseWhenValueMapAndDataAreEmpty() {
        assertInOrgUnitGroup(
            "uid1", mutableMapOf(), mutableMapOf(),
            "false"
        )
    }

    @Test
    fun returnFalseIfMemberIsNotInData() {
        val supplementaryData: Map<String, List<String>> = mutableMapOf()
        val valueMap: Map<String, RuleVariableValue> = givenAVariableValuesAndOneWithTwoCandidates("location1")
        assertInOrgUnitGroup("value", supplementaryData, valueMap, "false")
    }

    @Test
    fun returnTrueIfMemberIsInData() {
        val value = "value"
        val location = "location1"
        val supplementaryData: MutableMap<String, List<String>> = mutableMapOf()
        supplementaryData[value] = listOf(location)
        val valueMap: Map<String, RuleVariableValue> = givenAVariableValuesAndOneWithTwoCandidates(location)
        assertInOrgUnitGroup(value, supplementaryData, valueMap, "true")
    }

    private fun givenAVariableValuesAndOneWithTwoCandidates(locationValue: String): Map<String, RuleVariableValue> {
        val variableValues: MutableMap<String, RuleVariableValue> = mutableMapOf()
        variableValues["org_unit"] = RuleVariableValue(
            variableValue = locationValue,
            candidates = listOf(locationValue, "two"),
            ruleValueType = RuleValueType.TEXT
        )
        return variableValues
    }

    private fun assertInOrgUnitGroup(
        value: String,
        supplementaryData: Map<String, List<String>>,
        valueMap: Map<String, RuleVariableValue>, inOrgUnitGroup: String
    ) {
        assertTrue {
            InOrgUnitGroup(valueMap, supplementaryData).call(listOf(value)) == inOrgUnitGroup
        }
    }
}