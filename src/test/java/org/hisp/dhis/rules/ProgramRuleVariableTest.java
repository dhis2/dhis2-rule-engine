package org.hisp.dhis.rules;

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

import org.hisp.dhis.rules.models.Rule;
import org.hisp.dhis.rules.models.RuleAction;
import org.hisp.dhis.rules.models.RuleActionAssign;
import org.hisp.dhis.rules.models.RuleAttributeValue;
import org.hisp.dhis.rules.models.RuleDataValue;
import org.hisp.dhis.rules.models.RuleEffect;
import org.hisp.dhis.rules.models.RuleEnrollment;
import org.hisp.dhis.rules.models.RuleEvent;
import org.hisp.dhis.rules.models.TriggerEnvironment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

@RunWith( JUnit4.class )
public class ProgramRuleVariableTest
{
    private static final String DATE_PATTERN = "yyyy-MM-dd";

    private final static String CURRENT_DATE = new SimpleDateFormat( DATE_PATTERN, Locale.US ).format( new Date() );

    private final static String DUE_DATE_STRING = "2020-06-01";

    private final static Date DUE_DATE = parseDate( DUE_DATE_STRING );

    private final static String ENROLLMENT_DATE_STRING = "2019-01-01";

    private final static Date ENROLLMENT_DATE = parseDate( ENROLLMENT_DATE_STRING );

    private final static String EVENT_DATE_STRING = "2019-02-02";

    private final static Date EVENT_DATE = parseDate( EVENT_DATE_STRING );

    private final static String INCIDENT_DATE_STRING = "2020-01-01";

    private final static Date INCIDENT_DATE = parseDate( INCIDENT_DATE_STRING );

    private static final String PROGRAM_STAGE = "program stage";

    private static final String PROGRAM_STAGE_NAME = "program stage name";

    private static final RuleEvent.Status RULE_EVENT_STATUS = RuleEvent.Status.ACTIVE;

    private static final String ORGANISATION_UNIT = "organisation unit";

    private static final String ORGANISATION_UNIT_CODE = "organisation unit code";

    private static final String ENROLLMENT_ID = "enrollment id";

    private static final RuleEnrollment.Status ENROLLMENT_STATUS = RuleEnrollment.Status.ACTIVE;

    private static final String EVENT_ID = "event id";

    private static final String PROGRAM_NAME = "program name";

    private static Date parseDate(String date) {
        return Date.from(LocalDate.parse( date ).atStartOfDay().toInstant(ZoneOffset.UTC));
    }

    @Test
    public void testCurrentDateProgramVariableIsAssigned()
        throws Exception
    {
        org.hisp.dhis.rules.models.Rule rule = getRule( "V{current_date}" );

        List<RuleEffect> ruleEffects = callEnrollmentRuleEngine( rule );

        assertProgramRuleVariableAssignment( ruleEffects, rule, CURRENT_DATE );
    }

    @Test
    public void testDueDateProgramVariableIsAssigned()
        throws Exception
    {
        org.hisp.dhis.rules.models.Rule rule = getRule( "V{due_date}" );

        List<RuleEffect> ruleEffects = callEventRuleEngine( rule );

        assertProgramRuleVariableAssignment( ruleEffects, rule, DUE_DATE_STRING );
    }

    @Test
    public void testEnrollmentCountProgramVariableIsAssigned()
        throws Exception
    {
        org.hisp.dhis.rules.models.Rule rule = getRule( "V{enrollment_count}" );

        List<RuleEffect> ruleEffects = callEnrollmentRuleEngine( rule );

        assertProgramRuleVariableAssignment( ruleEffects, rule, "1" );
    }

    @Test
    public void testEnrollmentDateProgramVariableIsAssigned()
        throws Exception
    {
        Rule rule = getRule( "V{enrollment_date}" );

        List<RuleEffect> ruleEffects = callEnrollmentRuleEngine( rule );

        assertProgramRuleVariableAssignment( ruleEffects, rule, ENROLLMENT_DATE_STRING );
    }

