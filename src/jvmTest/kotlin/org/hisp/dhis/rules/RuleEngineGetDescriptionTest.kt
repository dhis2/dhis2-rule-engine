package org.hisp.dhis.rules

import org.hisp.dhis.rules.models.Rule
import org.hisp.dhis.rules.models.RuleAction
import org.hisp.dhis.rules.models.RuleActionText
import org.hisp.dhis.rules.models.RuleEngineValidationException
import kotlin.test.*

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
*/ /**
 * @author Zubair Asghar
 */
class RuleEngineGetDescriptionTest {
    private var itemStore: MutableMap<String, org.hisp.dhis.rules.DataItem> = HashMap()
    private val ruleAction: RuleAction = RuleActionText.createForFeedback(RuleActionText.Type.DISPLAYTEXT, "", "")
    @BeforeTest
    fun setUp() {
        itemStore = HashMap()
        val var_1 = org.hisp.dhis.rules.DataItem(
            org.hisp.dhis.rules.RuleEngineGetDescriptionTest.Companion.test_var_one,
            org.hisp.dhis.rules.ItemValueType.TEXT
        )
        val var_2 = org.hisp.dhis.rules.DataItem(
            org.hisp.dhis.rules.RuleEngineGetDescriptionTest.Companion.test_var_two,
            org.hisp.dhis.rules.ItemValueType.TEXT
        )
        val var_8 = org.hisp.dhis.rules.DataItem(
            org.hisp.dhis.rules.RuleEngineGetDescriptionTest.Companion.test_var_three,
            org.hisp.dhis.rules.ItemValueType.TEXT
        )
        val var_3 = org.hisp.dhis.rules.DataItem(
            org.hisp.dhis.rules.RuleEngineGetDescriptionTest.Companion.test_var_date_one,
            org.hisp.dhis.rules.ItemValueType.DATE
        )
        val var_4 = org.hisp.dhis.rules.DataItem(
            org.hisp.dhis.rules.RuleEngineGetDescriptionTest.Companion.test_var_date_two,
            org.hisp.dhis.rules.ItemValueType.DATE
        )
        val var_5 = org.hisp.dhis.rules.DataItem(
            org.hisp.dhis.rules.RuleEngineGetDescriptionTest.Companion.completionDate,
            org.hisp.dhis.rules.ItemValueType.DATE
        )
        val var_6 = org.hisp.dhis.rules.DataItem(
            org.hisp.dhis.rules.RuleEngineGetDescriptionTest.Companion.constant,
            org.hisp.dhis.rules.ItemValueType.TEXT
        )
        val var_7 = org.hisp.dhis.rules.DataItem(
            org.hisp.dhis.rules.RuleEngineGetDescriptionTest.Companion.currentDate,
            org.hisp.dhis.rules.ItemValueType.DATE
        )
        val var_9 = org.hisp.dhis.rules.DataItem(
            org.hisp.dhis.rules.RuleEngineGetDescriptionTest.Companion.test_var_number,
            org.hisp.dhis.rules.ItemValueType.NUMBER
        )
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
        val rule= Rule("d2:hasValue(#{test_var_one} + 1)", listOf(ruleAction))
        val rule1= Rule("d2:daysBetween((#{test_var_date_one},#{test_var_date_two})", listOf(ruleAction))
        var ruleEngine = getRuleEngineForDescription(itemStore)
        var result = ruleEngine.evaluate(rule.condition)
        assertNotNull(result)
        assertFalse(result.valid())
        ruleEngine = getRuleEngineForDescription(itemStore)
        result = ruleEngine.evaluate(rule1.condition)
        assertNotNull(result)
        assertFalse(result.valid())
    }

    @Test
    fun evaluateGetDescriptionWithInvalidProgramRuleVariable() {
        val rule = Rule("d2:hasValue(#{test_var_one1})", listOf(ruleAction))
        val ruleEngine = getRuleEngineForDescription(itemStore)
        val result = ruleEngine.evaluate(rule.condition)
        assertNotNull(result)
        assertFalse(result.valid())
    }

