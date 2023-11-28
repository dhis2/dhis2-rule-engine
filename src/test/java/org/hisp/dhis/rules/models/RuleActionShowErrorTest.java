package org.hisp.dhis.rules.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

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

        assertEquals( "" , ruleActionAssignNoContent.content() );
        assertEquals( "test_data" , ruleActionAssignNoContent.data() );
        assertEquals( "test_field" , ruleActionAssignNoContent.field() );

        assertEquals( "test_content" , ruleActionAssignNoData.content() );
        assertEquals( "" , ruleActionAssignNoData.data() );
        assertEquals( "test_field" , ruleActionAssignNoData.field() );
    }
}
