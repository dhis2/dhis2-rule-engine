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

import org.hisp.dhis.rules.ExpressionEvaluator;
import org.hisp.dhis.rules.RuleEngine;
import org.hisp.dhis.rules.RuleEngineContext;
import org.hisp.dhis.rules.RuleExpressionEvaluator;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;

import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;

/**
 * @Author Zubair Asghar.
 */

@RunWith( JUnit4.class )
public class CalculatedValueTests
{
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private RuleExpressionEvaluator ruleExpressionEvaluator;

    private Map<String, Map<String, String>> calculatedValueMap = new HashMap<>();

    @Test
    public void shouldThrowExceptionIfCalculatedValueMapIsNull()
    {
        thrown.expect( IllegalArgumentException.class );
        RuleEngineContext ruleEngineContext = RuleEngineContext.builder( ruleExpressionEvaluator )
                .ruleVariables( Arrays.asList( mock( RuleVariable.class ) ) )
                .supplementaryData( new HashMap<String, List<String>>() )
                .calculatedValueMap( null )
                .rules( Arrays.asList( mock( org.hisp.dhis.rules.models.Rule.class ) ) )
                .build();
    }

    @Test
    public void sendMessageMustGetValueFromAssignAction() throws Exception
    {
        RuleAction assignAction = RuleActionAssign.create(null, "2+2", "#{test_calculated_value}" );
        org.hisp.dhis.rules.models.Rule rule = org.hisp.dhis.rules.models.Rule.create( null, 1, "true", Arrays.asList( assignAction ), "test_program_rule1");

        RuleAction sendMessageAction = RuleActionSendMessage.create( "test_notification", "4" );
        org.hisp.dhis.rules.models.Rule rule2 = org.hisp.dhis.rules.models.Rule.create( null, 4, "#{test_calculated_value}==4", Arrays.asList( sendMessageAction ), "test_program_rule2");
        org.hisp.dhis.rules.models.Rule rule3 = org.hisp.dhis.rules.models.Rule.create( null, null, "true", Arrays.asList( sendMessageAction ), "test_program_rule3");


        RuleEngine.Builder ruleEngineBuilder = getRuleEngine( Arrays.asList( rule ) );

        RuleEnrollment enrollment = RuleEnrollment.create( "test_enrollment", new Date(), new Date(), RuleEnrollment.Status.ACTIVE, "test_ou", Arrays.asList(), "test_program");

        RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage",
                RuleEvent.Status.ACTIVE, new Date(), new Date(), "",Arrays.asList( RuleDataValue.create(
                        new Date(), "test_program_stage", "test_data_element", "test_value" ) ), "");

        RuleEngine ruleEngine = ruleEngineBuilder.enrollment( enrollment ).build();
        List<RuleEffect> ruleEffects = ruleEngine.evaluate( ruleEvent ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).data() ).isEqualTo( "4" );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( assignAction );

        RuleActionAssign assign = (RuleActionAssign) ruleEffects.get( 0 ).ruleAction();
        Map<String, String> valueMap = new HashMap<>();
        valueMap.put( "test_calculated_value", ruleEffects.get( 0 ).data() );
        calculatedValueMap.put( enrollment.enrollment(), valueMap );

        RuleEngine ruleEngine2 = getRuleEngine( Arrays.asList( rule2, rule, rule3 ) ).enrollment( enrollment ).build();
        List<RuleEffect> ruleEffects2 = ruleEngine2.evaluate( ruleEvent ).call();

        assertThat( ruleEffects2.size() ).isEqualTo( 3 );

        List<RuleAction> ruleActions = ruleEffects2.stream().map( RuleEffect::ruleAction ).collect( Collectors.toList() );

        assertThat( ruleActions.contains( assignAction ) ).isEqualTo( true );
        assertThat( ruleActions.contains( sendMessageAction ) ).isEqualTo( true );
    }

    @Test
    public void sendMessageMustGetValueFromAssignActionInSingleExecution() throws Exception
    {
        RuleAction assignAction = RuleActionAssign.create(null, "2+2", "#{test_calculated_value}" );
        org.hisp.dhis.rules.models.Rule rule = org.hisp.dhis.rules.models.Rule.create( null, 1, "true", Arrays.asList( assignAction ), "test_program_rule1");

        RuleAction sendMessageAction = RuleActionSendMessage.create( "test_notification", "4" );
        org.hisp.dhis.rules.models.Rule rule2 = org.hisp.dhis.rules.models.Rule.create( null, 4, "#{test_calculated_value}==4", Arrays.asList( sendMessageAction ), "test_program_rule2");

        RuleEngine.Builder ruleEngineBuilder = getRuleEngine( Arrays.asList( rule, rule2 ) );

        RuleEnrollment enrollment = RuleEnrollment.create( "test_enrollment", new Date(), new Date(), RuleEnrollment.Status.ACTIVE, "test_ou", Arrays.asList(), "test_program");

        RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage",
                RuleEvent.Status.ACTIVE, new Date(), new Date(), "",Arrays.asList( RuleDataValue.create(
                        new Date(), "test_program_stage", "test_data_element", "test_value" ) ), "");

        RuleEngine ruleEngine = ruleEngineBuilder.enrollment( enrollment ).build();
        List<RuleEffect> ruleEffects = ruleEngine.evaluate( ruleEvent ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 2 );
        assertThat( ruleEffects.get( 0 ).data() ).isEqualTo( "4" );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( assignAction );
        assertThat( ruleEffects.get( 1 ).ruleAction() ).isEqualTo( sendMessageAction );
    }

    private RuleEngine.Builder getRuleEngine( List<org.hisp.dhis.rules.models.Rule> rules )
    {
        RuleVariable ruleVariable = RuleVariableCalculatedValue.create("test_calculated_value", "", RuleValueType.TEXT );

        return RuleEngineContext
                .builder( new ExpressionEvaluator() )
                .rules( rules )
                .ruleVariables( Arrays.asList( ruleVariable ) )
                .calculatedValueMap( calculatedValueMap )
                .supplementaryData( new HashMap<>() )
                .build().toEngineBuilder().triggerEnvironment( TriggerEnvironment.SERVER );
    }
}
