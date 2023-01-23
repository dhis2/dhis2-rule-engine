package org.dhis2.ruleengine
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
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDate
import kotlinx.datetime.toLocalDateTime
import org.dhis2.ruleengine.models.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ProgramRuleVariableTest {
    @Test
    @Throws(Exception::class)
    fun testCurrentDateProgramVariableIsAssigned() {
        val rule: Rule = getRule("V{current_date}")
        val ruleEffects = callEnrollmentRuleEngine(rule)
        assertProgramRuleVariableAssignment(ruleEffects, rule, CURRENT_DATE.toString())
    }

    @Test
    @Throws(Exception::class)
    fun testDueDateProgramVariableIsAssigned() {
        val rule: Rule = getRule("V{due_date}")
        val ruleEffects = callEventRuleEngine(rule)
        assertProgramRuleVariableAssignment(ruleEffects, rule, DUE_DATE_STRING)
    }

    @Test
    @Throws(Exception::class)
    fun testEnrollmentCountProgramVariableIsAssigned() {
        val rule: Rule = getRule("V{enrollment_count}")
        val ruleEffects = callEnrollmentRuleEngine(rule)
        assertProgramRuleVariableAssignment(ruleEffects, rule, "1")
    }

    @Test
    @Throws(Exception::class)
    fun testEnrollmentDateProgramVariableIsAssigned() {
        val rule: Rule = getRule("V{enrollment_date}")
        val ruleEffects = callEnrollmentRuleEngine(rule)
        assertProgramRuleVariableAssignment(ruleEffects, rule, ENROLLMENT_DATE_STRING)
    }

    @Test
    @Throws(Exception::class)
    fun testEnrollmentIdProgramVariableIsAssigned() {
        val rule: Rule = getRule("V{enrollment_id}")
        val ruleEffects = callEnrollmentRuleEngine(rule)
        assertProgramRuleVariableAssignment(ruleEffects, rule, ENROLLMENT_ID)
    }

    @Test
    @Throws(Exception::class)
    fun testEnrollmentStatusProgramVariableIsAssigned() {
        val rule: Rule = getRule("V{enrollment_status}")
        val ruleEffects = callEnrollmentRuleEngine(rule)
        assertProgramRuleVariableAssignment(ruleEffects, rule, ENROLLMENT_STATUS.name)
    }

    @Test
    @Throws(Exception::class)
    fun testEnvironmentProgramVariableIsAssigned() {
        val rule: Rule = getRule("V{environment}")
        val ruleEffects = callEnrollmentRuleEngine(rule)
        assertProgramRuleVariableAssignment(ruleEffects, rule, getClientName().clientName)
    }

    @Test
    @Throws(Exception::class)
    fun testEventCountProgramVariableIsAssigned() {
        val rule: Rule = getRule("V{event_count}")
        val ruleEffects = callEventRuleEngine(rule)
        assertProgramRuleVariableAssignment(ruleEffects, rule, "1")
    }

    @Test
    @Throws(Exception::class)
    fun testEventDateProgramVariableIsAssigned() {
        val rule: Rule = getRule("V{event_date}")
        val ruleEffects = callEventRuleEngine(rule)
        assertProgramRuleVariableAssignment(ruleEffects, rule, EVENT_DATE_STRING)
    }

    @Test
    @Throws(Exception::class)
    fun testEventIdProgramVariableIsAssigned() {
        val rule: Rule = getRule("V{event_id}")
        val ruleEffects = callEventRuleEngine(rule)
        assertProgramRuleVariableAssignment(ruleEffects, rule, EVENT_ID)
    }

    @Test
    @Throws(Exception::class)
    fun testEventStatusProgramVariableIsAssigned() {
        val rule: Rule = getRule("V{event_status}")
        val ruleEffects = callEventRuleEngine(rule)
        assertProgramRuleVariableAssignment(ruleEffects, rule, RULE_EVENT_STATUS.name)
    }

    @Test
    @Throws(Exception::class)
    fun testIncidentDateProgramVariableIsAssigned() {
        val rule: Rule = getRule("V{incident_date}")
        val ruleEffects = callEventRuleEngine(rule)
        assertProgramRuleVariableAssignment(ruleEffects, rule, INCIDENT_DATE_STRING)
    }

    @Test
    @Throws(Exception::class)
    fun testOrganisationUnitProgramVariableIsAssigned() {
        val rule: Rule = getRule("V{org_unit}")
        val ruleEffects = callEnrollmentRuleEngine(rule)
        assertProgramRuleVariableAssignment(ruleEffects, rule, ORGANISATION_UNIT)
    }

    @Test
    @Throws(Exception::class)
    fun testOrganisationUnitCodeProgramVariableIsAssigned() {
        val rule: Rule = getRule("V{orgunit_code}")
        val ruleEffects = callEventRuleEngine(rule)
        assertProgramRuleVariableAssignment(ruleEffects, rule, ORGANISATION_UNIT_CODE)
    }

    @Test
    @Throws(Exception::class)
    fun testProgramNameProgramVariableIsAssigned() {
        val rule: Rule = getRule("V{program_name}")
        val ruleEffects = callEnrollmentRuleEngine(rule)
        assertProgramRuleVariableAssignment(ruleEffects, rule, PROGRAM_NAME)
    }

    @Test
    @Throws(Exception::class)
    fun testProgramStageIdProgramVariableIsAssigned() {
        val rule: Rule = getRule("V{program_stage_id}")
        val ruleEffects = callEventRuleEngine(rule)
        assertProgramRuleVariableAssignment(ruleEffects, rule, PROGRAM_STAGE)
    }

    @Test
    @Throws(Exception::class)
    fun testProgramStageNameProgramVariableIsAssigned() {
        val rule: Rule = getRule("V{program_stage_name}")
        val ruleEffects = callEventRuleEngine(rule)
        assertProgramRuleVariableAssignment(ruleEffects, rule, PROGRAM_STAGE_NAME)
    }

    @Test
    @Throws(Exception::class)
    fun testTEICountProgramVariableIsAssigned() {
        val rule: Rule = getRule("V{tei_count}")
        val ruleEffects = callEnrollmentRuleEngine(rule)
        assertProgramRuleVariableAssignment(ruleEffects, rule, "1")
    }

    private fun getRule(variable: String): Rule {
        val assignAction = RuleAction.Assign("test_data_element", variable, "")
        return Rule("test_program_rule1", null, 1, "true", listOf(assignAction), "")
    }

    private fun assertProgramRuleVariableAssignment(ruleEffects: List<RuleEffect>, rule: Rule, variableValue: String) {
        assertEquals(1, ruleEffects.size)
        assertEquals(variableValue, ruleEffects[0].data)
        assertEquals(rule.actions[0], ruleEffects[0].ruleAction)
    }

    @Throws(Exception::class)
    private fun callEnrollmentRuleEngine(rule: Rule): List<RuleEffect> {
        val ruleEngine = getRuleEngine(listOf(rule))
        return ruleEngine.evaluate(ruleEnrollment = enrollment)
    }

    @Throws(Exception::class)
    private fun callEventRuleEngine(rule: Rule): List<RuleEffect> {
        val ruleEngine = getRuleEngine(listOf(rule))
        val event = RuleEvent(
            EVENT_ID,
            PROGRAM_STAGE,
            PROGRAM_STAGE_NAME,
            RULE_EVENT_STATUS,
            EVENT_DATE,
            DUE_DATE,
            null,
            ORGANISATION_UNIT,
            ORGANISATION_UNIT_CODE,
            ArrayList<RuleDataValue>()
        )

        return ruleEngine.evaluate(ruleEnrollment = enrollment, ruleEvent = event)
    }

    private val enrollment: RuleEnrollment
        get() = RuleEnrollment(
            ENROLLMENT_ID,
            PROGRAM_NAME,
            INCIDENT_DATE,
            ENROLLMENT_DATE,
            RuleEnrollment.Status.ACTIVE,
            ORGANISATION_UNIT,
            ORGANISATION_UNIT_CODE,
            listOf(RuleAttributeValue("test_attribute", "test_value"))
        )

    private fun getRuleEngine(rules: List<Rule>): RuleEngine {
        return RuleEngineContext(
            rules = rules,
            ruleVariables = emptyList(),
            supplementaryData = emptyMap(),
            constantsValues = emptyMap(),
            dataItemStore = emptyMap()
        ).toRuleEngine(emptyList(), null)
    }

    companion object {
        private const val DATE_PATTERN = "yyyy-MM-dd"
        private val CURRENT_DATE = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
        private const val DUE_DATE_STRING = "2020-06-01"
        private val DUE_DATE = DUE_DATE_STRING.toLocalDate()
        private const val ENROLLMENT_DATE_STRING = "2019-01-01"
        private val ENROLLMENT_DATE = ENROLLMENT_DATE_STRING.toLocalDate()
        private const val EVENT_DATE_STRING = "2019-02-02"
        private val EVENT_DATE = EVENT_DATE_STRING.toLocalDate()
        private const val INCIDENT_DATE_STRING = "2020-01-01"
        private val INCIDENT_DATE = INCIDENT_DATE_STRING.toLocalDate()
        private const val PROGRAM_STAGE = "program stage"
        private const val PROGRAM_STAGE_NAME = "program stage name"
        private val RULE_EVENT_STATUS: RuleEvent.Status = RuleEvent.Status.ACTIVE
        private const val ORGANISATION_UNIT = "organisation unit"
        private const val ORGANISATION_UNIT_CODE = "organisation unit code"
        private const val ENROLLMENT_ID = "enrollment id"
        private val ENROLLMENT_STATUS: RuleEnrollment.Status = RuleEnrollment.Status.ACTIVE
        private const val EVENT_ID = "event id"
        private const val PROGRAM_NAME = "program name"
    }
}