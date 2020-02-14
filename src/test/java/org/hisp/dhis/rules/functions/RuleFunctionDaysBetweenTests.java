package org.hisp.dhis.rules.functions;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.hisp.dhis.parser.expression.antlr.ExpressionParser;
import org.hisp.dhis.rules.parser.expression.CommonExpressionVisitor;
import org.junit.Before;
import org.hisp.dhis.parser.expression.antlr.ExpressionParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

 import org.hamcrest.CoreMatchers;
import static org.mockito.Mockito.when;

@RunWith( MockitoJUnitRunner.class )
public class RuleFunctionDaysBetweenTests
{
        @Mock
        private ExpressionParser.ExprContext context;

        @Mock
        private CommonExpressionVisitor visitor;

        @Mock
        private ExpressionParser.ExprContext mockedFirstExpr;

        @Mock
        private ExpressionParser.ExprContext mockedSecondExpr;

        private RuleFunctionDaysBetween functionToTest = new RuleFunctionDaysBetween();

        @Before
        public void setUp() {
                when(context.expr(0)).thenReturn( mockedFirstExpr );
                when(context.expr(1)).thenReturn( mockedSecondExpr );
        }

        @Test
        public void return_zero_if_some_date_is_not_present()
        {
                assertDaysBetween( null, null,"0");
                assertDaysBetween( null, "","0");
                assertDaysBetween( "", null,"0");
                assertDaysBetween( "", "","0");
        }

        @Test
        public void evaluate_correct_number_of_days()
        {
                assertDaysBetween( "2010-10-15", "2010-10-20","5");
                assertDaysBetween( "2010-09-30", "2010-10-15","15");
                assertDaysBetween( "2010-12-31", "2011-01-01","1");

                assertDaysBetween( "2010-10-20", "2010-10-15","-5");
                assertDaysBetween( "2010-10-15", "2010-09-30","-15");
                assertDaysBetween( "2011-01-01", "2010-12-31","-1");
        }

        @Test(expected = IllegalArgumentException.class)
        public void throw_illegal_argument_exception_if_first_date_is_invalid()
        {
                assertDaysBetween( "bad date", "2010-01-01", null );
        }

        @Test(expected = IllegalArgumentException.class)
        public void throw_illegal_argument_exception_if_second_date_is_invalid()
        {
                assertDaysBetween( "2010-01-01", "bad date", null );
        }

        @Test(expected = IllegalArgumentException.class)
        public void throw_illegal_argument_exception_if_first_and_second_date_is_invalid()
        {
                assertDaysBetween(  "bad date", "bad date", null );
        }

        private void assertDaysBetween( String startDate, String endDate, String daysBetween )
        {
                when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( startDate );
                when( visitor.castStringVisit( mockedSecondExpr ) ).thenReturn( endDate );
                MatcherAssert.assertThat( functionToTest.evaluate( context, visitor ),
                    CoreMatchers.<Object>is( (daysBetween) ) );
        }
}
