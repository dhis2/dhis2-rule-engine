package org.hisp.dhis.rules.models

import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertFailsWith

@RunWith(JUnit4::class)
class RuleActionShowWarningTests {

    @Test
    fun createMustSubstituteEmptyStringsForNullArguments() {
        val ruleActionAssignNoContent = RuleActionShowWarning.create(null, "test_data", "test_field")
        val ruleActionAssignNoData = RuleActionShowWarning.create("test_content", null, "test_field")

        assertThat(ruleActionAssignNoContent.content).isEqualTo("")
        assertThat(ruleActionAssignNoContent.data).isEqualTo("test_data")
        assertThat(ruleActionAssignNoContent.field).isEqualTo("test_field")

        assertThat(ruleActionAssignNoData.content).isEqualTo("test_content")
        assertThat(ruleActionAssignNoData.data).isEqualTo("")
        assertThat(ruleActionAssignNoData.field).isEqualTo("test_field")
    }

    @Test
    fun createMustThrowWhenContentAndDataAreNull() {
        assertFailsWith<IllegalArgumentException> {
            RuleActionShowWarning.create(null, null, "test_field")
        }
    }

    @Test
    fun createMustThrowWhenFieldIsNull() {
        assertFailsWith<NullPointerException> {
            RuleActionShowWarning.create("test_content", "test_data", null!!)
        }
    }

    @Test
    fun equalsAndHashcodeFunctionsMustConformToContract() {
        EqualsVerifier.forClass(RuleActionShowWarning
                .create("test_content", "test_data", "test_field").javaClass)
                .suppress(Warning.NONFINAL_FIELDS)
                .verify()
    }
}
