package org.dhis2.ruleengine
/*
 * Copyright (c) 2004-2020, University of Oslo
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
import junit.framework.TestCase.assertEquals
import org.dhis2.ruleengine.models.*
import org.junit.Assert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertTrue
import org.junit.Rule as TestRule

/**
 * @author Zubair Asghar
 */
@RunWith(JUnit4::class)
class RuleEngineGetDescriptionTest {
    private val test_var_one = "Variable_ONE"
    private val test_var_two = "Variable_TWO"
    private val test_var_three = "Variable_THREE"
    private val test_var_date_one = "2020-01-01"
    private val test_var_date_two = "2020-02-02"
    private val completionDate = "Completion date"
    private val currentDate = "Current date"
    private val constant = "PI"
    private val test_var_number = "9"
    private var itemStore: MutableMap<String, DataItem> = HashMap()
    private val ruleAction: RuleAction =
        RuleAction.DisplayKeyValuePair("", DisplayLocation.LOCATION_FEEDBACK_WIDGET, "")

    @TestRule
    var thrown = ExpectedException.none()

    @BeforeEach
    fun setUp() {
        itemStore = HashMap()
        val var_1 = DataItem(test_var_one, ItemValueType.TEXT)
        val var_2 = DataItem(test_var_two, ItemValueType.TEXT)
        val var_8 = DataItem(test_var_three, ItemValueType.TEXT)
        val var_3 = DataItem(test_var_date_one, ItemValueType.DATE)
        val var_4 = DataItem(test_var_date_two, ItemValueType.DATE)
        val var_5 = DataItem(completionDate, ItemValueType.DATE)
        val var_6 = DataItem(constant, ItemValueType.TEXT)
        val var_7 = DataItem(currentDate, ItemValueType.DATE)
        val var_9 = DataItem(test_var_number, ItemValueType.NUMBER)
        itemStore["test_var_one"] = var_1
        itemStore["test_var_two"] = var_2
        itemStore["test_var_date_one"] = var_3
        itemStore["test_var_date_two"] = var_4
        itemStore["completed_date"] = var_5
        itemStore["NAgjOfWMXg6"] = var_6
        itemStore["current_date"] = var_7
        itemStore["test_var_three"] = var_8
        itemStore["test_var_number"] = var_9
    }

    @Test
    fun evaluateGetDescriptionWithIncorrectRules() {
        val incorrectRuleHasValue = Rule(
            "", null, null, "d2:hasValue(#{test_var_one} + 1)", listOf(ruleAction), ""
        )
        val incorrectSyntaxRule = Rule(
            "", null, null, "d2:daysBetween((#{test_var_date_one},#{test_var_date_two})",
            listOf(ruleAction), ""
        )
        var ruleEngine: RuleEngine = getRuleEngineBuilderForDescription(itemStore)
        var result = ruleEngine.getExpressionDescription(incorrectRuleHasValue.condition)
        Assert.assertNotNull(result)
        Assert.assertFalse(result is RuleValidationResult.Valid)
        ruleEngine = getRuleEngineBuilderForDescription(itemStore)
        result = ruleEngine.getExpressionDescription(incorrectSyntaxRule.condition)
        Assert.assertNotNull(result)
        Assert.assertFalse(result is RuleValidationResult.Valid)
    }

    @Test
    fun evaluateGetDescriptionWithInvalidProgramRuleVariable() {
        val rule = Rule("", null, null, "d2:hasValue(#{test_var_one1})", listOf(ruleAction), "")
        val ruleEngine: RuleEngine = getRuleEngineBuilderForDescription(itemStore)
        val result = ruleEngine.getExpressionDescription(rule.condition)
        Assert.assertNotNull(result)
        Assert.assertFalse(result is RuleValidationResult.Valid)
    }

    @get:Test
    val descriptionForLengthFunction: Unit
        get() {
            var rule = Rule("", null, null, "d2:length(#{test_var_one})", listOf(ruleAction), "")
            val ruleEngine: RuleEngine = getRuleEngineBuilderForDescription(itemStore)
            var result = ruleEngine.getExpressionDescription(rule.condition)
            Assert.assertNotNull(result)
            Assert.assertTrue(result is RuleValidationResult.Valid)
            rule = Rule("", null, null, "d2:length(#{test_var_date_one})", listOf(ruleAction), "")
            result = ruleEngine.getExpressionDescription(rule.condition)
            Assert.assertNotNull(result)
            Assert.assertTrue(result is RuleValidationResult.Valid)
            rule = Rule("", null, null, "d2:length(#{test_var_number})", listOf(ruleAction), "")
            result = ruleEngine.getExpressionDescription(rule.condition)
            Assert.assertNotNull(result)
            Assert.assertTrue(result is RuleValidationResult.Valid)
        }

