package org.hisp.dhis.rules.models;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.fail;

@RunWith( JUnit4.class )
public class RuleActionErrorOnCompletionTests
{

        @Test
        public void createMustSubstituteEmptyStringsForNullArguments()
        {
                RuleActionErrorOnCompletion ruleActionNoContent = RuleActionErrorOnCompletion
                    .create( null, "test_data", "test_field" );
                RuleActionErrorOnCompletion ruleActionNoData = RuleActionErrorOnCompletion
                    .create( "test_content", null, "test_field" );
                RuleActionErrorOnCompletion ruleActionNoField = RuleActionErrorOnCompletion
                    .create( "test_content", "test_data", null );

                assertThat( ruleActionNoContent.getContent() ).isEqualTo( "" );
                assertThat( ruleActionNoContent.getData() ).isEqualTo( "test_data" );
                assertThat( ruleActionNoContent.getField() ).isEqualTo( "test_field" );

                assertThat( ruleActionNoData.getContent() ).isEqualTo( "test_content" );
                assertThat( ruleActionNoData.getData() ).isEqualTo( "" );
                assertThat( ruleActionNoData.getField() ).isEqualTo( "test_field" );

                assertThat( ruleActionNoField.getContent() ).isEqualTo( "test_content" );
                assertThat( ruleActionNoField.getData() ).isEqualTo( "test_data" );
                assertThat( ruleActionNoField.getField() ).isEqualTo( "" );
        }

        @Test
        public void createMustThrowWhenContentDataFieldAreNull()
        {
                try
                {
                        RuleActionErrorOnCompletion.create( null, null, null );
                        fail( "IllegalArgumentException was expected, but nothing was thrown." );
                }
                catch ( Exception illegalArgumentException )
                {
                        // noop
                }
        }

        @Test
        public void equalsAndHashcodeFunctionsMustConformToContract()
        {
                EqualsVerifier.forClass( RuleActionErrorOnCompletion
                    .create( "test_content", "test_data", "test_field" ).getClass() )
                    .suppress( Warning.NULL_FIELDS )
                    .verify();
        }
}
