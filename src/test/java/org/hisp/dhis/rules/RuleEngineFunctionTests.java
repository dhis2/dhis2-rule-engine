package org.hisp.dhis.rules;

import org.hisp.dhis.rules.models.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith( JUnit4.class )
public class RuleEngineFunctionTests
{
    private static final String DATE_PATTERN = "yyyy-MM-dd";

    private SimpleDateFormat dateFormat = new SimpleDateFormat( DATE_PATTERN, Locale.US );

    @Test
    public void evaluateHasValueFunctionMustReturnTrueIfValueSpecified()
        throws Exception
    {
        RuleAction ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
            "test_action_content", "d2:hasValue(#{test_variable})" );
        RuleVariable ruleVariable = RuleVariableCurrentEvent.create(
            "test_variable", "test_data_element", RuleValueType.TEXT );
        Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ), "" );

        RuleEngine ruleEngine = getRuleEngine( rule, Arrays.asList( ruleVariable ) );

        RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList( RuleDataValue.create(
                new Date(), "test_program_stage", "test_data_element", "test_value" ) ), "" );
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
            "test_action_content", "d2:hasValue(#{test_variable})" );
        RuleVariable ruleVariable = RuleVariableCurrentEvent.create(
            "test_variable", "test_data_element", RuleValueType.TEXT );
        Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ), "" );

        RuleEngine ruleEngine = getRuleEngine( rule, Arrays.asList( ruleVariable ) );

        RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, new ArrayList<RuleDataValue>(),
            "test_program_stage_name" );
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
        RuleVariable ruleVariable = RuleVariableCurrentEvent
            .create( "variable", "test_data_element", RuleValueType.TEXT );
        Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ), "" );

        RuleEngine ruleEngine = getRuleEngine( rule, Arrays.asList( ruleVariable ) );

        RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage_id",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, new ArrayList<RuleDataValue>(),
            "test_program_stage_name" );
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
        Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ), "" );

        RuleEngine ruleEngine = getRuleEngine( rule, Arrays.asList( ruleVariableOne, ruleVariableTwo ) );

        RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "2017-01-01" ),
                RuleDataValue
                    .create( new Date(), "test_program_stage", "test_data_element_two", "2017-02-01" ) ), "" );
        List<RuleEffect> ruleEffects = ruleEngine.evaluate( ruleEvent ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).data() ).isEqualTo( "31" );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
    }

    @Test
    public void evaluateDaysBetweenWithSingleQuotedDateMustReturnCorrectDiff()
        throws Exception
    {
        RuleAction ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
            "test_action_content", "d2:daysBetween(#{test_var_one}, '2018-01-01')" );
        RuleVariable ruleVariableOne = RuleVariableCurrentEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT );
        RuleVariable ruleVariableTwo = RuleVariableCurrentEvent.create(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT );
        Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ), "" );

        RuleEngine ruleEngine = getRuleEngine( rule, Arrays.asList( ruleVariableOne, ruleVariableTwo ) );

        RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "2017-01-01" ),
                RuleDataValue
                    .create( new Date(), "test_program_stage", "test_data_element_two", "2017-02-01" ) ), "" );
        List<RuleEffect> ruleEffects = ruleEngine.evaluate( ruleEvent ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).data() ).isEqualTo( "365" );
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

        Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ), "" );

        RuleEngine ruleEngine = RuleEngineContext
            .builder()
            .rules( Arrays.asList( rule ) )
            .ruleVariables( Arrays.asList( ruleVariableOne ) )
            .supplementaryData( supplementaryData )
            .calculatedValueMap( new HashMap<String, Map<String, String>>() )
            .constantsValue( new HashMap<String, String>() )
            .build().toEngineBuilder().triggerEnvironment( TriggerEnvironment.SERVER )
            .build();

        RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "location1", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "OU_GROUP_ID" ) ),
            "" );

        List<RuleEffect> ruleEffects = ruleEngine.evaluate( ruleEvent ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
        assertThat( ruleEffects.get( 0 ).data() ).isEqualTo( "true" );
    }

    @Test
    public void evaluateD2HasUserRole()
        throws Exception
    {
        List<String> roles = Arrays.asList( "role1", "role2" );

        Map<String, List<String>> supplementaryData = new HashMap<>();
        supplementaryData.put( "USER", roles );

        RuleAction ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
            "test_action_content", "d2:hasUserRole(#{test_var_one})" );
        RuleVariable ruleVariableOne = RuleVariableCurrentEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT );

        Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ), "" );

        RuleEngine ruleEngine = RuleEngineContext
            .builder()
            .rules( Arrays.asList( rule ) )
            .ruleVariables( Arrays.asList( ruleVariableOne ) )
            .supplementaryData( supplementaryData )
            .calculatedValueMap( new HashMap<String, Map<String, String>>() )
            .constantsValue( new HashMap<String, String>() )
            .build().toEngineBuilder().triggerEnvironment( TriggerEnvironment.SERVER )
            .build();

        RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "location1", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "role1" ) ), "" );

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
        Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ), "" );

        RuleEngine ruleEngine = getRuleEngine( rule, Arrays.asList( ruleVariableOne, ruleVariableTwo ) );

        RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "2017-01-01" ),
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_two", "2" ) ), "" );
        List<RuleEffect> ruleEffects = ruleEngine.evaluate( ruleEvent ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
        assertThat( ruleEffects.get( 0 ).data() ).isEqualTo( "2017-01-03" );

        RuleEvent ruleEvent2 = RuleEvent.create( "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "2017-01-03" ),
                RuleDataValue
                    .create( new Date(), "test_program_stage", "test_data_element_two", "-2" ) ), "" );
        List<RuleEffect> ruleEffects2 = ruleEngine.evaluate( ruleEvent2 ).call();

        assertThat( ruleEffects2.size() ).isEqualTo( 1 );
        assertThat( ruleEffects2.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
        assertThat( ruleEffects2.get( 0 ).data() ).isEqualTo( "2017-01-01" );
    }

    @Test
    public void evaluateD2CountIfValue()
        throws Exception
    {
        RuleAction ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
            "test_action_content", "d2:countIfValue(#{test_var_one}, 'condition')" );
        RuleVariable ruleVariableOne = RuleVariableNewestEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT );

        Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ), "" );

        RuleEngine.Builder ruleEngineBuilder = getRuleEngineBuilder( rule, Arrays.asList( ruleVariableOne ) );

        RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "condition" ) ), "" );
        RuleEvent ruleEvent2 = RuleEvent.create( "test_event2", "test_program_stage2",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "condition2" ) ), "" );
        RuleEvent ruleEvent3 = RuleEvent.create( "test_event3", "test_program_stage3",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "condition" ) ), "" );

        ruleEngineBuilder.events( Arrays.asList( ruleEvent2, ruleEvent3 ) );

        List<RuleEffect> ruleEffects = ruleEngineBuilder.build().evaluate( ruleEvent ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
        assertTrue( ruleEffects.get( 0 ).data().equals( "2" ) );
    }

    @Test
    public void evaluateD2Count()
        throws Exception
    {
        RuleAction ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
            "test_action_content", "d2:count(#{test_var_one})" );
        RuleVariable ruleVariableOne = RuleVariableNewestEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT );

        RuleVariable ruleVariableTwo = RuleVariableNewestEvent.create(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT );

        Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ), "" );

        RuleEngine.Builder ruleEngineBuilder = getRuleEngineBuilder( rule, Arrays.asList( ruleVariableOne ) );

        RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "condition" ) ), "" );
        RuleEvent ruleEvent2 = RuleEvent.create( "test_event2", "test_program_stage2",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "condition2" ) ), "" );
        RuleEvent ruleEvent3 = RuleEvent.create( "test_event3", "test_program_stage3",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "condition" ) ), "" );

        RuleEvent ruleEvent4 = RuleEvent.create( "test_event3", "test_program_stage3",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_two", "condition" ) ), "" );

        ruleEngineBuilder.events( Arrays.asList( ruleEvent2, ruleEvent3, ruleEvent4 ) );

        List<RuleEffect> ruleEffects = ruleEngineBuilder.build().evaluate( ruleEvent ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
        assertEquals( "3", ruleEffects.get( 0 ).data() );
    }

    @Test
    public void evaluateD2Round()
        throws Exception
    {
        RuleAction ruleAction1 = RuleActionDisplayKeyValuePair.createForFeedback(
            "test_action_content", "d2:round(#{test_var_one})" );
        RuleVariable ruleVariableOne = RuleVariableNewestEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.NUMERIC );

        Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction1 ), "" );

        RuleEngine.Builder ruleEngineBuilder = getRuleEngineBuilder( rule, Arrays.asList( ruleVariableOne ) );

        RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "2.6" ) ), "" );

        List<RuleEffect> ruleEffects = ruleEngineBuilder.build().evaluate( ruleEvent ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction1 );
        assertEquals( "3", ruleEffects.get( 0 ).data() );
    }

    @Test
    public void evaluateD2Modulus()
        throws Exception
    {
        RuleAction ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
            "test_action_content", "d2:modulus(#{test_var_one}, 2)" );
        RuleVariable ruleVariableOne = RuleVariableNewestEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.NUMERIC );

        Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ), "" );

        RuleEngine.Builder ruleEngineBuilder = getRuleEngineBuilder( rule, Arrays.asList( ruleVariableOne ) );

        RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "2.6" ) ), "" );

        List<RuleEffect> ruleEffects = ruleEngineBuilder.build().evaluate( ruleEvent ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
        assertEquals( "0.6", ruleEffects.get( 0 ).data() );
    }

    @Test
    public void evaluateD2SubString()
        throws Exception
    {
        RuleAction ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
            "test_action_content", "d2:substring(#{test_var_one}, 1, 3)" );
        RuleVariable ruleVariableOne = RuleVariableNewestEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT );

        Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ), "" );

        RuleEngine.Builder ruleEngineBuilder = getRuleEngineBuilder( rule, Arrays.asList( ruleVariableOne ) );

        RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "ABCD" ) ), "" );

        List<RuleEffect> ruleEffects = ruleEngineBuilder.build().evaluate( ruleEvent ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
        assertEquals( "BC", ruleEffects.get( 0 ).data() );
    }

    @Test
    public void evaluateD2WeeksBetween()
        throws Exception
    {
        RuleAction ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
            "test_action_content", "d2:weeksBetween(#{test_var_one}, #{test_var_two})" );
        RuleVariable ruleVariableOne = RuleVariableNewestEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT );
        RuleVariable ruleVariableTwo = RuleVariableNewestEvent.create(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT );
        Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ), "" );

        RuleEngine.Builder ruleEngineBuilder = getRuleEngineBuilder( rule,
            Arrays.asList( ruleVariableOne, ruleVariableTwo ) );

        RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "2018-01-01" ),
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_two", "2018-02-01" ) ), "" );

        List<RuleEffect> ruleEffects = ruleEngineBuilder.build().evaluate( ruleEvent ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
        assertEquals( "4", ruleEffects.get( 0 ).data() );
    }

    @Test
    public void evaluateD2MonthsBetween()
        throws Exception
    {
        RuleAction ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
            "test_action_content", "d2:monthsBetween(#{test_var_one}, #{test_var_two})" );
        RuleVariable ruleVariableOne = RuleVariableNewestEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT );
        RuleVariable ruleVariableTwo = RuleVariableNewestEvent.create(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT );
        Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ), "" );

        RuleEngine.Builder ruleEngineBuilder = getRuleEngineBuilder( rule,
            Arrays.asList( ruleVariableOne, ruleVariableTwo ) );

        RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "2018-01-01" ),
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_two", "2018-09-01" ) ), "" );

        List<RuleEffect> ruleEffects = ruleEngineBuilder.build().evaluate( ruleEvent ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
        assertEquals( "8", ruleEffects.get( 0 ).data() );
    }

    @Test
    public void evaluateD2YearsBetween()
        throws Exception
    {
        RuleAction ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
            "test_action_content", "d2:yearsBetween(#{test_var_one}, #{test_var_two})" );
        RuleVariable ruleVariableOne = RuleVariableNewestEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT );
        RuleVariable ruleVariableTwo = RuleVariableNewestEvent.create(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT );
        Rule rule = Rule
            .create( null, null, "d2:yearsBetween('2016-01-01', '2018-09-01') == 2", Arrays.asList( ruleAction ), "" );

        RuleEngine.Builder ruleEngineBuilder = getRuleEngineBuilder( rule,
            Arrays.asList( ruleVariableOne, ruleVariableTwo ) );

        RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "2016-01-01" ),
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_two", "2018-09-01" ) ), "" );

        List<RuleEffect> ruleEffects = ruleEngineBuilder.build().evaluate( ruleEvent ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
        assertEquals( "2", ruleEffects.get( 0 ).data() );
    }

    @Test
    public void evaluateD2Zpvc()
        throws Exception
    {
        RuleAction ruleAction = RuleActionDisplayText.createForFeedback(
            "test_action_content", "d2:zpvc( '1', '0', '-1' )" );
        RuleVariable ruleVariableOne = RuleVariableNewestEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.NUMERIC );
        RuleVariable ruleVariableTwo = RuleVariableNewestEvent.create(
            "test_var_two", "test_data_element_two", RuleValueType.NUMERIC );
        RuleVariable ruleVariableThree = RuleVariableNewestEvent.create(
            "test_var_three", "test_data_element_two", RuleValueType.NUMERIC );
        Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ), "" );

        RuleEngine.Builder ruleEngineBuilder = getRuleEngineBuilder( rule,
            Arrays.asList( ruleVariableOne, ruleVariableTwo ) );

        RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.<RuleDataValue>asList(), "" );

        List<RuleEffect> ruleEffects = ruleEngineBuilder.build().evaluate( ruleEvent ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
        assertEquals( "2", ruleEffects.get( 0 ).data() );
    }

    @Test
    public void evaluateD2Zing()
        throws Exception
    {
        RuleAction ruleAction = RuleActionDisplayText.createForFeedback(
            "test_action_content", "d2:zing( '-1' )" );

        Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ), "" );

        RuleEngine.Builder ruleEngineBuilder = getRuleEngineBuilder( rule, Arrays.<RuleVariable>asList() );

        RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.<RuleDataValue>asList(), "" );

        List<RuleEffect> ruleEffects = ruleEngineBuilder.build().evaluate( ruleEvent ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
        assertEquals( "0", ruleEffects.get( 0 ).data() );
    }

    @Test
    public void evaluateD2Oizp()
        throws Exception
    {
        RuleAction ruleAction = RuleActionDisplayText.createForFeedback(
            "test_action_content", "d2:oizp( '0' )" );

        Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ), "" );

        RuleEngine.Builder ruleEngineBuilder = getRuleEngineBuilder( rule, Arrays.<RuleVariable>asList() );

        RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.<RuleDataValue>asList(), "" );

        List<RuleEffect> ruleEffects = ruleEngineBuilder.build().evaluate( ruleEvent ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
        assertEquals( "1", ruleEffects.get( 0 ).data() );
    }

    @Test
    public void evaluateD2CountIfZeroPos()
        throws Exception
    {
        RuleAction ruleAction = RuleActionDisplayText.createForFeedback(
            "test_action_content", "d2:countIfZeroPos(#{test_var_one})" );
        RuleVariable ruleVariableOne = RuleVariableNewestEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.NUMERIC );

        Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ), "" );

        RuleEngine.Builder ruleEngineBuilder = getRuleEngineBuilder( rule, Arrays.asList( ruleVariableOne ) );

        RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "0" ) ), "" );

        RuleEvent ruleEvent1 = RuleEvent.create( "test_event1", "test_program_stage",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "1" ) ), "" );

        RuleEvent ruleEvent2 = RuleEvent.create( "test_event1", "test_program_stage",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "-3" ) ), "" );

        List<RuleEffect> ruleEffects = ruleEngineBuilder.events( Arrays.asList( ruleEvent1, ruleEvent2 ) ).build()
            .evaluate( ruleEvent ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
        assertEquals( "2", ruleEffects.get( 0 ).data() );
    }

    @Test
    public void evaluateD2Left()
        throws Exception
    {
        RuleAction ruleAction = RuleActionDisplayText.createForFeedback(
            "test_action_content", "d2:left(#{test_var_one}, 4)" );
        RuleVariable ruleVariableOne = RuleVariableNewestEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT );

        Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ), "" );

        RuleEngine.Builder ruleEngineBuilder = getRuleEngineBuilder( rule, Arrays.asList( ruleVariableOne ) );

        RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "yyyy-mm-dd" ) ), "" );

        List<RuleEffect> ruleEffects = ruleEngineBuilder.build().evaluate( ruleEvent ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
        assertEquals( "yyyy", ruleEffects.get( 0 ).data() );
    }

    @Test
    public void evaluateD2Right()
        throws Exception
    {
        RuleAction ruleAction = RuleActionDisplayText.createForFeedback(
            "test_action_content", "d2:right(#{test_var_one}, 2)" );
        RuleVariable ruleVariableOne = RuleVariableNewestEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT );

        Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ), "" );

        RuleEngine.Builder ruleEngineBuilder = getRuleEngineBuilder( rule, Arrays.asList( ruleVariableOne ) );

        RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "yyyy-mm-dd" ) ), "" );

        List<RuleEffect> ruleEffects = ruleEngineBuilder.build().evaluate( ruleEvent ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
        assertEquals( "dd", ruleEffects.get( 0 ).data() );
    }

    @Test
    public void evaluateD2Concatenate()
        throws Exception
    {
        RuleAction ruleAction = RuleActionDisplayText.createForFeedback(
            "test_action_content", "d2:concatenate(#{test_var_one}, '+days')" );
        RuleVariable ruleVariableOne = RuleVariableNewestEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT );

        Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ), "" );

        RuleEngine.Builder ruleEngineBuilder = getRuleEngineBuilder( rule, Arrays.asList( ruleVariableOne ) );

        RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "weeks" ) ), "" );

        List<RuleEffect> ruleEffects = ruleEngineBuilder.build().evaluate( ruleEvent ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
        assertEquals( "weeks+days", ruleEffects.get( 0 ).data() );
    }

    @Test
    public void evaluateD2ValidatePattern()
        throws Exception
    {
        RuleAction ruleAction = RuleActionDisplayText.createForFeedback(
            "test_action_content", "d2:validatePattern(#{test_var_one}, '.*555.*')" );
        RuleVariable ruleVariableOne = RuleVariableNewestEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT );

        Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ), "" );

        RuleEngine.Builder ruleEngineBuilder = getRuleEngineBuilder( rule, Arrays.asList( ruleVariableOne ) );

        RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "44455545454" ) ),
            "" );

        List<RuleEffect> ruleEffects = ruleEngineBuilder.build().evaluate( ruleEvent ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
        assertEquals( "true", ruleEffects.get( 0 ).data() );

        RuleEvent ruleEvent2 = RuleEvent.create( "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "444887777" ) ), "" );

        List<RuleEffect> ruleEffects2 = ruleEngineBuilder.build().evaluate( ruleEvent2 ).call();

        assertThat( ruleEffects2.size() ).isEqualTo( 1 );
        assertThat( ruleEffects2.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
        assertEquals( "false", ruleEffects2.get( 0 ).data() );
    }

    @Test
    public void evaluateD2Length()
        throws Exception
    {
        RuleAction ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
            "test_action_content", "d2:length(#{test_var_one})" );
        RuleVariable ruleVariableOne = RuleVariableNewestEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT );

        Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ), "" );

        RuleEngine.Builder ruleEngineBuilder = getRuleEngineBuilder( rule, Arrays.asList( ruleVariableOne ) );

        RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "testString" ) ), "" );

        List<RuleEffect> ruleEffects = ruleEngineBuilder.build().evaluate( ruleEvent ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
        assertEquals( "10", ruleEffects.get( 0 ).data() );
    }

    @Test
    public void evaluateD2Split()
        throws Exception
    {
        RuleAction ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
            "test_action_content", "d2:split(#{test_var_one},'-',2)" );
        RuleVariable ruleVariableOne = RuleVariableNewestEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT );

        Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ), "" );

        RuleEngine.Builder ruleEngineBuilder = getRuleEngineBuilder( rule, Arrays.asList( ruleVariableOne ) );

        RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue
                    .create( new Date(), "test_program_stage", "test_data_element_one", "test-String-for-split" ) ),
            "" );

        List<RuleEffect> ruleEffects = ruleEngineBuilder.build().evaluate( ruleEvent ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
        assertEquals( "for", ruleEffects.get( 0 ).data() );
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

        Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ), "" );

        RuleEngine ruleEngine = getRuleEngine( rule,
            Arrays.asList( ruleVariableOne, ruleVariableTwo, ruleVariableThree ) );

        RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "19.9" ),
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_two", "0.9" ),
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_three", "10.6" ) ), "" );
        List<RuleEffect> ruleEffects = ruleEngine.evaluate( ruleEvent ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).data() ).isEqualTo( "6.0" );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
    }

    @Test
    public void evaluateD2ZScoreWFA()
        throws Exception
    {
        RuleAction ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
            "test_action_content", "true" );
        RuleVariable ruleVariableOne = RuleVariableNewestEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT );

        RuleVariable ruleVariableTwo = RuleVariableNewestEvent.create(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT );

        Rule rule = Rule
            .create( null, null, "d2:zScoreWFA(1,#{test_var_one},#{test_var_two}) == 0", Arrays.asList( ruleAction ),
                "" );

        RuleEngine.Builder ruleEngineBuilder = getRuleEngineBuilder( rule,
            Arrays.asList( ruleVariableOne, ruleVariableTwo ) );

        RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "4.5" ),
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_two", "male" ) ), "" );

        List<RuleEffect> ruleEffects = ruleEngineBuilder.build().evaluate( ruleEvent ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
    }

    @Test
    public void evaluateD2ZScoreHFAGirl()
        throws Exception
    {
        RuleAction ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
            "test_action_content", "true" );
        RuleVariable ruleVariableOne = RuleVariableNewestEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT );

        RuleVariable ruleVariableTwo = RuleVariableNewestEvent.create(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT );

        Rule rule = Rule
            .create( null, null, "d2:zScoreHFA(12,#{test_var_one},#{test_var_two}) == -3", Arrays.asList( ruleAction ),
                "" );

        RuleEngine.Builder ruleEngineBuilder = getRuleEngineBuilder( rule,
            Arrays.asList( ruleVariableOne, ruleVariableTwo ) );

        RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "66.3" ),
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_two", "1" ) ), "" );

        List<RuleEffect> ruleEffects = ruleEngineBuilder.build().evaluate( ruleEvent ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
    }

    @Test
    public void evaluateD2ZScoreHFABoy()
        throws Exception
    {
        RuleAction ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
            "test_action_content", "true" );
        RuleVariable ruleVariableOne = RuleVariableNewestEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT );

        RuleVariable ruleVariableTwo = RuleVariableNewestEvent.create(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT );

        Rule rule = Rule
            .create( null, null, "d2:zScoreHFA(10,#{test_var_one},#{test_var_two}) == -2", Arrays.asList( ruleAction ),
                "" );

        RuleEngine.Builder ruleEngineBuilder = getRuleEngineBuilder( rule,
            Arrays.asList( ruleVariableOne, ruleVariableTwo ) );

        RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "68.7" ),
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_two", "male" ) ), "" );

        List<RuleEffect> ruleEffects = ruleEngineBuilder.build().evaluate( ruleEvent ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
    }

    @Test
    public void evaluateD2ZScoreWFHBoy()
        throws Exception
    {
        RuleAction ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
            "test_action_content", "true" );
        RuleVariable ruleVariableOne = RuleVariableNewestEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT );

        RuleVariable ruleVariableTwo = RuleVariableNewestEvent.create(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT );

        Rule rule = Rule
            .create( null, null, "d2:zScoreWFH(55,#{test_var_one},#{test_var_two}) == -2", Arrays.asList( ruleAction ),
                "" );

        RuleEngine.Builder ruleEngineBuilder = getRuleEngineBuilder( rule,
            Arrays.asList( ruleVariableOne, ruleVariableTwo ) );

        RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "3.8" ),
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_two", "male" ) ), "" );

        List<RuleEffect> ruleEffects = ruleEngineBuilder.build().evaluate( ruleEvent ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
    }

    @Test
    public void evaluateD2ZScoreWFHGirl()
        throws Exception
    {
        RuleAction ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
            "test_action_content", "d2:zScoreWFH(81.5,9.6,'female') == 2" );
        RuleVariable ruleVariableOne = RuleVariableNewestEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT );

        RuleVariable ruleVariableTwo = RuleVariableNewestEvent.create(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT );

        Rule rule = Rule
            .create( null, null, "d2:zScoreWFH(81.5,#{test_var_one},#{test_var_two}) == 2", Arrays.asList( ruleAction ),
                "" );

        RuleEngine.Builder ruleEngineBuilder = getRuleEngineBuilder( rule,
            Arrays.asList( ruleVariableOne, ruleVariableTwo ) );

        RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "12.5" ),
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_two", "1" ) ), "" );

        List<RuleEffect> ruleEffects = ruleEngineBuilder.build().evaluate( ruleEvent ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
    }

    @Test
    public void evaluateD2MaxValue()
        throws Exception
    {
        RuleAction ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
            "test_action_content", "true" );
        RuleVariable ruleVariableOne = RuleVariableNewestEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.NUMERIC );

        RuleVariable ruleVariableTwo = RuleVariableNewestEvent.create(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT );

        Rule rule = Rule.create( null, null, "d2:maxValue(#{test_var_one}) == 8.0", Arrays.asList( ruleAction ), "" );

        RuleEngine.Builder ruleEngineBuilder = getRuleEngineBuilder( rule,
            Arrays.asList( ruleVariableOne, ruleVariableTwo ) );

        RuleEvent ruleEvent1 = RuleEvent.create( "test_event1", "test_program_stage1",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "5" ),
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_two", "male" ) ), "" );

        RuleEvent ruleEvent2 = RuleEvent.create( "test_event2", "test_program_stage2",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "7" ),
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_two", "male" ) ), "" );

        RuleEvent ruleEvent3 = RuleEvent.create( "test_event3", "test_program_stage3",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "8" ),
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_two", "male" ) ), "" );

        List<RuleEffect> ruleEffects = ruleEngineBuilder.events( Arrays.asList( ruleEvent1, ruleEvent2 ) ).build()
            .evaluate( ruleEvent3 ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
    }

    @Test
    public void testMinValue()
        throws Exception
    {
        RuleAction ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
            "test_action_content", "d2:minValue(#{test_var_one})" );
        RuleVariable ruleVariableOne = RuleVariableNewestEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.NUMERIC );

        RuleVariable ruleVariableTwo = RuleVariableNewestEvent.create(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT );

        Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ), "" );

        RuleEngine.Builder ruleEngineBuilder = getRuleEngineBuilder( rule,
            Arrays.asList( ruleVariableOne, ruleVariableTwo ) );

        RuleEvent ruleEvent1 = RuleEvent.create( "test_event1", "test_program_stage1",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "5" ),
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_two", "male" ) ), "" );

        RuleEvent ruleEvent2 = RuleEvent.create( "test_event2", "test_program_stage2",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "7" ),
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_two", "male" ) ), "" );

        RuleEvent ruleEvent3 = RuleEvent.create( "test_event3", "test_program_stage3",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "8" ),
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_two", "male" ) ), "" );

        List<RuleEffect> ruleEffects = ruleEngineBuilder.events( Arrays.asList( ruleEvent1, ruleEvent2 ) ).build()
            .evaluate( ruleEvent3 ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
        assertThat( ruleEffects.get( 0 ).data() ).isEqualTo( "5.0" );
    }

    @Test
    public void evaluateLastEventDate()
        throws Exception
    {
        java.util.Calendar cal = java.util.Calendar.getInstance();

        Date today = cal.getTime();
        cal.add( java.util.Calendar.DATE, -1 );
        Date yesterday = cal.getTime();
        cal.add( java.util.Calendar.DATE, -1 );
        Date dayBeforeYesterday = cal.getTime();
        cal.add( java.util.Calendar.DATE, 4 );
        Date dayAfterTomorrow = cal.getTime();

        RuleAction ruleAction = RuleActionDisplayText.createForFeedback(
            "test_action_content", "d2:lastEventDate('test_var_one')" );
        RuleVariable ruleVariableOne = RuleVariableNewestEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT );

        RuleVariable ruleVariableTwo = RuleVariableNewestEvent.create(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT );

        Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ), "test_rule" );

        RuleEngine.Builder ruleEngineBuilder = getRuleEngineBuilder( rule, Arrays.asList( ruleVariableOne ) );

        RuleEvent ruleEvent1 = RuleEvent.create( "test_event1", "test_program_stage1",
            RuleEvent.Status.ACTIVE, dayBeforeYesterday, new Date(), "", null, Arrays.asList(
                RuleDataValue.create( dayBeforeYesterday, "test_program_stage1", "test_data_element_one", "value1" ) ),
            "" );

        RuleEvent ruleEvent2 = RuleEvent.create( "test_event2", "test_program_stage2",
            RuleEvent.Status.ACTIVE, yesterday, new Date(), "", null, Arrays.asList(
                RuleDataValue.create( yesterday, "test_program_stage2", "test_data_element_one", "value2" ) ), "" );

        RuleEvent ruleEvent3 = RuleEvent.create( "test_event3", "test_program_stage3",
            RuleEvent.Status.ACTIVE, dayAfterTomorrow, dayAfterTomorrow, "", null, Arrays.asList(
                RuleDataValue.create( dayAfterTomorrow, "test_program_stage3", "test_data_element_one", "value3" ) ),
            "" );

        List<RuleEffect> ruleEffects = ruleEngineBuilder.events( Arrays.asList( ruleEvent1, ruleEvent2 ) ).build()
            .evaluate( ruleEvent3 ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
        assertEquals( dateFormat.format( yesterday ), ruleEffects.get( 0 ).data() );
    }

    private RuleEngine getRuleEngine( Rule rule, List<RuleVariable> ruleVariables )
    {
        return getRuleEngineBuilder( rule, ruleVariables )
            .build();
    }

    private RuleEngine.Builder getRuleEngineBuilder( Rule rule, List<RuleVariable> ruleVariables )
    {
        return RuleEngineContext
            .builder()
            .rules( Arrays.asList( rule ) )
            .ruleVariables( ruleVariables )
            .calculatedValueMap( new HashMap<String, Map<String, String>>() )
            .supplementaryData( new HashMap<String, List<String>>() )
            .constantsValue( new HashMap<String, String>() )
            .build().toEngineBuilder().triggerEnvironment( TriggerEnvironment.SERVER );
    }
}
