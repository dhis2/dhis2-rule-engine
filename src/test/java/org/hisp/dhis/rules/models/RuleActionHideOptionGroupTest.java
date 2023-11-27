package org.hisp.dhis.rules.models;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith( JUnit4.class )
public class RuleActionHideOptionGroupTest
{
    @Test
    public void createMustSubstituteEmptyStringIfArgumentsNull()
    {
        RuleActionHideOptionGroup ruleActionHideOptionGroup =
            RuleActionHideOptionGroup.create( null, "test_option_group", "field" );

        assertThat( ruleActionHideOptionGroup.content() ).isEqualTo( "" );
    }
}
