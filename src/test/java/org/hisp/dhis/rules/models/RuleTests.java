package org.hisp.dhis.rules.models;

import org.hisp.dhis.rules.ExpressionEvaluator;
import org.hisp.dhis.rules.RuleEngine;
import org.hisp.dhis.rules.RuleEngineContext;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.*;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

@RunWith( JUnit4.class )
public class RuleTests
{

        @Test(expected = IllegalArgumentException.class)
        public void createShouldThrowOnNullCondition()
        {
                new Rule( "test_program_stage", 1, null, new ArrayList<RuleAction>(), "");
        }

        @Test(expected = IllegalArgumentException.class)
        public void createShouldThrowOnNullActionsList()
        {
                new Rule( "test_program_stage", 1, "test_condition", null, "");
        }

        @Test
        public void createShouldPropagatePropertiesCorrectly()
        {
                RuleAction ruleAction = mock( RuleAction.class );

                Rule rule = new Rule( "test_program_stage", 1,
                    "test_condition", Arrays.asList( ruleAction ), "");

                assertThat( rule.getProgramStage() ).isEqualTo( "test_program_stage" );
                assertThat( rule.getCondition() ).isEqualTo( "test_condition" );
                assertThat( rule.getPriority() ).isEqualTo( 1 );
                assertThat( rule.getActions().size() ).isEqualTo( 1 );
                assertThat( rule.getActions().get( 0 ) ).isEqualTo( ruleAction );
        }

        @Test
        @Ignore
        public void createShouldReturnImmutableList()
        {
                RuleAction ruleActionOne = mock( RuleAction.class );
                RuleAction ruleActionTwo = mock( RuleAction.class );

                List<RuleAction> actions = new ArrayList<>();
                actions.add( ruleActionOne );
                actions.add( ruleActionTwo );

                Rule rule = new Rule( "test_program_stage", 1, "test_condition", actions, "");

                // mutating source array
                actions.clear();

                assertThat( rule.getActions().size() ).isEqualTo( 2 );
                assertThat( rule.getActions().get( 0 ) ).isEqualTo( ruleActionOne );
                assertThat( rule.getActions().get( 1 ) ).isEqualTo( ruleActionTwo );

                try
                {
                        rule.getActions().clear();
                        fail( "UnsupportedOperationException was expected, but nothing was thrown." );
                }
                catch ( UnsupportedOperationException unsupportedOperationException )
                {
                        // noop
                }
        }
}
