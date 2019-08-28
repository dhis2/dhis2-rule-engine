package org.hisp.dhis.rules.models

import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertFailsWith

@RunWith(JUnit4::class)
class RuleActionHideOptionGroupTest {

    @Test
    fun createMustSubstituteEmptyStringIfArgumentsNull() {
        val ruleActionHideOptionGroup = RuleActionHideOptionGroup.create(null, "test_option_group")

        assertThat(ruleActionHideOptionGroup.content).isEqualTo("")
    }

    @Test
    fun createMustThrowOnNullField() {
        assertFailsWith<NullPointerException> {
            RuleActionHideOptionGroup.create("test_content", null!!)
        }
    }

    @Test
    fun equalsAndHashCodeFunctionsMustConformContract() {
        EqualsVerifier.forClass(RuleActionHideOptionGroup.create("test_content", "test_option_group").javaClass)
                .suppress(Warning.NULL_FIELDS)
                .verify()
    }
}