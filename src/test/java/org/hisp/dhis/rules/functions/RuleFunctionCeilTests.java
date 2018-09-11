package org.hisp.dhis.rules.functions;

import org.hamcrest.MatcherAssert;
import org.hisp.dhis.rules.RuleVariableValue;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;

@RunWith( JUnit4.class )
public class RuleFunctionCeilTests
{

        @Rule
        public ExpectedException thrown = ExpectedException.none();

        private Map<String, RuleVariableValue> variableValues = new HashMap<>();

        @Test
        public void evaluateMustReturnCeiledValue()
        {
                RuleFunction ceil = RuleFunctionCeil.create();

                MatcherAssert.assertThat(ceil.evaluate(asList("0"), variableValues, null), is("0"));
                MatcherAssert.assertThat(ceil.evaluate(asList("0.8"), variableValues, null), is("1"));
                MatcherAssert.assertThat(ceil.evaluate(asList("5.1"), variableValues, null), is("6"));
                MatcherAssert.assertThat(ceil.evaluate(asList("1"), variableValues, null), is("1"));
                MatcherAssert.assertThat(ceil.evaluate(asList("-9.3"), variableValues, null), is("-9"));
                MatcherAssert.assertThat(ceil.evaluate(asList("-5.9"), variableValues, null), is("-5"));
        }

        @Test
        public void throw_illegal_argument_exception_when_argument_count_is_greater_than_expected()
        {
                thrown.expect(IllegalArgumentException.class);
                RuleFunctionCeil.create().evaluate(asList("5.9", "6.8"), variableValues, null);
        }

        @Test
        public void throw_illegal_argument_exception_when_arguments_count_is_lower_than_expected()
        {
                thrown.expect(IllegalArgumentException.class);
                RuleFunctionCeil.create().evaluate(new ArrayList<String>(), variableValues, null);
        }

        @Test
        public void throw_null_pointer_exception_when_arguments_is_null()
        {
                thrown.expect( NullPointerException.class );
                RuleFunctionCeil.create().evaluate(null, variableValues, null);
        }
}
