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
import org.hamcrest.MatcherAssert;
import org.hisp.dhis.parser.expression.antlr.ExpressionParser;
import org.hisp.dhis.rules.parser.expression.CommonExpressionVisitor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.when;


@RunWith( MockitoJUnitRunner.class )
public class RuleFunctionZScoreHFATest
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

    private RuleFunctionZScoreHFA functionToTest = new RuleFunctionZScoreHFA();

    @Before
    public void setUp()
    {
        when( context.expr( 0 ) ).thenReturn( mockedFirstExpr );
        when( context.expr( 1 ) ).thenReturn( mockedSecondExpr );
        when( context.expr( 2 ) ).thenReturn( mockedThirdExpr );
    }

    @Test
    public void testZscoreAtExactSDValue()
    {
        assertZScore( "1", "55.6", "1", "1" );
        assertZScore( "1", "49.8", "1", "-2" );
        assertZScore( "39", "85.3", "1", "-3" );
        assertZScore( "39", "89.9", "1", "-1.82" );
    }

    @Test
    public void testZscoreBeyond3SD()
    {
        assertZScore( "1", "60", "1", "3.5" );
    }

    @Test
    public void testZscoreBeyondNegative3SD()
    {
        assertZScore( "1", "43", "1", "-3.5" );
    }

    @Test
    public void testZscoreAboveSD0()
    {
        assertZScore( "1", "55.7", "1", "1.05" );
        assertZScore( "6", "68.5", "1", "1.22" );
        assertZScore( "1", "58.0", "1", "2.21" );
    }

    @Test
    public void testZscoreBelowSD0()
    {
        assertZScore( "1", "50.9", "1", "-1.42" );
        assertZScore( "12", "70.1", "1", "-1.52" );
        assertZScore( "1", "48.22", "1", "-2.79" );
    }

    @Test
    public void testFractionAgeParameter()
    {
        assertZScore( "1.2","49.23", "1", "-2.28" );
        assertZScore( "1.3", "55.92", "1", "1.16" );
        assertZScore( "1.4", "54.43", "1", "0.38" );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testExceptionIfInvalidArgument()
    {
        assertZScore( "1", "2.9", null, "0" );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testExceptionWeightIsInvalid()
    {
        assertZScore( "1", "abc", "1", "2.40" );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testExceptionIfParameterOutOfRange()
    {
        assertZScore("61", "52.9", "1" , "-2");
    }

    private void assertZScore( String parameter, String weight, String gender, String zScore )
    {
        when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( parameter );
        when( visitor.castStringVisit( mockedSecondExpr ) ).thenReturn( weight );
        when( visitor.castStringVisit( mockedThirdExpr ) ).thenReturn( gender );
        MatcherAssert.assertThat( functionToTest.evaluate( context, visitor ), CoreMatchers.<Object>is( (zScore) ) );
    }
}
