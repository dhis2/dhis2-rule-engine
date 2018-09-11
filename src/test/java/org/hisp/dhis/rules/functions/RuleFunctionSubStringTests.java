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

/**
 * @Author Zubair Asghar.
 */

@RunWith( JUnit4.class )
public class RuleFunctionSubStringTests
{
        @Rule
        public ExpectedException thrown = ExpectedException.none();

        private Map<String, RuleVariableValue> variableValues = new HashMap<>();

        @Test
        public void return_empty_string_for_null_inputs()
        {
                RuleFunction subStringFunction = RuleFunctionSubString.create();

                MatcherAssert
                    .assertThat( subStringFunction.evaluate( asList( null, "0", "0" ), variableValues, null ), is( "" ) );
                MatcherAssert
                    .assertThat( subStringFunction.evaluate( asList( null, "0", "10" ), variableValues, null ), is( "" ) );
        }

        @Test
        public void return_substring_from_start_index_to_end_index_of_input_string()
        {
                RuleFunction subStringFunction = RuleFunctionSubString.create();

                MatcherAssert.assertThat( subStringFunction.evaluate(
                    asList( "abcdef", "0", "0" ), variableValues, null ), is( "''" ) );

                MatcherAssert.assertThat( subStringFunction.evaluate(
                    asList( "abcdef", "0", "1" ), variableValues, null ), is( "'a'" ) );

                MatcherAssert.assertThat( subStringFunction.evaluate(
                    asList( "abcdef", "-10", "1" ), variableValues, null ), is( "'a'" ) );

                MatcherAssert.assertThat( subStringFunction.evaluate(
                    asList( "abcdef", "2", "4" ), variableValues, null ), is( "'cd'" ) );

                MatcherAssert.assertThat( subStringFunction.evaluate(
                    asList( "abcdef", "2", "10" ), variableValues, null ), is( "'cdef'" ) );
        }

        @Test
        public void throw_illegal_argument_exception_if_start_index_is_a_text()
        {
                thrown.expect( IllegalArgumentException.class );

                RuleFunctionSubString.create().evaluate(
                    Arrays.asList( "test_variable_one", "variable", "3" ), variableValues, null );
        }

        @Test
        public void throw_illegal_argument_exception_if_end_index_is_a_text()
        {
                thrown.expect( IllegalArgumentException.class );

                RuleFunctionSubString.create().evaluate(
                    Arrays.asList( "test_variable_one", "3", "ede" ), variableValues, null );
        }

        @Test
        public void throw_illegal_argument_exception_if_first_parameter_is_empty_list()
        {
                thrown.expect( IllegalArgumentException.class );
                RuleFunctionSubString.create().evaluate( new ArrayList<>(), variableValues, null );
        }

        @Test
        public void throw_illegal_argument_exception_when_argument_count_is_greater_than_expected()
        {
                thrown.expect( IllegalArgumentException.class );
                RuleFunctionSubString.create().evaluate(
                    asList( "test_variable_one", "1", "2", "4" ), variableValues, null );
        }

        @Test
        public void throw_illegal_argument_exception_when_argument_count_is_lower_than_expected()
        {
                thrown.expect( IllegalArgumentException.class );
                RuleFunctionSubString.create().evaluate(
                    asList( "test_variable_one", "0" ), variableValues, null );
        }

        @Test
        public void throw_null_pointer_exception_when_arguments_is_null()
        {
                thrown.expect( NullPointerException.class );
                RuleFunctionSubString.create().evaluate( null, variableValues, null );
        }
}
