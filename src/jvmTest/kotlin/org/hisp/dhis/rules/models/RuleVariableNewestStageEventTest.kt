package org.hisp.dhis.rules.models

import kotlin.test.Test
import kotlin.test.assertEquals
class RuleVariableNewestStageEventTest {
    @Test
    fun createShouldPropagatePropertiesCorrectly() {
        val ruleVariablePreviousEvent = RuleVariableNewestStageEvent(
            "test_variable", true, ArrayList(), "test_dataelement", RuleValueType.NUMERIC, "test_programstage"
        )
        assertEquals("test_variable", ruleVariablePreviousEvent.name)
        assertEquals("test_dataelement", ruleVariablePreviousEvent.dataElement)
        assertEquals("test_programstage", ruleVariablePreviousEvent.programStage)
        assertEquals(RuleValueType.NUMERIC, ruleVariablePreviousEvent.dataElementType)
    }
}
