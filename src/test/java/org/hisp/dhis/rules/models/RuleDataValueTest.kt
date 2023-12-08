package org.hisp.dhis.rules.models

import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class RuleDataValueTest {
    @Test
    fun createShouldPropagateValuesCorrectly() {
        val eventDate = Date()
        val ruleDataValue = RuleDataValue(
            eventDate,
            "test_program_stage_uid", "test_dataelement", "test_value"
        )
        assertEquals(eventDate, ruleDataValue.eventDate)
        assertEquals("test_program_stage_uid", ruleDataValue.programStage)
        assertEquals("test_dataelement", ruleDataValue.dataElement)
        assertEquals("test_value", ruleDataValue.value)
    }
}
