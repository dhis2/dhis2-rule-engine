package org.hisp.dhis.rules.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith( JUnit4.class )
public class RuleActionErrorOnCompletionTest
{
    @Test
    public void createMustSubstituteEmptyStringsForNullArguments()
    {
        RuleActionMessage ruleActionNoContent = RuleActionMessage
            .create( null, "test_data", "test_field", RuleActionMessage.Type.ERROR_ON_COMPILATION );
        RuleActionMessage ruleActionNoData = RuleActionMessage
            .create( "test_content", null, "test_field", RuleActionMessage.Type.ERROR_ON_COMPILATION );
        RuleActionMessage ruleActionNoField = RuleActionMessage
            .create( "test_content", "test_data", null, RuleActionMessage.Type.ERROR_ON_COMPILATION );

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
        RuleActionMessage.create( null, null, null, RuleActionMessage.Type.ERROR_ON_COMPILATION );
    }

}
