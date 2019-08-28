package org.hisp.dhis.rules.models

import nl.jqno.equalsverifier.EqualsVerifier
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertFailsWith

@RunWith(JUnit4::class)
class RuleActionAssignTests {

    @Test
    fun createMustSubstituteEmptyStringsForNullArguments() {
        val ruleActionAssignNoContent = RuleActionAssign.create(null, "test_data", "test_field")
        val ruleActionAssignNoField = RuleActionAssign.create("test_content", "test_data", null)

        assertThat(ruleActionAssignNoContent.content).isEqualTo("")
        assertThat(ruleActionAssignNoContent.data).isEqualTo("test_data")
        assertThat(ruleActionAssignNoContent.field).isEqualTo("test_field")

        assertThat(ruleActionAssignNoField.content).isEqualTo("test_content")
        assertThat(ruleActionAssignNoField.data).isEqualTo("test_data")
        assertThat(ruleActionAssignNoField.field).isEqualTo("")
    }

    @Test
    fun createMustThrowWhenContentAndFieldAreNull() {
        assertFailsWith<IllegalArgumentException> {
            RuleActionAssign.create(null, "test_data", null)
        }
    }

    @Test
    fun createMustThrowWhenDataIsNull() {
        assertFailsWith<NullPointerException> {
            RuleActionAssign.create("test_content", null!!, "test_field")
        }
    }

    @Test
    fun equalsAndHashcodeFunctionsMustConformToContract() {
        EqualsVerifier.forClass(RuleActionAssign
                .create("test_content", "test_data", "test_field").javaClass)
                .verify()
    }
}
