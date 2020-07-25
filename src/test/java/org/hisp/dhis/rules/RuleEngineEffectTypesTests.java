package org.hisp.dhis.rules;

import org.hisp.dhis.rules.models.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

// ToDo: function tests (check that function invocations are producing expected values; check nested function invocation)
// ToDo: various source type tests (referencing variables from different events)
@RunWith( JUnit4.class )
public class RuleEngineEffectTypesTests
{

    private RuleEvent getTestRuleEvent( RuleEvent.Status status )
    {
        return RuleEvent.builder()
            .event( "test_event" )
            .programStage( "test_program_stage" )
            .programStageName( "" )
            .status( status )
            .eventDate( new Date() )
            .dueDate( new Date() )
            .organisationUnit( "" )
            .organisationUnitCode( "" )
            .dataValues( Arrays.asList( RuleDataValue.create(
                new Date(), "test_program_stage", "test_data_element", "test_value" ) ) )
            .build();
    }

    @Test
    public void simpleConditionMustResultInAssignEffect()
        throws Exception
    {
        RuleAction ruleAction = RuleActionAssign.create(
            null, "'test_string'", "#{test_data_element}" );
        Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ), "" );

        RuleEngine ruleEngine = getRuleEngine( rule );

        List<RuleEffect> ruleEffects = ruleEngine.evaluate( getTestRuleEvent( RuleEvent.Status.ACTIVE ) ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).data() ).isEqualTo( "test_string" );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
    }

    @Test
    public void simpleConditionMustResultInCreateEventEffect()
        throws Exception
    {
        RuleAction ruleAction = RuleActionCreateEvent.create(
            "test_action_content", "'event_uid;test_data_value_one'", "test_program_stage" );
        Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ), "" );

        RuleEngine ruleEngine = getRuleEngine( rule );

        List<RuleEffect> ruleEffects = ruleEngine.evaluate( getTestRuleEvent( RuleEvent.Status.ACTIVE ) ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).data() ).isEqualTo( "event_uid;test_data_value_one" );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
    }

    @Test
    public void simpleConditionMustResultInDisplayKeyValuePairEffect()
        throws Exception
    {
        RuleAction ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
            "test_action_content", "2 + 2" );
        Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ), "" );

        RuleEngine ruleEngine = getRuleEngine( rule );

        List<RuleEffect> ruleEffects = ruleEngine.evaluate( getTestRuleEvent( RuleEvent.Status.ACTIVE ) ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).data() ).isEqualTo( "4.0" );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
    }

    @Test
    public void simpleConditionMustResultInDisplayTextEffect()
        throws Exception
    {
        RuleAction ruleAction = RuleActionDisplayText.createForFeedback(
            "test_action_content", "2 + 2" );
        Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ), "" );

        RuleEngine ruleEngine = getRuleEngine( rule );

        List<RuleEffect> ruleEffects = ruleEngine.evaluate( getTestRuleEvent( RuleEvent.Status.ACTIVE ) ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).data() ).isEqualTo( "4.0" );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
    }

    @Test
    public void simpleConditionMustResultInErrorOnCompletionEffect()
        throws Exception
    {
        RuleAction ruleAction = RuleActionErrorOnCompletion.create(
            "test_action_content", "2 + 2", "test_data_element" );
        Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ), "" );

        RuleEngine ruleEngine = getRuleEngine( rule );

        List<RuleEffect> ruleEffects = ruleEngine.evaluate( getTestRuleEvent( RuleEvent.Status.ACTIVE ) ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).data() ).isEqualTo( "4.0" );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
    }

    @Test
    public void simpleConditionMustResultInHideFieldEffect()
        throws Exception
    {
        RuleAction ruleAction = RuleActionHideField.create(
            "test_action_content", "test_data_element" );
        Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ), "" );

        RuleEngine ruleEngine = RuleEngineContext
            .builder()
            .rules( Arrays.asList( rule ) )
            .supplementaryData( new HashMap<String, List<String>>() )
            .constantsValue( new HashMap<String, String>() )
            .build().toEngineBuilder().triggerEnvironment( TriggerEnvironment.SERVER )
            .build();

        List<RuleEffect> ruleEffects = ruleEngine.evaluate( getTestRuleEvent( RuleEvent.Status.ACTIVE ) ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).data() ).isEqualTo( "" );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
    }

    @Test
    public void testEnvironmentVariableExpression()
        throws Exception
    {
        RuleAction ruleAction = RuleActionHideField.create(
            "test_action_content", "test_data_element" );
        Rule rule = Rule.create( null, null, "V{event_status} =='COMPLETED'", Arrays.asList( ruleAction ), "" );

        RuleEngine ruleEngine = getRuleEngine( rule );

        List<RuleEffect> ruleEffects = ruleEngine.evaluate( getTestRuleEvent( RuleEvent.Status.COMPLETED ) ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).data() ).isEqualTo( "" );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
    }

    @Test
    public void testTriggerEnvironment()
        throws Exception
    {
        RuleAction ruleAction = RuleActionHideField.create(
            "test_action_content", "test_data_element" );
        Rule rule = Rule.create( null, null, "V{environment} =='Server'", Arrays.asList( ruleAction ), "" );

        RuleEngine ruleEngine = getRuleEngine( rule );

        List<RuleEffect> ruleEffects = ruleEngine.evaluate( getTestRuleEvent( RuleEvent.Status.ACTIVE ) ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).data() ).isEqualTo( "" );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
    }

    @Test
    public void simpleConditionMustResultInHideProgramStageEffect()
        throws Exception
    {
        RuleAction ruleAction = RuleActionHideProgramStage.create( "test_program_stage" );
        Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ), "" );

        RuleEngine ruleEngine = getRuleEngine( rule );

        List<RuleEffect> ruleEffects = ruleEngine.evaluate( getTestRuleEvent( RuleEvent.Status.ACTIVE ) ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).data() ).isEqualTo( "" );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
    }

    @Test
    public void simpleConditionMustResultInScheduleMessage()
        throws Exception
    {
        RuleAction ruleAction = RuleActionScheduleMessage.create( "", "'2018-04-24'" );
        Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ), "" );

        RuleEngine ruleEngine = getRuleEngine( rule );

        List<RuleEffect> ruleEffects = ruleEngine.evaluate( getTestRuleEvent( RuleEvent.Status.ACTIVE ) ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).ruleAction() instanceof RuleActionScheduleMessage );
        assertThat( ruleEffects.get( 0 ).data() ).isEqualTo( "2018-04-24" );
    }

    @Test
    public void simpleConditionMustResultInHideSectionEffect()
        throws Exception
    {
        RuleAction ruleAction = RuleActionHideSection.create( "test_section" );
        Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ), "" );

        RuleEngine ruleEngine = getRuleEngine( rule );

        List<RuleEffect> ruleEffects = ruleEngine.evaluate( getTestRuleEvent( RuleEvent.Status.ACTIVE ) ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).data() ).isEqualTo( "" );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
    }

    @Test
    public void simpleConditionMustResultInHideOptionEffect()
        throws Exception
    {
        RuleAction ruleAction = RuleActionHideOption.create( "test_content", "test_option", "test_field" );
        Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ), "" );

        RuleEngine ruleEngine = getRuleEngine( rule );

        List<RuleEffect> ruleEffects = ruleEngine.evaluate( getTestRuleEvent( RuleEvent.Status.ACTIVE ) ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).data() ).isEqualTo( "" );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
    }

    @Test
    public void simpleConditionMustResultInHideOptionGroupEffect()
        throws Exception
    {
        RuleAction ruleAction = RuleActionHideOptionGroup.create( "test_content", "test_option_group", "field" );
        Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ), "" );

        RuleEngine ruleEngine = getRuleEngine( rule );

        List<RuleEffect> ruleEffects = ruleEngine.evaluate( getTestRuleEvent( RuleEvent.Status.ACTIVE ) ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).data() ).isEqualTo( "" );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
    }

    @Test
    public void simpleConditionMustResultInSetMandatoryFieldEffect()
        throws Exception
    {
        RuleAction ruleAction = RuleActionSetMandatoryField.create( "test_data_element" );
        Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ), "" );

        RuleEngine ruleEngine = getRuleEngine( rule );

        List<RuleEffect> ruleEffects = ruleEngine.evaluate( getTestRuleEvent( RuleEvent.Status.ACTIVE ) ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).data() ).isEqualTo( "" );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
    }

    @Test
    public void simpleConditionMustResultInWarningEffect()
        throws Exception
    {
        RuleAction ruleAction = RuleActionShowWarning.create(
            "test_warning_message", null, "target_field" );
        Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ), "" );

        RuleEngine ruleEngine = getRuleEngine( rule );

        List<RuleEffect> ruleEffects = ruleEngine.evaluate( getTestRuleEvent( RuleEvent.Status.ACTIVE ) ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).data() ).isEqualTo( "" );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
    }

    @Test
    public void simpleConditionMustResultInErrorEffect()
        throws Exception
    {
        RuleAction ruleAction = RuleActionShowError.create(
            "test_error_message", "2 + 2", "target_field" );
        Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ), "" );

        RuleEngine ruleEngine = getRuleEngine( rule );

        List<RuleEffect> ruleEffects = ruleEngine.evaluate( getTestRuleEvent( RuleEvent.Status.ACTIVE ) ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).data() ).isEqualTo( "4.0" );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
    }

    @Test
    public void simpleConditionMustResultInOnCompletionWarningEffect()
        throws Exception
    {
        RuleAction ruleAction = RuleActionWarningOnCompletion.create(
            "test_warning_message", "2 + 2", "target_field" );
        Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ), "" );

        RuleEngine ruleEngine = getRuleEngine( rule );

        List<RuleEffect> ruleEffects = ruleEngine.evaluate( getTestRuleEvent( RuleEvent.Status.ACTIVE ) ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).data() ).isEqualTo( "4.0" );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
    }

    private RuleEngine getRuleEngine( Rule rule )
    {
        return RuleEngineContext
            .builder()
            .rules( Arrays.asList( rule ) )
            .supplementaryData( new HashMap<String, List<String>>() )
            .constantsValue( new HashMap<String, String>() )
            .build().toEngineBuilder().triggerEnvironment( TriggerEnvironment.SERVER )
            .build();
    }
}