    @Test
    public void testEnrollmentIdProgramVariableIsAssigned()
        throws Exception
    {
        Rule rule = getRule( "V{enrollment_id}" );

        List<RuleEffect> ruleEffects = callEnrollmentRuleEngine( rule );

        assertProgramRuleVariableAssignment( ruleEffects, rule, ENROLLMENT_ID );
    }

    @Test
    public void testEnrollmentStatusProgramVariableIsAssigned()
        throws Exception
    {
        Rule rule = getRule( "V{enrollment_status}" );

        List<RuleEffect> ruleEffects = callEnrollmentRuleEngine( rule );

        assertProgramRuleVariableAssignment( ruleEffects, rule, ENROLLMENT_STATUS.name() );
    }

    @Test
    public void testEnvironmentProgramVariableIsAssigned()
        throws Exception
    {
        org.hisp.dhis.rules.models.Rule rule = getRule( "V{environment}" );

        List<RuleEffect> ruleEffects = callEnrollmentRuleEngine( rule );

        assertProgramRuleVariableAssignment( ruleEffects, rule, "Server" );
    }

    @Test
    public void testEventCountProgramVariableIsAssigned()
        throws Exception
    {
        org.hisp.dhis.rules.models.Rule rule = getRule( "V{event_count}" );

        List<RuleEffect> ruleEffects = callEventRuleEngine( rule );

        assertProgramRuleVariableAssignment( ruleEffects, rule, "1" );
    }

    @Test
    public void testEventDateProgramVariableIsAssigned()
        throws Exception
    {
        org.hisp.dhis.rules.models.Rule rule = getRule( "V{event_date}" );

        List<RuleEffect> ruleEffects = callEventRuleEngine( rule );

        assertProgramRuleVariableAssignment( ruleEffects, rule, EVENT_DATE_STRING );
    }

    @Test
    public void testEventIdProgramVariableIsAssigned()
        throws Exception
    {
        org.hisp.dhis.rules.models.Rule rule = getRule( "V{event_id}" );

        List<RuleEffect> ruleEffects = callEventRuleEngine( rule );

        assertProgramRuleVariableAssignment( ruleEffects, rule, EVENT_ID );
    }

    @Test
    public void testEventStatusProgramVariableIsAssigned()
        throws Exception
    {
        org.hisp.dhis.rules.models.Rule rule = getRule( "V{event_status}" );

        List<RuleEffect> ruleEffects = callEventRuleEngine( rule );

        assertProgramRuleVariableAssignment( ruleEffects, rule, RULE_EVENT_STATUS.name() );
    }

    @Test
    public void testIncidentDateProgramVariableIsAssigned()
        throws Exception
    {
        org.hisp.dhis.rules.models.Rule rule = getRule( "V{incident_date}" );

        List<RuleEffect> ruleEffects = callEventRuleEngine( rule );

        assertProgramRuleVariableAssignment( ruleEffects, rule, INCIDENT_DATE_STRING );
    }

    @Test
    public void testOrganisationUnitProgramVariableIsAssigned()
        throws Exception
    {
        org.hisp.dhis.rules.models.Rule rule = getRule( "V{org_unit}" );

        List<RuleEffect> ruleEffects = callEnrollmentRuleEngine( rule );

        assertProgramRuleVariableAssignment( ruleEffects, rule, ORGANISATION_UNIT );
    }

    @Test
    public void testOrganisationUnitCodeProgramVariableIsAssigned()
        throws Exception
    {
        org.hisp.dhis.rules.models.Rule rule = getRule( "V{orgunit_code}" );

        List<RuleEffect> ruleEffects = callEventRuleEngine( rule );

        assertProgramRuleVariableAssignment( ruleEffects, rule, ORGANISATION_UNIT_CODE );
    }

    @Test
    public void testProgramNameProgramVariableIsAssigned()
        throws Exception
    {
        org.hisp.dhis.rules.models.Rule rule = getRule( "V{program_name}" );

        List<RuleEffect> ruleEffects = callEnrollmentRuleEngine( rule );

        assertProgramRuleVariableAssignment( ruleEffects, rule, PROGRAM_NAME );
    }

