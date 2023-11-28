package org.hisp.dhis.rules.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

@RunWith( JUnit4.class )
public class RuleActionHideOptionGroupTest
{
    @Test
    public void createMustSubstituteEmptyStringIfArgumentsNull()
    {
        RuleActionHideOptionGroup ruleActionHideOptionGroup =
            RuleActionHideOptionGroup.create( null, "test_option_group", "field" );

        assertEquals( "" , ruleActionHideOptionGroup.content() );
    }
}
