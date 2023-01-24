package org.dhis2.ruleengine.exprk

import org.dhis2.ruleengine.RuleVariableValue
import org.dhis2.ruleengine.models.RuleValueType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FunctionTest {
    @Test
    fun testVariableInsideFunction() {
        val result = Expressions()
            .withValueMap(
                mapOf(
                    "test_variable" to RuleVariableValue("123", RuleValueType.NUMERIC)
                )
            ).eval("d2:hasValue(#{test_variable})")
        assertTrue { result.toBoolean() }
    }

    @Test
    fun testVariableInsideFunctionWithoutIndicator() {
        val result = Expressions()
            .withValueMap(
                mapOf(
                    "test_variable" to RuleVariableValue("123", RuleValueType.NUMERIC)
                )
            ).eval("d2:hasValue('test_variable')")
        assertTrue { result.toBoolean() }
    }

    @Test
    fun testLiteral() {
        val result = Expressions()
            .eval("'test_variable'")
        assertEquals("test_variable", result)
    }

    @Test
    fun testRoundWithLiteral() {
        val result = Expressions()
            .eval("d2:round('2.6')")
        assertEquals("3", result)
    }

    @Test
    fun testLiteralOperations() {
        val result = Expressions()
            .eval("'B' > 'A'")
        assertEquals("true", result)
    }

    @Test
    fun testVariable() {
        val result = Expressions()
            .withValueMap(
                mapOf(
                    "test_variable" to RuleVariableValue("123", RuleValueType.NUMERIC)
                )
            ).eval("#{test_variable}")
        assertTrue { result == "123" }
    }

    @Test
    fun testVariableExpression() {
        val result = Expressions()
            .withValueMap(
                mapOf(
                    "test_variable" to RuleVariableValue("1", RuleValueType.NUMERIC)
                )
            ).eval("#{test_variable} + 1")
        assertTrue { result == "2" }
    }

    @Test
    fun testTrue() {
        val result = Expressions()
            .withValueMap(
                mapOf(
                    "test_variable" to RuleVariableValue("123", RuleValueType.NUMERIC)
                )
            ).eval("true")
        assertEquals("true", result)
    }

    @Test
    fun testEnvVariable() {
        val result = Expressions()
            .withValueMap(
                mapOf(
                    "test_variable" to RuleVariableValue("123", RuleValueType.NUMERIC)
                )
            ).eval("V{test_variable}")
        assertEquals("123", result)
    }

    @Test
    fun testConstantVariable() {
        val result = Expressions()
            .withValueMap(
                mapOf(
                    "test_variable" to RuleVariableValue("123", RuleValueType.NUMERIC)
                )
            ).eval("C{test_variable}")
        assertEquals("123", result)
    }

    @Test
    fun testBooleanNegation() {
        val result = Expressions()
            .withValueMap(
                mapOf(
                    "test_variable" to RuleVariableValue("true", RuleValueType.BOOLEAN)
                )
            ).eval("!#{test_variable}")
        assertEquals("false", result)
    }

    @Test
    fun testBooleanNegationWithoutVariable() {
        val result = Expressions()
            .withValueMap(mapOf())
            .eval("!true")
        assertEquals("false", result)
    }
}