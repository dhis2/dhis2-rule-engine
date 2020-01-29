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
import org.hisp.dhis.rules.parser.expression.CommonExpressionVisitor;
import org.junit.Before;
import org.hisp.dhis.parser.expression.antlr.ExpressionParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

/**
 * @Author Zubair Asghar.
 */

@RunWith( MockitoJUnitRunner.class )
public class RuleFunctionHasUserRoleTests
{
    @Mock
    private ExpressionParser.ExprContext context;

    @Mock
    private CommonExpressionVisitor visitor;

    @Mock
    private ExpressionParser.ExprContext mockedFirstExpr;

    private RuleFunctionHasUserRole functionToTest = new RuleFunctionHasUserRole();

    @Before
    public void setUp() {
        when(context.expr(0)).thenReturn( mockedFirstExpr );
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwExceptionWhenSupplementaryDataIsNull()
    {
        assertHasUserRole( "uid1", new HashMap<String, List<String>>(), "true" );
    }

    @Test
    public void returnTrueIfUserHasRole()
    {
        Map<String, List<String>> supplementaryData = new HashMap<>();
        supplementaryData.put( "USER", Arrays.asList( "uid1" ) );

        assertHasUserRole( "uid1", supplementaryData, "true" );
    }

    @Test
    public void returnFalseIfUserHasNoRole()
    {
        Map<String, List<String>> supplementaryData = new HashMap<>();
        supplementaryData.put( "USER", Arrays.asList( "uid1" ) );

        assertHasUserRole( "uid2", supplementaryData, "false" );
    }

    @Test
    public void returnFalseIfRoleListIsNull()
    {
        Map<String, List<String>> supplementaryData = new HashMap<>();
        supplementaryData.put( "USER", null );

        assertHasUserRole( "uid2", supplementaryData, "false" );
    }

    private void assertHasUserRole( String value, Map<String, List<String>> supplementaryData, String hasUserRole )
    {
        when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( value );
        when( visitor.getSupplementaryData() ).thenReturn( supplementaryData );
        MatcherAssert.assertThat( functionToTest.evaluate( context, visitor ),
            CoreMatchers.<Object>is( (hasUserRole) ) );
    }
}
