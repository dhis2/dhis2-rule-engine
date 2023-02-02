package org.hisp.dhis.rules.models;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith( JUnit4.class )
public class RuleActionCreateEventTest
{

    @Test
    public void createMustSubstituteEmptyStringsForNullArguments()
    {
        RuleActionCreateEvent ruleActionAssignNoContent = RuleActionCreateEvent
            .create( null, "test_data", "test_program_stage" );
        RuleActionCreateEvent ruleActionAssignNoField = RuleActionCreateEvent
            .create( "test_content", null, "test_program_stage" );

        assertThat( ruleActionAssignNoContent.content() ).isEqualTo( "" );
        assertThat( ruleActionAssignNoContent.data() ).isEqualTo( "test_data" );
        assertThat( ruleActionAssignNoContent.programStage() ).isEqualTo( "test_program_stage" );

        assertThat( ruleActionAssignNoField.content() ).isEqualTo( "test_content" );
        assertThat( ruleActionAssignNoField.data() ).isEqualTo( "" );
        assertThat( ruleActionAssignNoField.programStage() ).isEqualTo( "test_program_stage" );
    }

    @Test( expected = NullPointerException.class )
    public void createMustThrowWhenFieldIsNull()
    {
        RuleActionCreateEvent.create( "test_content", "test_data", null );
    }

}
