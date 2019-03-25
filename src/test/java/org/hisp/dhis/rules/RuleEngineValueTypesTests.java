package org.hisp.dhis.rules;

import org.hisp.dhis.rules.models.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.*;

import static com.google.common.truth.Truth.assertThat;

@RunWith( JUnit4.class )
public class RuleEngineValueTypesTests
{
        @Test
        public void booleanVariableWithoutValueMustFallbackToDefaultBooleanValue()
            throws Exception
        {
                RuleAction ruleAction = RuleActionDisplayKeyValuePair
                    .createForFeedback( "test_action_content", "#{test_variable}" );
                Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ), "");
                RuleVariable ruleVariable = RuleVariableCurrentEvent
                    .create( "test_variable", "test_data_element", RuleValueType.BOOLEAN );

                RuleEngine ruleEngine = getRuleEngine( rule, Arrays.asList( ruleVariable ) );

                RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage",
                    RuleEvent.Status.ACTIVE, new Date(), new Date(), "",null, new ArrayList<RuleDataValue>(), "");
                List<RuleEffect> ruleEffects = ruleEngine.evaluate( ruleEvent ).call();

                assertThat( ruleEffects.size() ).isEqualTo( 1 );
                assertThat( ruleEffects.get( 0 ).data() ).isEqualTo( "false" );
                assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
        }

        @Test
        public void numericVariableWithoutValueMustFallbackToDefaultNumericValue()
            throws Exception
        {
                RuleAction ruleAction = RuleActionDisplayKeyValuePair
                    .createForFeedback( "test_action_content", "#{test_variable}" );
                Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ), "");
                RuleVariable ruleVariable = RuleVariableCurrentEvent
                    .create( "test_variable", "test_data_element", RuleValueType.NUMERIC );

                RuleEngine ruleEngine = getRuleEngine( rule, Arrays.asList( ruleVariable ) );

                RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage",
                    RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null,new ArrayList<RuleDataValue>(), "");
                List<RuleEffect> ruleEffects = ruleEngine.evaluate( ruleEvent ).call();

                assertThat( ruleEffects.size() ).isEqualTo( 1 );
                assertThat( ruleEffects.get( 0 ).data() ).isEqualTo( "0.0" );
                assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
        }

        @Test
        public void textVariableWithoutValueMustFallbackToDefaultTextValue()
            throws Exception
        {
                RuleAction ruleAction = RuleActionDisplayKeyValuePair
                    .createForFeedback( "test_action_content", "#{test_variable}" );
                Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ), "");
                RuleVariable ruleVariable = RuleVariableCurrentEvent
                    .create( "test_variable", "test_data_element", RuleValueType.TEXT );

                RuleEngine ruleEngine = getRuleEngine( rule, Arrays.asList( ruleVariable ) );

                RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage",
                    RuleEvent.Status.ACTIVE, new Date(), new Date(), "",null, new ArrayList<RuleDataValue>(), "");
                List<RuleEffect> ruleEffects = ruleEngine.evaluate( ruleEvent ).call();

                assertThat( ruleEffects.size() ).isEqualTo( 1 );
                assertThat( ruleEffects.get( 0 ).data() ).isEqualTo( "" );
                assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
        }

        private RuleEngine getRuleEngine( Rule rule, List<RuleVariable> ruleVariables )
        {
                return RuleEngineContext
                        .builder( new ExpressionEvaluator() )
                        .rules( Arrays.asList( rule ) )
                        .ruleVariables( ruleVariables )
                        .calculatedValueMap( new HashMap<>( ) )
                        .supplementaryData( new HashMap<>() )
                        .constantsValue( new HashMap<>() )
                        .build().toEngineBuilder().triggerEnvironment( TriggerEnvironment.SERVER )
                        .build();
        }
}
