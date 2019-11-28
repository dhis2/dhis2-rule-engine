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
import org.hisp.dhis.parser.expression.ParserExceptionWithoutContext;
import org.hisp.dhis.rules.RuleVariableValue;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.internal.matchers.Null;

import java.util.*;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;

/**
 * @Author Zubair Asghar.
 */

@RunWith( JUnit4.class )
public class RuleFunctionLeftTests
{
        @Rule
        public ExpectedException thrown = ExpectedException.none();

        private Map<String, RuleVariableValue> variableValues = new HashMap<>();

        @Test
        public void return_empty_string_for_null_input()
        {
                RuleFunction leftFunction = RuleFunctionLeft.create();

                MatcherAssert
                    .assertThat( leftFunction.evaluate( Arrays.asList( null, "0" ), variableValues, null ), is( "" ) );
                MatcherAssert
                    .assertThat( leftFunction.evaluate( Arrays.asList( null, "10" ), variableValues, null ), is( "" ) );
                MatcherAssert
                    .assertThat( leftFunction.evaluate( Arrays.asList( null, "-10" ), variableValues, null ), is( "" ) );
        }

        @Test
        public void return_substring_of_first_argument_from_the_beginning()
        {
                RuleFunction leftFunction = RuleFunctionLeft.create();

                MatcherAssert.assertThat( leftFunction.evaluate(
                    Arrays.asList( "abcdef", "0" ), variableValues, null ), is( "" ) );

                MatcherAssert.assertThat( leftFunction.evaluate(
                    Arrays.asList( "abcdef", "-5" ), variableValues, null ), is( "a" ) );

                MatcherAssert.assertThat( leftFunction.evaluate(
                    Arrays.asList( "abcdef", "2" ), variableValues, null ), is( "ab" ) );

                MatcherAssert.assertThat( leftFunction.evaluate(
                    Arrays.asList( "abcdef", "30" ), variableValues, null ), is( "abcdef" ) );
        }

        @Test
        public void throw_parser_exception_without_context_if_position_is_a_text()
        {
                thrown.expect( ParserExceptionWithoutContext.class );
                RuleFunction ruleFunction = RuleFunctionLeft.create();

                ruleFunction.evaluate( asList( "test_variable_one", "text" ), variableValues, null );
        }

        @Test
        public void throw_illegal_argument_exception_if_first_parameter_is_empty_list()
        {
                thrown.expect( IllegalArgumentException.class );
                RuleFunction leftFunction = RuleFunctionLeft.create();

                leftFunction.evaluate( new ArrayList<>(), variableValues, null );
        }

        @Test
        public void throw_illegal_argument_exception_when_argument_count_is_greater_than_expected()
        {
                thrown.expect( IllegalArgumentException.class );
                RuleFunctionLeft.create().evaluate(
                    asList( "cdcdcd", "2", "2016-01-01" ), variableValues, null );
        }

        @Test
        public void throw_illegal_argument_exception_when_arguments_count_is_lower_than_expected()
        {
                thrown.expect( IllegalArgumentException.class );
                RuleFunctionLeft.create().evaluate( asList( "cdcdcdcdc" ), variableValues, null );
        }

        @Test
        public void throw_null_pointer_exception_when_arguments_is_null()
        {
                thrown.expect( NullPointerException.class );
                RuleFunctionLeft.create().evaluate( null, variableValues, null );
        }

        @Test
        public void throw_illegal_argument_when_number_not_an_integer()
        {
                thrown.expect( IllegalArgumentException.class );
                RuleFunctionLeft.create().evaluate( Arrays.asList( "yyyy-MM-dd", "6.8" ),
                    new HashMap<>(), null );
        }
}
