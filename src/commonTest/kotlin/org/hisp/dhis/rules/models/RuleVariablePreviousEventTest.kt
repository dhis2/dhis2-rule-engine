package org.hisp.dhis.rules.models

import kotlinx.datetime.*
import org.hisp.dhis.rules.utils.currentDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull


class RuleVariablePreviousEventTest {
    private val today = LocalDate.Companion.currentDate()
    private val yesterday = today.minus(1, DateTimeUnit.DAY)
    private val dayBeforeYesterday = yesterday.minus(1, DateTimeUnit.DAY)
    private val todayInstant = LocalDate.Companion.currentDate().atStartOfDayIn(TimeZone.currentSystemDefault())
    private val yesterdayInstant = yesterday.atStartOfDayIn(TimeZone.currentSystemDefault())
    private val dayBeforeYesterdayInstant = dayBeforeYesterday.atStartOfDayIn(TimeZone.currentSystemDefault())
    private val tomorrow = today.plus(1, DateTimeUnit.DAY).atStartOfDayIn(TimeZone.currentSystemDefault())
    private val todayMorning = today.atTime(LocalTime(10, 0, 0)).toInstant(TimeZone.currentSystemDefault())
    private val todayAfternoon = today.atTime(LocalTime(14, 0, 0)).toInstant(TimeZone.currentSystemDefault())

    private val ruleVariablePreviousEvent = RuleVariablePreviousEvent("previous value", true, emptyList(), "data_element", RuleValueType.NUMERIC)

    @Test
    fun shouldCreateEmptyRuleVariableValueWhenNoDataValuesArePresent() {
        val ruleVariableValue =
            ruleVariablePreviousEvent.createValues(event(tomorrow, tomorrow), emptyMap(), emptyMap(), emptyMap())

        assertEquals(RuleValueType.NUMERIC, ruleVariableValue.type)
        assertNull(ruleVariableValue.value)
        assertNull(ruleVariableValue.eventDate)
        assertEquals(emptyList(), ruleVariableValue.candidates)
    }

    @Test
    fun shouldCreateTodayRuleVariableValueWhenEventDateIsTomorrow() {
        val ruleVariableValue =
            ruleVariablePreviousEvent.createValues(event(tomorrow, tomorrow), allEventsDataValues(), emptyMap(), emptyMap())

        assertEquals(RuleValueType.NUMERIC, ruleVariableValue.type)
        assertEquals(todayDataValue().value, ruleVariableValue.value)
        assertEquals(today.toString(), ruleVariableValue.eventDate)
        assertEquals(setOf("1", "2", "3", "4", "5"), ruleVariableValue.candidates.toSet())
    }

    @Test
    fun shouldCreateYesterdayAfternoonRuleVariableValueWhenEventDateIsToday() {
        val ruleVariableValue =
            ruleVariablePreviousEvent.createValues(event(todayInstant, todayInstant), allEventsDataValues(), emptyMap(), emptyMap())

        assertEquals(RuleValueType.NUMERIC, ruleVariableValue.type)
        assertEquals(yesterdayDataValueCreatedThisAfternoon().value, ruleVariableValue.value)
        assertEquals(yesterday.toString(), ruleVariableValue.eventDate)
        assertEquals(setOf("1", "2", "3", "4", "5"), ruleVariableValue.candidates.toSet())
    }

    @Test
    fun shouldCreateYesterdayRuleVariableValueCreateTodayMorningWhenEventDateIsYesterdayAndItWasCreatedTodayAfternoon() {
        val ruleVariableValue =
            ruleVariablePreviousEvent.createValues(event(yesterdayInstant, todayAfternoon), allEventsDataValues(), emptyMap(), emptyMap())

        assertEquals(RuleValueType.NUMERIC, ruleVariableValue.type)
        assertEquals(yesterdayDataValueCreatedThisMorning().value, ruleVariableValue.value)
        assertEquals(yesterday.toString(), ruleVariableValue.eventDate)
        assertEquals(setOf("1", "2", "3", "4", "5"), ruleVariableValue.candidates.toSet())
    }

    @Test
    fun shouldCreateDayBeforeYesterdayRuleVariableValueWhenEventDateIsYesterdayAndItWasCreatedYesterday() {
        val ruleVariableValue =
            ruleVariablePreviousEvent.createValues(event(yesterdayInstant, yesterdayInstant), allEventsDataValues(), emptyMap(), emptyMap())

        assertEquals(RuleValueType.NUMERIC, ruleVariableValue.type)
        assertEquals(dayBeforeYesterdayDataValue().value, ruleVariableValue.value)
        assertEquals(dayBeforeYesterday.toString(), ruleVariableValue.eventDate)
        assertEquals(setOf("1", "2", "3", "4", "5"), ruleVariableValue.candidates.toSet())
    }

    @Test
    fun shouldGetNoValueWhenThereIsNoPreviousDataValue() {
        val ruleVariableValue =
            ruleVariablePreviousEvent.createValues(event(dayBeforeYesterdayInstant, yesterdayInstant), allEventsDataValues(), emptyMap(), emptyMap())

        assertEquals(RuleValueType.NUMERIC, ruleVariableValue.type)
        assertNull(ruleVariableValue.value)
        assertNull(ruleVariableValue.eventDate)
        assertEquals(emptyList(), ruleVariableValue.candidates)
    }

    private fun event(eventDate: Instant, createdDate: Instant): RuleEvent {
        return RuleEvent(
            event = "test_event",
            programStage = "test_program_stage",
            programStageName = "",
            status = RuleEventStatus.ACTIVE,
            eventDate = eventDate,
            createdDate = createdDate,
            dueDate = LocalDate.currentDate(),
            organisationUnit = "",
            organisationUnitCode = "",
            completedDate = LocalDate.currentDate(),
            dataValues =
            listOf(
                RuleDataValue("data_element", "1"),
                RuleDataValue("data_element", "2"),
                RuleDataValue("data_element", "3"),
                RuleDataValue("data_element", "4"),
                RuleDataValue("data_element", "5")
            )
        )
    }

    private fun allEventsDataValues(): Map<String, List<RuleDataValueHistory>> {
        val ruleDataValues = mutableListOf(
            todayDataValue(),
            yesterdayDataValueCreatedThisMorning(),
            yesterdayDataValueCreatedThisAfternoon(),
            tomorrowDataValue(),
            dayBeforeYesterdayDataValue()
        )
        ruleDataValues.sortWith(compareBy<RuleDataValueHistory>({ it.eventDate}, {it.createdDate }).reversed())

        return mapOf(Pair("data_element", ruleDataValues))
    }

    private fun todayDataValue(): RuleDataValueHistory {
        return RuleDataValueHistory("1", todayInstant, todayInstant, "")
    }

    private fun yesterdayDataValueCreatedThisMorning(): RuleDataValueHistory {
        return RuleDataValueHistory("2", yesterdayInstant, todayMorning, "")
    }

    private fun yesterdayDataValueCreatedThisAfternoon(): RuleDataValueHistory {
        return RuleDataValueHistory("3", yesterdayInstant, todayAfternoon, "")
    }

    private fun dayBeforeYesterdayDataValue(): RuleDataValueHistory {
        return RuleDataValueHistory("5", dayBeforeYesterdayInstant, todayAfternoon, "")
    }

    private fun tomorrowDataValue(): RuleDataValueHistory {
        return RuleDataValueHistory("4", tomorrow, tomorrow, "")
    }


}