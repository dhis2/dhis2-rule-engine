package org.hisp.dhis.rules.models;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith( JUnit4.class )
public class RuleActionShowErrorTest
{
    @Test
    public void createMustSubstituteEmptyStringsForNullArguments()
    {
        RuleActionShowError ruleActionAssignNoContent = RuleActionShowError
            .create( null, "test_data", "test_field" );
        RuleActionShowError ruleActionAssignNoData = RuleActionShowError
            .create( "test_content", null, "test_field" );

        assertThat( ruleActionAssignNoContent.content() ).isEqualTo( "" );
        assertThat( ruleActionAssignNoContent.data() ).isEqualTo( "test_data" );
        assertThat( ruleActionAssignNoContent.field() ).isEqualTo( "test_field" );

        assertThat( ruleActionAssignNoData.content() ).isEqualTo( "test_content" );
        assertThat( ruleActionAssignNoData.data() ).isEqualTo( "" );
        assertThat( ruleActionAssignNoData.field() ).isEqualTo( "test_field" );
    }

    @Test( expected = IllegalArgumentException.class )
    public void createMustThrowWhenContentAndDataAreNull()
    {
        RuleActionShowError.create( null, null, "test_field" );
    }

    @Test( expected = NullPointerException.class )
    public void createMustThrowWhenFieldIsNull()
    {
        RuleActionShowError.create( "test_content", "test_data", null );
    }

    @Test
    public void equalsAndHashcodeFunctionsMustConformToContract()
    {
        EqualsVerifier.forClass( RuleActionShowError
            .create( "test_content", "test_data", "test_field", AttributeType.DATA_ELEMENT ).getClass() )
            .suppress( Warning.NULL_FIELDS )
            .verify();
    }
}
