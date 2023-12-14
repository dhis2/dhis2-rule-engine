package org.hisp.dhis.rules.models

import kotlin.test.Test
import kotlin.test.assertEquals
class RuleVariableNewestEventTest {
    @Test
    fun createShouldPropagatePropertiesCorrectly() {
        val ruleVariableNewestEvent = RuleVariableNewestEvent(
            "test_variable", true, ArrayList(), "test_dataelement", RuleValueType.NUMERIC
        )
        assertEquals("test_variable", ruleVariableNewestEvent.name)
        assertEquals("test_dataelement", ruleVariableNewestEvent.dataElement)
        assertEquals(org.hisp.dhis.rules.models.RuleValueType.NUMERIC, ruleVariableNewestEvent.dataElementType)
    }
}
