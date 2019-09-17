package org.hisp.dhis.rules.models

import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertFailsWith

@RunWith(JUnit4::class)
class RuleAttributeValueTests {

    @Test
    fun shouldThrowOnNullTrackedEntityAttribute() {
        assertFailsWith<NullPointerException> {
            RuleAttributeValue.create(null!!, "test_value")
        }
    }

    @Test
    fun shouldThrowOnNullValue() {
        assertFailsWith<NullPointerException> {
            RuleAttributeValue.create("test_tracked_entity_attribute", null!!)
        }
    }

    @Test
    fun propertiesShouldBePropagatedCorrectly() {
        val ruleAttributeValue = RuleAttributeValue.create("test_tracked_entity_attribute", "test_value")

        assertThat(ruleAttributeValue.trackedEntityAttribute).isEqualTo("test_tracked_entity_attribute")
        assertThat(ruleAttributeValue.value).isEqualTo("test_value")
    }
}
