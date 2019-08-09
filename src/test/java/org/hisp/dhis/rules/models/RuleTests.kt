package org.hisp.dhis.rules.models

import kotlinx.collections.immutable.persistentListOf
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock
import kotlin.test.assertFailsWith

@RunWith(JUnit4::class)
class RuleTests {

    val thrown: ExpectedException = ExpectedException.none()

    @Test
    fun createShouldThrowOnNullCondition() {
        assertFailsWith<NullPointerException> {
            Rule.create("test_program_stage", 1, null!!, persistentListOf(), "")
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
        val rule = Rule.create("test_program_stage", 1,
                "test_condition", persistentListOf(ruleAction), "")

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

        val actions = persistentListOf(ruleActionOne, ruleActionTwo)
        val rule = Rule.create("test_program_stage", 1, "test_condition", actions, "")

        actions.clear()

        assertThat(rule.actions.size).isEqualTo(2)
        assertThat(rule.actions[0]).isEqualTo(ruleActionOne)
        assertThat(rule.actions[1]).isEqualTo(ruleActionTwo)

        // tests immutability
        assertThat(actions.size).isEqualTo(2)
    }
}
