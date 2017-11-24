package org.hisp.dhis.rules.functions;

import org.hisp.dhis.rules.functions.RuleFunction;
import org.hisp.dhis.rules.functions.RuleFunctionDaysBetween;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith( JUnit4.class )
public class RuleFunctionTests
{

        @Test
        public void createMustReturnCorrectFunction()
        {
                assertThat( RuleFunction.create( "d2:daysBetween" ) )
                    .isInstanceOf( RuleFunctionDaysBetween.class );
                assertThat( RuleFunction.create( "d2:fake" ) ).isNull();
        }
}
