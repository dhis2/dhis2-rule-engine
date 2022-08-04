package org.hisp.dhis.rules.functions;

/*
 * Copyle (c) 2004-2018, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the HISP project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.hisp.dhis.antlr.ParserExceptionWithoutContext;
import org.hisp.dhis.parser.expression.antlr.ExpressionParser;
import org.hisp.dhis.rules.parser.expression.CommonExpressionVisitor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

/**
 * @author Zubair Asghar.
 */

@RunWith( MockitoJUnitRunner.class )
public class RuleFunctionRightTest
{
    @Mock
    private ExpressionParser.ExprContext context;

    @Mock
    private CommonExpressionVisitor visitor;

    @Mock
    private ExpressionParser.ExprContext mockedFirstExpr;

    @Mock
    private ExpressionParser.ExprContext mockedSecondExpr;

    @Before
    public void setUp()
    {
        when( context.expr( 0 ) ).thenReturn( mockedFirstExpr );
        when( context.expr( 1 ) ).thenReturn( mockedSecondExpr );
    }

    @Test
    public void return_empty_string_for_null_input()
    {
        RuleFunctionRight rightFunction = new RuleFunctionRight();

        when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( null );
        when( visitor.castStringVisit( mockedSecondExpr ) ).thenReturn( "0" );
        MatcherAssert.assertThat( rightFunction.evaluate( context, visitor ), CoreMatchers.<Object>is( "" ) );

        when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( null );
        when( visitor.castStringVisit( mockedSecondExpr ) ).thenReturn( "10" );
        MatcherAssert.assertThat( rightFunction.evaluate( context, visitor ), CoreMatchers.<Object>is( "" ) );

        when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( null );
        when( visitor.castStringVisit( mockedSecondExpr ) ).thenReturn( "-10" );
        MatcherAssert.assertThat( rightFunction.evaluate( context, visitor ), CoreMatchers.<Object>is( "" ) );
    }

    @Test
    public void return_substring_of_first_argument_from_the_beginning()
    {
        RuleFunctionRight rightFunction = new RuleFunctionRight();

        when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( "abcdef" );
        when( visitor.castStringVisit( mockedSecondExpr ) ).thenReturn( "0" );
        MatcherAssert.assertThat( rightFunction.evaluate( context, visitor ), CoreMatchers.<Object>is( "" ) );

        when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( "abcdef" );
        when( visitor.castStringVisit( mockedSecondExpr ) ).thenReturn( "-5" );
        MatcherAssert.assertThat( rightFunction.evaluate( context, visitor ), CoreMatchers.<Object>is( "f" ) );

        when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( "abcdef" );
        when( visitor.castStringVisit( mockedSecondExpr ) ).thenReturn( "2" );
        MatcherAssert.assertThat( rightFunction.evaluate( context, visitor ), CoreMatchers.<Object>is( "ef" ) );

        when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( "abcdef" );
        when( visitor.castStringVisit( mockedSecondExpr ) ).thenReturn( "30" );
        MatcherAssert.assertThat( rightFunction.evaluate( context, visitor ), CoreMatchers.<Object>is( "abcdef" ) );
    }

    @Test( expected = ParserExceptionWithoutContext.class )
    public void throw_parser_exception_without_context_if_position_is_a_text()
    {
        when( visitor.castStringVisit( mockedSecondExpr ) ).thenReturn( "text" );
        new RuleFunctionRight().evaluate( context, visitor );
    }

    @Test( expected = IllegalArgumentException.class )
    public void throw_illegal_argument_when_number_not_an_integer()
    {
        when( visitor.castStringVisit( mockedSecondExpr ) ).thenReturn( "6.8" );
        new RuleFunctionRight().evaluate( context, visitor );
    }
}
