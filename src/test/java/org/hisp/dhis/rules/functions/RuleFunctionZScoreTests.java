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
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;

/**
 * @Author Zubair Asghar.
 */

@RunWith( JUnit4.class )
public class RuleFunctionZScoreTests
{
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testZscoreAtExactSDValue()
    {
        RuleFunction zScore = RuleFunctionZScore.create();

        MatcherAssert.assertThat( zScore.evaluate( Arrays.asList( "1", "4.8", "1" ), null, null ), is( "1" ) );
        MatcherAssert.assertThat( zScore.evaluate( Arrays.asList( "1", "3.2", "1" ), null, null ), is( "-2" ) );
        MatcherAssert.assertThat( zScore.evaluate( Arrays.asList( "39", "14.4", "1" ), null, null ), is( "0" ) );
    }

    @Test
    public void testZscoreAboveSD0()
    {
        RuleFunction zScore = RuleFunctionZScore.create();

        MatcherAssert.assertThat( zScore.evaluate( Arrays.asList( "1", "5.2", "1" ), null, null ), is( "1.57" ) );
        MatcherAssert.assertThat( zScore.evaluate( Arrays.asList( "6", "9.5", "1" ), null, null ), is( "2.15" ) );
    }

    @Test
    public void testZscoreBelowSD0()
    {
        RuleFunction zScore = RuleFunctionZScore.create();

        MatcherAssert.assertThat( zScore.evaluate( Arrays.asList( "1", "2.9", "1" ), null, null ), is( "-2.40" ) );
        MatcherAssert.assertThat( zScore.evaluate( Arrays.asList( "12", "7.5", "1" ), null, null ), is( "-1.56" ) );
    }

    @Test
    public void testExceptionIfInvalidArgument()
    {
        thrown.expect( IllegalArgumentException.class );

        RuleFunction zScore = RuleFunctionZScore.create();
        MatcherAssert.assertThat( zScore.evaluate( Arrays.asList( "1", "2.9" ), null, null ), is( "-2.40" ) );
    }

    @Test
    public void testExceptionWeightIsInvalid()
    {
        thrown.expect( IllegalArgumentException.class );
        thrown.expectMessage( "Byte parsing failed" );

        RuleFunction zScore = RuleFunctionZScore.create();
        MatcherAssert.assertThat( zScore.evaluate( Arrays.asList( "1", "abc", "1" ), null, null ), is( "-2.40" ) );

    }
}
