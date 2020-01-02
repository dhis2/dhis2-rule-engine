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
import org.hisp.dhis.parser.expression.CommonExpressionVisitor;
import org.hisp.dhis.parser.expression.antlr.ExpressionParser;
import org.hisp.dhis.rules.RuleVariableValue;
import org.hisp.dhis.rules.RuleVariableValueBuilder;
import org.hisp.dhis.rules.models.RuleValueType;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;

@RunWith( MockitoJUnitRunner.class )
public class RuleFunctionCountIfValueTests
{
    @Mock
    private ExpressionParser.ExprContext context;

    @Mock
    private CommonExpressionVisitor visitor;

    @Mock
    private ExpressionParser.ExprContext mockedFirstExpr;

    @Mock
    private ExpressionParser.ExprContext mockedSecondExpr;

    private RuleFunctionCountIfValue functionToTest = new RuleFunctionCountIfValue();

    @Before
    public void setUp() {
        when(context.expr(0)).thenReturn( mockedFirstExpr );
        when(context.expr(1)).thenReturn( mockedSecondExpr );
    }
    @Test
    public void return_zero_for_non_existing_variable()
    {
        assertCountIfValue( "non existing variable", "value", givenAEmptyVariableValues(),  "0" );
    }

    @Test
    public void return_zero_for_empty_value_to_compare()
    {
        assertCountIfValue( "var1", null, givenAEmptyVariableValues(), "0" );

        assertCountIfValue("var1", "", givenAEmptyVariableValues(),  "0" );
    }

    @Test
    public void return_zero_for_variable_without_values()
    {
        String variableName = "non_value_var";

        assertCountIfValue( variableName, "valueToCompare", givenAVariableValuesAndOneWithoutValue( variableName ), "0" );
    }

    @Test
    public void return_size_of_matched_values_for_variable_with_value_and_candidates()
    {
        String variableName = "with_value_var";
        String value = "valueA";

        Map<String, RuleVariableValue> variableValues = givenAVariableValuesAndOneWithTwoExpectedCountCandidates(
            variableName, value );

        assertCountIfValue( variableName, value , variableValues, "2" );
    }

    @Test
    public void return_zero_for_variable_with_no_matched_value_and_candidates()
    {
        String variableName = "with_value_var";
        String value = "valueA";

        Map<String, RuleVariableValue> variableValues = givenAVariableValuesAndOneWithTwoExpectedCountCandidates(
            variableName, value );

        assertCountIfValue( variableName, "NoMatchedValue", variableValues, "0" );
    }

    @Test
    public void return_zero_for_no_matched_variable_with_value_and_without_candidates()
    {
        String variableName = "with_value_var";
        String value = "valueA";

        Map<String, RuleVariableValue> variableValues = givenAVariableValuesAndOneWithUndefinedCandidates(
            variableName, value );

        assertCountIfValue( variableName, "NoMatchedValue" , variableValues, "0" );
    }

    @Test
    public void support_numeric_values_in_expression_for_booleans()
    {
        String variableName = "boolean_variable";
        Map<String, RuleVariableValue> variableValues = givenVariableValueWithBooleanValues( variableName );

        assertCountIfValue( "boolean_variable", "1", variableValues,"2" );

        assertCountIfValue( "boolean_variable", "0", variableValues,"1" );
    }

    private Map<String, RuleVariableValue> givenAEmptyVariableValues()
    {
        return new HashMap<>();
    }

    private Map<String, RuleVariableValue> givenAVariableValuesAndOneWithoutValue( String variableNameWithoutValue )
    {
        Map<String, RuleVariableValue> variableValues = new HashMap<>();
        variableValues.put( variableNameWithoutValue, null );

        variableValues.put( "test_variable_two", RuleVariableValueBuilder.create().withValue( "Value two" ).build() );

        return variableValues;
    }

    private Map<String, RuleVariableValue> givenAVariableValuesAndOneWithTwoExpectedCountCandidates(
        String variableNameWithValueAndCandidates, String valueToCompare )
    {
        Map<String, RuleVariableValue> variableValues = new HashMap<>();
        variableValues.put( "test_variable_one", null );

        variableValues.put( variableNameWithValueAndCandidates,
            RuleVariableValueBuilder.create().withValue( valueToCompare )
                .withCandidates( Arrays.asList( "one", valueToCompare, valueToCompare ) ).build() );

        return variableValues;
    }

    private Map<String, RuleVariableValue> givenAVariableValuesAndOneWithUndefinedCandidates(
        String variableNameWithValueAndNonCandidates, String valueToCompare )
    {
        Map<String, RuleVariableValue> variableValues = new HashMap<>();
        variableValues.put( "test_variable_one", null );

        variableValues.put( variableNameWithValueAndNonCandidates,
            RuleVariableValueBuilder.create().withValue( valueToCompare ).build() );

        return variableValues;
    }

    private Map<String, RuleVariableValue> givenVariableValueWithBooleanValues(
        String variableNameWithValueAndNonCandidates )
    {
        Map<String, RuleVariableValue> variableValues = new HashMap<>();
        variableValues.put( variableNameWithValueAndNonCandidates,
            RuleVariableValueBuilder.create().withType( RuleValueType.BOOLEAN ).withValue( "true" )
                .withCandidates( Arrays.asList( "false", "true", "true" ) ).build() );

        return variableValues;
    }

    private void assertCountIfValue( String variableName, String valueToFind, Map<String, RuleVariableValue> valueMap, String countIfValue )
    {
        when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( variableName );
        when( visitor.castStringVisit( mockedSecondExpr ) ).thenReturn( valueToFind );
        when( visitor.getValueMap() ).thenReturn( valueMap );
        MatcherAssert.assertThat( functionToTest.evaluate( context, visitor ), CoreMatchers.is( (countIfValue) ) );
    }
}
