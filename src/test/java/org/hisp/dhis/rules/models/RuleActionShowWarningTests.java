package org.hisp.dhis.rules.models;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.hisp.dhis.rules.models.RuleActionShowWarning;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.fail;

@RunWith( JUnit4.class )
public class RuleActionShowWarningTests
{

        @Test
        public void createMustSubstituteEmptyStringsForNullArguments()
        {
                RuleActionShowWarning ruleActionAssignNoContent = RuleActionShowWarning.Companion
                    .create( null, "test_data", "test_field" );
                RuleActionShowWarning ruleActionAssignNoData = RuleActionShowWarning.Companion
                    .create( "test_content", null, "test_field" );

                assertThat( ruleActionAssignNoContent.getContent() ).isEqualTo( "" );
                assertThat( ruleActionAssignNoContent.getData() ).isEqualTo( "test_data" );
                assertThat( ruleActionAssignNoContent.getField() ).isEqualTo( "test_field" );

                assertThat( ruleActionAssignNoData.getContent() ).isEqualTo( "test_content" );
                assertThat( ruleActionAssignNoData.getData() ).isEqualTo( "" );
                assertThat( ruleActionAssignNoData.getField() ).isEqualTo( "test_field" );
        }

        @Test
        public void createMustThrowWhenContentAndDataAreNull()
        {
                try
                {
                        RuleActionShowWarning.Companion.create( null, null, "test_field" );
                        fail( "IllegalArgumentException was expected, but nothing was thrown." );
                }
                catch ( Exception illegalArgumentException )
                {
                        // noop
                }
        }

        @Test
        public void createMustThrowWhenFieldIsNull()
        {
                try
                {
                        RuleActionShowWarning.Companion.create( "test_content", "test_data", null );
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
                EqualsVerifier.forClass( RuleActionShowWarning.Companion
                    .create( "test_content", "test_data", "test_field" ).getClass() )
                    .suppress( Warning.NULL_FIELDS )
                    .verify();
        }
}
