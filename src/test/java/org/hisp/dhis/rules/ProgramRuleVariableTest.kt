package org.hisp.dhis.rules

import org.hisp.dhis.rules.models.*
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.*

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
@RunWith(JUnit4::class)
class ProgramRuleVariableTest {
    @Test
    
    fun testCurrentDateProgramVariableIsAssigned() {
        val rule = getRule("V{current_date}")
        val ruleEffects = callEnrollmentRuleEngine(rule)
        assertProgramRuleVariableAssignment(ruleEffects, rule, CURRENT_DATE)
    }

    @Test
    
    fun testDueDateProgramVariableIsAssigned() {
        val rule = getRule("V{due_date}")
        val ruleEffects = callEventRuleEngine(rule)
        assertProgramRuleVariableAssignment(ruleEffects, rule, DUE_DATE_STRING)
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
        assertProgramRuleVariableAssignment(ruleEffects, rule, ENROLLMENT_DATE_STRING)
    }

    @Test
    
    fun testEnrollmentIdProgramVariableIsAssigned() {
        val rule = getRule("V{enrollment_id}")
        val ruleEffects = callEnrollmentRuleEngine(rule)
        assertProgramRuleVariableAssignment(ruleEffects, rule, ENROLLMENT_ID)
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
        assertProgramRuleVariableAssignment(ruleEffects, rule, "Server")
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
        assertProgramRuleVariableAssignment(ruleEffects, rule, EVENT_DATE_STRING)
    }

    @Test
    
    fun testEventIdProgramVariableIsAssigned() {
        val rule = getRule("V{event_id}")
        val ruleEffects = callEventRuleEngine(rule)
        assertProgramRuleVariableAssignment(ruleEffects, rule, EVENT_ID)
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
        assertProgramRuleVariableAssignment(ruleEffects, rule, INCIDENT_DATE_STRING)
    }

    @Test
    
    fun testOrganisationUnitProgramVariableIsAssigned() {
        val rule = getRule("V{org_unit}")
        val ruleEffects = callEnrollmentRuleEngine(rule)
        assertProgramRuleVariableAssignment(ruleEffects, rule, ORGANISATION_UNIT)
    }

    @Test
    
    fun testOrganisationUnitCodeProgramVariableIsAssigned() {
        val rule = getRule("V{orgunit_code}")
        val ruleEffects = callEventRuleEngine(rule)
        assertProgramRuleVariableAssignment(ruleEffects, rule, ORGANISATION_UNIT_CODE)
    }

    @Test
    
    fun testProgramNameProgramVariableIsAssigned() {
        val rule = getRule("V{program_name}")
        val ruleEffects = callEnrollmentRuleEngine(rule)
        assertProgramRuleVariableAssignment(ruleEffects, rule, PROGRAM_NAME)
    }

    @Test
    
    fun testProgramStageIdProgramVariableIsAssigned() {
        val rule = getRule("V{program_stage_id}")
        val ruleEffects = callEventRuleEngine(rule)
        assertProgramRuleVariableAssignment(ruleEffects, rule, PROGRAM_STAGE)
    }

    @Test
    
    fun testProgramStageNameProgramVariableIsAssigned() {
        val rule = getRule("V{program_stage_name}")
        val ruleEffects = callEventRuleEngine(rule)
        assertProgramRuleVariableAssignment(ruleEffects, rule, PROGRAM_STAGE_NAME)
    }

    @Test
    
    fun testTEICountProgramVariableIsAssigned() {
        val rule = getRule("V{tei_count}")
        val ruleEffects = callEnrollmentRuleEngine(rule)
        assertProgramRuleVariableAssignment(ruleEffects, rule, "1")
    }

    private fun getRule(variable: String): Rule {
        val assignAction: RuleAction = RuleActionAssign.create(null, variable, "#{test_data_element}")
        return Rule("true", listOf(assignAction), "test_program_rule1")
    }

