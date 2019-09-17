package org.hisp.dhis.rules.models

import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertFailsWith

@RunWith(JUnit4::class)
class RuleActionHideFieldTests {

    @Test
    fun createMustSubstituteEmptyStringIfArgumentsNull() {
        val ruleActionHideField = RuleActionHideField.create(null, "test_field")

        assertThat(ruleActionHideField.content).isEqualTo("")
    }

    @Test
    fun createMustThrowOnNullField() {
        assertFailsWith<NullPointerException> {
            RuleActionHideField.create("test_content", null!!)
        }
    }

    @Test
    fun equalsAndHashCodeFunctionsMustConformContract() {
        EqualsVerifier.forClass(RuleActionHideField.create("test_content", "test_field").javaClass)
                .suppress(Warning.NONFINAL_FIELDS)
                .verify()
    }
}
