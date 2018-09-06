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
import org.hisp.dhis.rules.RuleVariableValue;
import org.hisp.dhis.rules.RuleVariableValueBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;

@RunWith( JUnit4.class )
public class RuleFunctionCountTests
{
        @Rule
        public ExpectedException thrown = ExpectedException.none();

        private Map<String, RuleVariableValue> variableValues = new HashMap<>();

        @Test
        public void return_zero_for_non_existing_variable() {
                RuleFunction countFunction = RuleFunctionCount.create();

                variableValues = givenAEmptyVariableValues();

                MatcherAssert.assertThat(countFunction.evaluate(
                    asList("nonexisting"), variableValues, null), is("0"));
        }

        @Test
        public void return_zero_for_variable_without_values() {
                RuleFunction countFunction = RuleFunctionCount.create();

                String variableName = "non_value_var";

                variableValues = givenAVariableValuesAndOneWithoutValue(variableName);

                MatcherAssert.assertThat(countFunction.evaluate(
                    asList(variableName), variableValues, null), is("0"));
        }

        @Test
        public void return_size_of_values_for_variable_with_value_and_candidates() {
                RuleFunction countFunction = RuleFunctionCount.create();

                String variableName = "with_value_var";

                variableValues = givenAVariableValuesAndOneWithTwoCandidates(variableName);

                MatcherAssert.assertThat(countFunction.evaluate(
                    asList(variableName), variableValues, null), is("2"));
        }

        @Test
        public void throw_illegal_argument_exception_when_variable_map_is_null() {
                thrown.expect(IllegalArgumentException.class);
                RuleFunctionCount.create().evaluate(asList("variable_name"), null, null);
        }

        @Test
        public void throw_illegal_argument_exception_when_argument_count_is_greater_than_expected() {
                thrown.expect(IllegalArgumentException.class);
                RuleFunctionCount.create().evaluate(asList("variable_name", "6.8"), variableValues, null);
        }

        @Test
        public void throw_illegal_argument_exception_when_arguments_count_is_lower_than_expected()
        {
                thrown.expect(IllegalArgumentException.class);
                RuleFunctionCount.create().evaluate( new ArrayList<String>(), variableValues, null );
        }

        @Test
        public void throw_null_pointer_exception_when_arguments_is_null()
        {
                thrown.expect( NullPointerException.class );
                RuleFunctionCount.create().evaluate(null, variableValues, null);
        }

        private Map<String, RuleVariableValue> givenAEmptyVariableValues()
        {
                return new HashMap<>();
        }

        private Map<String, RuleVariableValue> givenAVariableValuesAndOneWithoutValue( String variableNameWithoutValue )
        {
                variableValues.put(variableNameWithoutValue, null);

                variableValues.put("test_variable_two",
                    RuleVariableValueBuilder.create()
                        .withValue("Value two")
                        .build());

                return variableValues;
        }

        private Map<String, RuleVariableValue> givenAVariableValuesAndOneWithTwoCandidates( String variableNameWithValueAndCandidates )
        {
                variableValues.put("test_variable_one", null);

                variableValues.put(variableNameWithValueAndCandidates,
                    RuleVariableValueBuilder.create()
                        .withValue("one")
                        .withCandidates(Arrays.asList("one", "two"))
                        .build());

                return variableValues;
        }

        private Map<String, RuleVariableValue> givenAVariableValuesAndOneWithUndefinedCandidates( String variableNameWithValueAndNonCandidates )
        {
                variableValues.put( "test_variable_one", null );

                variableValues.put(variableNameWithValueAndNonCandidates,
                    RuleVariableValueBuilder.create()
                        .withValue("one")
                        .build());

                return variableValues;
        }
}
