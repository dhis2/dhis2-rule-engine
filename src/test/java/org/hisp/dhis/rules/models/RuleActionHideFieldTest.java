package org.hisp.dhis.rules.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith( JUnit4.class )
public class RuleActionHideFieldTest
{
    @Test
    public void createMustSubstituteEmptyStringIfArgumentsNull()
    {
        RuleActionHideField ruleActionHideField =
            RuleActionHideField.create( null, "test_field" );

        assertThat( ruleActionHideField.content() ).isEqualTo( "" );
    }
}
