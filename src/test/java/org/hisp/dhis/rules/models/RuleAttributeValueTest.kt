package org.hisp.dhis.rules.models

import kotlin.test.Test
import kotlin.test.assertEquals

class RuleAttributeValueTest {
    @Test
    fun propertiesShouldBePropagatedCorrectly() {
        val ruleAttributeValue = RuleAttributeValue
            .create("test_tracked_entity_attribute", "test_value")
        assertEquals("test_tracked_entity_attribute", ruleAttributeValue.trackedEntityAttribute)
        assertEquals("test_value", ruleAttributeValue.value)
    }
}
