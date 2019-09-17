package org.hisp.dhis.rules.models

import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertFailsWith

@RunWith(JUnit4::class)
class RuleActionSetMandatoryFieldTests {

    @Test
    fun createMustThrowOnNullArgument() {
        assertFailsWith<NullPointerException> {
            RuleActionSetMandatoryField.create(null!!)
        }
    }

    @Test
    fun equalsAndHashcodeMustConformToContract() {
        EqualsVerifier.forClass(RuleActionSetMandatoryField.create("").javaClass)
                .suppress(Warning.NULL_FIELDS)
                .verify()
    }
}
