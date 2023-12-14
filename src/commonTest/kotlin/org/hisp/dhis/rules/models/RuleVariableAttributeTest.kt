package org.hisp.dhis.rules.models

import kotlin.test.Test
import kotlin.test.assertEquals

class RuleVariableAttributeTest {
    @Test
    fun createShouldPropagatePropertiesCorrectly() {
        val ruleVariableAttribute = RuleVariableAttribute(
            "test_variable", true, listOf(), "test_attribute", RuleValueType.NUMERIC
        )
        assertEquals("test_variable", ruleVariableAttribute.name)
        assertEquals("test_attribute", ruleVariableAttribute.trackedEntityAttribute)
        assertEquals(RuleValueType.NUMERIC, ruleVariableAttribute.trackedEntityAttributeType)
    }
}
