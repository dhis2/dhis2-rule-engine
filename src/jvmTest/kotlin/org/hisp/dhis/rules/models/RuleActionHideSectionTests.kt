package org.hisp.dhis.rules.models

import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertFailsWith

@RunWith(JUnit4::class)
class RuleActionHideSectionTests {

    @Test
    fun createMustThrowOnNullField() {
        assertFailsWith<NullPointerException> {
            RuleActionHideSection.create(null!!)
        }
    }

    @Test
    fun equalsAndHashCodeFunctionsMustConformContract() {
        EqualsVerifier.forClass(RuleActionHideSection.create("test_field").javaClass)
                .suppress(Warning.NULL_FIELDS)
                .verify()
    }
}
