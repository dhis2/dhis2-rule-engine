package org.hisp.dhis.rules.models;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith( JUnit4.class )
public class RuleActionErrorOnCompletionTests
{

    @Test
    public void createMustSubstituteEmptyStringsForNullArguments()
    {
        RuleActionErrorOnCompletion ruleActionNoContent = RuleActionErrorOnCompletion
            .create( null, "test_data", "test_field" );
        RuleActionErrorOnCompletion ruleActionNoData = RuleActionErrorOnCompletion
            .create( "test_content", null, "test_field" );
        RuleActionErrorOnCompletion ruleActionNoField = RuleActionErrorOnCompletion
            .create( "test_content", "test_data", null );

        assertThat( ruleActionNoContent.content() ).isEqualTo( "" );
        assertThat( ruleActionNoContent.data() ).isEqualTo( "test_data" );
        assertThat( ruleActionNoContent.field() ).isEqualTo( "test_field" );

        assertThat( ruleActionNoData.content() ).isEqualTo( "test_content" );
        assertThat( ruleActionNoData.data() ).isEqualTo( "" );
        assertThat( ruleActionNoData.field() ).isEqualTo( "test_field" );

        assertThat( ruleActionNoField.content() ).isEqualTo( "test_content" );
        assertThat( ruleActionNoField.data() ).isEqualTo( "test_data" );
        assertThat( ruleActionNoField.field() ).isEqualTo( "" );
    }

    @Test( expected = IllegalArgumentException.class )
    public void createMustThrowWhenContentDataFieldAreNull()
    {
        RuleActionErrorOnCompletion.create( null, null, null );
    }

    @Test
    public void equalsAndHashcodeFunctionsMustConformToContract()
    {
        EqualsVerifier.forClass( RuleActionErrorOnCompletion
            .create( "test_content", "test_data", "test_field", AttributeType.DATA_ELEMENT ).getClass() )
            .suppress( Warning.NULL_FIELDS )
            .verify();
    }
}
