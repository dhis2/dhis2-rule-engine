package org.hisp.dhis.rules.functions;

/*
 * Copyright (c) 2004-2018, University of Oslo
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
import org.hisp.dhis.antlr.ParserExceptionWithoutContext;
import org.hisp.dhis.parser.expression.antlr.ExpressionParser;
import org.hisp.dhis.rules.parser.expression.CommonExpressionVisitor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * @author Zubair Asghar.
 */

@RunWith( MockitoJUnitRunner.class )
public class RuleFunctionSubStringTest
{
    @Mock
    private ExpressionParser.ExprContext context;

    @Mock
    private CommonExpressionVisitor visitor;

    @Mock
    private ExpressionParser.ExprContext mockedFirstExpr;

    @Mock
    private ExpressionParser.ExprContext mockedSecondExpr;

    @Mock
    private ExpressionParser.ExprContext mockedThirdExpr;

    @Before
    public void setUp()
    {
        when( context.expr( 0 ) ).thenReturn( mockedFirstExpr );
        when( context.expr( 1 ) ).thenReturn( mockedSecondExpr );
        when( context.expr( 2 ) ).thenReturn( mockedThirdExpr );
    }

    @Test
    public void return_empty_string_for_null_inputs()
    {
        RuleFunctionSubString subStringFunction = new RuleFunctionSubString();

        when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( null );
        when( visitor.castStringVisit( mockedSecondExpr ) ).thenReturn( "0" );
        when( visitor.castStringVisit( mockedThirdExpr ) ).thenReturn( "0" );

        assertThat( subStringFunction.evaluate( context, visitor ), CoreMatchers.<Object>is( "" ) );

        when( visitor.castStringVisit( mockedThirdExpr ) ).thenReturn( "10" );
        assertThat( subStringFunction.evaluate( context, visitor ), CoreMatchers.<Object>is( "" ) );
    }

    @Test
    public void return_substring_from_start_index_to_end_index_of_input_string()
    {
        RuleFunctionSubString subStringFunction = new RuleFunctionSubString();

        when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( "abcdef" );
        when( visitor.castStringVisit( mockedSecondExpr ) ).thenReturn( "0" );
        when( visitor.castStringVisit( mockedThirdExpr ) ).thenReturn( "0" );

        assertThat( subStringFunction.evaluate( context, visitor ), CoreMatchers.<Object>is( "" ) );

        when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( "abcdef" );
        when( visitor.castStringVisit( mockedSecondExpr ) ).thenReturn( "0" );
        when( visitor.castStringVisit( mockedThirdExpr ) ).thenReturn( "1" );
        assertThat( subStringFunction.evaluate( context, visitor ), CoreMatchers.<Object>is( "a" ) );

        when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( "abcdef" );
        when( visitor.castStringVisit( mockedSecondExpr ) ).thenReturn( "-10" );
        when( visitor.castStringVisit( mockedThirdExpr ) ).thenReturn( "1" );
        assertThat( subStringFunction.evaluate( context, visitor ), CoreMatchers.<Object>is( "a" ) );

        when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( "abcdef" );
        when( visitor.castStringVisit( mockedSecondExpr ) ).thenReturn( "2" );
        when( visitor.castStringVisit( mockedThirdExpr ) ).thenReturn( "4" );
        assertThat( subStringFunction.evaluate( context, visitor ), CoreMatchers.<Object>is( "cd" ) );

        when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( "abcdef" );
        when( visitor.castStringVisit( mockedSecondExpr ) ).thenReturn( "2" );
        when( visitor.castStringVisit( mockedThirdExpr ) ).thenReturn( "10" );
        assertThat( subStringFunction.evaluate( context, visitor ), CoreMatchers.<Object>is( "cdef" ) );
    }

    @Test( expected = ParserExceptionWithoutContext.class )
    public void throw_parser_exception_without_context_if_start_index_is_a_text()
    {

        RuleFunctionSubString subStringFunction = new RuleFunctionSubString();

        when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( "test_variable_one" );
        when( visitor.castStringVisit( mockedSecondExpr ) ).thenReturn( "variable" );

        subStringFunction.evaluate( context, visitor );
    }

    @Test( expected = ParserExceptionWithoutContext.class )
    public void throw_parser_exception_without_context_if_end_index_is_a_text()
    {
        RuleFunctionSubString subStringFunction = new RuleFunctionSubString();

        when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( "test_variable_one" );
        when( visitor.castStringVisit( mockedSecondExpr ) ).thenReturn( "3" );
        when( visitor.castStringVisit( mockedThirdExpr ) ).thenReturn( "ede" );

        subStringFunction.evaluate( context, visitor );
    }
}
