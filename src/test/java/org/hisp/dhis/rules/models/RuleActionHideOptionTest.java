package org.hisp.dhis.rules.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

@RunWith( JUnit4.class )
public class RuleActionHideOptionTest
{
    @Test
    public void createMustSubstituteEmptyStringIfArgumentsNull()
    {
        RuleActionHideOption ruleActionHideOption =
            RuleActionHideOption.create( null, "test_option", "test_field" );

        assertEquals( "" , ruleActionHideOption.content() );
    }
}