    @Test
    public void testProgramStageIdProgramVariableIsAssigned()
        throws Exception
    {
        org.hisp.dhis.rules.models.Rule rule = getRule( "V{program_stage_id}" );

        List<RuleEffect> ruleEffects = callEventRuleEngine( rule );

        assertProgramRuleVariableAssignment( ruleEffects, rule, PROGRAM_STAGE );
    }

    @Test
    public void testProgramStageNameProgramVariableIsAssigned()
        throws Exception
    {
        org.hisp.dhis.rules.models.Rule rule = getRule( "V{program_stage_name}" );

        List<RuleEffect> ruleEffects = callEventRuleEngine( rule );

        assertProgramRuleVariableAssignment( ruleEffects, rule, PROGRAM_STAGE_NAME );
    }

    @Test
    public void testTEICountProgramVariableIsAssigned()
        throws Exception
    {
        org.hisp.dhis.rules.models.Rule rule = getRule( "V{tei_count}" );

        List<RuleEffect> ruleEffects = callEnrollmentRuleEngine( rule );

        assertProgramRuleVariableAssignment( ruleEffects, rule, "1" );
    }

    private org.hisp.dhis.rules.models.Rule getRule( String variable )
    {
        RuleAction assignAction = RuleActionAssign.create( null, variable, "#{test_data_element}" );
        return org.hisp.dhis.rules.models.Rule
            .create( null, 1, "true", List.of(assignAction), "test_program_rule1", "" );
    }

    private void assertProgramRuleVariableAssignment( List<RuleEffect> ruleEffects, Rule rule, String variableValue )
    {
        assertEquals( 1 , ruleEffects.size() );
        assertEquals( variableValue , ruleEffects.get( 0 ).data() );
        assertEquals( rule.actions().get( 0 ) , ruleEffects.get( 0 ).ruleAction() );
    }

    private List<RuleEffect> callEnrollmentRuleEngine( Rule rule )
        throws Exception
    {
        RuleEngine.Builder ruleEngineBuilder = getRuleEngine(List.of(rule));

        RuleEngine ruleEngine = ruleEngineBuilder.build();
        return ruleEngine.evaluate( getEnrollment() ).call();
    }

    private List<RuleEffect> callEventRuleEngine( Rule rule )
        throws Exception
    {
        RuleEngine.Builder ruleEngineBuilder = getRuleEngine(List.of(rule));

        RuleEvent event = RuleEvent.builder()
            .event( EVENT_ID )
            .programStage( PROGRAM_STAGE )
            .programStageName( PROGRAM_STAGE_NAME )
            .status( RULE_EVENT_STATUS )
            .eventDate( EVENT_DATE )
            .dueDate( DUE_DATE )
            .organisationUnit( ORGANISATION_UNIT )
            .organisationUnitCode( ORGANISATION_UNIT_CODE )
            .dataValues( new ArrayList<RuleDataValue>() )
            .build();

        RuleEngine ruleEngine = ruleEngineBuilder.enrollment( getEnrollment() ).build();
        return ruleEngine.evaluate( event ).call();
    }

    private RuleEnrollment getEnrollment()
    {
        return RuleEnrollment.builder()
            .enrollment( ENROLLMENT_ID )
            .programName( PROGRAM_NAME )
            .incidentDate( INCIDENT_DATE )
            .enrollmentDate( ENROLLMENT_DATE )
            .status( RuleEnrollment.Status.ACTIVE )
            .organisationUnit( ORGANISATION_UNIT )
            .organisationUnitCode( ORGANISATION_UNIT_CODE )
            .attributeValues(List.of(RuleAttributeValue.create("test_attribute", "test_value")))
            .build();
    }

    private RuleEngine.Builder getRuleEngine( List<Rule> rules )
    {
        return RuleEngineContext
            .builder()
            .rules( rules )
            .ruleVariables(List.of())
            .supplementaryData( new HashMap<String, List<String>>() )
            .constantsValue( new HashMap<String, String>() )
            .build().toEngineBuilder().triggerEnvironment( TriggerEnvironment.SERVER );
    }
}
