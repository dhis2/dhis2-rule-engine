package org.hisp.dhis.rules.models;

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
        RuleActionMessage ruleActionAssignNoContent = RuleActionMessage
            .create( null, "test_data", "test_field", RuleActionMessage.Type.SHOW_ERROR );
        RuleActionMessage ruleActionAssignNoData = RuleActionMessage
            .create( "test_content", null, "test_field", RuleActionMessage.Type.SHOW_ERROR );

        assertThat( ruleActionAssignNoContent.content() ).isEqualTo( "" );
        assertThat( ruleActionAssignNoContent.data() ).isEqualTo( "test_data" );
        assertThat( ruleActionAssignNoContent.field() ).isEqualTo( "test_field" );

        assertThat( ruleActionAssignNoData.content() ).isEqualTo( "test_content" );
        assertThat( ruleActionAssignNoData.data() ).isEqualTo( "" );
        assertThat( ruleActionAssignNoData.field() ).isEqualTo( "test_field" );
    }
}
