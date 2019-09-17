package org.hisp.dhis.rules.models

import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertFailsWith

@RunWith(JUnit4::class)
class RuleVariableNewestEventTests {

    @Test
    fun createShouldThrowOnNullName() {
        assertFailsWith<NullPointerException> {
            RuleVariableNewestEvent.create(null!!, "test_dataelement", RuleValueType.TEXT)
        }
    }

    @Test
    fun createShouldThrowOnNullDataElement() {
        assertFailsWith<NullPointerException> {
            RuleVariableNewestEvent.create("test_variable", null!!, RuleValueType.TEXT)
        }
    }

    @Test
    fun createShouldThrowOnNullDataElementType() {
        assertFailsWith<NullPointerException> {
            RuleVariableNewestEvent.create("test_variable", "test_dataelement", null!!)
        }
    }

    @Test
    fun createShouldPropagatePropertiesCorrectly() {
        val ruleVariableNewestEvent = RuleVariableNewestEvent.create(
                "test_variable", "test_dataelement", RuleValueType.NUMERIC)

        assertThat(ruleVariableNewestEvent.name).isEqualTo("test_variable")
        assertThat(ruleVariableNewestEvent.dataElement).isEqualTo("test_dataelement")
        assertThat(ruleVariableNewestEvent.dataElementType).isEqualTo(RuleValueType.NUMERIC)
    }
}
