package org.hisp.dhis.rules;

import org.hisp.dhis.rules.models.Rule;
import org.hisp.dhis.rules.models.RuleAction;
import org.hisp.dhis.rules.models.RuleActionText;
import org.hisp.dhis.rules.models.RuleDataValue;
import org.hisp.dhis.rules.models.RuleEffect;
import org.hisp.dhis.rules.models.RuleEvent;
import org.hisp.dhis.rules.models.RuleValueType;
import org.hisp.dhis.rules.models.RuleVariable;
import org.hisp.dhis.rules.models.RuleVariableCurrentEvent;
import org.hisp.dhis.rules.models.TriggerEnvironment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith( JUnit4.class )
public class RuleEngineValueTypesTest
{
    @Test
    public void booleanVariableWithoutValueMustFallbackToDefaultBooleanValue()
        throws Exception
    {
        RuleAction ruleAction = RuleActionText
            .createForFeedback( "test_action_content", "#{test_variable}" );
        Rule rule = Rule.create( null, null, "true", List.of(ruleAction), "", "" );
        RuleVariable ruleVariable = RuleVariableCurrentEvent
            .create( "test_variable", "test_data_element", RuleValueType.BOOLEAN, true, new ArrayList<>());

        RuleEngine ruleEngine = getRuleEngine( rule, List.of(ruleVariable));

        RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, new ArrayList<RuleDataValue>(), "", null);
        List<RuleEffect> ruleEffects = ruleEngine.evaluate( ruleEvent ).call();

        assertEquals( 1 , ruleEffects.size() );
        assertEquals( "false" , ruleEffects.get( 0 ).data() );
        assertEquals( ruleAction , ruleEffects.get( 0 ).ruleAction() );
    }

    @Test
    public void numericVariableWithoutValueMustFallbackToDefaultNumericValue()
        throws Exception
    {
        RuleAction ruleAction = RuleActionText
            .createForFeedback( "test_action_content", "#{test_variable}" );
        Rule rule = Rule.create( null, null, "true", List.of(ruleAction), "", "" );
        RuleVariable ruleVariable = RuleVariableCurrentEvent
            .create( "test_variable", "test_data_element", RuleValueType.NUMERIC, true, new ArrayList<>());

        RuleEngine ruleEngine = getRuleEngine( rule, List.of(ruleVariable));

        RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, new ArrayList<RuleDataValue>(), "", null);
        List<RuleEffect> ruleEffects = ruleEngine.evaluate( ruleEvent ).call();

        assertEquals( 1 , ruleEffects.size() );
        assertEquals( "0" , ruleEffects.get( 0 ).data() );
        assertEquals( ruleAction , ruleEffects.get( 0 ).ruleAction() );
    }

    @Test
    public void textVariableWithoutValueMustFallbackToDefaultTextValue()
        throws Exception
    {
        RuleAction ruleAction = RuleActionText
            .createForFeedback( "test_action_content", "#{test_variable}" );
        Rule rule = Rule.create( null, null, "true", List.of(ruleAction), "", "" );
        RuleVariable ruleVariable = RuleVariableCurrentEvent
            .create( "test_variable", "test_data_element", RuleValueType.TEXT, true, new ArrayList<>());

        RuleEngine ruleEngine = getRuleEngine( rule, List.of(ruleVariable));

        RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, new ArrayList<RuleDataValue>(), "", null);
        List<RuleEffect> ruleEffects = ruleEngine.evaluate( ruleEvent ).call();

        assertEquals( 1 , ruleEffects.size() );
        assertEquals( "" , ruleEffects.get( 0 ).data() );
        assertEquals( ruleAction , ruleEffects.get( 0 ).ruleAction() );
    }

    private RuleEngine getRuleEngine( Rule rule, List<RuleVariable> ruleVariables )
    {
        return RuleEngineContext
            .builder()
            .rules(Collections.singletonList(rule))
            .ruleVariables( ruleVariables )
            .supplementaryData(new HashMap<>() )
            .constantsValue(new HashMap<>() )
            .build().toEngineBuilder().triggerEnvironment( TriggerEnvironment.SERVER )
            .build();
    }
}
