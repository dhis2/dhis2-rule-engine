package org.hisp.dhis.rules.models

import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertFailsWith

@RunWith(JUnit4::class)
class RuleActionShowErrorTests {

    @Test
    fun createMustSubstituteEmptyStringsForNullArguments() {
        val ruleActionAssignNoContent = RuleActionShowError.create(null, "test_data", "test_field")
        val ruleActionAssignNoData = RuleActionShowError.create("test_content", null, "test_field")

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
            RuleActionShowError.create(null, null, "test_field")
        }
    }

    @Test
    fun createMustThrowWhenFieldIsNull() {
        assertFailsWith<NullPointerException> {
            RuleActionShowError.create("test_content", "test_data", null!!)
        }
    }

    @Test
    fun equalsAndHashcodeFunctionsMustConformToContract() {
        EqualsVerifier.forClass(RuleActionShowError
                .create("test_content", "test_data", "test_field").javaClass)
                .suppress(Warning.NONFINAL_FIELDS)
                .verify()
    }
}
