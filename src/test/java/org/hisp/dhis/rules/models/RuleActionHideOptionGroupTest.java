package org.hisp.dhis.rules.models;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith( JUnit4.class )
public class RuleActionHideOptionGroupTest
{
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void createMustSubstituteEmptyStringIfArgumentsNull()
    {
        RuleActionHideOptionGroup ruleActionHideOptionGroup =
            RuleActionHideOptionGroup.create( null, "test_option_group", "field" );

        assertThat( ruleActionHideOptionGroup.content() ).isEqualTo( "" );
    }

    @Test
    public void createMustThrowOnNullField()
    {
        thrown.expect( NullPointerException.class );
        RuleActionHideOptionGroup.create( "test_content", "test_option_group" , null);
    }

    @Test
    public void createMustThrowOnNullOptionGroup()
    {
        thrown.expect( NullPointerException.class );
        RuleActionHideOptionGroup.create( "test_content", null ,"field");
    }

}
