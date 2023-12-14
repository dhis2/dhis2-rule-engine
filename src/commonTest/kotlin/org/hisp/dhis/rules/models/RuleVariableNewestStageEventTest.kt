package org.hisp.dhis.rules.models

import kotlin.test.Test
import kotlin.test.assertEquals
class RuleVariableNewestStageEventTest {
    @Test
    fun createShouldPropagatePropertiesCorrectly() {
        val ruleVariableNewestStageEvent = RuleVariableNewestStageEvent(
            "test_variable", true, ArrayList(), "test_dataelement", RuleValueType.NUMERIC, "test_programstage"
        )
        assertEquals("test_variable", ruleVariableNewestStageEvent.name)
        assertEquals("test_programstage", ruleVariableNewestStageEvent.programStage)
        assertEquals("test_dataelement", ruleVariableNewestStageEvent.dataElement())
        assertEquals(RuleValueType.NUMERIC, ruleVariableNewestStageEvent.dataElementType())
    }
}
