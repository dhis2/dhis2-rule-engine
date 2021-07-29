package org.hisp.dhis.rules.models;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith( JUnit4.class )
public class RuleActionAssignTest
{
    @Test
    public void createMustSubstituteEmptyStringsForNullArguments()
    {
        RuleActionAssign ruleActionAssignNoContent = RuleActionAssign
            .create( null, "test_data", "test_field" );
        RuleActionAssign ruleActionAssignNoField = RuleActionAssign
            .create( "test_content", "test_data", null );

        assertThat( ruleActionAssignNoContent.content() ).isEqualTo( "" );
        assertThat( ruleActionAssignNoContent.data() ).isEqualTo( "test_data" );
        assertThat( ruleActionAssignNoContent.field() ).isEqualTo( "test_field" );

        assertThat( ruleActionAssignNoField.content() ).isEqualTo( "test_content" );
        assertThat( ruleActionAssignNoField.data() ).isEqualTo( "test_data" );
        assertThat( ruleActionAssignNoField.field() ).isEqualTo( "" );
    }

    @Test( expected = IllegalArgumentException.class )
    public void createMustThrowWhenContentAndFieldAreNull()
    {
        RuleActionAssign.create( null, "test_data", null );
    }

    @Test( expected = NullPointerException.class )
    public void createMustThrowWhenDataIsNull()
    {
        RuleActionAssign.create( "test_content", null, "test_field" );
    }

    @Test
    public void equalsAndHashcodeFunctionsMustConformToContract()
    {
        EqualsVerifier.forClass( RuleActionAssign
            .create( "test_content", "test_data", "test_field" ).getClass() )
            .suppress( Warning.NULL_FIELDS )
            .verify();
    }
}
