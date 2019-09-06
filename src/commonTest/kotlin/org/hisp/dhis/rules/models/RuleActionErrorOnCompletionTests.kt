package org.hisp.dhis.rules.models

import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertFailsWith

@RunWith(JUnit4::class)
class RuleActionErrorOnCompletionTests {

    @Test
    fun createMustSubstituteEmptyStringsForNullArguments() {
        val ruleActionNoContent = RuleActionErrorOnCompletion.create(null, "test_data", "test_field")
        val ruleActionNoData = RuleActionErrorOnCompletion.create("test_content", null, "test_field")
        val ruleActionNoField = RuleActionErrorOnCompletion.create("test_content", "test_data", null)

        assertThat(ruleActionNoContent.content).isEqualTo("")
        assertThat(ruleActionNoContent.data).isEqualTo("test_data")
        assertThat(ruleActionNoContent.field).isEqualTo("test_field")

        assertThat(ruleActionNoData.content).isEqualTo("test_content")
        assertThat(ruleActionNoData.data).isEqualTo("")
        assertThat(ruleActionNoData.field).isEqualTo("test_field")

        assertThat(ruleActionNoField.content).isEqualTo("test_content")
        assertThat(ruleActionNoField.data).isEqualTo("test_data")
        assertThat(ruleActionNoField.field).isEqualTo("")
    }

    @Test
    fun createMustThrowWhenContentDataFieldAreNull() {
        assertFailsWith<IllegalArgumentException> {
            RuleActionErrorOnCompletion.create(null, null, null)
        }
    }

    @Test
    fun equalsAndHashcodeFunctionsMustConformToContract() {
        EqualsVerifier.forClass(RuleActionErrorOnCompletion
                .create("test_content", "test_data", "test_field").javaClass)
                .suppress(Warning.NONFINAL_FIELDS)
                .verify()
    }
}
