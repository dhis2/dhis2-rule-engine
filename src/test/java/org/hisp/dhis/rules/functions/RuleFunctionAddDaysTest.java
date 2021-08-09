package org.hisp.dhis.rules.functions;

import org.hamcrest.CoreMatchers;
import org.hisp.dhis.parser.expression.antlr.ExpressionParser;
import org.hisp.dhis.rules.parser.expression.CommonExpressionVisitor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith( MockitoJUnitRunner.class )
public class RuleFunctionAddDaysTest
{
    @Mock
    private ExpressionParser.ExprContext context;

    @Mock
    private CommonExpressionVisitor visitor;

    @Mock
    private ExpressionParser.ExprContext mockedDateExpr;

    @Mock
    private ExpressionParser.ExprContext mockedIntExpr;

    @Before
    public void setUp()
    {
        when( context.expr( 0 ) ).thenReturn( mockedDateExpr );
        when( context.expr( 1 ) ).thenReturn( mockedIntExpr );
    }

    @Test
    public void return_new_date_with_days_added()
    {
        RuleFunctionAddDays addDaysFunction = new RuleFunctionAddDays();

        when( visitor.castStringVisit( mockedDateExpr ) ).thenReturn( "2011-01-01" );
        when( visitor.castStringVisit( mockedIntExpr ) ).thenReturn( "6.0" );
        assertThat( addDaysFunction.evaluate( context, visitor ), CoreMatchers.<Object>is( ("2011-01-07") ) );

        when( visitor.castStringVisit( mockedDateExpr ) ).thenReturn( "2010-10-10" );
        when( visitor.castStringVisit( mockedIntExpr ) ).thenReturn( "1" );
        assertThat( addDaysFunction.evaluate( context, visitor ), CoreMatchers.<Object>is( ("2010-10-11") ) );

        when( visitor.castStringVisit( mockedDateExpr ) ).thenReturn( "2010-10-10" );
        when( visitor.castStringVisit( mockedIntExpr ) ).thenReturn( "1.3" );
        assertThat( addDaysFunction.evaluate( context, visitor ), CoreMatchers.<Object>is( ("2010-10-11") ) );

        when( visitor.castStringVisit( mockedDateExpr ) ).thenReturn( "2010-10-31" );
        when( visitor.castStringVisit( mockedIntExpr ) ).thenReturn( "1" );
        assertThat( addDaysFunction.evaluate( context, visitor ), CoreMatchers.<Object>is( ("2010-11-01") ) );

        when( visitor.castStringVisit( mockedDateExpr ) ).thenReturn( "2010-12-01" );
        when( visitor.castStringVisit( mockedIntExpr ) ).thenReturn( "31" );
        assertThat( addDaysFunction.evaluate( context, visitor ), CoreMatchers.<Object>is( ("2011-01-01") ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void throw_runtime_exception_if_first_argument_is_invalid()
    {
        when( visitor.castStringVisit( mockedDateExpr ) ).thenReturn( "bad date" );
        when( visitor.castStringVisit( mockedIntExpr ) ).thenReturn( "6" );

        new RuleFunctionAddDays().evaluate( context, visitor );
    }

    @Test( expected = IllegalArgumentException.class )
    public void throw_illegal_argument_exception_if_second_argument_is_invalid()
    {
        when( visitor.castStringVisit( mockedDateExpr ) ).thenReturn( "2010-01-01" );
        when( visitor.castStringVisit( mockedIntExpr ) ).thenReturn( "bad number" );

        new RuleFunctionAddDays().evaluate( context, visitor );
    }

    @Test( expected = IllegalArgumentException.class )
    public void throw_illegal_argument_exception_if_first_and_second_argument_is_invalid()
    {
        when( visitor.castStringVisit( mockedDateExpr ) ).thenReturn( "bad date" );
        when( visitor.castStringVisit( mockedIntExpr ) ).thenReturn( "bad number" );

        new RuleFunctionAddDays().evaluate( context, visitor );
    }
}
