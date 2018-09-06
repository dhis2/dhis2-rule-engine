package org.hisp.dhis.rules.functions;

import org.hamcrest.MatcherAssert;
import org.hisp.dhis.rules.RuleVariableValue;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;

@RunWith( JUnit4.class )
public class RuleFunctionDaysBetweenTests
{
        @Rule
        public ExpectedException thrown = ExpectedException.none();

        private Map<String, RuleVariableValue> variableValues = new HashMap<>();

        @Test
        public void return_zero_if_some_date_is_not_present() {
                RuleFunction daysBetween = RuleFunctionDaysBetween.create();

                MatcherAssert.assertThat(daysBetween.evaluate(Arrays.<String>asList(null, null), variableValues, null),
                    is(("0")));
                MatcherAssert.assertThat(daysBetween.evaluate(asList(null, ""), variableValues, null), is(("0")));
                MatcherAssert.assertThat(daysBetween.evaluate(asList("", null), variableValues, null), is(("0")));
                MatcherAssert.assertThat(daysBetween.evaluate(asList("", ""), variableValues, null), is(("0")));
        }

        @Test
        public void throw_illegal_argument_exception_if_first_date_is_invalid() {
                thrown.expect(RuntimeException.class);

                RuleFunction daysBetween = RuleFunctionDaysBetween.create();

                daysBetween.evaluate(asList("bad date", "2010-01-01"), variableValues, null);
        }

        @Test
        public void throw_illegal_argument_exception_if_second_date_is_invalid() {
                thrown.expect(RuntimeException.class);

                RuleFunction daysBetween = RuleFunctionDaysBetween.create();

                daysBetween.evaluate(asList("2010-01-01", "bad date"), variableValues, null);
        }

        @Test
        public void throw_illegal_argument_exception_if_first_and_second_date_is_invalid() {
                thrown.expect(RuntimeException.class);
                RuleFunctionDaysBetween.create().evaluate(asList("bad date", "bad date"),
                    new HashMap<String, RuleVariableValue>(), null);
        }

        @Test
        public void evaluate_correct_number_of_days() {
                RuleFunction daysBetween = RuleFunctionDaysBetween.create();

                MatcherAssert.assertThat(daysBetween.evaluate(asList("2010-10-15", "2010-10-20"), variableValues, null),
                    is(("5")));
                MatcherAssert.assertThat(daysBetween.evaluate(asList("2010-09-30", "2010-10-15"), variableValues, null),
                    is(("15")));
                MatcherAssert.assertThat(daysBetween.evaluate(asList("2010-12-31", "2011-01-01"), variableValues, null),
                    is(("1")));

                MatcherAssert.assertThat(daysBetween.evaluate(asList("2010-10-20", "2010-10-15"), variableValues, null),
                    is(("-5")));
                MatcherAssert.assertThat(daysBetween.evaluate(asList("2010-10-15", "2010-09-30"), variableValues, null),
                    is(("-15")));
                MatcherAssert.assertThat(daysBetween.evaluate(asList("2011-01-01", "2010-12-31"), variableValues, null),
                    is(("-1")));
        }

        @Test
        public void throw_illegal_argument_exception_when_argument_count_is_greater_than_expected() {
                thrown.expect(IllegalArgumentException.class);
                RuleFunctionDaysBetween.create().evaluate(
                    Arrays.asList("2016-01-01", "2016-01-01", "2016-01-01"),
                    variableValues, null);
        }

        @Test
        public void throw_illegal_argument_exception_when_arguments_count_is_lower_than_expected() {
                thrown.expect(IllegalArgumentException.class);
                RuleFunctionDaysBetween.create().evaluate(Arrays.asList("2016-01-01"), variableValues, null);
        }

        @Test
        public void throw_illegal_argument_exception_when_arguments_is_null() {
                thrown.expect(IllegalArgumentException.class);
                RuleFunctionDaysBetween.create().evaluate(null, variableValues, null);
        }
}
