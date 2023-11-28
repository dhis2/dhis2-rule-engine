package org.hisp.dhis.rules.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

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

        assertEquals( "" , ruleActionNoContent.content() );
        assertEquals( "test_data" , ruleActionNoContent.data() );
        assertEquals( "test_field" , ruleActionNoContent.field() );

        assertEquals( "test_content" , ruleActionNoData.content() );
        assertEquals( "" , ruleActionNoData.data() );
        assertEquals( "test_field" , ruleActionNoData.field() );

        assertEquals( "test_content" , ruleActionNoField.content() );
        assertEquals( "test_data" , ruleActionNoField.data() );
        assertEquals( "" , ruleActionNoField.field() );
    }

    @Test( expected = IllegalArgumentException.class )
    public void createMustThrowWhenContentDataFieldAreNull()
    {
        RuleActionMessage.create( null, null, null, RuleActionMessage.Type.ERROR_ON_COMPILATION );
    }

}
