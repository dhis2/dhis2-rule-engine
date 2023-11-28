package org.hisp.dhis.rules.models;

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

import org.hisp.dhis.rules.RuleEngine;
import org.hisp.dhis.rules.RuleEngineContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Zubair Asghar.
 */

@RunWith( JUnit4.class )
public class CalculatedValueTest
{
    @Test
    public void evaluateTenThousandRulesTest()
        throws Exception
    {
        int i = 10000;
        RuleEngine.Builder ruleEngineBuilder = getRuleEngine( createRules( i ) );

        RuleEnrollment enrollment = RuleEnrollment.builder()
            .enrollment( "test_enrollment" )
            .programName( "test_program" )
            .incidentDate( new Date() )
            .enrollmentDate( new Date() )
            .status( RuleEnrollment.Status.ACTIVE )
            .organisationUnit( "test_ou" )
            .organisationUnitCode( "test_ou_code" )
            .attributeValues(List.of())
            .build();

        RuleEvent ruleEvent = RuleEvent.builder()
            .event( "test_event" )
            .programStage( "test_program_stage" )
            .programStageName( "" )
            .status( RuleEvent.Status.ACTIVE )
            .eventDate( new Date() )
            .dueDate( new Date() )
            .organisationUnit( "" )
            .organisationUnitCode( "" )
            .dataValues( List.of( RuleDataValue.create(
                new Date(), "test_program_stage", "test_data_element", "test_value" ) ) )
            .build();

        RuleEngine ruleEngine = ruleEngineBuilder.enrollment( enrollment ).build();
        List<RuleEffect> ruleEffects = ruleEngine.evaluate( ruleEvent ).call();

        assertEquals( i , ruleEffects.size() );
    }

    @Test
    public void sendMessageMustGetValueFromAssignAction()
        throws Exception
    {
        RuleAction assignAction = RuleActionAssign.create( "#{test_calculated_value}", "2+2", null );
        org.hisp.dhis.rules.models.Rule rule = org.hisp.dhis.rules.models.Rule
            .create( null, 1, "true", List.of( assignAction ), "test_program_rule1", "" );

        RuleAction sendMessageAction = RuleActionSendMessage.create( "test_notification", "4" );
        org.hisp.dhis.rules.models.Rule rule2 = org.hisp.dhis.rules.models.Rule
            .create( null, 4, "#{test_calculated_value}==4", List.of( sendMessageAction ), "test_program_rule2",
                "" );

        RuleEnrollment enrollment = RuleEnrollment.builder()
            .enrollment( "test_enrollment" )
            .programName( "test_program" )
            .incidentDate( new Date() )
            .enrollmentDate( new Date() )
            .status( RuleEnrollment.Status.ACTIVE )
            .organisationUnit( "test_ou" )
            .organisationUnitCode( "test_ou_code" )
            .attributeValues(List.of())
            .build();

        RuleEvent ruleEvent = RuleEvent.builder()
            .event( "test_event" )
            .programStage( "test_program_stage" )
            .programStageName( "" )
            .status( RuleEvent.Status.ACTIVE )
            .eventDate( new Date() )
            .dueDate( new Date() )
            .organisationUnit( "" )
            .organisationUnitCode( "" )
            .dataValues( List.of( RuleDataValue.create(
                new Date(), "test_program_stage", "test_data_element", "test_value" ) ) )
            .build();

        RuleEngine ruleEngine = getRuleEngine( List.of( rule, rule2 ) ).enrollment( enrollment ).build();
        List<RuleEffect> ruleEffects = ruleEngine.evaluate( ruleEvent ).call();

        assertEquals( "4" , ruleEffects.get( 0 ).data() );
        assertEquals( sendMessageAction , ruleEffects.get( 0 ).ruleAction() );
    }

    private List<org.hisp.dhis.rules.models.Rule> createRules( int i )
    {
        List<org.hisp.dhis.rules.models.Rule> rules = new ArrayList<>();
        RuleAction assignAction = RuleActionAssign.create( "#{test_calculated_value}", "2+2", null );
        org.hisp.dhis.rules.models.Rule rule = org.hisp.dhis.rules.models.Rule
            .create( null, 1, "true", List.of( assignAction ), "test_program_rule1", "" );

        RuleAction sendMessageAction = RuleActionSendMessage.create( "test_notification", "4" );
        org.hisp.dhis.rules.models.Rule rule2 = org.hisp.dhis.rules.models.Rule
            .create( null, 4, "#{test_calculated_value}==4", List.of( sendMessageAction ), "test_program_rule2",
                "" );
        for ( int j = 0; j < i; j++ )
        {
            rules.add( rule );
            rules.add( rule2 );
        }
        return rules;
    }

    @Test
    public void sendMessageMustGetValueFromAssignActionInSingleExecution()
        throws Exception
    {
        RuleAction assignAction = RuleActionAssign.create( "#{test_calculated_value}", "2+2", null );
        org.hisp.dhis.rules.models.Rule rule = org.hisp.dhis.rules.models.Rule
            .create( null, 1, "true", List.of( assignAction ), "test_program_rule1", "" );

        RuleAction sendMessageAction = RuleActionSendMessage.create( "test_notification", "4.0" );
        org.hisp.dhis.rules.models.Rule rule2 = org.hisp.dhis.rules.models.Rule
            .create( null, 4, "#{test_calculated_value}==4.0", List.of( sendMessageAction ),
                "test_program_rule2", "" );

        RuleEngine.Builder ruleEngineBuilder = getRuleEngine( List.of( rule, rule2 ) );

        RuleEnrollment enrollment = RuleEnrollment.builder()
            .enrollment( "test_enrollment" )
            .programName( "test_program" )
            .incidentDate( new Date() )
            .enrollmentDate( new Date() )
            .status( RuleEnrollment.Status.ACTIVE )
            .organisationUnit( "test_ou" )
            .organisationUnitCode( "test_ou_code" )
            .attributeValues(List.of())
            .build();

        RuleEvent ruleEvent = RuleEvent.builder()
            .event( "test_event" )
            .programStage( "test_program_stage" )
            .programStageName( "" )
            .status( RuleEvent.Status.ACTIVE )
            .eventDate( new Date() )
            .dueDate( new Date() )
            .organisationUnit( "" )
            .organisationUnitCode( "" )
            .dataValues( List.of( RuleDataValue.create(
                new Date(), "test_program_stage", "test_data_element", "test_value" ) ) )
            .build();

        RuleEngine ruleEngine = ruleEngineBuilder.enrollment( enrollment ).build();
        List<RuleEffect> ruleEffects = ruleEngine.evaluate( ruleEvent ).call();

        assertEquals( 1 , ruleEffects.size() );
        assertEquals( "4" , ruleEffects.get( 0 ).data() );
        assertEquals( sendMessageAction , ruleEffects.get( 0 ).ruleAction() );

    }

    private RuleEngine.Builder getRuleEngine( List<org.hisp.dhis.rules.models.Rule> rules )
    {
        RuleVariable ruleVariable = RuleVariableCalculatedValue
            .create( "test_calculated_value", "", RuleValueType.TEXT, true, new ArrayList<>());

        return RuleEngineContext
            .builder()
            .rules( rules )
            .ruleVariables( List.of( ruleVariable ) )
            .supplementaryData( new HashMap<String, List<String>>() )
            .constantsValue( new HashMap<String, String>() )
            .build().toEngineBuilder().triggerEnvironment( TriggerEnvironment.SERVER );
    }
}