    private fun assertProgramRuleVariableAssignment(ruleEffects: List<RuleEffect>, rule: Rule, variableValue: String) {
        Assert.assertEquals(1, ruleEffects.size.toLong())
        Assert.assertEquals(variableValue, ruleEffects[0].data())
        Assert.assertEquals(rule.actions()[0], ruleEffects[0].ruleAction())
    }

    
    private fun callEnrollmentRuleEngine(rule: Rule): List<RuleEffect> {
        val ruleEngineBuilder = getRuleEngine(java.util.List.of(rule))
        val ruleEngine = ruleEngineBuilder.build()
        return ruleEngine.evaluate(enrollment).call()
    }

    
    private fun callEventRuleEngine(rule: Rule): List<RuleEffect> {
        val ruleEngineBuilder = getRuleEngine(java.util.List.of(rule))
        val event = RuleEvent.builder()
            .event(EVENT_ID)
            .programStage(PROGRAM_STAGE)
            .programStageName(PROGRAM_STAGE_NAME)
            .status(RULE_EVENT_STATUS)
            .eventDate(EVENT_DATE)
            .dueDate(DUE_DATE)
            .organisationUnit(ORGANISATION_UNIT)
            .organisationUnitCode(ORGANISATION_UNIT_CODE)
            .dataValues(ArrayList())
            .build()
        val ruleEngine = ruleEngineBuilder.enrollment(enrollment).build()
        return ruleEngine.evaluate(event).call()
    }

    private val enrollment: RuleEnrollment
        private get() = RuleEnrollment.builder()
            .enrollment(ENROLLMENT_ID)
            .programName(PROGRAM_NAME)
            .incidentDate(INCIDENT_DATE)
            .enrollmentDate(ENROLLMENT_DATE)
            .status(RuleEnrollment.Status.ACTIVE)
            .organisationUnit(ORGANISATION_UNIT)
            .organisationUnitCode(ORGANISATION_UNIT_CODE)
            .attributeValues(java.util.List.of(RuleAttributeValue.create("test_attribute", "test_value")))
            .build()

    private fun getRuleEngine(rules: List<Rule>): RuleEngine.Builder {
        return RuleEngineContext
            .builder()
            .rules(rules)
            .ruleVariables(listOf())
            .supplementaryData(HashMap())
            .constantsValue(HashMap())
            .build().toEngineBuilder().triggerEnvironment(TriggerEnvironment.SERVER)
    }

    companion object {
        private const val DATE_PATTERN = "yyyy-MM-dd"
        private val CURRENT_DATE = SimpleDateFormat(DATE_PATTERN, Locale.US).format(Date())
        private const val DUE_DATE_STRING = "2020-06-01"
        private val DUE_DATE = parseDate(DUE_DATE_STRING)
        private const val ENROLLMENT_DATE_STRING = "2019-01-01"
        private val ENROLLMENT_DATE = parseDate(ENROLLMENT_DATE_STRING)
        private const val EVENT_DATE_STRING = "2019-02-02"
        private val EVENT_DATE = parseDate(EVENT_DATE_STRING)
        private const val INCIDENT_DATE_STRING = "2020-01-01"
        private val INCIDENT_DATE = parseDate(INCIDENT_DATE_STRING)
        private const val PROGRAM_STAGE = "program stage"
        private const val PROGRAM_STAGE_NAME = "program stage name"
        private val RULE_EVENT_STATUS = RuleEvent.Status.ACTIVE
        private const val ORGANISATION_UNIT = "organisation unit"
        private const val ORGANISATION_UNIT_CODE = "organisation unit code"
        private const val ENROLLMENT_ID = "enrollment id"
        private val ENROLLMENT_STATUS = RuleEnrollment.Status.ACTIVE
        private const val EVENT_ID = "event id"
        private const val PROGRAM_NAME = "program name"
        private fun parseDate(date: String): Date {
            return Date.from(LocalDate.parse(date).atStartOfDay().toInstant(ZoneOffset.UTC))
        }
    }
}
