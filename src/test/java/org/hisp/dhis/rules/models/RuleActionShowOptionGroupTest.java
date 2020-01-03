package org.hisp.dhis.rules.models;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(JUnit4.class)
public class RuleActionShowOptionGroupTest
{
        @Rule
        public ExpectedException thrown = ExpectedException.none();

        @Test
        public void createMustSubstituteEmptyStringIfArgumentsNull()
        {
            RuleActionShowOptionGroup ruleActionHideOptionGroup =
                    RuleActionShowOptionGroup.create( null, "test_option_group", "test_field" );

            assertThat( ruleActionHideOptionGroup.content() ).isEqualTo( "" );
        }

        @Test
        public void createMustThrowOnNullOptionGroupField()
        {
            thrown.expect( NullPointerException.class );
            RuleActionShowOptionGroup.create(  "test_content", null, "test_field" );
        }

        @Test
        public void createMustThrowOnNullField()
        {
            thrown.expect( NullPointerException.class );
            RuleActionShowOptionGroup.create(  "test_content", "test_option_group", null );
        }

        @Test
        public void equalsAndHashCodeFunctionsMustConformContract()
        {
            EqualsVerifier.forClass( RuleActionShowOptionGroup.create( "test_content", "test_option_group", "test_field" ).getClass() )
                    .suppress( Warning.NULL_FIELDS )
                    .verify();
        }
}