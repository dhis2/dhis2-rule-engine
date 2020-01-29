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
import org.hisp.dhis.rules.RuleVariableValue;
import org.hisp.dhis.rules.RuleVariableValueBuilder;
import org.hisp.dhis.rules.parser.expression.CommonExpressionVisitor;
import org.junit.Before;
import org.hisp.dhis.parser.expression.antlr.ExpressionParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;

@RunWith( MockitoJUnitRunner.class )
public class RuleFunctionCountTests
{
        @Mock
        private ExpressionParser.ExprContext context;

        @Mock
        private CommonExpressionVisitor visitor;

        @Mock
        private ExpressionParser.ExprContext mockedFirstExpr;

        private RuleFunctionCount functionToTest = new RuleFunctionCount();

        @Before
        public void setUp() {
                when(context.expr(0)).thenReturn( mockedFirstExpr );
        }

        @Test
        public void return_zero_for_non_existing_variable() {
                assertCountValue( "nonexisting", givenAEmptyVariableValues(), "0");
        }

        @Test
        public void return_zero_for_variable_without_values() {
                String variableName = "non_value_var";

                assertCountValue( variableName, givenAVariableValuesAndOneWithoutValue(variableName), "0");
        }

        @Test
        public void return_size_of_values_for_variable_with_value_and_candidates() {
                String variableName = "with_value_var";

                assertCountValue(variableName, givenAVariableValuesAndOneWithTwoCandidates( variableName ),"2");
        }

        private Map<String, RuleVariableValue> givenAEmptyVariableValues()
        {
                return new HashMap<>();
        }

        private Map<String, RuleVariableValue> givenAVariableValuesAndOneWithoutValue( String variableNameWithoutValue )
        {
                Map<String, RuleVariableValue> variableValues = new HashMap<>();
                variableValues.put(variableNameWithoutValue, null);

                variableValues.put("test_variable_two",
                    RuleVariableValueBuilder.create()
                        .withValue("Value two")
                        .build());

                return variableValues;
        }

        private Map<String, RuleVariableValue> givenAVariableValuesAndOneWithTwoCandidates( String variableNameWithValueAndCandidates )
        {
                Map<String, RuleVariableValue> variableValues = new HashMap<>();
                variableValues.put("test_variable_one", null);

                variableValues.put(variableNameWithValueAndCandidates,
                    new RuleVariableValueBuilder()
                        .withValue("one")
                        .withCandidates(Arrays.asList("one", "two"))
                        .build());

                return variableValues;
        }

        private void assertCountValue( String value, Map<String, RuleVariableValue> valueMap, String countValue )
        {
                when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( value );
                when( visitor.getValueMap() ).thenReturn( valueMap );
                MatcherAssert.assertThat( functionToTest.evaluate( context, visitor ),
                    CoreMatchers.<Object>is( (countValue) ) );
        }
}
