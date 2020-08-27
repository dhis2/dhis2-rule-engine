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
import org.hamcrest.MatcherAssert;
import org.hisp.dhis.parser.expression.antlr.ExpressionParser;
import org.hisp.dhis.rules.parser.expression.CommonExpressionVisitor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

/**
 * @author Zubair Asghar.
 */

@RunWith( MockitoJUnitRunner.class )
public class RuleFunctionConcatenateTests
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

    @Test
    public void evaluateD2Concatenate()
    {
        RuleFunctionConcatenate concatenateFunction = new RuleFunctionConcatenate();

        when( context.expr() ).thenReturn( Lists.newArrayList( mockedFirstExpr ) );
        when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( "hello" );
        MatcherAssert.assertThat( concatenateFunction.evaluate( context, visitor ),
            CoreMatchers.<Object>is( "hello" ) );

        when( context.expr() ).thenReturn( Lists.newArrayList( mockedFirstExpr, mockedSecondExpr ) );
        when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( "hello" );
        when( visitor.castStringVisit( mockedSecondExpr ) ).thenReturn( null );
        MatcherAssert.assertThat( concatenateFunction.evaluate( context, visitor ),
            CoreMatchers.<Object>is( "hello" ) );

        when( context.expr() ).thenReturn( Lists.newArrayList( mockedFirstExpr, mockedSecondExpr ) );
        when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( null );
        when( visitor.castStringVisit( mockedSecondExpr ) ).thenReturn( null );
        MatcherAssert.assertThat( concatenateFunction.evaluate( context, visitor ),
            CoreMatchers.<Object>is( "" ) );

        when( context.expr() ).thenReturn( Lists.newArrayList( mockedFirstExpr, mockedSecondExpr, mockedThirdExpr ) );
        when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( "hello" );
        when( visitor.castStringVisit( mockedSecondExpr ) ).thenReturn( " " );
        when( visitor.castStringVisit( mockedThirdExpr ) ).thenReturn( "there" );
        MatcherAssert.assertThat( concatenateFunction.evaluate( context, visitor ),
            CoreMatchers.<Object>is( "hello there" ) );
    }
}
