package org.hisp.dhis.rules.models;

import org.hisp.dhis.rules.util.MockRuleAction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith( JUnit4.class )
public class RuleTest
{
    @Test( expected = NullPointerException.class )
    public void createShouldThrowOnNullActionsList()
    {
        Rule.create( "test_program_stage", 1, "test_condition", null, "", "" );
    }

    @Test
    public void createShouldPropagatePropertiesCorrectly()
    {
        RuleAction ruleAction = new MockRuleAction();

        Rule rule = Rule.create( "test_program_stage", 1,
            "test_condition", List.of(ruleAction), "", "uid" );

        assertEquals( "test_program_stage" , rule.programStage() );
        assertEquals( "test_condition" , rule.condition() );
        assertEquals( Integer.valueOf(1) , rule.priority() );
        assertEquals( 1 , rule.actions().size() );
        assertEquals( ruleAction , rule.actions().get( 0 ) );
        assertEquals( "uid" , rule.uid() );
    }

    @Test( expected = UnsupportedOperationException.class )
    public void createShouldReturnImmutableList()
    {
        RuleAction ruleActionOne = new MockRuleAction();
        RuleAction ruleActionTwo = new MockRuleAction();

        List<RuleAction> actions = new ArrayList<>();
        actions.add( ruleActionOne );
        actions.add( ruleActionTwo );

        Rule rule = Rule.create( "test_program_stage", 1, "test_condition", actions, "", "" );

        // mutating source array
        actions.clear();

        assertEquals( 2 , rule.actions().size() );
        assertEquals( ruleActionOne , rule.actions().get( 0 ) );
        assertEquals( ruleActionTwo , rule.actions().get( 1 ) );

        rule.actions().clear();
    }
}
