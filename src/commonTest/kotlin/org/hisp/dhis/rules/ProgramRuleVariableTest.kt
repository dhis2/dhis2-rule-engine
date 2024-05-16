package org.hisp.dhis.rules

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import org.hisp.dhis.rules.api.RuleEngine
import org.hisp.dhis.rules.api.RuleEngineContext
import org.hisp.dhis.rules.models.*
import org.hisp.dhis.rules.utils.currentDate
import kotlin.test.Test
import kotlin.test.assertEquals

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
class ProgramRuleVariableTest {
    @Test
    fun testCurrentDateProgramVariableIsAssigned() {
        val rule = getRule("V{current_date}")
        val ruleEffects = callEnrollmentRuleEngine(rule)
        assertProgramRuleVariableAssignment(ruleEffects, rule, CURRENT_DATE.toString())
    }

    @Test
    fun testDueDateProgramVariableIsAssigned() {
        val rule = getRule("V{due_date}")
        val ruleEffects = callEventRuleEngine(rule)
        assertProgramRuleVariableAssignment(ruleEffects, rule,
            DUE_DATE_STRING
        )
    }

    @Test
    fun testEnrollmentCountProgramVariableIsAssigned() {
        val rule = getRule("V{enrollment_count}")
        val ruleEffects = callEnrollmentRuleEngine(rule)
        assertProgramRuleVariableAssignment(ruleEffects, rule, "1")
    }

    @Test
    fun testEnrollmentDateProgramVariableIsAssigned() {
        val rule = getRule("V{enrollment_date}")
        val ruleEffects = callEnrollmentRuleEngine(rule)
        assertProgramRuleVariableAssignment(ruleEffects, rule,
            ENROLLMENT_DATE_STRING
        )
    }

    @Test
    fun testEnrollmentIdProgramVariableIsAssigned() {
        val rule = getRule("V{enrollment_id}")
        val ruleEffects = callEnrollmentRuleEngine(rule)
        assertProgramRuleVariableAssignment(ruleEffects, rule,
            ENROLLMENT_ID
        )
    }

    @Test
    fun testEnrollmentStatusProgramVariableIsAssigned() {
        val rule = getRule("V{enrollment_status}")
        val ruleEffects = callEnrollmentRuleEngine(rule)
        assertProgramRuleVariableAssignment(ruleEffects, rule, ENROLLMENT_STATUS.name)
    }

    @Test
    fun testEnvironmentProgramVariableIsAssigned() {
        val rule = getRule("V{environment}")
        val ruleEffects = callEnrollmentRuleEngine(rule)
        assertProgramRuleVariableAssignment(ruleEffects, rule, getEnvironment().clientName)
    }

    @Test
    fun testEventCountProgramVariableIsAssigned() {
        val rule = getRule("V{event_count}")
        val ruleEffects = callEventRuleEngine(rule)
        assertProgramRuleVariableAssignment(ruleEffects, rule, "1")
    }

    @Test
    fun testEventDateProgramVariableIsAssigned() {
        val rule = getRule("V{event_date}")
        val ruleEffects = callEventRuleEngine(rule)
        assertProgramRuleVariableAssignment(ruleEffects, rule,
            EVENT_DATE_STRING
        )
    }

    @Test
    fun testEventIdProgramVariableIsAssigned() {
        val rule = getRule("V{event_id}")
        val ruleEffects = callEventRuleEngine(rule)
        assertProgramRuleVariableAssignment(ruleEffects, rule,
            EVENT_ID
        )
    }

    @Test
    fun testEventStatusProgramVariableIsAssigned() {
        val rule = getRule("V{event_status}")
        val ruleEffects = callEventRuleEngine(rule)
        assertProgramRuleVariableAssignment(ruleEffects, rule, RULE_EVENT_STATUS.name)
    }

    @Test
    fun testIncidentDateProgramVariableIsAssigned() {
        val rule = getRule("V{incident_date}")
        val ruleEffects = callEventRuleEngine(rule)
        assertProgramRuleVariableAssignment(ruleEffects, rule,
            INCIDENT_DATE_STRING
        )
    }

    @Test
    fun testOrganisationUnitProgramVariableIsAssigned() {
        val rule = getRule("V{org_unit}")
        val ruleEffects = callEnrollmentRuleEngine(rule)
        assertProgramRuleVariableAssignment(ruleEffects, rule,
            ORGANISATION_UNIT
        )
    }

    @Test
    fun testOrganisationUnitCodeProgramVariableIsAssigned() {
        val rule = getRule("V{orgunit_code}")
        val ruleEffects = callEventRuleEngine(rule)
        assertProgramRuleVariableAssignment(ruleEffects, rule,
            ORGANISATION_UNIT_CODE
        )
    }

