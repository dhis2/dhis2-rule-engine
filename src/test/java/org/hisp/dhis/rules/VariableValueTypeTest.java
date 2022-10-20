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

import com.google.common.collect.Maps;
import org.hisp.dhis.rules.models.Rule;
import org.hisp.dhis.rules.models.RuleAction;
import org.hisp.dhis.rules.models.RuleActionAssign;
import org.hisp.dhis.rules.models.RuleActionDisplayKeyValuePair;
import org.hisp.dhis.rules.models.RuleActionShowError;
import org.hisp.dhis.rules.models.RuleAttributeValue;
import org.hisp.dhis.rules.models.RuleDataValue;
import org.hisp.dhis.rules.models.RuleEffect;
import org.hisp.dhis.rules.models.RuleEnrollment;
import org.hisp.dhis.rules.models.RuleEvent;
import org.hisp.dhis.rules.models.RuleValueType;
import org.hisp.dhis.rules.models.RuleVariable;
import org.hisp.dhis.rules.models.RuleVariableCurrentEvent;
import org.hisp.dhis.rules.models.TriggerEnvironment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@RunWith( JUnit4.class )
public class VariableValueTypeTest
{
    @Test
    public void testNumericVariablesAreComparedCorrectly()
            throws Exception
    {
        RuleAction ruleAction = RuleActionDisplayKeyValuePair
                .createForFeedback( "test_action_content", "#{test_variable}" );
        Rule rule = Rule.create( null, null, "#{test_variable} > #{test_variable2}", Arrays.asList( ruleAction ), "", "" );
        RuleVariable ruleVariable = RuleVariableCurrentEvent
                .create( "test_variable", "test_data_element", RuleValueType.NUMERIC );
        RuleVariable ruleVariable2 = RuleVariableCurrentEvent
                .create( "test_variable2", "test_data_element2", RuleValueType.NUMERIC );

        RuleEngine ruleEngine = getRuleEngine( rule, Arrays.asList( ruleVariable, ruleVariable2 ) );

        RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage",
                RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null,
                Arrays.asList(RuleDataValue.create(new Date(), "", "test_data_element", "30"),
                        RuleDataValue.create(new Date(), "", "test_data_element2", "4")), "", null);
        List<RuleEffect> ruleEffects = ruleEngine.evaluate( ruleEvent ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).data() ).isEqualTo( "30" );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
    }

    @Test
    public void testTextVariablesAreComparedCorrectly()
            throws Exception
    {
        RuleAction ruleAction = RuleActionDisplayKeyValuePair
                .createForFeedback( "test_action_content", "#{test_variable}" );
        Rule rule = Rule.create( null, null, "#{test_variable} > #{test_variable2}", Arrays.asList( ruleAction ), "", "" );
        RuleVariable ruleVariable = RuleVariableCurrentEvent
                .create( "test_variable", "test_data_element", RuleValueType.TEXT );
        RuleVariable ruleVariable2 = RuleVariableCurrentEvent
                .create( "test_variable2", "test_data_element2", RuleValueType.TEXT );

        RuleEngine ruleEngine = getRuleEngine( rule, Arrays.asList( ruleVariable, ruleVariable2 ) );

        RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage",
                RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null,
                Arrays.asList(RuleDataValue.create(new Date(), "", "test_data_element", "30"),
                        RuleDataValue.create(new Date(), "", "test_data_element2", "4")), "", null);
        List<RuleEffect> ruleEffects = ruleEngine.evaluate( ruleEvent ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 0 );
    }

    private RuleEngine getRuleEngine( Rule rule, List<RuleVariable> ruleVariables )
    {
        return RuleEngineContext
                .builder()
                .rules( Arrays.asList( rule ) )
                .ruleVariables( ruleVariables )
                .supplementaryData( new HashMap<String, List<String>>() )
                .constantsValue( new HashMap<String, String>() )
                .build().toEngineBuilder().triggerEnvironment( TriggerEnvironment.SERVER )
                .build();
    }
}
