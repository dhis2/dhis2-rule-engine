package org.hisp.dhis.rules

import org.assertj.core.api.Java6Assertions.assertThat
import org.hisp.dhis.rules.models.Rule
import org.hisp.dhis.rules.models.RuleVariable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import java.util.*
import kotlin.test.assertFailsWith

@RunWith(JUnit4::class)
class RuleEngineContextTests {

    @Mock private val ruleExpressionEvaluator: RuleExpressionEvaluator? = ExpressionEvaluator()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun builderShouldThrowOnNullExpressionEvaluator() {
        assertFailsWith<IllegalArgumentException> {
            RuleEngineContext.builder(null)
        }
    }

    @Test
    fun builderShouldThrowOnNullVariableList() {
        assertFailsWith<IllegalArgumentException> {
            RuleEngineContext.builder(ruleExpressionEvaluator).rules(ArrayList()).ruleVariables(null)
        }
    }

    @Test
    fun builderShouldThrowOnNullRulesList() {
        assertFailsWith<IllegalArgumentException> {
            RuleEngineContext.builder(ruleExpressionEvaluator).ruleVariables(ArrayList()).ruleVariables(null)
        }
    }

    @Test
    fun builderShouldContainImmutableCopyOfRules() {
        val ruleVariableOne = mock(RuleVariable::class.java)
        val ruleVariableTwo = mock(RuleVariable::class.java)

        val ruleOne = mock(Rule::class.java)
        val ruleTwo = mock(Rule::class.java)

        val members = listOf("one", "two")
        val supplementaryData = hashMapOf("text-key" to members)

        val ruleVariables = mutableListOf(ruleVariableOne)
        val rules = mutableListOf(ruleOne)

        val ruleEngineContext = RuleEngineContext.builder(ruleExpressionEvaluator)
                .ruleVariables(ruleVariables)
                .supplementaryData(supplementaryData)
                .rules(rules)
                .build()


        assertThat(ruleEngineContext.expressionEvaluator).isEqualTo(ruleExpressionEvaluator)
        assertThat(ruleEngineContext.ruleVariables.size).isEqualTo(1)
        assertThat(ruleEngineContext.ruleVariables[0]).isEqualTo(ruleVariableOne)

        assertThat(ruleEngineContext.supplementaryData.size).isEqualTo(1)
        assertThat(ruleEngineContext.supplementaryData["text-key"]).isNotNull
        assertThat(ruleEngineContext.supplementaryData["text-key"]).isEqualTo(members)


        assertThat(ruleEngineContext.rules.size).isEqualTo(1)
        assertThat(ruleEngineContext.rules[0]).isEqualTo(ruleOne)

        // Test immutability
        ruleEngineContext.ruleVariables.add(ruleVariableTwo)
        ruleEngineContext.rules.add(ruleTwo)

        assertThat(ruleEngineContext.rules.size).isEqualTo(1)
        assertThat(ruleEngineContext.ruleVariables.size).isEqualTo(1)
        assertThat(ruleTwo).isNotIn(ruleEngineContext.rules)
        assertThat(ruleVariableTwo).isNotIn(ruleEngineContext.ruleVariables)

    }

    @Test
    fun toEngineBuilderShouldReturnNewInstances() {
        val ruleEngineContext = RuleEngineContext.builder(ruleExpressionEvaluator)
                .ruleVariables(listOf(mock(RuleVariable::class.java)))
                .supplementaryData(HashMap())
                .rules(listOf(mock(Rule::class.java)))
                .build()

        val ruleEngineBuilderOne = ruleEngineContext.toEngineBuilder()
        val ruleEngineBuilderTwo = ruleEngineContext.toEngineBuilder()

        assertThat(ruleEngineBuilderOne).isNotEqualTo(ruleEngineBuilderTwo)
    }
}
