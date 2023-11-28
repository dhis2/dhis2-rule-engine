package org.hisp.dhis.rules.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

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

        assertEquals( "" , ruleActionAssignNoContent.content() );
        assertEquals( "test_data" , ruleActionAssignNoContent.data() );
        assertEquals( "test_program_stage" , ruleActionAssignNoContent.programStage() );

        assertEquals( "test_content" , ruleActionAssignNoField.content() );
        assertEquals( "" , ruleActionAssignNoField.data() );
        assertEquals( "test_program_stage" , ruleActionAssignNoField.programStage() );
    }
}
