package org.hisp.dhis.rules.models;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.fail;

@RunWith(JUnit4.class)
public class RuleActionHideOptionTest
{
        @Test
        public void createMustSubstituteEmptyStringIfArgumentsNull()
        {
            RuleActionHideOption ruleActionHideOption =
                    RuleActionHideOption.create( null, "test_option", "test_field" );

            assertThat( ruleActionHideOption.content() ).isEqualTo( "" );
        }

        @Test
        public void createMustThrowOnNullField()
        {
            try
            {
                RuleActionHideOption.create(  "test_content", null, "null" );
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
            EqualsVerifier.forClass( RuleActionHideOption.create( "test_content", "test_option", "test_field" ).getClass() )
                    .suppress( Warning.NULL_FIELDS )
                    .verify();
        }
}