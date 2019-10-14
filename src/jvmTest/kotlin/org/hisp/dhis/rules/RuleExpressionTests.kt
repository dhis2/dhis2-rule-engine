package org.hisp.dhis.rules

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertFailsWith

@RunWith(JUnit4::class)
class RuleExpressionTests {

    @Test
    fun fromShouldReturnExpressionWithDataElementVariables() {
        val expression = "#{test_variable_one} <0 && #{test_variable_two} == ''"

        val ruleExpression = RuleExpression.from(expression)
        assertThat(ruleExpression.variable.size).isEqualTo(2)
        assertThat(ruleExpression.variable).contains("#{test_variable_one}")
        assertThat(ruleExpression.variable).contains("#{test_variable_two}")
        assertThat(ruleExpression.functions.size).isEqualTo(0)
    }

    @Test
    fun fromShouldReturnExpressionWhenVariableNameHasSpaces() {
        val expression = "#{test_variable one} <0 && #{test variable two} == ''"

        val ruleExpression = RuleExpression.from(expression)
        assertThat(ruleExpression.variable.size).isEqualTo(2)
        assertThat(ruleExpression.variable).contains("#{test_variable one}")
        assertThat(ruleExpression.variable).contains("#{test variable two}")
        assertThat(ruleExpression.functions.size).isEqualTo(0)
    }

    @Test
    fun fromShouldReturnExpressionWithAttributeVariables() {
        val expression = "A{test_variable_one} <0 && A{test_variable_two} == ''"

        val ruleExpression = RuleExpression.from(expression)
        assertThat(ruleExpression.variable.size).isEqualTo(2)
        assertThat(ruleExpression.variable).contains("A{test_variable_one}")
        assertThat(ruleExpression.variable).contains("A{test_variable_two}")
        assertThat(ruleExpression.functions.size).isEqualTo(0)
    }

    @Test
    fun fromShouldReturnExpressionWithEnvironmentVariables() {
        val expression = "V{test_variable_one} <0 && V{test_variable_two} == ''"

        val ruleExpression = RuleExpression.from(expression)
        assertThat(ruleExpression.variable.size).isEqualTo(2)
        assertThat(ruleExpression.variable).contains("V{test_variable_one}")
        assertThat(ruleExpression.variable).contains("V{test_variable_two}")
        assertThat(ruleExpression.functions.size).isEqualTo(0)
    }

    @Test
    fun fromShouldReturnExpressionWithConstantVariables() {
        val expression = "C{test_variable_one} <0 && C{test_variable_two} == ''"

        val ruleExpression = RuleExpression.from(expression)
        assertThat(ruleExpression.variable.size).isEqualTo(2)
        assertThat(ruleExpression.variable).contains("C{test_variable_one}")
        assertThat(ruleExpression.variable).contains("C{test_variable_two}")
        assertThat(ruleExpression.functions.size).isEqualTo(0)
    }

    @Test
    fun fromShouldReturnExpressionWithAllVariables() {
        val expression = "A{test_variable_one} <0 && C{test_variable_two} == '' && " +
                "V{test_variable_three} <0 && #{test_variable_four} == ''"

        val ruleExpression = RuleExpression.from(expression)
        assertThat(ruleExpression.variable.size).isEqualTo(4)
        assertThat(ruleExpression.variable).contains("A{test_variable_one}")
        assertThat(ruleExpression.variable).contains("C{test_variable_two}")
        assertThat(ruleExpression.variable).contains("V{test_variable_three}")
        assertThat(ruleExpression.variable).contains("#{test_variable_four}")
        assertThat(ruleExpression.functions.size).isEqualTo(0)
    }

    @Test
    fun fromShouldReturnExpressionWithAllVariablesWithoutDuplicates() {
        val expression = "A{test_variable_one} <0 && C{test_variable_two} == '' && " +
                "V{test_variable_three} <0 && #{test_variable_four} == '' && " +
                "A{test_variable_one} + C{test_variable_two} == 10"

        val ruleExpression = RuleExpression.from(expression)
        assertThat(ruleExpression.variable.size).isEqualTo(4)
        assertThat(ruleExpression.variable).contains("A{test_variable_one}")
        assertThat(ruleExpression.variable).contains("C{test_variable_two}")
        assertThat(ruleExpression.variable).contains("V{test_variable_three}")
        assertThat(ruleExpression.variable).contains("#{test_variable_four}")
        assertThat(ruleExpression.functions.size).isEqualTo(0)
    }

    @Test
    fun fromShouldIgnoreEmptyVariables() {
        val expression = "#{} && A{} && V{}"

        val ruleExpression = RuleExpression.from(expression)
        assertThat(ruleExpression.variable.size).isEqualTo(0)
    }

    @Test
    fun fromShouldPropagateExpressionToTheModel() {
        val ruleExpression = RuleExpression.from("test_expression")
        assertThat(ruleExpression.expression).isEqualTo("test_expression")
    }

