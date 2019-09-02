package org.hisp.dhis.rules

import kotlinx.collections.immutable.persistentSetOf
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import kotlin.test.assertFailsWith

@RunWith(JUnit4::class)
class RuleExpressionBinderTests {

    @Mock
    private lateinit var ruleExpression: RuleExpression

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        val expression = "A{test_variable_one} <0 && C{test_variable_two} == '' && " +
                "V{test_variable_three} <0 && #{test_variable_four} == '' && " +
                "d2:floor(d2:ceil(3.8)) + d2:ceil(3.8) == 6 && " +
                "d2:hasValue(4.5)"

        val variables = persistentSetOf(
                "A{test_variable_one}",
                "C{test_variable_two}",
                "V{test_variable_three}",
                "#{test_variable_four}"
        )

        val functions = persistentSetOf(
                "d2:ceil(3.8)",
                "d2:hasValue(4.5)"
        )

        `when`(ruleExpression.expression).thenReturn(expression)
        `when`(ruleExpression.variable).thenReturn(variables)
        `when`(ruleExpression.functions).thenReturn(functions)
    }

    @Test
    fun buildShouldBindValuesCorrectly() {
        val result = RuleExpressionBinder.from(ruleExpression)
                .bindVariable("A{test_variable_one}", "1")
                .bindVariable("C{test_variable_two}", "2")
                .bindVariable("V{test_variable_three}", "3")
                .bindVariable("#{test_variable_four}", "4")
                .bindFunction("d2:hasValue(4.5)", "true")
                .bindFunction("d2:ceil(3.8)", "4")
                .build()

        assertThat(result).isEqualTo("1 <0 && 2 == '' && 3 <0 && 4 == '' && " + "d2:floor(4) + 4 == 6 && true")
    }

    @Test
    fun buildShouldBindValuesForDuplicateVariableReferences() {
        val expression = "A{test_variable_one} <0 && C{test_variable_two} == '' && " +
                "V{test_variable_three} <0 && #{test_variable_four} == '' && " +
                "A{test_variable_one} + C{test_variable_two} == 10 && " +
                "d2:floor(d2:ceil(3.8)) + d2:ceil(3.8) == 6 && " +
                "d2:hasValue(4.5) && d2:hasValue(4.5)"
        `when`(ruleExpression.expression).thenReturn(expression)

        val result = RuleExpressionBinder.from(ruleExpression)
                .bindVariable("A{test_variable_one}", "1")
                .bindVariable("C{test_variable_two}", "2")
                .bindVariable("V{test_variable_three}", "3")
                .bindVariable("#{test_variable_four}", "4")
                .bindFunction("d2:hasValue(4.5)", "true")
                .bindFunction("d2:ceil(3.8)", "4")
                .build()

        assertThat(result).isEqualTo("1 <0 && 2 == '' && 3 <0 && 4 == '' && 1 + 2 == 10 && " + "d2:floor(4) + 4 == 6 && true && true")
    }

    @Test
    fun buildShouldNotFailIfNoVariablesInExpression() {
        `when`(ruleExpression.expression).thenReturn("1 < 0")
        `when`( ruleExpression.functions ).thenReturn(persistentSetOf() )
        `when`( ruleExpression.functions ).thenReturn(  persistentSetOf() )

        val expression = RuleExpressionBinder.from(ruleExpression).build()
        assertThat(expression).isEqualTo("1 < 0")
    }

    @Test
    fun bindVariableShouldThrowIfPassingIllegalVariable() {
        assertFailsWith<IllegalArgumentException> {
            RuleExpressionBinder.from(ruleExpression).bindVariable("V{test_variable_five}", "3")
        }
    }

    @Test
    fun buildMustReturnUnmodifiedStringIfNoVariablesOrFunctions() {
        val ruleExpression = RuleExpression.from("'test_expression'")
        val ruleExpressionBinder = RuleExpressionBinder.from(ruleExpression)
        assertThat(ruleExpressionBinder.build()).isEqualTo("'test_expression'")
    }
}
