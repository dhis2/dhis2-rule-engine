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

import org.hamcrest.MatcherAssert;
import org.hisp.dhis.parser.expression.CommonExpressionVisitor;
import org.hisp.dhis.parser.expression.antlr.ExpressionParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;

/**
 * @Author Zubair Asghar.
 */

@RunWith( MockitoJUnitRunner.class )
public class RuleFunctionModulusTests
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
        public void setUp() {
                when(context.expr(0)).thenReturn( mockedFirstExpr );
                when(context.expr(1)).thenReturn( mockedSecondExpr );
        }

        @Test
        public void return_argument_rounded_down_to_nearest_whole_number()
        {
                RuleFunctionModulus modulusFunction = new RuleFunctionModulus();

                when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( "0" );
                when( visitor.castStringVisit( mockedSecondExpr ) ).thenReturn( "2" );
                MatcherAssert.assertThat( modulusFunction.evaluate( context, visitor ), is( "0.0" ) );

                when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( "11" );
                when( visitor.castStringVisit( mockedSecondExpr ) ).thenReturn( "3" );
                MatcherAssert.assertThat( modulusFunction.evaluate( context, visitor ), is( "2.0" ) );

                when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( "-11" );
                when( visitor.castStringVisit( mockedSecondExpr ) ).thenReturn( "3" );
                MatcherAssert.assertThat( modulusFunction.evaluate( context, visitor ), is( "-2.0" ) );
        }

        @Test
        public void return_NaN_when_invalid_operations()
        {
                RuleFunctionModulus modulusFunction = new RuleFunctionModulus();

                when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( "2" );
                when( visitor.castStringVisit( mockedSecondExpr ) ).thenReturn( "0" );
                MatcherAssert.assertThat( modulusFunction.evaluate( context, visitor ), is( "NaN" ) );

                when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( "bad number" );
                when( visitor.castStringVisit( mockedSecondExpr ) ).thenReturn( "bad number" );
                MatcherAssert.assertThat( modulusFunction.evaluate( context, visitor ), is( "NaN" ) );

                when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( null );
                when( visitor.castStringVisit( mockedSecondExpr ) ).thenReturn( null );
                MatcherAssert.assertThat( modulusFunction.evaluate( context, visitor ), is( "NaN" ) );
        }
}
