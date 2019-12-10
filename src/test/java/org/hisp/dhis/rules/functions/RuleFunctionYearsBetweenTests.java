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

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.hamcrest.MatcherAssert;
import org.hisp.dhis.rules.RuleVariableValue;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * @Author Zubair Asghar.
 */

@RunWith( JUnit4.class )
public class RuleFunctionYearsBetweenTests
{
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Map<String, RuleVariableValue> variableValues = new HashMap<>();

    @Test
    public void return_empty_if_some_date_is_not_present()
    {
        RuleFunction yearsBetween = RuleFunctionYearsBetween.create();

        MatcherAssert.assertThat( yearsBetween.evaluate( asList( null, null ), variableValues, null ), is( ("0") ) );
        MatcherAssert.assertThat( yearsBetween.evaluate( asList( null, "" ), variableValues, null ), is( ("0") ) );
        MatcherAssert.assertThat( yearsBetween.evaluate( asList( "", null ), variableValues, null ), is( ("0") ) );
        MatcherAssert.assertThat( yearsBetween.evaluate( asList( "", "" ), variableValues, null ), is( ("0") ) );
    }

    @Test
    public void throw_illegal_argument_exception_if_first_date_is_invalid()
    {
        thrown.expect( IllegalArgumentException.class );

        RuleFunction yearsBetween = RuleFunctionYearsBetween.create();

        yearsBetween.evaluate( asList( "bad date", "2010-01-01" ), variableValues, null );
    }

    @Test
    public void throw_illegal_argument_exception_if_second_date_is_invalid()
    {
        thrown.expect( IllegalArgumentException.class );

        RuleFunction yearsBetween = RuleFunctionYearsBetween.create();

        yearsBetween.evaluate( asList( "2010-01-01", "bad date" ), variableValues, null );
    }

    @Test
    public void throw_illegal_argument_exception_if_first_and_second_date_is_invalid()
    {
        thrown.expect( RuntimeException.class );
        RuleFunctionYearsBetween.create().evaluate( asList( "bad date", "bad date" ), new HashMap<>(), null );
    }

    @Test
    public void return_difference_of_years_of_two_dates()
    {
        RuleFunction yearsBetween = RuleFunctionYearsBetween.create();

        MatcherAssert.assertThat( yearsBetween.evaluate( asList( "2010-10-15", "2010-10-22" ), variableValues, null ),
            is( "0" ) );
        MatcherAssert.assertThat( yearsBetween.evaluate( asList( "2010-09-30", "2011-10-31" ), variableValues, null ),
            is( "1" ) );
        MatcherAssert.assertThat( yearsBetween.evaluate( asList( "2015-01-01", "2016-06-30" ), variableValues, null ),
            is( "1" ) );
        MatcherAssert.assertThat( yearsBetween.evaluate( asList( "2010-01-01", "2016-06-30" ), variableValues, null ),
            is( "6" ) );

        MatcherAssert.assertThat( yearsBetween.evaluate( asList( "2010-10-22", "2010-10-15" ), variableValues, null ),
            is( "0" ) );
        MatcherAssert.assertThat( yearsBetween.evaluate( asList( "2011-10-31", "2010-09-30" ), variableValues, null ),
            is( "-1" ) );
        MatcherAssert.assertThat( yearsBetween.evaluate( asList( "2016-06-30", "2015-01-01" ), variableValues, null ),
            is( "-1" ) );
        MatcherAssert.assertThat( yearsBetween.evaluate( asList( "2016-06-30", "2010-01-01" ), variableValues, null ),
            is( "-6" ) );

        MatcherAssert.assertThat( yearsBetween.evaluate( asList( "2017-02-27", "2018-02-26" ), variableValues, null ),
            is( "0" ) );
        MatcherAssert.assertThat( yearsBetween.evaluate( asList( "2017-02-27", "2018-02-27" ), variableValues, null ),
            is( "1" ) );
        MatcherAssert.assertThat( yearsBetween.evaluate( asList( "2017-02-27", "2018-02-28" ), variableValues, null ),
            is( "1" ) );
        MatcherAssert.assertThat( yearsBetween.evaluate( asList( "2015-02-27", "2018-02-27" ), variableValues, null ),
            is( "3" ) );
        MatcherAssert.assertThat( yearsBetween.evaluate( asList( "2018-06-04", "2019-01-04" ), variableValues, null ),
            is( "0" ) );
        MatcherAssert.assertThat( yearsBetween.evaluate( asList( "2019-10-10", "1995-11-02" ), variableValues, null ),
            is( "-23" ) );
        MatcherAssert.assertThat( yearsBetween.evaluate( asList( "1995-11-02", "2019-10-10" ), variableValues, null ),
            is( "23" ) );
    }

    @Test
    public void throw_illegal_argument_exception_when_argument_count_is_greater_than_expected()
    {
        thrown.expect( IllegalArgumentException.class );
        RuleFunctionYearsBetween.create().evaluate( Arrays.asList( "2016-01-01", "2016-01-01", "2016-01-01" ),
            variableValues, null );
    }

    @Test
    public void throw_illegal_argument_exception_when_arguments_count_is_lower_than_expected()
    {
        thrown.expect( IllegalArgumentException.class );
        RuleFunctionYearsBetween.create().evaluate( Arrays.asList( "2016-01-01" ), variableValues, null );
    }

    @Test
    public void throw_null_pointer_exception_when_arguments_is_null()
    {
        thrown.expect( NullPointerException.class );
        RuleFunctionYearsBetween.create().evaluate( null, variableValues, null );
    }
}