    @Test
    fun testProgramNameProgramVariableIsAssigned() {
        val rule = getRule("V{program_name}")
        val ruleEffects = callEnrollmentRuleEngine(rule)
        assertProgramRuleVariableAssignment(ruleEffects, rule,
            PROGRAM_NAME
        )
    }

    @Test
    fun testProgramStageIdProgramVariableIsAssigned() {
        val rule = getRule("V{program_stage_id}")
        val ruleEffects = callEventRuleEngine(rule)
        assertProgramRuleVariableAssignment(ruleEffects, rule,
            PROGRAM_STAGE
        )
    }

    @Test
    fun testProgramStageNameProgramVariableIsAssigned() {
        val rule = getRule("V{program_stage_name}")
        val ruleEffects = callEventRuleEngine(rule)
        assertProgramRuleVariableAssignment(ruleEffects, rule,
            PROGRAM_STAGE_NAME
        )
    }

    @Test
    fun testTEICountProgramVariableIsAssigned() {
        val rule = getRule("V{tei_count}")
        val ruleEffects = callEnrollmentRuleEngine(rule)
        assertProgramRuleVariableAssignment(ruleEffects, rule, "1")
    }

    private fun getRule(variable: String): Rule {
        val assignAction = RuleAction(variable, "ASSIGN", mapOf(Pair("field","#{test_data_element}")))
        return Rule("true", listOf(assignAction), "test_program_rule1")
    }

    private fun assertProgramRuleVariableAssignment(ruleEffects: List<RuleEffect>, rule: Rule, variableValue: String) {
        assertEquals(1, ruleEffects.size)
        assertEquals(variableValue, ruleEffects[0].data)
        assertEquals(rule.actions[0], ruleEffects[0].ruleAction)
    }
    
    private fun callEnrollmentRuleEngine(rule: Rule): List<RuleEffect> {
        val ruleEngineContext = getRuleEngineContext(listOf(rule))
        return RuleEngine.getInstance().evaluate(enrollment, emptyList(), ruleEngineContext)
    }
    
    private fun callEventRuleEngine(rule: Rule): List<RuleEffect> {
        val ruleEngineContext = getRuleEngineContext(listOf(rule))
        val event = RuleEvent(
            event = EVENT_ID,
            programStage = PROGRAM_STAGE,
            programStageName = PROGRAM_STAGE_NAME,
            status = RULE_EVENT_STATUS,
            eventDate = EVENT_DATE,
            dueDate = DUE_DATE,
            organisationUnit = ORGANISATION_UNIT,
            organisationUnitCode = ORGANISATION_UNIT_CODE,
            completedDate = null,
            dataValues = emptyList()
        )
        return RuleEngine.getInstance().evaluate(event, enrollment, emptyList(), ruleEngineContext)
    }

    private val enrollment: RuleEnrollment
        get() = RuleEnrollment(
            ENROLLMENT_ID,
            PROGRAM_NAME,
            INCIDENT_DATE,
            ENROLLMENT_DATE,
            RuleEnrollmentStatus.ACTIVE,
            ORGANISATION_UNIT,
            ORGANISATION_UNIT_CODE,
            listOf(RuleAttributeValue("test_attribute", "test_value"))
        )

    private fun getRuleEngineContext(rules: List<Rule>): RuleEngineContext {
        return RuleEngineContext(rules)
    }

    companion object {
        private val CURRENT_DATE = LocalDate.Companion.currentDate()
        private const val DUE_DATE_STRING = "2020-06-01"
        private val DUE_DATE = LocalDate.parse(DUE_DATE_STRING)
        private const val ENROLLMENT_DATE_STRING = "2019-01-01"
        private val ENROLLMENT_DATE = LocalDate.parse(ENROLLMENT_DATE_STRING)
        private const val EVENT_DATE_STRING = "2019-02-02"
        private val EVENT_DATE = Instant.parse(EVENT_DATE_STRING + "T01:00:00Z")
        private const val INCIDENT_DATE_STRING = "2020-01-01"
        private val INCIDENT_DATE = LocalDate.parse(INCIDENT_DATE_STRING)
        private const val PROGRAM_STAGE = "program stage"
        private const val PROGRAM_STAGE_NAME = "program stage name"
        private val RULE_EVENT_STATUS = RuleEventStatus.ACTIVE
        private const val ORGANISATION_UNIT = "organisation unit"
        private const val ORGANISATION_UNIT_CODE = "organisation unit code"
        private const val ENROLLMENT_ID = "enrollment id"
        private val ENROLLMENT_STATUS = RuleEnrollmentStatus.ACTIVE
        private const val EVENT_ID = "event id"
        private const val PROGRAM_NAME = "program name"
    }
}
