package org.hisp.dhis.rules.models

import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertFailsWith

@RunWith(JUnit4::class)
class RuleActionHideOptionTest {

    @Test
    fun createMustSubstituteEmptyStringIfArgumentsNull() {
        val ruleActionHideOption = RuleActionHideOption.create(null, "test_option", "test_field")

        assertThat(ruleActionHideOption.content).isEqualTo("")
    }

    @Test
    fun createMustThrowOnNullField() {
        assertFailsWith<NullPointerException> {
            RuleActionHideOption.create("test_content", null!!, "null")
        }
    }

    @Test
    fun equalsAndHashCodeFunctionsMustConformContract() {
        EqualsVerifier.forClass(RuleActionHideOption.create("test_content", "test_option", "test_field").javaClass)
                .suppress(Warning.NULL_FIELDS)
                .verify()
    }
}