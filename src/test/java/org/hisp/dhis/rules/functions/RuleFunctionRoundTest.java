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

import com.google.common.collect.Lists;
import org.hamcrest.CoreMatchers;
import org.hisp.dhis.parser.expression.antlr.ExpressionParser;
import org.hisp.dhis.rules.parser.expression.CommonExpressionVisitor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

/**
 * @author Zubair Asghar.
 */

@RunWith( MockitoJUnitRunner.class )
public class RuleFunctionRoundTest
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
        when( context.expr() ).thenReturn(Lists.newArrayList( mockedFirstExpr, mockedSecondExpr ) );
    }

    @Test
    public void return_argument_rounded_up_to_nearest_whole_number()
    {
        RuleFunctionRound roundFunction = new RuleFunctionRound();

        when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( "0" );
        assertThat( roundFunction.evaluate( context, visitor ), CoreMatchers.<Object>is( "0" ) );

        when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( "0.8" );
        assertThat( roundFunction.evaluate( context, visitor ), CoreMatchers.<Object>is( "1" ) );

        when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( "0.5" );
        assertThat( roundFunction.evaluate( context, visitor ), CoreMatchers.<Object>is( "1" ) );

        when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( "0.4999" );
        assertThat( roundFunction.evaluate( context, visitor ), CoreMatchers.<Object>is( "0" ) );

        when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( "0.5001" );
        assertThat( roundFunction.evaluate( context, visitor ), CoreMatchers.<Object>is( "1" ) );

        when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( "-9.3" );
        assertThat( roundFunction.evaluate( context, visitor ), CoreMatchers.<Object>is( "-9" ) );

        when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( "-9.8" );
        assertThat( roundFunction.evaluate( context, visitor ), CoreMatchers.<Object>is( "-10" ) );
    }

    @Test
    public void return_argument_rounded_up_to_nearest_precision()
    {
        RuleFunctionRound roundFunction = new RuleFunctionRound();

        when( visitor.castStringVisit( mockedSecondExpr ) ).thenReturn( "1" );
        when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( "0" );
        assertThat( roundFunction.evaluate( context, visitor ), CoreMatchers.<Object>is( "0" ) );

        when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( "0.8" );
        assertThat( roundFunction.evaluate( context, visitor ), CoreMatchers.<Object>is( "0.8" ) );

        when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( "0.4999" );
        assertThat( roundFunction.evaluate( context, visitor ), CoreMatchers.<Object>is( "0.5" ) );

        when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( "0.45999" );
        assertThat( roundFunction.evaluate( context, visitor ), CoreMatchers.<Object>is( "0.5" ) );

        when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( "-9.333" );
        assertThat( roundFunction.evaluate( context, visitor ), CoreMatchers.<Object>is( "-9.3" ) );

        when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( "-9.888" );
        assertThat( roundFunction.evaluate( context, visitor ), CoreMatchers.<Object>is( "-9.9" ) );

        when( visitor.castStringVisit( mockedSecondExpr ) ).thenReturn( "2" );
        when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( "0" );
        assertThat( roundFunction.evaluate( context, visitor ), CoreMatchers.<Object>is( "0" ) );

        when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( "0.8" );
        assertThat( roundFunction.evaluate( context, visitor ), CoreMatchers.<Object>is( "0.8" ) );

        when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( "0.4999" );
        assertThat( roundFunction.evaluate( context, visitor ), CoreMatchers.<Object>is( "0.5" ) );

        when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( "0.45999" );
        assertThat( roundFunction.evaluate( context, visitor ), CoreMatchers.<Object>is( "0.46" ) );

        when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( "-9.333" );
        assertThat( roundFunction.evaluate( context, visitor ), CoreMatchers.<Object>is( "-9.33" ) );

        when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( "-9.888" );
        assertThat( roundFunction.evaluate( context, visitor ), CoreMatchers.<Object>is( "-9.89" ) );
    }

    @Test
    public void return_zero_when_number_is_invalid()
    {
        when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( "not a number" );

        RuleFunctionRound roundFunction = new RuleFunctionRound();

        assertThat( roundFunction.evaluate( context, visitor ), CoreMatchers.<Object>is( "0" ) );
    }
}
