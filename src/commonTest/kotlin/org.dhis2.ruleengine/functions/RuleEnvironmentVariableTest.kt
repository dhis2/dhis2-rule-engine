package org.dhis2.ruleengine.functions
/*
 * Copyright (c) 2004-2020, University of Oslo
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
import org.dhis2.ruleengine.RuleEffect
import org.dhis2.ruleengine.RuleEngine
import org.dhis2.ruleengine.RuleEngineTestUtils.currentDate
import org.dhis2.ruleengine.RuleEngineTestUtils.getRuleEngine
import org.dhis2.ruleengine.models.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class RuleEnvironmentVariableTest {
    @Test
    @Throws(Exception::class)
    fun testWithCompletedDateNull() {
        val ruleAction: RuleAction = RuleAction.DisplayKeyValuePair(
            "test_action_content", DisplayLocation.LOCATION_FEEDBACK_WIDGET, "1"
        )
        val rule: Rule =
            Rule("", null, null, "d2:hasValue(V{completed_date})", listOf(ruleAction), "")
        val ruleEngine: RuleEngine = getRuleEngine(rule, listOf<RuleVariable>())
        val ruleEvent: RuleEvent = RuleEvent(
            "test_event", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), null, "", null,
            listOf<RuleDataValue>()
        )
        val ruleEffects: List<RuleEffect> = ruleEngine.evaluate(ruleEvent)
        assertTrue { ruleEffects.isEmpty() }
    }

    @Test
    @Throws(Exception::class)
    fun testWithCompletedDate() {
        val ruleAction: RuleAction = RuleAction.DisplayKeyValuePair(
            "test_action_content", DisplayLocation.LOCATION_FEEDBACK_WIDGET, "1"
        )
        val rule: Rule =
            Rule("", null, null, "d2:hasValue(V{completed_date})", listOf(ruleAction), "")
        val ruleEngine: RuleEngine = getRuleEngine(rule, listOf())
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(),  currentDate(), "", null,
            listOf()
        )
        val ruleEffects: List<RuleEffect> = ruleEngine.evaluate(ruleEvent)
        assertEquals(1, ruleEffects.size)
        assertEquals("1", ruleEffects[0].data)
        assertEquals(ruleAction, ruleEffects[0].ruleAction)
    }
}