package org.hisp.dhis.rules.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

@RunWith( JUnit4.class )
public class RuleActionShowOptionGroupTest
{
    @Test
    public void createMustSubstituteEmptyStringIfArgumentsNull()
    {
        RuleActionShowOptionGroup ruleActionHideOptionGroup =
            RuleActionShowOptionGroup.create( null, "test_option_group", "test_field" );

        assertEquals( "", ruleActionHideOptionGroup.content() );
    }
}