    @Test
    fun fromShouldThrowOnNullExpression() {
        assertFailsWith<NullPointerException> {
            RuleExpression.from(null)
        }
    }

    @Test
    fun fromShouldReturnExpressionWithImmutableVariables() {
        val ruleExpression = RuleExpression.from("")

        //ruleExpression.variable.add("another_variable")

        assertThat(ruleExpression.variable.size).isEqualTo(0)

    }

    @Test
    fun fromShouldReturnExpressionWithImmutableFunctions() {
        val ruleExpression = RuleExpression.from("")

        //ruleExpression.functions.add("another_function")

        assertThat(ruleExpression.functions.size).isEqualTo(0)
    }

    @Test
    fun fromShouldReturnExpressionWithFunctions() {
        val ruleExpression = RuleExpression.from("d2:floor(16.4) + d2:ceil(8.7)")

        assertThat(ruleExpression.functions.size).isEqualTo(2)
        assertThat(ruleExpression.functions).contains("d2:floor(16.4)")
        assertThat(ruleExpression.functions).contains("d2:ceil(8.7)")
    }

    @Test
    fun fromShouldReturnExpressionWithFunctionsWithoutDuplicates() {
        val ruleExpression = RuleExpression.from("d2:floor(16.4) + " + "d2:ceil(8.7) + d2:ceil(8.7) + d2:floor(15.9)")

        assertThat(ruleExpression.functions.size).isEqualTo(3)
        assertThat(ruleExpression.functions).contains("d2:floor(16.4)")
        assertThat(ruleExpression.functions).contains("d2:ceil(8.7)")
        assertThat(ruleExpression.functions).contains("d2:floor(15.9)")
    }

    @Test
    fun fromShouldReturnExpressionWithInnerFunctionCallOnly() {
        val ruleExpression = RuleExpression.from("d2:some(1, d2:ceil(8.7)) == 9 " +
                "&& d2:hasValue(A{test_variable_one}) " +
                "&& d2:ceil(d2:floor(d2:floor(9.8))) == A{test_variable_two}")

        assertThat(ruleExpression.variable.size).isEqualTo(2)
        assertThat(ruleExpression.variable).contains("A{test_variable_one}")
        assertThat(ruleExpression.variable).contains("A{test_variable_two}")

        assertThat(ruleExpression.functions.size).isEqualTo(2)
        assertThat(ruleExpression.functions).contains("d2:ceil(8.7)")
        assertThat(ruleExpression.functions).contains("d2:floor(9.8)")
    }

    @Test
    fun fromShouldReturnExpressionWithFunctionsAndVariables() {
        val ruleExpression = RuleExpression.from("A{test_variable_one} <0 && " +
                "C{test_variable_two} == '' && (d2:floor(16.4) + d2:ceil(8.7)) == 20")

        assertThat(ruleExpression.variable.size).isEqualTo(2)
        assertThat(ruleExpression.variable).contains("A{test_variable_one}")
        assertThat(ruleExpression.variable).contains("C{test_variable_two}")

        assertThat(ruleExpression.functions.size).isEqualTo(2)
        assertThat(ruleExpression.functions).contains("d2:floor(16.4)")
        assertThat(ruleExpression.functions).contains("d2:ceil(8.7)")
    }

    @Test
    fun unwrapMustReturnVariableName() {
        assertThat(RuleExpression.unwrapVariableName("A{test_variable_one}")).isEqualTo("test_variable_one")
        assertThat(RuleExpression.unwrapVariableName("C{test_variable_two}")).isEqualTo("test_variable_two")
        assertThat(RuleExpression.unwrapVariableName("V{test_variable_three}")).isEqualTo("test_variable_three")
        assertThat(RuleExpression.unwrapVariableName("#{test_variable_four}")).isEqualTo("test_variable_four")
    }

    @Test
    fun fromMustReturnUnmodifiedStringIfNoVariablesOrFunctions() {
        val ruleExpression = RuleExpression.from("'test_expression'")
        assertThat(ruleExpression.expression).isEqualTo("'test_expression'")
    }

    @Test
    fun fromMustReturnFunctionCallWithParameter() {
        val ruleExpression = RuleExpression.from("d2:hasValue('test_value')")

        assertThat(ruleExpression.expression).isEqualTo("d2:hasValue('test_value')")
        assertThat(ruleExpression.variable.size).isEqualTo(0)
        assertThat(ruleExpression.functions.size).isEqualTo(1)
        assertThat(ruleExpression.functions).contains("d2:hasValue('test_value')")
    }

    @Test
    fun mathExpressionTest() {
        val evaluator = ExpressionEvaluator()
        val ruleExpression = RuleExpression.from("1.0 / 2.0")

        assertThat(evaluator.evaluate(ruleExpression.expression)).isEqualToIgnoringCase("0.5")
    }


}
