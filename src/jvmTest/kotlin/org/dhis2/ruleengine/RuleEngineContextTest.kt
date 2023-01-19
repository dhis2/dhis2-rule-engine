package org.dhis2.ruleengine

import junit.framework.TestCase
import org.assertj.core.api.Assertions.assertThat
import org.dhis2.ruleengine.models.Rule
import org.dhis2.ruleengine.models.RuleValueType
import org.dhis2.ruleengine.models.RuleVariable
import org.junit.Before
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.*

@RunWith(JUnit4::class)
class RuleEngineContextTest {
    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

   /* @Test(expected = IllegalArgumentException::class)
    fun builderShouldThrowOnNullVariableList() {
        RuleEngineContext.builder()
            .rules(ArrayList<Rule>())
            .ruleVariables(null)
    }

    @Test(expected = IllegalArgumentException::class)
    fun builderShouldThrowOnNullRulesList() {
        RuleEngineContext.builder()
            .ruleVariables(ArrayList<RuleVariable>())
            .ruleVariables(null)
    }*/

    /*@Test
    fun builderShouldContainImmutableCopyOfRules() {
        val ruleVariableOne: RuleVariable = Mockito.mock(RuleVariable::class.java)
        val ruleVariableTwo: RuleVariable = Mockito.mock(RuleVariable::class.java)
        val ruleOne: Rule = Mockito.mock(Rule::class.java)
        val ruleTwo: Rule = Mockito.mock(Rule::class.java)
        val members = listOf("one", "two")
        val supplementaryData: MutableMap<String, List<String>> = HashMap()
        supplementaryData["text-key"] = members
        val ruleVariables: MutableList<RuleVariable> = ArrayList<RuleVariable>()
        val rules: MutableList<Rule> = ArrayList<Rule>()
        ruleVariables.add(ruleVariableOne)
        rules.add(ruleOne)
        val ruleEngineContext = RuleEngineContext(
            rules = rules,
            ruleVariables = ruleVariables,
            supplementaryData = supplementaryData,
            constantsValues = emptyMap(),
            dataItemStore = emptyMap()
        )
        ruleVariables.add(ruleVariableTwo)
        rules.add(ruleTwo)
        assertThat(ruleEngineContext.ruleVariables.size).isEqualTo(1)
        assertThat(ruleEngineContext.ruleVariables[0]).isEqualTo(ruleVariableOne)
        assertThat(ruleEngineContext.supplementaryData.size).isEqualTo(1)
        assertThat(ruleEngineContext.supplementaryData["text-key"]).isNotNull
        assertThat(ruleEngineContext.supplementaryData["text-key"]).isEqualTo(members)
        assertThat(ruleEngineContext.rules.size).isEqualTo(1)
        assertThat(ruleEngineContext.rules[0]).isEqualTo(ruleOne)
        try {
            ruleEngineContext.ruleVariables.clear()
            TestCase.fail("UnsupportedOperationException was expected, but nothing was thrown.")
        } catch (unsupportedOperationException: UnsupportedOperationException) {
            // noop
        }
        try {
            ruleEngineContext.rules.clear()
            TestCase.fail("UnsupportedOperationException was expected, but nothing was thrown.")
        } catch (unsupportedOperationException: UnsupportedOperationException) {
            // noop
        }
    }*/

    @Test
    fun toEngineBuilderShouldReturnNewInstances() {
        val mockedRule = Rule("test",null,null,"true", emptyList(),"")
        val mockedVariable = RuleVariable.RuleVariableCurrentEvent("test","test",RuleValueType.TEXT)
        val ruleEngineContext = RuleEngineContext(
            rules = listOf(mockedRule),
            ruleVariables = listOf(mockedVariable),
            supplementaryData = emptyMap(),
            constantsValues = emptyMap(),
            dataItemStore = emptyMap()
        )
        val ruleEngineBuilderOne: RuleEngine = ruleEngineContext.toRuleEngine(emptyList(),null)
        val ruleEngineBuilderTwo: RuleEngine = ruleEngineContext.toRuleEngine(emptyList(),null)
        assertThat(ruleEngineBuilderOne).isNotEqualTo(ruleEngineBuilderTwo)
    }
}