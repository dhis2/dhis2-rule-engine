package org.hisp.dhis.rules.models

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import org.hisp.dhis.rules.currentDate
import kotlin.test.Test
import kotlin.test.assertEquals

class RuleEventTest {
    @Test
    fun createShouldPropagateValuesCorrectly() {
        val ruleDataValue = RuleDataValue(Clock.System.now(), "programStage", "dataElement", "value")
        val ruleDataValues: MutableList<RuleDataValue> = ArrayList()
        ruleDataValues.add(ruleDataValue)
        val eventDate = Clock.System.now()
        val dueDate = LocalDate.Companion.currentDate()
        val (event, programStage, _, status, eventDate1, dueDate1, _, _, _, dataValues) = RuleEvent(
            "test_event_uid", "test_stage_uid", "",
            RuleEvent.Status.ACTIVE, eventDate, dueDate, null, "", "", ruleDataValues
        )
        assertEquals("test_event_uid", event)
        assertEquals(RuleEvent.Status.ACTIVE, status)
        assertEquals("test_stage_uid", programStage)
        assertEquals(eventDate, eventDate1)
        assertEquals(dueDate, dueDate1)
        assertEquals(1, dataValues.size.toLong())
        assertEquals(ruleDataValue, dataValues[0])
    }

    @Test
    fun eventDateComparatorTest() {
        val ruleEvents: List<RuleEvent> =
            listOf(
                RuleEvent(
                    "test_event_one", "test_program_stage_one", "", RuleEvent.Status.ACTIVE,
                    Instant.parse("2014-02-11T01:00:00Z"), LocalDate.parse("2014-02-11"), null, "", null,
                    emptyList()
                ),
                RuleEvent(
                    "test_event_two", "test_program_stage_two", "", RuleEvent.Status.ACTIVE,
                    Instant.parse("2017-03-22T01:00:00Z"), LocalDate.parse("2017-03-22"), null, "", null,
                    emptyList()
                )
            )
        val reversed = ruleEvents.sortedBy { e -> e.eventDate }.reversed()
        assertEquals("test_event_two", reversed[0].event)
        assertEquals("test_event_one", reversed[1].event)
    }

    companion object {
        private const val DATE_PATTERN = "yyyy-MM-dd"
    }
}
