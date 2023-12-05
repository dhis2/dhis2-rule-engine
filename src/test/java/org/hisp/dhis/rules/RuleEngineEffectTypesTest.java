package org.hisp.dhis.rules;

import org.hisp.dhis.rules.models.Rule;
import org.hisp.dhis.rules.models.RuleAction;
import org.hisp.dhis.rules.models.RuleActionAssign;
import org.hisp.dhis.rules.models.RuleActionCreateEvent;
import org.hisp.dhis.rules.models.RuleActionHideField;
import org.hisp.dhis.rules.models.RuleActionHideOption;
import org.hisp.dhis.rules.models.RuleActionHideOptionGroup;
import org.hisp.dhis.rules.models.RuleActionHideProgramStage;
import org.hisp.dhis.rules.models.RuleActionHideSection;
import org.hisp.dhis.rules.models.RuleActionMessage;
import org.hisp.dhis.rules.models.RuleActionScheduleMessage;
import org.hisp.dhis.rules.models.RuleActionSetMandatoryField;
import org.hisp.dhis.rules.models.RuleActionText;
import org.hisp.dhis.rules.models.RuleDataValue;
import org.hisp.dhis.rules.models.RuleEffect;
import org.hisp.dhis.rules.models.RuleEffects;
import org.hisp.dhis.rules.models.RuleEvent;
import org.hisp.dhis.rules.models.TriggerEnvironment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

