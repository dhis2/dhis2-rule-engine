package org.hisp.dhis.rules.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

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

        assertEquals( "" , ruleActionAssignNoContent.content() );
        assertEquals( "test_data" , ruleActionAssignNoContent.data() );
        assertEquals( "test_field" , ruleActionAssignNoContent.field() );

        assertEquals( "test_content" , ruleActionAssignNoField.content() );
        assertEquals( "test_data" , ruleActionAssignNoField.data() );
        assertEquals( "" , ruleActionAssignNoField.field() );
    }

    @Test( expected = IllegalArgumentException.class )
    public void createMustThrowWhenContentAndFieldAreNull()
    {
        RuleActionAssign.create( null, "test_data", null );
    }

}
