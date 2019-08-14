package org.hisp.dhis.rules.models;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.hisp.dhis.rules.models.RuleActionAssign;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.fail;

@RunWith( JUnit4.class )
public class RuleActionAssignTests
{
        @Rule
        public ExpectedException thrown = ExpectedException.none();

        @Test
        public void createMustSubstituteEmptyStringsForNullArguments()
        {
                RuleActionAssign ruleActionAssignNoContent = RuleActionAssign.Companion
                    .create( null, "test_data", "test_field" );
                RuleActionAssign ruleActionAssignNoField = RuleActionAssign.Companion
                    .create( "test_content", "test_data", null );

                assertThat( ruleActionAssignNoContent.getContent() ).isEqualTo( "" );
                assertThat( ruleActionAssignNoContent.getData() ).isEqualTo( "test_data" );
                assertThat( ruleActionAssignNoContent.getField() ).isEqualTo( "test_field" );

                assertThat( ruleActionAssignNoField.getContent() ).isEqualTo( "test_content" );
                assertThat( ruleActionAssignNoField.getData() ).isEqualTo( "test_data" );
                assertThat( ruleActionAssignNoField.getField() ).isEqualTo( "" );
        }

        @Test
        public void createMustThrowWhenContentAndFieldAreNull()
        {
            thrown.expect( IllegalArgumentException.class );
            RuleActionAssign.Companion.create( null, "test_data", null );
        }

        @Test
        public void createMustThrowWhenDataIsNull()
        {
            thrown.expect( Exception.class );
            RuleActionAssign.Companion.create( "test_content", null, "test_field" );
        }

        @Test
        public void equalsAndHashcodeFunctionsMustConformToContract()
        {
                EqualsVerifier.forClass( RuleActionAssign.Companion
                    .create( "test_content", "test_data", "test_field" ).getClass() )
                    .suppress( Warning.NULL_FIELDS )
                    .verify();
        }
}
