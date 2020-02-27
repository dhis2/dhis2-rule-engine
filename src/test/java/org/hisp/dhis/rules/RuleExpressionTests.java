package org.hisp.dhis.rules;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith( JUnit4.class )
public class RuleExpressionTests
{

    @Test
    public void unwrapMustReturnVariableName()
    {
        assertThat( RuleExpression.unwrapVariableName( "#{test_variable_one}" ) )
            .isEqualTo( "test_variable_one" );
    }
}
