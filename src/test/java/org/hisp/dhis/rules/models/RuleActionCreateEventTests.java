package org.hisp.dhis.rules.models;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.hisp.dhis.rules.models.RuleActionCreateEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.fail;

@RunWith( JUnit4.class )
public class RuleActionCreateEventTests
{

        @Test
        public void createMustSubstituteEmptyStringsForNullArguments()
        {
                RuleActionCreateEvent ruleActionAssignNoContent = RuleActionCreateEvent.Companion
                    .create( null, "test_data", "test_program_stage" );
                RuleActionCreateEvent ruleActionAssignNoField = RuleActionCreateEvent.Companion
                    .create( "test_content", null, "test_program_stage" );

                assertThat( ruleActionAssignNoContent.getContent() ).isEqualTo( "" );
                assertThat( ruleActionAssignNoContent.getData() ).isEqualTo( "test_data" );
                assertThat( ruleActionAssignNoContent.getProgramStage() ).isEqualTo( "test_program_stage" );

                assertThat( ruleActionAssignNoField.getContent() ).isEqualTo( "test_content" );
                assertThat( ruleActionAssignNoField.getData() ).isEqualTo( "" );
                assertThat( ruleActionAssignNoField.getProgramStage() ).isEqualTo( "test_program_stage" );
        }

        @Test
        public void createMustThrowWhenFieldIsNull()
        {
                try
                {
                        RuleActionCreateEvent.Companion.create( "test_content", "test_data", null );
                        fail( "NullPointerException was expected, but nothing was thrown." );
                }
                catch ( Exception nullPointerException )
                {
                        // noop
                }
        }

        @Test
        public void equalsAndHashcodeFunctionsMustConformToContract()
        {
                EqualsVerifier.forClass( RuleActionCreateEvent.Companion
                    .create( "test_content", "test_data", "test_field" ).getClass() )
                    .suppress( Warning.NULL_FIELDS )
                    .verify();
        }
}
