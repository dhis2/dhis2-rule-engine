package org.hisp.dhis.rules;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith( JUnit4.class )
public class RuleExpressionTests {

        @Test
        public void unwrapMustReturnVariableName()
        {
                assertThat( RuleExpression.unwrapVariableName( "A{test_variable_one}" ) )
                    .isEqualTo( "test_variable_one" );
                assertThat( RuleExpression.unwrapVariableName( "C{test_variable_two}" ) )
                    .isEqualTo( "test_variable_two" );
                assertThat( RuleExpression.unwrapVariableName( "V{test_variable_three}" ) )
                    .isEqualTo( "test_variable_three" );
                assertThat( RuleExpression.unwrapVariableName( "X{test_variable_four}" ) )
                    .isEqualTo( "test_variable_four" );
        }
}
