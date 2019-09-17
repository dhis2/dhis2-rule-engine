package org.hisp.dhis.rules.models

import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertFailsWith

@RunWith(JUnit4::class)
class RuleVariableCurrentEventTests {

    @Test
    fun createShouldThrowOnNullName() {
        assertFailsWith<NullPointerException> {
            RuleVariableCurrentEvent.create(null!!, "test_dataelement", RuleValueType.TEXT)
        }
    }

    @Test
    fun createShouldThrowOnNullDataElement() {
        assertFailsWith<NullPointerException> {
            RuleVariableCurrentEvent.create("test_variable", null!!, RuleValueType.TEXT)
        }
    }

    @Test
    fun createShouldThrowOnNullDataElementType() {
        assertFailsWith<NullPointerException> {
            RuleVariableCurrentEvent.create("test_variable", "test_dataelement", null!!)
        }
    }

    @Test
    fun createShouldPropagatePropertiesCorrectly() {
        val ruleVariableCurrentEvent = RuleVariableCurrentEvent.create(
                "test_variable", "test_dataelement", RuleValueType.NUMERIC)

        assertThat(ruleVariableCurrentEvent.name).isEqualTo("test_variable")
        assertThat(ruleVariableCurrentEvent.dataElement).isEqualTo("test_dataelement")
        assertThat(ruleVariableCurrentEvent.dataElementType).isEqualTo(RuleValueType.NUMERIC)
    }
}
