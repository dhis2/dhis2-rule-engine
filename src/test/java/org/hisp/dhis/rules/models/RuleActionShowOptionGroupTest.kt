package org.hisp.dhis.rules.models

import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertFailsWith

@RunWith(JUnit4::class)
class RuleActionShowOptionGroupTest {

    @Test
    fun createMustSubstituteEmptyStringIfArgumentsNull() {
        val ruleActionShowOptionGroup = RuleActionShowOptionGroup.create(null, "test_option_group", "test_field")

        assertThat(ruleActionShowOptionGroup.content).isEqualTo("")
    }

    @Test
    fun createMustThrowOnNullOptionGroupField() {
        assertFailsWith<NullPointerException> {
            RuleActionShowOptionGroup.create("test_content", null!!, "test_field")
        }
    }

    @Test
    fun createMustThrowOnNullField() {
        assertFailsWith<NullPointerException> {
            RuleActionShowOptionGroup.create("test_content", "test_option_group", null!!)
        }
    }

    @Test
    fun equalsAndHashCodeFunctionsMustConformContract() {
        EqualsVerifier.forClass(RuleActionShowOptionGroup.create("test_content", "test_option_group", "test_field").javaClass)
                .suppress(Warning.NULL_FIELDS)
                .verify()
    }
}