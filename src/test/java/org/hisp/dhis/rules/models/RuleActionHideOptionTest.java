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
public class RuleActionHideOptionTest
{
        @Rule
        public ExpectedException thrown = ExpectedException.none();

        @Test
        public void createMustSubstituteEmptyStringIfArgumentsNull()
        {
            RuleActionHideOption ruleActionHideOption =
                    RuleActionHideOption.create( null, "test_option", "test_field" );

            assertThat( ruleActionHideOption.getContent() ).isEqualTo( "" );
        }

        @Test
        public void createMustThrowOnNullField()
        {
            thrown.expect( Exception.class );
            RuleActionHideOption.create(  "test_content", null, "null" );
        }

        @Test
        public void equalsAndHashCodeFunctionsMustConformContract()
        {
            EqualsVerifier.forClass( RuleActionHideOption.create( "test_content", "test_option", "test_field" ).getClass() )
                    .suppress( Warning.NULL_FIELDS )
                    .verify();
        }
}