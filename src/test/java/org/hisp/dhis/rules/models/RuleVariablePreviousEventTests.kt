package org.hisp.dhis.rules.models

import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertFailsWith

@RunWith(JUnit4::class)
class RuleVariablePreviousEventTests {

    @Test
    fun createShouldThrowOnNullName() {
        assertFailsWith<NullPointerException> {
            RuleVariablePreviousEvent.create(null!!, "test_dataelement", RuleValueType.TEXT)
        }
    }

    @Test
    fun createShouldThrowOnNullDataElement() {
        assertFailsWith<NullPointerException> {
            RuleVariablePreviousEvent.create("test_variable", null!!, RuleValueType.TEXT)
        }
    }

    @Test
    fun createShouldThrowOnNullDataElementType() {
        assertFailsWith<NullPointerException> {
            RuleVariablePreviousEvent.create("test_variable", "test_dataelement", null!!)
        }
    }

    @Test
    fun createShouldPropagatePropertiesCorrectly() {
        val ruleVariablePreviousEvent = RuleVariablePreviousEvent.create(
                "test_variable", "test_dataelement", RuleValueType.NUMERIC)

        assertThat(ruleVariablePreviousEvent.name).isEqualTo("test_variable")
        assertThat(ruleVariablePreviousEvent.dataElement).isEqualTo("test_dataelement")
        assertThat(ruleVariablePreviousEvent.dataElementType).isEqualTo(RuleValueType.NUMERIC)
    }
}
