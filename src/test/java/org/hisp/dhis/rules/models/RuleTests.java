package org.hisp.dhis.rules.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;

@RunWith( JUnit4.class )
public class RuleTests
{

    @Test( expected = NullPointerException.class )
    public void createShouldThrowOnNullCondition()
    {
        Rule.create( "test_program_stage", 1, null, new ArrayList<RuleAction>(), "", "" );
    }

    @Test( expected = NullPointerException.class )
    public void createShouldThrowOnNullActionsList()
    {
        Rule.create( "test_program_stage", 1, "test_condition", null, "", "" );
    }

    @Test
    public void createShouldPropagatePropertiesCorrectly()
    {
        RuleAction ruleAction = mock( RuleAction.class );

        Rule rule = Rule.create( "test_program_stage", 1,
            "test_condition", Arrays.asList( ruleAction ), "", "uid" );

        assertThat( rule.programStage() ).isEqualTo( "test_program_stage" );
        assertThat( rule.condition() ).isEqualTo( "test_condition" );
        assertThat( rule.priority() ).isEqualTo( 1 );
        assertThat( rule.actions().size() ).isEqualTo( 1 );
        assertThat( rule.actions().get( 0 ) ).isEqualTo( ruleAction );
        assertThat( rule.uid() ).isEqualTo( "uid" );
    }

    @Test( expected = UnsupportedOperationException.class )
    public void createShouldReturnImmutableList()
    {
        RuleAction ruleActionOne = mock( RuleAction.class );
        RuleAction ruleActionTwo = mock( RuleAction.class );

        List<RuleAction> actions = new ArrayList<>();
        actions.add( ruleActionOne );
        actions.add( ruleActionTwo );

        Rule rule = Rule.create( "test_program_stage", 1, "test_condition", actions, "", "" );

        // mutating source array
        actions.clear();

        assertThat( rule.actions().size() ).isEqualTo( 2 );
        assertThat( rule.actions().get( 0 ) ).isEqualTo( ruleActionOne );
        assertThat( rule.actions().get( 1 ) ).isEqualTo( ruleActionTwo );

        rule.actions().clear();
    }
}
