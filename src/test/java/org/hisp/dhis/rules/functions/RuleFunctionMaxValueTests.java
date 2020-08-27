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
import org.hisp.dhis.rules.RuleVariableValue;
import org.hisp.dhis.rules.RuleVariableValueBuilder;
import org.hisp.dhis.rules.parser.expression.CommonExpressionVisitor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;

/**
 * @author Zubair Asghar.
 */
@RunWith( MockitoJUnitRunner.class )
public class RuleFunctionMaxValueTests
{
    @Mock
    private ExpressionParser.ExprContext context;

    @Mock
    private CommonExpressionVisitor visitor;

    @Mock
    private ExpressionParser.ProgramRuleVariableNameContext mockedVariableName;

    private RuleFunctionMaxValue functionToTest = new RuleFunctionMaxValue();

    @Before
    public void setUp()
    {
        when( context.programRuleVariableName() ).thenReturn( mockedVariableName );
    }

    @Test
    public void return_Max_Value()
    {
        String variableNameOne = "test_variable_one";
        String value = "5.0";

        Map<String, RuleVariableValue> variableValues = new HashMap<>();
        variableValues.put( variableNameOne, RuleVariableValueBuilder.create()
            .withValue( value )
            .withCandidates( Arrays.asList( value, "6", "7" ) )
            .withEventDate( new Date().toString() )
            .build() );

        assertMaxValue( variableNameOne, variableValues, "7.0" );
    }

    @Test
    public void return_Empty_String_If_Value_Absent()
    {
        String variableNameOne = "test_variable_one";

        assertMaxValue( variableNameOne, new HashMap<String, RuleVariableValue>(), "" );
    }

    private void assertMaxValue( String value, Map<String, RuleVariableValue> valueMap, String maxValue )
    {
        when( mockedVariableName.getText() ).thenReturn( value );
        when( visitor.getValueMap() ).thenReturn( valueMap );
        MatcherAssert.assertThat( functionToTest.evaluate( context, visitor ), CoreMatchers.<Object>is( (maxValue) ) );
    }
}
