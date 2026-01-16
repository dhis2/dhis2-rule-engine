/*
 * Copyright (c) 2004-2024, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors
 * may be used to endorse or promote products derived from this software without
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

package org.hisp.dhis.rules

import org.hisp.dhis.rules.api.RuleEngine
import org.hisp.dhis.rules.api.RuleEngineContext
import org.hisp.dhis.rules.models.Rule
import org.hisp.dhis.rules.models.RuleAction
import org.hisp.dhis.rules.models.RuleAttributeValue
import org.hisp.dhis.rules.models.RuleEnrollment
import org.hisp.dhis.rules.models.RuleEnrollmentStatus
import org.hisp.dhis.rules.models.RuleEvent
import org.hisp.dhis.rules.models.RuleLocalDate
import kotlin.test.Test
import kotlin.test.assertEquals

class OrderedRulesTest {
    @Test
    fun shouldEvaluateRulesOrderedByPriority() {
        val highPriorityRuleAction = RuleAction("1", "SHOWERROR")
        val lowPriorityRuleAction = RuleAction("2", "SHOWERROR")
        val nullPriorityRuleAction = RuleAction("3", "SHOWERROR")
        val highPriorityRule = Rule("true", listOf(highPriorityRuleAction), priority = 1)
        val lowPriorityRule = Rule("true", listOf(lowPriorityRuleAction), priority = 100)
        val nullPriorityRule = Rule("true", listOf(nullPriorityRuleAction), priority = null)

        val ruleEngineContext = RuleEngineContext(listOf(lowPriorityRule, nullPriorityRule, highPriorityRule))

        val effects = RuleEngine.getInstance().evaluate(enrollment(), listOf(), ruleEngineContext)

        assertEquals(highPriorityRuleAction.data, effects[0].data)
        assertEquals(lowPriorityRuleAction.data, effects[1].data)
        assertEquals(nullPriorityRuleAction.data, effects[2].data)
    }

    @Test
    fun shouldEvaluateRulesAndRuleActionsOrderedByPriority() {
        val highPriorityRuleActionInHighPriorityRule = RuleAction("1", "SHOWERROR", priority = 1)
        val lowPriorityRuleActionInHighPriorityRule = RuleAction("2", "SHOWERROR", priority = 100)
        val nullPriorityRuleActionInHighPriorityRule = RuleAction("3", "SHOWERROR", priority = null)
        val highPriorityRuleActionInLowPriorityRule = RuleAction("4", "SHOWERROR", priority = 1)
        val lowPriorityRuleActionInLowPriorityRule = RuleAction("5", "SHOWERROR", priority = 100)
        val nullPriorityRuleActionInLowPriorityRule = RuleAction("6", "SHOWERROR", priority = null)
        val highPriorityRuleActionInNullPriorityRule = RuleAction("7", "SHOWERROR", priority = 1)
        val lowPriorityRuleActionInNullPriorityRule = RuleAction("8", "SHOWERROR", priority = 100)
        val nullPriorityRuleActionInNullPriorityRule = RuleAction("9", "SHOWERROR", priority = null)
        val highPriorityRule = Rule("true", listOf(highPriorityRuleActionInHighPriorityRule,
                lowPriorityRuleActionInHighPriorityRule,
                nullPriorityRuleActionInHighPriorityRule), priority = 1)
        val lowPriorityRule = Rule("true", listOf(highPriorityRuleActionInLowPriorityRule,
            lowPriorityRuleActionInLowPriorityRule,
            nullPriorityRuleActionInLowPriorityRule), priority = 100)
        val nullPriorityRule = Rule("true", listOf(highPriorityRuleActionInNullPriorityRule,
            lowPriorityRuleActionInNullPriorityRule,
            nullPriorityRuleActionInNullPriorityRule), priority = null)

        val ruleEngineContext = RuleEngineContext(listOf(lowPriorityRule, nullPriorityRule, highPriorityRule))

        val effects = RuleEngine.getInstance().evaluate(enrollment(), listOf(), ruleEngineContext)

        assertEquals(highPriorityRuleActionInHighPriorityRule.data, effects[0].data)
        assertEquals(lowPriorityRuleActionInHighPriorityRule.data, effects[1].data)
        assertEquals(nullPriorityRuleActionInHighPriorityRule.data, effects[2].data)

        assertEquals(highPriorityRuleActionInLowPriorityRule.data, effects[3].data)
        assertEquals(lowPriorityRuleActionInLowPriorityRule.data, effects[4].data)
        assertEquals(nullPriorityRuleActionInLowPriorityRule.data, effects[5].data)

        assertEquals(highPriorityRuleActionInNullPriorityRule.data, effects[6].data)
        assertEquals(lowPriorityRuleActionInNullPriorityRule.data, effects[7].data)
        assertEquals(nullPriorityRuleActionInNullPriorityRule.data, effects[8].data)
    }

    private fun enrollment(): RuleEnrollment {
        return RuleEnrollment(
            enrollment = "test_enrollment",
            programName = "test_program",
            incidentDate = RuleLocalDate.currentDate(),
            enrollmentDate = RuleLocalDate.currentDate(),
            status = RuleEnrollmentStatus.ACTIVE,
            organisationUnit = "test_ou",
            organisationUnitCode = "test_ou_code",
            attributeValues = listOf(RuleAttributeValue("test_attribute", "test_value")),
        )
    }
}
