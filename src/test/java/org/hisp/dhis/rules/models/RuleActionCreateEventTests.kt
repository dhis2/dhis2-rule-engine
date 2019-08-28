package org.hisp.dhis.rules.models

import nl.jqno.equalsverifier.EqualsVerifier
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertFailsWith

@RunWith(JUnit4::class)
class RuleActionCreateEventTests {

    @Test
    fun createMustSubstituteEmptyStringsForNullArguments() {
        val ruleActionCreateEventNoContent = RuleActionCreateEvent.create(null, "test_data", "test_program_stage")
        val ruleActionCreateEventNoField = RuleActionCreateEvent.create("test_content", null, "test_program_stage")

        assertThat(ruleActionCreateEventNoContent.content).isEqualTo("")
        assertThat(ruleActionCreateEventNoContent.data).isEqualTo("test_data")
        assertThat(ruleActionCreateEventNoContent.programStage).isEqualTo("test_program_stage")

        assertThat(ruleActionCreateEventNoField.content).isEqualTo("test_content")
        assertThat(ruleActionCreateEventNoField.data).isEqualTo("")
        assertThat(ruleActionCreateEventNoField.programStage).isEqualTo("test_program_stage")
    }

    @Test
    fun createMustThrowWhenFieldIsNull() {
        assertFailsWith<NullPointerException> {
            RuleActionCreateEvent.create("test_content", "test_data", null!!)
        }
    }

    @Test
    fun equalsAndHashcodeFunctionsMustConformToContract() {
        EqualsVerifier.forClass(RuleActionCreateEvent
                .create("test_content", "test_data", "test_field").javaClass)
                .verify()
    }
}
