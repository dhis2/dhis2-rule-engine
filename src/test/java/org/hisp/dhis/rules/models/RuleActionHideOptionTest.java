package org.hisp.dhis.rules.models;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith( JUnit4.class )
public class RuleActionHideOptionTest
{
    @Test
    public void createMustSubstituteEmptyStringIfArgumentsNull()
    {
        RuleActionHideOption ruleActionHideOption =
            RuleActionHideOption.create( null, "test_option", "test_field" );

        assertThat( ruleActionHideOption.content() ).isEqualTo( "" );
    }
}
