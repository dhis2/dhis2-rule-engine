package org.hisp.dhis.rules.models;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.fail;

@RunWith(JUnit4.class)
public class RuleActionHideOptionGroupTest
{
        @Test
        public void createMustSubstituteEmptyStringIfArgumentsNull()
        {
            RuleActionHideOptionGroup ruleActionHideOptionGroup =
                    RuleActionHideOptionGroup.create( null, "test_option_group" );

            assertThat( ruleActionHideOptionGroup.content() ).isEqualTo( "" );
        }

        @Test
        public void createMustThrowOnNullField()
        {
            try
            {
                RuleActionHideOptionGroup.create(  "test_content", null );
                fail( "NullPointerException was expected, but nothing was thrown." );
            }
            catch ( NullPointerException nullPointerException )
            {
                // noop
            }
        }

        @Test
        public void equalsAndHashCodeFunctionsMustConformContract()
        {
            EqualsVerifier.forClass( RuleActionHideOptionGroup.create( "test_content", "test_option_group" ).getClass() )
                    .suppress( Warning.NULL_FIELDS )
                    .verify();
        }
}