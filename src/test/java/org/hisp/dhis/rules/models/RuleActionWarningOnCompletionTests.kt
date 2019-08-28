package org.hisp.dhis.rules.models

import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertFailsWith

@RunWith(JUnit4::class)
class RuleActionWarningOnCompletionTests {

    @Test
    fun createMustSubstituteEmptyStringsForNullArguments() {
        val ruleActionNoContent = RuleActionWarningOnCompletion.create(null, "test_data", "test_field")
        val ruleActionNoContentNoData = RuleActionWarningOnCompletion.create("test_content", null, "test_field")
        val ruleActionNoContentNoField = RuleActionWarningOnCompletion.create("test_content", "test_data", null)

        assertThat(ruleActionNoContent.content).isEqualTo("")
        assertThat(ruleActionNoContent.data).isEqualTo("test_data")
        assertThat(ruleActionNoContent.field).isEqualTo("test_field")

        assertThat(ruleActionNoContentNoData.content).isEqualTo("test_content")
        assertThat(ruleActionNoContentNoData.data).isEqualTo("")
        assertThat(ruleActionNoContentNoData.field).isEqualTo("test_field")

        assertThat(ruleActionNoContentNoField.content).isEqualTo("test_content")
        assertThat(ruleActionNoContentNoField.data).isEqualTo("test_data")
        assertThat(ruleActionNoContentNoField.field).isEqualTo("")
    }

    @Test
    fun createMustThrowWhenContentDataFieldAreNull() {
        assertFailsWith<IllegalArgumentException> {
            RuleActionWarningOnCompletion.create(null, null, null)
        }
    }

    @Test
    fun equalsAndHashcodeFunctionsMustConformToContract() {
        EqualsVerifier.forClass(RuleActionWarningOnCompletion
                .create("test_content", "test_data", "test_field").javaClass)
                .suppress(Warning.NONFINAL_FIELDS)
                .verify()
    }
}
