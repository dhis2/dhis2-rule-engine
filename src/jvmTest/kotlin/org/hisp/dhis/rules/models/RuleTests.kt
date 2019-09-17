package org.hisp.dhis.rules.models

import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock
import kotlin.test.assertFailsWith

@RunWith(JUnit4::class)
class RuleTests {

    @Test
    fun createShouldThrowOnNullCondition() {
        assertFailsWith<NullPointerException> {
            Rule.create("test_program_stage", 1, null!!, listOf(), "")
        }
    }

    @Test
    fun createShouldThrowOnNullActionsList() {
        assertFailsWith<NullPointerException> {
            Rule.create("test_program_stage", 1, "test_condition", null!!, "")
        }
    }

    @Test
    fun createShouldPropagatePropertiesCorrectly() {
        val ruleAction = mock(RuleAction::class.java)
        val rule = Rule.create("test_program_stage", 1, "test_condition", listOf(ruleAction), "")

        assertThat(rule.programStage).isEqualTo("test_program_stage")
        assertThat(rule.condition).isEqualTo("test_condition")
        assertThat(rule.priority).isEqualTo(1)
        assertThat(rule.actions.size).isEqualTo(1)
        assertThat(rule.actions[0]).isEqualTo(ruleAction)
    }

    @Test
    fun createShouldReturnImmutableList() {
        val ruleActionOne = mock(RuleAction::class.java)
        val ruleActionTwo = mock(RuleAction::class.java)
        val ruleActionThree = mock(RuleAction::class.java)

        val actions = mutableListOf(ruleActionOne, ruleActionTwo)
        val rule = Rule.create("test_program_stage", 1, "test_condition", actions, "")

        //rule.actions.add(ruleActionThree)
        //rule.actions.clear()

        assertThat(rule.actions.size).isEqualTo(2)
        assertThat(rule.actions[0]).isEqualTo(ruleActionOne)
        assertThat(rule.actions[1]).isEqualTo(ruleActionTwo)
        assertThat(ruleActionThree).isNotIn(rule.actions)
    }
}