    @Test
    fun descriptionForLengthFunction() {
        var rule = Rule("d2:length(#{test_var_one}) > 0", listOf(ruleAction))
        val ruleEngine = getRuleEngineForDescription(itemStore)
        var result = ruleEngine.evaluate(rule.condition())
        assertNotNull(result)
        assertTrue(result.valid())
        rule = Rule("d2:length(#{test_var_date_one}) > 0 ", listOf(ruleAction))
        result = ruleEngine.evaluate(rule.condition())
        assertNotNull(result)
        assertFalse(result.valid())
        rule = Rule("d2:length(#{test_var_number}) > 0 ", listOf(ruleAction))
        result = ruleEngine.evaluate(rule.condition())
        assertNotNull(result)
        assertFalse(result.valid())
        }

    @Test
    fun testGetDescriptionWithD2FunctionsAndLogicalAnd() {
        val rule = Rule("d2:count(#{test_var_one}) > 0 && d2:hasValue(#{test_var_two})", listOf(ruleAction))
        val ruleEngine = getRuleEngineForDescription(itemStore)
        val result = ruleEngine.evaluate(rule.condition)
        assertNotNull(result)
        assertTrue(result.valid())
    }

    @Test
    fun testGetDescriptionWithD2FunctionsTEA() {
        val rule= Rule("d2:hasValue('test_var_three')", listOf(ruleAction))
        val ruleEngine = getRuleEngineForDescription(itemStore)
        val result = ruleEngine.evaluate(rule.condition)
        assertNotNull(result)
        assertEquals("d2:hasValue(Variable_THREE)", result.description())
        assertTrue(result.valid())
    }

    @Test
    fun testGetDescriptionWithPlainAttributeComparisonWithName() {
        val rule= Rule("'test_var_three' == 'email'", listOf(ruleAction))
        val ruleEngine = getRuleEngineForDescription(itemStore)
        val result = ruleEngine.evaluate(rule.condition)
        assertNotNull(result)
        assertEquals("'test_var_three' == 'email'", result.description())
        assertTrue(result.valid())
    }

    @Test
    fun testGetDescriptionWithPlainAttributeComparison() {
        val rule= Rule("A{test_var_three} == 'email'", listOf(ruleAction))
        val ruleEngine = getRuleEngineForDescription(itemStore)
        val result = ruleEngine.evaluate(rule.condition)
        assertNotNull(result)
        assertEquals("Variable_THREE == 'email'", result.description())
        assertTrue(result.valid())
    }

    @Test
    fun testGetDescriptionStringLiterals() {
        val condition = " true && false || 1 > 3"
        val rule= Rule(condition, listOf(ruleAction))
        val ruleEngine = getRuleEngineForDescription(itemStore)
        val result = ruleEngine.evaluate(rule.condition)
        assertNotNull(result)
        assertTrue(result.valid())
    }

    @Test
    fun testGetDescriptionD2BetweenFunction() {
        val condition = "d2:daysBetween(#{test_var_date_one},#{test_var_date_two}) > 0"
        val rule= Rule(condition, listOf(ruleAction))
        val ruleEngine = getRuleEngineForDescription(itemStore)
        val result = ruleEngine.evaluate(rule.condition)
        assertNotNull(result)
        assertTrue(result.valid())
    }

    @Test
    fun testGetDescriptionD2BetweenFunctionWithEnvironmentVariables() {
        val condition = "d2:daysBetween(V{completed_date},V{current_date}) > 0"
        val rule= Rule(condition, listOf(ruleAction))
        val ruleEngine = getRuleEngineForDescription(itemStore)
        val result = ruleEngine.evaluate(rule.condition)
        assertNotNull(result)
        assertTrue(result.valid())
    }

    @Test
    fun testGetDescriptionD2FunctionAttribute() {
        val condition = "A{test_var_number} > 0"
        val rule= Rule(condition, listOf(ruleAction))
        val ruleEngine = getRuleEngineForDescription(itemStore)
        val result = ruleEngine.evaluate(rule.condition)
        assertNotNull(result)
        assertTrue(result.valid())
    }

