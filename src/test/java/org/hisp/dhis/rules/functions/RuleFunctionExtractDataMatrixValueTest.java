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

import static org.mockito.Mockito.when;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.hisp.dhis.parser.expression.antlr.ExpressionParser;
import org.hisp.dhis.rules.RuleVariableValue;
import org.hisp.dhis.rules.RuleVariableValueBuilder;
import org.hisp.dhis.rules.parser.expression.CommonExpressionVisitor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith( MockitoJUnitRunner.class )
public class RuleFunctionExtractDataMatrixValueTest
{
    @Mock
    private ExpressionParser.ExprContext context;

    @Mock
    private CommonExpressionVisitor visitor;

    @Mock
    private ExpressionParser.ExprContext mockedFirstExpr;

    private RuleFunctionExtractDataMatrixValue functionToTest = new RuleFunctionExtractDataMatrixValue();

    @Before
    public void setUp()
    {
        when( context.expr( 0 ) ).thenReturn( mockedFirstExpr );
    }

    @Test
    public void return_empty_when_value_is_null()
    {
        testValues( null, "serial number" );
        MatcherAssert.assertThat( functionToTest.evaluate( context, visitor ), CoreMatchers.is( "" ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void throw_argument_exception_if_value_is_not_gs1()
    {
        testValues( "testingvalue", "serial number" );
        functionToTest.evaluate( context, visitor );
    }

    @Test( expected = IllegalArgumentException.class )
    public void throw_argument_exception_if_key_is_not_valid()
    {
        String testValue = "]d2\u001D01084700069915412110081996195256\u001D10DXB2005\u001D17220228";
        String gs1Key = "serial numb";
        testValues( testValue, gs1Key );
        functionToTest.evaluate( context, visitor );
    }

    @Test
    public void return_gs1_value_if_named_key_is_in_value()
    {
        String testValue = "]d2\u001D01084700069915412110081996195256\u001D10DXB2005\u001D17220228";
        String gs1Key = "serial number";
        testValues( testValue, gs1Key );
        MatcherAssert.assertThat( functionToTest.evaluate( context, visitor ), CoreMatchers.is( "10081996195256" ) );
    }

    @Test
    public void return_gs1_value_if_numeric_key_is_in_value()
    {
        String testValue = "]d2\u001D01084700069915412110081996195256\u001D10DXB2005\u001D17220228";
        String gs1Key = "21";
        testValues( testValue, gs1Key );
        MatcherAssert.assertThat( functionToTest.evaluate( context, visitor ), CoreMatchers.is( "10081996195256" ) );
    }

    @Test
    public void return_gs1_value_if_key_is_in_value2()
    {
        String testValue = "]d201084700069915412110081996195256\u001D10DXB2005\u001D17220228";
        String gs1Key = "serial number";
        testValues( testValue, gs1Key );
        MatcherAssert.assertThat( functionToTest.evaluate( context, visitor ), CoreMatchers.is( "10081996195256" ) );
    }

    @Test
    public void return_null_if_key_is_not_in_value()
    {
        String testValue = "]d2\u001D01084700069915412110081996195256\u001D10DXB2005\u001D17220228";
        String gs1Key = "production date";
        testValues( testValue, gs1Key );
        MatcherAssert.assertThat( functionToTest.evaluate( context, visitor ), CoreMatchers.nullValue() );
    }

    private Map<String, RuleVariableValue> givenAVariableValue( String variableName, String value )
    {
        Map<String, RuleVariableValue> variableValues = new HashMap<>();

        variableValues.put( variableName,
                new RuleVariableValueBuilder()
                        .withValue( value )
                        .build() );

        return variableValues;
    }

    private void testValues( String value, String gs1Key )
    {
        when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( gs1Key );
        when( visitor.getValueMap() ).thenReturn( givenAVariableValue( "variableName", value) );
    }
}