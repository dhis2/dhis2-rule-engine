package org.hisp.dhis.rules.models

import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertFailsWith

@RunWith(JUnit4::class)
class RuleVariableAttributeTests {

    @Test
    fun createShouldThrowOnNullName() {
        assertFailsWith<NullPointerException> {
            RuleVariableAttribute.create(null!!, "test_attribute", RuleValueType.TEXT)
        }
    }

    @Test
    fun createShouldThrowOnNullTrackedEntityAttribute() {
        assertFailsWith<NullPointerException> {
            RuleVariableAttribute.create("test_variable", null!!, RuleValueType.TEXT)
        }
    }

    @Test
    fun createShouldThrowOnNullTrackedEntityAttributeType() {
        assertFailsWith<NullPointerException> {
            RuleVariableAttribute.create("test_variable", "test_attribute", null!!)
        }
    }

    @Test
    fun createShouldPropagatePropertiesCorrectly() {
        val ruleVariableAttribute = RuleVariableAttribute.create(
                "test_variable", "test_attribute", RuleValueType.NUMERIC)

        assertThat(ruleVariableAttribute.name).isEqualTo("test_variable")
        assertThat(ruleVariableAttribute.trackedEntityAttribute).isEqualTo("test_attribute")
        assertThat(ruleVariableAttribute.trackedEntityAttributeType).isEqualTo(RuleValueType.NUMERIC)
    }
}
