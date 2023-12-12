package org.hisp.dhis.rules.models

import kotlin.test.Test
import kotlin.test.assertEquals

class RuleVariablePreviousEventTest {
    @Test
    fun createShouldPropagatePropertiesCorrectly() {
        val ruleVariablePreviousEvent = RuleVariablePreviousEvent(
            "test_variable", true, ArrayList(), "test_dataelement", RuleValueType.NUMERIC
        )
        assertEquals("test_variable", ruleVariablePreviousEvent.name)
        assertEquals("test_dataelement", ruleVariablePreviousEvent.dataElement)
        assertEquals(RuleValueType.NUMERIC, ruleVariablePreviousEvent.dataElementType)
    }
}