    @Test
    fun testGetDescriptionWithD2FunctionDataElement() {
        val condition = "#{test_var_number} > 0"
        val rule= Rule(condition, listOf(ruleAction))
        val ruleEngine = getRuleEngineForDescription(itemStore)
        val result = ruleEngine.evaluate(rule.condition)
        assertNotNull(result)
        assertTrue(result.valid())
    }

    @Test
    fun testGetDescriptionWithConstant() {
        val condition = "C{NAgjOfWMXg6} == 0"
        val rule= Rule(condition, listOf(ruleAction))
        val ruleEngine = getRuleEngineForDescription(itemStore)
        val result = ruleEngine.evaluate(rule.condition)
        assertNotNull(result)
        assertTrue(result.valid())
    }

    @Test
    fun testGetDescriptionWithProgramEnvironmentVariable() {
        val condition = "d2:hasValue(V{completed_date})"
        val rule= Rule(condition, listOf(ruleAction))
        val ruleEngine = getRuleEngineForDescription(itemStore)
        val result = ruleEngine.evaluate(rule.condition)
        assertNotNull(result)
        assertTrue(result.valid())
    }

    @Test
    fun testGetDescriptionWithSingleD2Function() {
        val condition = "d2:hasValue(#{test_var_one})"
        val rule= Rule(condition, listOf(ruleAction))
        val ruleEngine = getRuleEngineForDescription(itemStore)
        val result = ruleEngine.evaluate(rule.condition)
        assertNotNull(result)
        assertTrue(result.valid())
    }

    @Test
    fun testGetDescriptionWithMultipleD2FunctionsAndLogicalOROperator() {
        val condition = "d2:hasValue(#{test_var_two}) || d2:count(#{test_var_one}) > 0 "
        val rule= Rule(condition, listOf(ruleAction))
        val ruleEngine = getRuleEngineForDescription(itemStore)
        val result = ruleEngine.evaluate(rule.condition)
        assertNotNull(result)
        assertTrue(result.valid())
    }

    @Test
    fun testGetDescriptionWithMultipleD2FunctionsAndLogicalANDOperator() {
        val condition = "d2:hasValue(#{test_var_two}) && d2:count(#{test_var_one}) > 0 "
        val rule = Rule(condition, listOf(ruleAction))
        val ruleEngine = getRuleEngineForDescription(itemStore)
        val result = ruleEngine.evaluate(rule.condition)
        assertNotNull(result)
        assertTrue(result.valid())
    }

    @Test
    fun testGetDescriptionForDataFieldExpression() {
        val ruleEngine = getRuleEngineForDescription(itemStore)
        var result = ruleEngine.evaluateDataFieldExpression("1 + 1")
        assertNotNull(result)
        assertTrue(result.valid())
        result =
            ruleEngine.evaluateDataFieldExpression("d2:hasValue(#{test_var_two}) && d2:count(#{test_var_one}) > 0 ")
        assertNotNull(result)
        assertTrue(result.valid())
        result = ruleEngine.evaluateDataFieldExpression("1 + 1 +")
        assertNotNull(result)
        assertFalse(result.valid())
        assertTrue(result.exception() is RuleEngineValidationException)
        result =
            ruleEngine.evaluateDataFieldExpression("d2:hasValue(#{test_var_two}) && d2:count(#{test_var_one}) > 0 (")
        assertNotNull(result)
        assertFalse(result.valid())
        assertTrue(result.exception() is RuleEngineValidationException)
    }

    private fun getRuleEngineForDescription(itemStore: Map<String, org.hisp.dhis.rules.DataItem>): RuleEngine {
        return RuleEngine(RuleEngineContext(emptyList(), emptyList(), emptyMap(),
            emptyMap(), RuleEngineIntent.DESCRIPTION, itemStore))
    }

    companion object {
        private const val test_var_one = "Variable_ONE"
        private const val test_var_two = "Variable_TWO"
        private const val test_var_three = "Variable_THREE"
        private const val test_var_date_one = "2020-01-01"
        private const val test_var_date_two = "2020-02-02"
        private const val completionDate = "Completion date"
        private const val currentDate = "Current date"
        private const val constant = "PI"
        private const val test_var_number = "9"
    }
}
