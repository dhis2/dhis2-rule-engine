package org.hisp.dhis.rules.models

import kotlinx.datetime.*
import kotlin.time.Instant
import org.hisp.dhis.rules.utils.currentDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull


class RuleVariablePreviousEventTest {
    private val today = currentDate()
    private val yesterday = today.minus(1, DateTimeUnit.DAY)
    private val dayBeforeYesterday = yesterday.minus(1, DateTimeUnit.DAY)
    private val tomorrow = today.plus(1, DateTimeUnit.DAY)
    private val todayMorning = today.atTime(LocalTime(10, 0, 0)).toInstant(TimeZone.currentSystemDefault())
    private val todayAfternoon = today.atTime(LocalTime(14, 0, 0)).toInstant(TimeZone.currentSystemDefault())
    private val todayInstant = currentDate().atStartOfDayIn(TimeZone.currentSystemDefault())
    private val yesterdayInstant = yesterday.atStartOfDayIn(TimeZone.currentSystemDefault())
    private val tomorrowInstant = tomorrow.atStartOfDayIn(TimeZone.currentSystemDefault())

    private val ruleVariablePreviousEvent =
        RuleVariablePreviousEvent("previous value", true, emptyList(), "data_element", RuleValueType.NUMERIC)

    @Test
    fun shouldCreateEmptyRuleVariableValueWhenNoDataValuesArePresent() {
        val ruleVariableValue =
            ruleVariablePreviousEvent.createValues(event(tomorrow, todayInstant), emptyMap(), emptyMap())

        assertEquals(RuleValueType.NUMERIC, ruleVariableValue.type)
        assertNull(ruleVariableValue.value)
        assertNull(ruleVariableValue.eventDate)
        assertEquals(emptyList(), ruleVariableValue.candidates)
    }

    @Test
    fun shouldCreateTodayRuleVariableValueWhenEventDateIsTomorrow() {
        val ruleVariableValue =
            ruleVariablePreviousEvent.createValues(event(tomorrow, todayInstant), allEventsDataValues(), emptyMap())

        assertEquals(RuleValueType.NUMERIC, ruleVariableValue.type)
        assertEquals(todayDataValue().value, ruleVariableValue.value)
        assertEquals(today.toString(), ruleVariableValue.eventDate)
        assertEquals(setOf("1", "2", "3", "4", "5"), ruleVariableValue.candidates.toSet())
    }

    @Test
    fun shouldCreateYesterdayAfternoonRuleVariableValueWhenEventDateIsToday() {
        val ruleVariableValue =
            ruleVariablePreviousEvent.createValues(event(today, todayInstant), allEventsDataValues(), emptyMap())

        assertEquals(RuleValueType.NUMERIC, ruleVariableValue.type)
        assertEquals(yesterdayDataValueCreatedThisAfternoon().value, ruleVariableValue.value)
        assertEquals(yesterday.toString(), ruleVariableValue.eventDate)
        assertEquals(setOf("1", "2", "3", "4", "5"), ruleVariableValue.candidates.toSet())
    }

    @Test
    fun shouldCreateYesterdayRuleVariableValueCreateTodayMorningWhenEventDateIsYesterdayAndItWasCreatedTodayAfternoon() {
        val ruleVariableValue =
            ruleVariablePreviousEvent.createValues(event(yesterday, todayAfternoon), allEventsDataValues(), emptyMap())

        assertEquals(RuleValueType.NUMERIC, ruleVariableValue.type)
        assertEquals(yesterdayDataValueCreatedThisMorning().value, ruleVariableValue.value)
        assertEquals(yesterday.toString(), ruleVariableValue.eventDate)
        assertEquals(setOf("1", "2", "3", "4", "5"), ruleVariableValue.candidates.toSet())
    }

    @Test
    fun shouldCreateYesterdayRuleVariableValueCreateTodayMorningWhenEventDateIsYesterdayAndCreatedAtClientIsTodayAfternoon() {
        val ruleVariableValue =
            ruleVariablePreviousEvent.createValues(event(yesterday, tomorrowInstant,todayAfternoon), allEventsDataValues(), emptyMap())

        assertEquals(RuleValueType.NUMERIC, ruleVariableValue.type)
        assertEquals(yesterdayDataValueCreatedThisMorning().value, ruleVariableValue.value)
        assertEquals(yesterday.toString(), ruleVariableValue.eventDate)
        assertEquals(setOf("1", "2", "3", "4", "5"), ruleVariableValue.candidates.toSet())
    }

