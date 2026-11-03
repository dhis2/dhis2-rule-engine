package org.hisp.dhis.rules

import org.hisp.dhis.rules.api.EnvironmentVariable
import org.hisp.dhis.rules.api.EnvironmentVariables
import org.hisp.dhis.rules.api.ExpressionFunctionParameter
import org.hisp.dhis.rules.api.ExpressionValueType
import org.hisp.dhis.rules.api.ItemValueType
import org.hisp.dhis.rules.api.RuleEngine
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class RuleEngineExpressionSyntaxTest {
    private val syntax = RuleEngine.getInstance().getExpressionSyntax()

    @Test
    fun shouldExposeRuleEngineFunctions() {
        val functions = syntax.functions.associateBy { it.name }

        val daysBetween = assertNotNull(functions["d2:daysBetween"])
        assertEquals(ExpressionValueType.NUMBER, daysBetween.returnType)
        assertEquals(
            listOf(
                ExpressionFunctionParameter(ExpressionValueType.DATE, isOptional = false, isVarargs = false, name = "startDate"),
                ExpressionFunctionParameter(ExpressionValueType.DATE, isOptional = false, isVarargs = false, name = "endDate"),
            ),
            daysBetween.parameters,
        )

        val validatePattern = assertNotNull(functions["d2:validatePattern"])
        assertEquals(listOf("text", "regex"), validatePattern.parameters.map { it.name })

        val ceil = assertNotNull(functions["d2:ceil"])
        assertNull(ceil.parameters.single().name)

        val concatenate = assertNotNull(functions["d2:concatenate"])
        assertTrue(concatenate.parameters.last().isVarargs)

        assertTrue(functions.containsKey("d2:hasValue"))
        assertTrue(functions.containsKey("d2:inOrgUnitGroup"))
    }

    @Test
    fun shouldNotExposeFunctionsOfOtherExpressionModes() {
        val names = syntax.functions.map { it.name }

        assertFalse(names.contains("sum"))
        assertFalse(names.contains("avg"))
    }

    @Test
    fun shouldExposeBinaryOperators() {
        val operators = syntax.binaryOperators.associateBy { it.symbol }

        val and = assertNotNull(operators["&&"])
        assertEquals(listOf("and"), and.aliases)
        assertEquals(ExpressionValueType.BOOLEAN, and.returnType)
        assertEquals(ExpressionValueType.BOOLEAN, and.operandsType)

        val add = assertNotNull(operators["+"])
        assertEquals(ExpressionValueType.NUMBER, add.returnType)
        assertEquals(ExpressionValueType.NUMBER, add.operandsType)

        val eq = assertNotNull(operators["=="])
        assertEquals(ExpressionValueType.BOOLEAN, eq.returnType)
        assertEquals(ExpressionValueType.SAME, eq.operandsType)
    }

    @Test
    fun shouldExposeUnaryOperators() {
        val operators = syntax.unaryOperators.associateBy { it.symbol }

        assertEquals(setOf("+", "-", "!"), operators.keys)
        assertEquals(listOf("not"), operators.getValue("!").aliases)
        assertEquals(ExpressionValueType.BOOLEAN, operators.getValue("!").operandsType)
    }

    @Test
    fun shouldExposeEnvironmentVariables() {
        val variables = syntax.environmentVariables

        assertEquals(EnvironmentVariables.ENV_VARIABLES.size, variables.size)
        assertTrue(variables.contains(EnvironmentVariable("current_date", ItemValueType.DATE)))
        assertTrue(variables.contains(EnvironmentVariable("event_count", ItemValueType.NUMBER)))
        assertTrue(variables.contains(EnvironmentVariable("org_unit", ItemValueType.TEXT)))
    }
}
