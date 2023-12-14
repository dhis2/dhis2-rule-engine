package org.hisp.dhis.rules.models

import kotlinx.datetime.LocalDate
import org.hisp.dhis.rules.currentDate
import kotlin.test.Test
import kotlin.test.assertEquals

class RuleDataValueTest {
    @Test
    fun createShouldPropagateValuesCorrectly() {
        val eventDate = LocalDate.Companion.currentDate()
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
