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

/**
 * @author Zubair Asghar.
 */

@RunWith( MockitoJUnitRunner.class )
public class RuleFunctionZScoreWFATest
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

    private RuleFunctionZScoreWFA functionToTest = new RuleFunctionZScoreWFA();

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
        assertZScore( "1", "4.8", "1", "1" );
        assertZScore( "1", "3.2", "1", "-2" );
        assertZScore( "39", "9.9", "1", "-3" );
        assertZScore( "39", "11.5", "1", "-1.80" );
    }

    @Test
    public void testZscoreBeyond3SD()
    {
        assertZScore( "1", "7.5", "1", "3.5" );
    }

    @Test
    public void testZscoreBeyondNegative3SD()
    {
        assertZScore( "1", "4.8", "1", "1" );
    }

    @Test
    public void testZscoreAboveSD0()
    {
        assertZScore( "1", "5.2", "1", "1.57" );
        assertZScore( "6", "9.5", "1", "2.15" );
        assertZScore( "1", "6.0", "1", "2.71" );
    }

    @Test
    public void testZscoreBelowSD0()
    {
        assertZScore( "1", "2.9", "1", "-2.60" );
        assertZScore( "12", "7.5", "1", "-1.44" );
        assertZScore( "1", "2.8", "1", "-2.80" );
    }

    @Test
    public void testFractionAgeParameter()
    {
        assertZScore( "1.2","2.9", "1", "-2.88" );
        assertZScore( "1.3", "5.2", "1", "1.14" );
        assertZScore( "1.4", "4.8", "1", "0.38" );
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

    private void assertZScore( String parameter, String weight, String gender, String zScore )
    {
        when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( parameter );
        when( visitor.castStringVisit( mockedSecondExpr ) ).thenReturn( weight );
        when( visitor.castStringVisit( mockedThirdExpr ) ).thenReturn( gender );
        MatcherAssert.assertThat( functionToTest.evaluate( context, visitor ), CoreMatchers.<Object>is( (zScore) ) );
    }
}