    @Test
    fun shouldCreateDayBeforeYesterdayRuleVariableValueWhenEventDateIsYesterdayAndItWasCreatedYesterday() {
        val ruleVariableValue =
            ruleVariablePreviousEvent.createValues(event(yesterday, yesterdayInstant), allEventsDataValues(), emptyMap())

        assertEquals(RuleValueType.NUMERIC, ruleVariableValue.type)
        assertEquals(dayBeforeYesterdayDataValue().value, ruleVariableValue.value)
        assertEquals(dayBeforeYesterday.toString(), ruleVariableValue.eventDate)
        assertEquals(setOf("1", "2", "3", "4", "5"), ruleVariableValue.candidates.toSet())
    }

    @Test
    fun shouldCreateDayBeforeYesterdayRuleVariableValueWhenEventDateIsYesterdayAndCreatedAtClientIsYesterday() {
        val ruleVariableValue =
            ruleVariablePreviousEvent.createValues(event(yesterday, tomorrowInstant, yesterdayInstant), allEventsDataValues(), emptyMap())

        assertEquals(RuleValueType.NUMERIC, ruleVariableValue.type)
        assertEquals(dayBeforeYesterdayDataValue().value, ruleVariableValue.value)
        assertEquals(dayBeforeYesterday.toString(), ruleVariableValue.eventDate)
        assertEquals(setOf("1", "2", "3", "4", "5"), ruleVariableValue.candidates.toSet())
    }

    @Test
    fun shouldGetNoValueWhenThereIsNoPreviousDataValue() {
        val ruleVariableValue =
            ruleVariablePreviousEvent.createValues(event(dayBeforeYesterday, yesterdayInstant), allEventsDataValues(), emptyMap())

        assertEquals(RuleValueType.NUMERIC, ruleVariableValue.type)
        assertNull(ruleVariableValue.value)
        assertNull(ruleVariableValue.eventDate)
        assertEquals(emptyList(), ruleVariableValue.candidates)
    }

    private fun event(
        eventDate: LocalDate,
        createdDate: Instant,
        createdAtClientDate: Instant? = null,
    ): RuleEvent =
        RuleEvent(
            event = "test_event",
            programStage = "test_program_stage",
            programStageName = "",
            status = RuleEventStatus.ACTIVE,
            eventDate = RuleLocalDate.fromLocalDate(eventDate),
            createdDate = RuleInstant.fromInstant(createdDate),
            createdAtClientDate = createdAtClientDate?.let { RuleInstant.fromInstant(it) },
            dueDate = RuleLocalDate.currentDate(),
            completedDate = RuleLocalDate.currentDate(),
            organisationUnit = "",
            organisationUnitCode = "",
            dataValues =
                listOf(
                    RuleDataValue("data_element", "1"),
                    RuleDataValue("data_element", "2"),
                    RuleDataValue("data_element", "3"),
                    RuleDataValue("data_element", "4"),
                    RuleDataValue("data_element", "5"),
                ),
        )

    private fun allEventsDataValues(): Map<String, List<RuleDataValueHistory>> {
        val ruleDataValues =
            mutableListOf(
                todayDataValue(),
                yesterdayDataValueCreatedThisMorning(),
                yesterdayDataValueCreatedThisAfternoon(),
                tomorrowDataValue(),
                dayBeforeYesterdayDataValue(),
            )
        ruleDataValues.sortWith(compareBy<RuleDataValueHistory>({ it.eventDate.localDate }, { it.createdDate }).reversed())

        return mapOf(Pair("data_element", ruleDataValues))
    }

    private fun todayDataValue(): RuleDataValueHistory = RuleDataValueHistory("1", RuleLocalDate.fromLocalDate(today), todayInstant, "")

    private fun yesterdayDataValueCreatedThisMorning(): RuleDataValueHistory = RuleDataValueHistory("2", RuleLocalDate.fromLocalDate(yesterday), todayMorning, "")

    private fun yesterdayDataValueCreatedThisAfternoon(): RuleDataValueHistory =
        RuleDataValueHistory("3", RuleLocalDate.fromLocalDate(yesterday), todayAfternoon, "")

    private fun dayBeforeYesterdayDataValue(): RuleDataValueHistory =
        RuleDataValueHistory("5", RuleLocalDate.fromLocalDate(dayBeforeYesterday), todayAfternoon, "")

    private fun tomorrowDataValue(): RuleDataValueHistory = RuleDataValueHistory("4", RuleLocalDate.fromLocalDate(tomorrow), todayInstant, "")
}
