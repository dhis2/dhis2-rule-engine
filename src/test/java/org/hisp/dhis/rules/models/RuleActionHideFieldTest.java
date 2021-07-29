package org.hisp.dhis.rules.models;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith( JUnit4.class )
public class RuleActionHideFieldTest
{

    @Test
    public void createMustSubstituteEmptyStringIfArgumentsNull()
    {
        RuleActionHideField ruleActionHideField =
            RuleActionHideField.create( null, "test_field" );

        assertThat( ruleActionHideField.content() ).isEqualTo( "" );
    }

    @Test( expected = NullPointerException.class )
    public void createMustThrowOnNullField()
    {
        RuleActionHideField.create( "test_content", null );
    }

    @Test
    public void equalsAndHashCodeFunctionsMustConformContract()
    {
        EqualsVerifier.forClass(
            RuleActionHideField.create( "test_content", "test_field", AttributeType.DATA_ELEMENT ).getClass() )
            .suppress( Warning.NULL_FIELDS )
            .verify();
    }
}