// ToDo: function tests (check that function invocations are producing expected values; check nested function invocation)
// ToDo: various source type tests (referencing variables from different events)
@RunWith( JUnit4.class )
public class RuleEngineEffectTypesTest
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
            .dataValues(List.of(RuleDataValue.create(
                    new Date(), "test_program_stage", "test_data_element", "test_value")))
            .build();
    }

    @Test
    public void simpleConditionMustResultInAssignEffect()
        throws Exception
    {
        RuleAction ruleAction = RuleActionAssign.create(
            null, "'test_string'", "#{test_data_element}" );
        Rule rule = Rule.create( null, null, "true", List.of(ruleAction), "", "" );

        RuleEngine ruleEngine = getRuleEngine( rule );

        List<RuleEffect> ruleEffects = ruleEngine.evaluate( getTestRuleEvent( RuleEvent.Status.ACTIVE ) ).call();

        assertEquals( 1 , ruleEffects.size() );
        assertEquals( "test_string" , ruleEffects.get( 0 ).data() );
        assertEquals( ruleAction , ruleEffects.get( 0 ).ruleAction() );
    }

    @Test
    public void simpleConditionMustResultInAssignEffectMultipleExecution()
        throws Exception
    {
        RuleAction ruleAction = RuleActionAssign.create(
            null, "'test_string'", "#{test_data_element}" );
        Rule rule = Rule.create( null, null, "true", List.of(ruleAction), "", "" );

        RuleEngine ruleEngine = getRuleEngineMultiple( rule, getTestRuleEvent( RuleEvent.Status.ACTIVE ) );

        List<RuleEffects> ruleEffects = ruleEngine.evaluate();

        assertEquals( 1 , ruleEffects.size() );
        assertEquals( "test_string" , ruleEffects.get( 0 ).ruleEffects().get( 0 ).data() );
        assertEquals( ruleAction , ruleEffects.get( 0 ).ruleEffects().get( 0 ).ruleAction() );
    }

    @Test
    public void simpleConditionMustResultInCreateEventEffect()
        throws Exception
    {
        RuleAction ruleAction = RuleActionCreateEvent.create(
            "test_action_content", "'event_uid;test_data_value_one'", "test_program_stage" );
        Rule rule = Rule.create( null, null, "true", List.of(ruleAction), "", "" );

        RuleEngine ruleEngine = getRuleEngine( rule );

        List<RuleEffect> ruleEffects = ruleEngine.evaluate( getTestRuleEvent( RuleEvent.Status.ACTIVE ) ).call();

        assertEquals( 1 , ruleEffects.size() );
        assertEquals( "event_uid;test_data_value_one" , ruleEffects.get( 0 ).data() );
        assertEquals( ruleAction , ruleEffects.get( 0 ).ruleAction() );
    }

    @Test
    public void simpleConditionMustResultInDisplayKeyValuePairEffect()
        throws Exception
    {
        RuleAction ruleAction = RuleActionText.createForFeedback(
            "test_action_content", "2 + 2" );
        Rule rule = Rule.create( null, null, "true", List.of(ruleAction), "", "" );

        RuleEngine ruleEngine = getRuleEngine( rule );

        List<RuleEffect> ruleEffects = ruleEngine.evaluate( getTestRuleEvent( RuleEvent.Status.ACTIVE ) ).call();

        assertEquals( 1 , ruleEffects.size() );
        assertEquals( "4" , ruleEffects.get( 0 ).data() );
        assertEquals( ruleAction , ruleEffects.get( 0 ).ruleAction() );
    }

    @Test
    public void simpleConditionMustResultInDisplayTextEffect()
        throws Exception
    {
        RuleAction ruleAction = RuleActionText.createForFeedback(
            "test_action_content", "2 + 2" );
        Rule rule = Rule.create( null, null, "true", List.of(ruleAction), "", "" );

        RuleEngine ruleEngine = getRuleEngine( rule );

        List<RuleEffect> ruleEffects = ruleEngine.evaluate( getTestRuleEvent( RuleEvent.Status.ACTIVE ) ).call();

        assertEquals( 1 , ruleEffects.size() );
        assertEquals( "4" , ruleEffects.get( 0 ).data() );
        assertEquals( ruleAction , ruleEffects.get( 0 ).ruleAction() );
    }

    @Test
    public void simpleConditionMustResultInErrorOnCompletionEffect()
        throws Exception
    {
        RuleAction ruleAction = RuleActionMessage.create(
            "test_action_content", "2 + 2", "test_data_element", RuleActionMessage.Type.ERROR_ON_COMPILATION );
        Rule rule = Rule.create( null, null, "true", List.of(ruleAction), "", "" );

        RuleEngine ruleEngine = getRuleEngine( rule );

        List<RuleEffect> ruleEffects = ruleEngine.evaluate( getTestRuleEvent( RuleEvent.Status.ACTIVE ) ).call();

        assertEquals( 1 , ruleEffects.size() );
        assertEquals( "4" , ruleEffects.get( 0 ).data() );
        assertEquals( ruleAction , ruleEffects.get( 0 ).ruleAction() );
    }

    @Test
    public void simpleConditionMustResultInHideFieldEffect()
        throws Exception
    {
        RuleAction ruleAction = RuleActionHideField.create(
            "test_action_content", "test_data_element" );
        Rule rule = Rule.create( null, null, "true", List.of(ruleAction), "", "" );

        RuleEngine ruleEngine = RuleEngineContext
            .builder()
            .rules(List.of(rule))
            .supplementaryData( new HashMap<String, List<String>>() )
            .constantsValue( new HashMap<String, String>() )
            .build().toEngineBuilder().triggerEnvironment( TriggerEnvironment.SERVER )
            .build();

        List<RuleEffect> ruleEffects = ruleEngine.evaluate( getTestRuleEvent( RuleEvent.Status.ACTIVE ) ).call();

        assertEquals( 1 , ruleEffects.size() );
        assertEquals( "" , ruleEffects.get( 0 ).data() );
        assertEquals( ruleAction , ruleEffects.get( 0 ).ruleAction() );
    }

    @Test
    public void testEnvironmentVariableExpression()
        throws Exception
    {
        RuleAction ruleAction = RuleActionHideField.create(
            "test_action_content", "test_data_element" );
        Rule rule = Rule.create( null, null, "V{event_status} =='COMPLETED'", List.of(ruleAction), "", "" );

        RuleEngine ruleEngine = getRuleEngine( rule );

        List<RuleEffect> ruleEffects = ruleEngine.evaluate( getTestRuleEvent( RuleEvent.Status.COMPLETED ) ).call();

        assertEquals( 1 , ruleEffects.size() );
        assertEquals( "" , ruleEffects.get( 0 ).data() );
        assertEquals( ruleAction , ruleEffects.get( 0 ).ruleAction() );
    }

    @Test
    public void testTriggerEnvironment()
        throws Exception
    {
        RuleAction ruleAction = RuleActionHideField.create(
            "test_action_content", "test_data_element" );
        Rule rule = Rule.create( null, null, "V{environment} =='Server'", List.of(ruleAction), "", "" );

        RuleEngine ruleEngine = getRuleEngine( rule );

        List<RuleEffect> ruleEffects = ruleEngine.evaluate( getTestRuleEvent( RuleEvent.Status.ACTIVE ) ).call();

        assertEquals( 1 , ruleEffects.size() );
        assertEquals( "" , ruleEffects.get( 0 ).data() );
        assertEquals( ruleAction , ruleEffects.get( 0 ).ruleAction() );
    }

    @Test
    public void simpleConditionMustResultInHideProgramStageEffect()
        throws Exception
    {
        RuleAction ruleAction = RuleActionHideProgramStage.create( "test_program_stage" );
        Rule rule = Rule.create( null, null, "true", List.of(ruleAction), "", "" );

        RuleEngine ruleEngine = getRuleEngine( rule );

        List<RuleEffect> ruleEffects = ruleEngine.evaluate( getTestRuleEvent( RuleEvent.Status.ACTIVE ) ).call();

        assertEquals( 1 , ruleEffects.size() );
        assertEquals( "" , ruleEffects.get( 0 ).data() );
        assertEquals( ruleAction , ruleEffects.get( 0 ).ruleAction() );
    }

    @Test
    public void simpleConditionMustResultInScheduleMessage()
        throws Exception
    {
        RuleAction ruleAction = RuleActionScheduleMessage.create( "", "'2018-04-24'" );
        Rule rule = Rule.create( null, null, "true", List.of(ruleAction), "", "" );

        RuleEngine ruleEngine = getRuleEngine( rule );

        List<RuleEffect> ruleEffects = ruleEngine.evaluate( getTestRuleEvent( RuleEvent.Status.ACTIVE ) ).call();

        assertEquals( 1 , ruleEffects.size() );
        assertTrue( ruleEffects.get( 0 ).ruleAction() instanceof RuleActionScheduleMessage );
        assertEquals( "2018-04-24" , ruleEffects.get( 0 ).data() );
    }

    @Test
    public void simpleConditionMustResultInHideSectionEffect()
        throws Exception
    {
        RuleAction ruleAction = RuleActionHideSection.create( "test_section" );
        Rule rule = Rule.create( null, null, "true", List.of(ruleAction), "", "" );

        RuleEngine ruleEngine = getRuleEngine( rule );

        List<RuleEffect> ruleEffects = ruleEngine.evaluate( getTestRuleEvent( RuleEvent.Status.ACTIVE ) ).call();

        assertEquals( 1 , ruleEffects.size() );
        assertEquals( "" , ruleEffects.get( 0 ).data() );
        assertEquals( ruleAction , ruleEffects.get( 0 ).ruleAction() );
    }

    @Test
    public void simpleConditionMustResultInHideOptionEffect()
        throws Exception
    {
        RuleAction ruleAction = RuleActionHideOption.create( "test_content", "test_option", "test_field" );
        Rule rule = Rule.create( null, null, "true", List.of(ruleAction), "", "" );

        RuleEngine ruleEngine = getRuleEngine( rule );

        List<RuleEffect> ruleEffects = ruleEngine.evaluate( getTestRuleEvent( RuleEvent.Status.ACTIVE ) ).call();

        assertEquals( 1 , ruleEffects.size() );
        assertEquals( "" , ruleEffects.get( 0 ).data() );
        assertEquals( ruleAction , ruleEffects.get( 0 ).ruleAction() );
    }

    @Test
    public void simpleConditionMustResultInHideOptionGroupEffect()
        throws Exception
    {
        RuleAction ruleAction = RuleActionHideOptionGroup.create( "test_content", "test_option_group", "field" );
        Rule rule = Rule.create( null, null, "true", List.of(ruleAction), "", "" );

        RuleEngine ruleEngine = getRuleEngine( rule );

        List<RuleEffect> ruleEffects = ruleEngine.evaluate( getTestRuleEvent( RuleEvent.Status.ACTIVE ) ).call();

        assertEquals( 1 , ruleEffects.size() );
        assertEquals( "" , ruleEffects.get( 0 ).data() );
        assertEquals( ruleAction , ruleEffects.get( 0 ).ruleAction() );
    }

    @Test
    public void simpleConditionMustResultInSetMandatoryFieldEffect()
        throws Exception
    {
        RuleAction ruleAction = RuleActionSetMandatoryField.create( "test_data_element" );
        Rule rule = Rule.create( null, null, "true", List.of(ruleAction), "", "" );

        RuleEngine ruleEngine = getRuleEngine( rule );

        List<RuleEffect> ruleEffects = ruleEngine.evaluate( getTestRuleEvent( RuleEvent.Status.ACTIVE ) ).call();

        assertEquals( 1 , ruleEffects.size() );
        assertEquals( "" , ruleEffects.get( 0 ).data() );
        assertEquals( ruleAction , ruleEffects.get( 0 ).ruleAction() );
    }

    @Test
    public void simpleConditionMustResultInWarningEffect()
        throws Exception
    {
        RuleAction ruleAction = RuleActionMessage.create(
            "test_warning_message", null, "target_field", RuleActionMessage.Type.SHOW_WARNING );
        Rule rule = Rule.create( null, null, "true", List.of(ruleAction), "", "" );

        RuleEngine ruleEngine = getRuleEngine( rule );

        List<RuleEffect> ruleEffects = ruleEngine.evaluate( getTestRuleEvent( RuleEvent.Status.ACTIVE ) ).call();

        assertEquals( 1 , ruleEffects.size() );
        assertEquals( "" , ruleEffects.get( 0 ).data() );
        assertEquals( ruleAction , ruleEffects.get( 0 ).ruleAction() );
    }

    @Test
    public void simpleConditionMustResultInErrorEffect()
        throws Exception
    {
        RuleAction ruleAction = RuleActionMessage.create(
            "test_error_message", "2 + 2", "target_field", RuleActionMessage.Type.SHOW_ERROR );
        Rule rule = Rule.create( null, null, "true", List.of(ruleAction), "", "" );

        RuleEngine ruleEngine = getRuleEngine( rule );

        List<RuleEffect> ruleEffects = ruleEngine.evaluate( getTestRuleEvent( RuleEvent.Status.ACTIVE ) ).call();

        assertEquals( 1 , ruleEffects.size() );
        assertEquals( "4" , ruleEffects.get( 0 ).data() );
        assertEquals( ruleAction , ruleEffects.get( 0 ).ruleAction() );
    }

    @Test
    public void simpleConditionMustResultInOnCompletionWarningEffect()
        throws Exception
    {
        RuleAction ruleAction = RuleActionMessage.create(
            "test_warning_message", "2 + 2", "target_field", RuleActionMessage.Type.WARNING_ON_COMPILATION );
        Rule rule = Rule.create( null, null, "true", List.of(ruleAction), "", "" );

        RuleEngine ruleEngine = getRuleEngine( rule );

        List<RuleEffect> ruleEffects = ruleEngine.evaluate( getTestRuleEvent( RuleEvent.Status.ACTIVE ) ).call();

        assertEquals( 1 , ruleEffects.size() );
        assertEquals( "4" , ruleEffects.get( 0 ).data() );
        assertEquals( ruleAction , ruleEffects.get( 0 ).ruleAction() );
    }

    private RuleEngine getRuleEngine( Rule rule )
    {
        return RuleEngineContext
            .builder()
            .rules(Collections.singletonList(rule))
            .supplementaryData(new HashMap<>() )
            .constantsValue(new HashMap<>() )
            .build().toEngineBuilder().triggerEnvironment( TriggerEnvironment.SERVER )
            .build();
    }

    private RuleEngine getRuleEngineMultiple( Rule rule, RuleEvent ruleEvent )
    {
        return RuleEngineContext
            .builder()
            .rules(Collections.singletonList(rule))
            .supplementaryData(new HashMap<>() )
            .constantsValue(new HashMap<>() )
            .build().toEngineBuilder().triggerEnvironment( TriggerEnvironment.SERVER )
            .events( List.of( ruleEvent ) )
            .build();
    }
}
