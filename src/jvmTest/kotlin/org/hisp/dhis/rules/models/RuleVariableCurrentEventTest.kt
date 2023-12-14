package org.hisp.dhis.rules.models

import kotlin.test.Test
import kotlin.test.assertEquals

class RuleVariableCurrentEventTest {
    @Test
    fun createShouldPropagatePropertiesCorrectly() {
        val ruleVariableCurrentEvent = RuleVariableCurrentEvent(
            "test_variable", true, ArrayList(), "test_dataelement", RuleValueType.NUMERIC
        )
        assertEquals("test_variable", ruleVariableCurrentEvent.name)
        assertEquals("test_dataelement", ruleVariableCurrentEvent.dataElement)
        assertEquals(org.hisp.dhis.rules.models.RuleValueType.NUMERIC, ruleVariableCurrentEvent.dataElementType)
    }
}
