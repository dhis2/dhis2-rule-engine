package org.hisp.dhis.rules

import io.mockk.mockk
import org.hisp.dhis.rules.models.Rule
import org.hisp.dhis.rules.models.RuleVariable
import org.hisp.dhis.rules.util.MockRuleVariable
import org.junit.Assert
import org.junit.Test

class RuleEngineContextTest {
    private val ruleVariable: RuleVariable = MockRuleVariable()
    private val ruleVariableTwo: RuleVariable = MockRuleVariable()
    private val rule: Rule = mockk<Rule>()
    private val ruleTwo: Rule = mockk<Rule>()
    @Test(expected = IllegalArgumentException::class)
    fun builderShouldThrowOnNullVariableList() {
        RuleEngineContext.builder()
            .rules(ArrayList())
            .ruleVariables(null)
    }

    @Test(expected = IllegalArgumentException::class)
    fun builderShouldThrowOnNullRulesList() {
        RuleEngineContext.builder()
            .ruleVariables(ArrayList())
            .ruleVariables(null)
    }

    @Test
    fun builderShouldContainImmutableCopyOfRules() {
        val members = listOf("one", "two")
        val supplementaryData: MutableMap<String, List<String>> = HashMap()
        supplementaryData["text-key"] = members
        val ruleVariables: MutableList<RuleVariable> = ArrayList<RuleVariable>()
        val rules: MutableList<Rule> = ArrayList()
        ruleVariables.add(ruleVariable)
        rules.add(rule)
        val ruleEngineContext = RuleEngineContext.builder()
            .ruleVariables(ruleVariables)
            .supplementaryData(supplementaryData)
            .rules(rules)
            .build()
        ruleVariables.add(ruleVariableTwo)
        rules.add(ruleTwo)
        Assert.assertEquals(1, ruleEngineContext.ruleVariables.size.toLong())
        Assert.assertEquals(ruleVariable, ruleEngineContext.ruleVariables[0])
        Assert.assertEquals(1, ruleEngineContext.supplementaryData.size.toLong())
        Assert.assertNotNull(ruleEngineContext.supplementaryData["text-key"])
        Assert.assertEquals(members, ruleEngineContext.supplementaryData["text-key"])
        Assert.assertEquals(1, ruleEngineContext.rules.size.toLong())
        Assert.assertEquals(rule, ruleEngineContext.rules[0])
        try {
            ruleEngineContext.ruleVariables.clear()
            Assert.fail("UnsupportedOperationException was expected, but nothing was thrown.")
        } catch (unsupportedOperationException: UnsupportedOperationException) {
            // noop
        }
        try {
            ruleEngineContext.rules.clear()
            Assert.fail("UnsupportedOperationException was expected, but nothing was thrown.")
        } catch (unsupportedOperationException: UnsupportedOperationException) {
            // noop
        }
    }

    @Test
    fun toEngineBuilderShouldReturnNewInstances() {
        val ruleEngineContext = RuleEngineContext.builder()
            .ruleVariables(listOf<RuleVariable>(MockRuleVariable()))
            .supplementaryData(HashMap<String, List<String>>())
            .rules(listOf(mockk<Rule>()))
            .build()
        val ruleEngineBuilderOne = ruleEngineContext.toEngineBuilder()
        val ruleEngineBuilderTwo = ruleEngineContext.toEngineBuilder()
        Assert.assertNotEquals(ruleEngineBuilderOne, ruleEngineBuilderTwo)
    }
}
