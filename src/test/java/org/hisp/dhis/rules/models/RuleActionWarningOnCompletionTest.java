package org.hisp.dhis.rules.models;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith( JUnit4.class )
public class RuleActionWarningOnCompletionTest
{

    @Test
    public void createMustSubstituteEmptyStringsForNullArguments()
    {
        RuleActionWarningOnCompletion ruleActionNoContent = RuleActionWarningOnCompletion
            .create( null, "test_data", "test_field" );
        RuleActionWarningOnCompletion ruleActionNoData = RuleActionWarningOnCompletion
            .create( "test_content", null, "test_field" );
        RuleActionWarningOnCompletion ruleActionNoField = RuleActionWarningOnCompletion
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
        RuleActionWarningOnCompletion.create( null, null, null );
    }
}