    @Test
    fun testGetDescriptionWithD2FunctionsAndLogicalAnd() {
        val correctMultipleD2FunctionRule = Rule(
            "",
            null, null, "d2:count(#{test_var_one}) > 0 && d2:hasValue(#{test_var_two})",
            listOf(ruleAction), ""
        )
        val ruleEngine: RuleEngine = getRuleEngineBuilderForDescription(itemStore)
        val result = ruleEngine.getExpressionDescription(correctMultipleD2FunctionRule.condition)
        Assert.assertNotNull(result)
        Assert.assertTrue(result is RuleValidationResult.Valid)
    }

    @Test
    fun testGetDescriptionWithD2FunctionsTEA() {
        val conditionWithD2FunctionsTEA =
            Rule("", null, null, "d2:hasValue('test_var_three')", listOf(ruleAction), "")
        val ruleEngine: RuleEngine = getRuleEngineBuilderForDescription(itemStore)
        val result = ruleEngine.getExpressionDescription(conditionWithD2FunctionsTEA.condition)
        Assert.assertNotNull(result)
        Assert.assertTrue(result is RuleValidationResult.Valid)
        assertEquals("Variable_THREE", (result as RuleValidationResult.Valid).description)
    }

    @Test
    fun testGetDescriptionWithPlainAttributeComparisonWithName() {
        val conditionWithD2FunctionsTEA =
            Rule("", null, null, "'test_var_three' == 'email'", listOf(ruleAction), "")
        val ruleEngine: RuleEngine = getRuleEngineBuilderForDescription(itemStore)
        val result = ruleEngine.getExpressionDescription(conditionWithD2FunctionsTEA.condition)
        Assert.assertNotNull(result)
        Assert.assertTrue(result is RuleValidationResult.Valid)
        assertEquals("'test_var_three' == 'email'", (result as RuleValidationResult.Valid).description)
    }

    @Test
    fun testGetDescriptionWithPlainAttributeComparison() {
        val conditionWithD2FunctionsTEA =
            Rule("", null, null, "A{test_var_three} == 'email'", listOf(ruleAction), "")
        val ruleEngine: RuleEngine = getRuleEngineBuilderForDescription(itemStore)
        val result = ruleEngine.getExpressionDescription(conditionWithD2FunctionsTEA.condition)
        Assert.assertNotNull(result)
        Assert.assertTrue(result is RuleValidationResult.Valid)
        assertEquals("Variable_THREE == 'email'", (result as RuleValidationResult.Valid).description)
    }

    @Test
    fun testGetDescriptionStringLiterals() {
        val condition = " true && false || 1 > 3"
        val literalStringRule = Rule("", null, null, condition, listOf(ruleAction), "")
        val ruleEngine: RuleEngine = getRuleEngineBuilderForDescription(itemStore)
        val result = ruleEngine.getExpressionDescription(literalStringRule.condition)
        Assert.assertNotNull(result)
        Assert.assertTrue(result is RuleValidationResult.Valid)
    }

    @Test
    fun testGetDescriptionD2BetweenFunction() {
        val condition = "d2:daysBetween(#{test_var_date_one},#{test_var_date_two}) > 0"
        val correctD2betweenFunctionRule = Rule("", null, null, condition, listOf(ruleAction), "")
        val ruleEngine: RuleEngine = getRuleEngineBuilderForDescription(itemStore)
        val result = ruleEngine.getExpressionDescription(correctD2betweenFunctionRule.condition)
        Assert.assertNotNull(result)
        Assert.assertTrue(result is RuleValidationResult.Valid)
    }

    @Test
    fun testGetDescriptionD2BetweenFunctionWithEnvironmentVariables() {
        val condition = "d2:daysBetween(V{completed_date},V{current_date}) > 0"
        val correctD2betweenFunctionRule = Rule("", null, null, condition, listOf(ruleAction), "")
        val ruleEngine: RuleEngine = getRuleEngineBuilderForDescription(itemStore)
        val result = ruleEngine.getExpressionDescription(correctD2betweenFunctionRule.condition)
        Assert.assertNotNull(result)
        Assert.assertTrue(result is RuleValidationResult.Valid)
    }

    @Test
    fun testGetDescriptionD2FunctionAttribute() {
        val condition = "A{test_var_one} > 0"
        val withoutD2AttFunctionRule = Rule("", null, null, condition, listOf(ruleAction), "")
        val ruleEngine: RuleEngine = getRuleEngineBuilderForDescription(itemStore)
        val result = ruleEngine.getExpressionDescription(withoutD2AttFunctionRule.condition)
        Assert.assertNotNull(result)
        Assert.assertTrue(result is RuleValidationResult.Valid)
    }

