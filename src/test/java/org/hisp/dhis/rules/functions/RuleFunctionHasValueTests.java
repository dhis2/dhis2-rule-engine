package org.hisp.dhis.rules.functions;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.hisp.dhis.parser.expression.CommonExpressionVisitor;
import org.hisp.dhis.parser.expression.antlr.ExpressionParser;
import org.hisp.dhis.rules.RuleVariableValue;
import org.hisp.dhis.rules.RuleVariableValueBuilder;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

@RunWith( MockitoJUnitRunner.class )
public class RuleFunctionHasValueTests
{
        @Mock
        private ExpressionParser.ExprContext context;

        @Mock
        private CommonExpressionVisitor visitor;

        @Mock
        private ExpressionParser.ExprContext mockedFirstExpr;

        private RuleFunctionHasValue functionToTest = new RuleFunctionHasValue();

        @Before
        public void setUp() {
                when(context.expr(0)).thenReturn( mockedFirstExpr );
        }

        @Test
        public void return_false_for_non_existing_variable()
        {
                Map<String, RuleVariableValue>  variableValues = givenAEmptyVariableValues();

                assertHasValue("nonexisting" , variableValues, "false" );
        }

        @Test
        public void return_false_for_existing_variable_without_value()
        {
                String variableName = "non_value_var";

                Map<String, RuleVariableValue> variableValues = givenAVariableValuesAndOneWithoutValue( variableName );

                assertHasValue( variableName, variableValues,"false" );
        }

        @Test
        public void return_true_for_existing_variable_with_value()
        {
                String variableName = "with_value_var";

                Map<String, RuleVariableValue> variableValues = givenAVariableValuesAndOneWithValue( variableName );

                assertHasValue( variableName, variableValues,"true" );
        }

        private Map<String, RuleVariableValue> givenAEmptyVariableValues()
        {
                return new HashMap<>();
        }

        private Map<String, RuleVariableValue> givenAVariableValuesAndOneWithoutValue(
            String variableNameWithoutValue )
        {
                Map<String, RuleVariableValue> variableValues = new HashMap<>();

                variableValues.put( variableNameWithoutValue, null );

                variableValues.put( "test_variable_two",
                    RuleVariableValueBuilder.create()
                        .withValue( "Value two" )
                        .build() );

                return variableValues;
        }

        private Map<String, RuleVariableValue> givenAVariableValuesAndOneWithValue(
            String variableNameWithValue )
        {
                Map<String, RuleVariableValue> variableValues = new HashMap<>();

                variableValues.put( "test_variable_one", null );

                variableValues.put( variableNameWithValue,
                    RuleVariableValueBuilder.create()
                        .withValue( "Value two" )
                        .build() );

                return variableValues;
        }

        private void assertHasValue( String value, Map<String, RuleVariableValue> valueMap, String hasValue )
        {
                when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( value );
                when( visitor.getValueMap() ).thenReturn( valueMap );
                MatcherAssert.assertThat( functionToTest.evaluate( context, visitor ), CoreMatchers.is( (hasValue) ) );
        }
}
