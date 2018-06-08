package org.hisp.dhis.rules;

import org.hisp.dhis.rules.models.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.*;

import static com.google.common.truth.Truth.assertThat;

@RunWith( JUnit4.class )
public class RuleEngineFunctionTests
{
        @Test
        public void evaluateHasValueFunctionMustReturnTrueIfValueSpecified()
            throws Exception
        {
                RuleAction ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
                    "test_action_content", "d2:hasValue('test_variable')" );
                RuleVariable ruleVariable = RuleVariableCurrentEvent.create(
                    "test_variable", "test_data_element", RuleValueType.TEXT );
                Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ) );

                RuleEngine ruleEngine = getRuleEngine( rule, Arrays.asList( ruleVariable ) );

                RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage",
                    RuleEvent.Status.ACTIVE, new Date(), new Date(), "", Arrays.asList( RuleDataValue.create(
                        new Date(), "test_program_stage", "test_data_element", "test_value" ) ), "");
                List<RuleEffect> ruleEffects = ruleEngine.evaluate( ruleEvent ).call();

                assertThat( ruleEffects.size() ).isEqualTo( 1 );
                assertThat( ruleEffects.get( 0 ).data() ).isEqualTo( "true" );
                assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
        }

        @Test
        public void evaluateHasValueFunctionMustReturnTrueIfNoValueSpecified()
            throws Exception
        {
                RuleAction ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
                    "test_action_content", "d2:hasValue('test_variable')" );
                RuleVariable ruleVariable = RuleVariableCurrentEvent.create(
                    "test_variable", "test_data_element", RuleValueType.TEXT );
                Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ) );

                RuleEngine ruleEngine = getRuleEngine( rule, Arrays.asList( ruleVariable ) );

                RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage",
                    RuleEvent.Status.ACTIVE, new Date(), new Date(), "", new ArrayList<RuleDataValue>(), "test_program_stage_name");
                List<RuleEffect> ruleEffects = ruleEngine.evaluate( ruleEvent ).call();

                assertThat( ruleEffects.size() ).isEqualTo( 1 );
                assertThat( ruleEffects.get( 0 ).data() ).isEqualTo( "false" );
                assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
        }

        @Test
        public void evaluateEnvironmentVariableProgramStageName()
                throws Exception
        {
                RuleAction ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
                        "test_action_content", "V{program_stage_name}" );
                RuleVariable ruleVariable = RuleVariableCurrentEvent.create( "variable", "test_data_element", RuleValueType.TEXT );
                Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ) );

                RuleEngine ruleEngine = getRuleEngine( rule, Arrays.asList( ruleVariable ) );

                RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage_id",
                        RuleEvent.Status.ACTIVE, new Date(), new Date(), "", new ArrayList<RuleDataValue>(), "test_program_stage_name");
                List<RuleEffect> ruleEffects = ruleEngine.evaluate( ruleEvent ).call();

                assertThat( ruleEffects.size() ).isEqualTo( 1 );
                assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
                assertThat( ruleEffects.get( 0 ).data() ).isEqualTo( "test_program_stage_name" );
        }

        @Test
        public void evaluateDaysBetweenMustReturnCorrectDiff()
            throws Exception
        {
                RuleAction ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
                    "test_action_content", "d2:daysBetween(#{test_var_one}, #{test_var_two})" );
                RuleVariable ruleVariableOne = RuleVariableCurrentEvent.create(
                    "test_var_one", "test_data_element_one", RuleValueType.TEXT );
                RuleVariable ruleVariableTwo = RuleVariableCurrentEvent.create(
                    "test_var_two", "test_data_element_two", RuleValueType.TEXT );
                Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ) );

                RuleEngine ruleEngine = getRuleEngine( rule, Arrays.asList( ruleVariableOne, ruleVariableTwo ) );

                RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage",
                    RuleEvent.Status.ACTIVE, new Date(), new Date(), "", Arrays.asList(
                        RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "2017-01-01" ),
                        RuleDataValue
                            .create( new Date(), "test_program_stage", "test_data_element_two", "2017-02-01" ) ), "");
                List<RuleEffect> ruleEffects = ruleEngine.evaluate( ruleEvent ).call();

                assertThat( ruleEffects.size() ).isEqualTo( 1 );
                assertThat( ruleEffects.get( 0 ).data() ).isEqualTo( "31" );
                assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
        }

        @Test
        public void evaluateD2InOrgUnitGroup()
            throws Exception
        {
                List<String> members = Arrays.asList( "location1", "location2" );

                Map<String, List<String>> supplementaryData = new HashMap<>();
                supplementaryData.put( "OU_GROUP_ID", members );

                RuleAction ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
                    "test_action_content", "d2:inOrgUnitGroup(#{test_var_one})" );
                RuleVariable ruleVariableOne = RuleVariableCurrentEvent.create(
                    "test_var_one", "test_data_element_one", RuleValueType.TEXT );

                Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ) );

                RuleEngine ruleEngine = RuleEngineContext
                    .builder( new ExpressionEvaluator() )
                    .rules( Arrays.asList( rule ) )
                    .ruleVariables( Arrays.asList( ruleVariableOne ) )
                    .supplementaryData( supplementaryData )
                    .build().toEngineBuilder().triggerEnvironment( TriggerEnvironment.SERVER )
                    .build();

                RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage",
                    RuleEvent.Status.ACTIVE, new Date(), new Date(), "location1" ,Arrays.asList(
                        RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "OU_GROUP_ID" )), "");

                List<RuleEffect> ruleEffects = ruleEngine.evaluate( ruleEvent ).call();

                assertThat( ruleEffects.size() ).isEqualTo( 1 );
                assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
                assertThat( ruleEffects.get( 0 ).data() ).isEqualTo( "true" );
        }

        @Test
        public void evaluateD2AddDays()
            throws Exception
        {
                RuleAction ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
                    "test_action_content", "d2:addDays(#{test_var_one}, #{test_var_two})" );
                RuleVariable ruleVariableOne = RuleVariableCurrentEvent.create(
                    "test_var_one", "test_data_element_one", RuleValueType.TEXT );
                RuleVariable ruleVariableTwo = RuleVariableCurrentEvent.create(
                    "test_var_two", "test_data_element_two", RuleValueType.TEXT );
                Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ) );

                RuleEngine ruleEngine = getRuleEngine( rule, Arrays.asList( ruleVariableOne, ruleVariableTwo ) );

                RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage",
                    RuleEvent.Status.ACTIVE, new Date(), new Date(), "", Arrays.asList(
                        RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "2017-01-01" ),
                        RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_two", "2" ) ), "");
                List<RuleEffect> ruleEffects = ruleEngine.evaluate( ruleEvent ).call();

                assertThat( ruleEffects.size() ).isEqualTo( 1 );
                assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
                assertThat( ruleEffects.get( 0 ).data() ).isEqualTo( "2017-01-03" );

                RuleEvent ruleEvent2 = RuleEvent.create( "test_event", "test_program_stage",
                    RuleEvent.Status.ACTIVE, new Date(), new Date(), "", Arrays.asList(
                        RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "2017-01-03" ),
                        RuleDataValue
                            .create( new Date(), "test_program_stage", "test_data_element_two", "-2" ) ), "");
                List<RuleEffect> ruleEffects2 = ruleEngine.evaluate( ruleEvent2 ).call();

                assertThat( ruleEffects2.size() ).isEqualTo( 1 );
                assertThat( ruleEffects2.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
                assertThat( ruleEffects2.get( 0 ).data() ).isEqualTo( "2017-01-01" );
        }

        @Test
        public void evaluateNestedFunctionCalls()
            throws Exception
        {
                RuleAction ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
                    "test_action_content", "d2:floor(#{test_var_one} + d2:ceil(#{test_var_three})) " +
                        "/ 5 * d2:ceil(#{test_var_two})" );

                RuleVariable ruleVariableOne = RuleVariableCurrentEvent.create(
                    "test_var_one", "test_data_element_one", RuleValueType.NUMERIC );
                RuleVariable ruleVariableTwo = RuleVariableCurrentEvent.create(
                    "test_var_two", "test_data_element_two", RuleValueType.NUMERIC );
                RuleVariable ruleVariableThree = RuleVariableCurrentEvent.create(
                    "test_var_three", "test_data_element_three", RuleValueType.NUMERIC );

                Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ) );

                RuleEngine ruleEngine = getRuleEngine( rule, Arrays.asList( ruleVariableOne, ruleVariableTwo, ruleVariableThree ) );

                RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage",
                    RuleEvent.Status.ACTIVE, new Date(), new Date(), "", Arrays.asList(
                        RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "19.9" ),
                        RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_two", "0.9" ),
                        RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_three", "10.6" ) ), "");
                List<RuleEffect> ruleEffects = ruleEngine.evaluate( ruleEvent ).call();

                assertThat( ruleEffects.size() ).isEqualTo( 1 );
                assertThat( ruleEffects.get( 0 ).data() ).isEqualTo( "6" );
                assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
        }

        private RuleEngine getRuleEngine( Rule rule, List<RuleVariable> ruleVariables )
        {
                return RuleEngineContext
                        .builder( new ExpressionEvaluator() )
                        .rules( Arrays.asList( rule ) )
                        .ruleVariables( ruleVariables )
                        .build().toEngineBuilder().triggerEnvironment( TriggerEnvironment.SERVER )
                        .build();
        }
}