    @Test
    fun testGetDescriptionWithD2FunctionDataElement() {
        val condition = "#{test_var_one} > 0"
        val withoutD2DEFunctionRule = Rule("", null, null, condition, listOf(ruleAction), "")
        val ruleEngine: RuleEngine = getRuleEngineBuilderForDescription(itemStore)
        val result = ruleEngine.getExpressionDescription(withoutD2DEFunctionRule.condition)
        Assert.assertNotNull(result)
        Assert.assertTrue(result is RuleValidationResult.Valid)
    }

    @Test
    fun testGetDescriptionWithConstant() {
        val condition = "C{NAgjOfWMXg6} == 0"
        val constantRule = Rule("", null, null, condition, listOf(ruleAction), "")
        val ruleEngine: RuleEngine = getRuleEngineBuilderForDescription(itemStore)
        val result = ruleEngine.getExpressionDescription(constantRule.condition)
        Assert.assertNotNull(result)
        Assert.assertTrue(result is RuleValidationResult.Valid)
    }

    @Test
    fun testGetDescriptionWithProgramEnvironmentVariable() {
        val condition = "d2:hasValue(V{completed_date})"
        val programEnvVariableRule = Rule("", null, null, condition, listOf(ruleAction), "")
        val ruleEngine: RuleEngine = getRuleEngineBuilderForDescription(itemStore)
        val result = ruleEngine.getExpressionDescription(programEnvVariableRule.condition)
        Assert.assertNotNull(result)
        Assert.assertTrue(result is RuleValidationResult.Valid)
    }

    @Test
    fun testGetDescriptionWithSingleD2Function() {
        val condition = "d2:hasValue(#{test_var_one})"
        val correctRuleHasValue = Rule("", null, null, condition, listOf(ruleAction), "")
        val ruleEngine: RuleEngine = getRuleEngineBuilderForDescription(itemStore)
        val result = ruleEngine.getExpressionDescription(correctRuleHasValue.condition)
        Assert.assertNotNull(result)
        Assert.assertTrue(result is RuleValidationResult.Valid)
    }

    @Test
    fun testGetDescriptionWithMultipleD2FunctionsAndLogicalOROperator() {
        val condition = "d2:hasValue(#{test_var_two}) || d2:count(#{test_var_one}) > 0 "
        val correctMultipleD2FunctionRuleWithOr = Rule("", null, null, condition, listOf(ruleAction), "")
        val ruleEngine: RuleEngine = getRuleEngineBuilderForDescription(itemStore)
        val result = ruleEngine.getExpressionDescription(correctMultipleD2FunctionRuleWithOr.condition)
        Assert.assertNotNull(result)
        Assert.assertTrue(result is RuleValidationResult.Valid)
    }

    @Test
    fun testGetDescriptionWithMultipleD2FunctionsAndLogicalANDOperator() {
        val condition = "d2:hasValue(#{test_var_two}) && d2:count(#{test_var_one}) > 0 "
        val correctMultipleD2FunctionRuleWithAnd = Rule("", null, null, condition, listOf(ruleAction), "")
        val ruleEngine: RuleEngine = getRuleEngineBuilderForDescription(itemStore)
        val result = ruleEngine.getExpressionDescription(correctMultipleD2FunctionRuleWithAnd.condition)
        Assert.assertNotNull(result)
        Assert.assertTrue(result is RuleValidationResult.Valid)
    }

    @Test
    fun testGetDescriptionForDataFieldExpression() {
        val ruleEngine: RuleEngine = getRuleEngineBuilderForDescription(itemStore)
        var result = ruleEngine.getExpressionDescription("1 + 1")
        Assert.assertNotNull(result)
        Assert.assertTrue(result is RuleValidationResult.Valid)
        result =
            ruleEngine.getExpressionDescription("d2:hasValue(#{test_var_two}) && d2:count(#{test_var_one}) > 0 ")
        Assert.assertNotNull(result)
        Assert.assertTrue(result is RuleValidationResult.Valid)
        result = ruleEngine.getExpressionDescription("1 + 1 +")
        Assert.assertNotNull(result)
        Assert.assertFalse(result is RuleValidationResult.Valid)
        assertTrue { (result as RuleValidationResult.Error).exception is java.lang.IllegalStateException }
        result =
            ruleEngine.getExpressionDescription("d2:hasValue(#{test_var_two}) && d2:count(#{test_var_one}) > 0 (")
        Assert.assertNotNull(result)
        Assert.assertFalse(result is RuleValidationResult.Valid)
        assertTrue { (result as RuleValidationResult.Error).exception is java.lang.IllegalStateException }
    }

    private fun getRuleEngineBuilderForDescription(itemStore: Map<String, DataItem>): RuleEngine {
        return RuleEngineContext(
            ruleEngineIntent = RuleEngineIntent.DESCRIPTION,
            rules = emptyList(),
            ruleVariables = emptyList(),
            supplementaryData = emptyMap(),
            constantsValues = emptyMap(),
            dataItemStore = itemStore
        ).toRuleEngine(emptyList(), null)
    }
}