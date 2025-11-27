package org.hisp.dhis.rules.models

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.minus

import org.hisp.dhis.rules.RuleVariableValueAssert
import org.hisp.dhis.rules.engine.RuleVariableValueMapBuilder
import org.hisp.dhis.rules.utils.currentDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.time.Clock
import kotlin.time.Instant


class RuleVariableValueMapBuilderTest {
    @Test
    fun currentEventVariableShouldContainNullValueForEnrollmentEvaluation() {
        val ruleVariableOne: RuleVariable =
            RuleVariableCurrentEvent(
                "test_variable_one",
                true,
                ArrayList(),
                "test_dataelement_one",
                RuleValueType.TEXT,
            )
        val eventDate = RuleInstant.parse("2015-01-01T01:00:00Z")

        // values from context events should be ignored
        val ruleEnrollment =
            RuleEnrollment(
                "test_enrollment",
                "",
                RuleLocalDate.parse("2015-01-01"),
                RuleLocalDate.parse("2015-01-01"),
                RuleEnrollmentStatus.ACTIVE,
                "",
                "",
                listOf(
                    RuleAttributeValue("test_attribute_one", "test_attribute_value_one"),
                    RuleAttributeValue("test_attribute_two", "test_attribute_value_two"),
                ),
            )
        val contextEventOne =
            RuleEvent(
                "test_context_event_one",
                "test_program_stage",
                "",
                RuleEventStatus.ACTIVE,
                RuleInstant.now(),
                RuleInstant.now(),
                RuleLocalDate.currentDate(),
                null,
                "",
                null,
                listOf(
                    RuleDataValue(
                        "test_dataelement_one",
                        "test_context_value_one",
                    ),
                    RuleDataValue(
                        "test_dataelement_two",
                        "test_context_value_two",
                    ),
                ),
            )
        val valueMap =
            RuleVariableValueMapBuilder()
                .build(emptyMap(), listOf(ruleVariableOne), setOf(contextEventOne), ruleEnrollment)
        RuleVariableValueAssert
            .assertThatVariable(valueMap["test_variable_one"]!!)
            .hasValue(null)
            .isTypeOf(RuleValueType.TEXT)
    }

    @Test
    fun currentEventVariableShouldContainValuesFromCurrentEvent() {
        val ruleVariableOne: RuleVariable =
            RuleVariableCurrentEvent(
                "test_variable_one",
                true,
                ArrayList(),
                "test_dataelement_one",
                RuleValueType.TEXT,
            )
        val ruleVariableTwo: RuleVariable =
            RuleVariableCurrentEvent(
                "test_variable_two",
                true,
                ArrayList(),
                "test_dataelement_two",
                RuleValueType.TEXT,
            )
        val eventInstant = RuleInstant.parse("2015-01-01T01:00:00Z")
        val eventDate = RuleLocalDate.parse("2015-01-01")
        val dueDate = RuleLocalDate.parse("2016-01-01")

        // values from context events should be ignored
        val contextEventOne =
            RuleEvent(
                "test_context_event_one",
                "test_program_stage",
                "",
                RuleEventStatus.ACTIVE,
                RuleInstant.now(),
                RuleInstant.now(),
                RuleLocalDate.currentDate(),
                null,
                "",
                null,
                listOf(
                    RuleDataValue(
                        "test_dataelement_one",
                        "test_context_value_one",
                    ),
                    RuleDataValue(
                        "test_dataelement_two",
                        "test_context_value_two",
                    ),
                ),
            )
        val contextEventTwo =
            RuleEvent(
                "test_context_event_two",
                "test_program_stage",
                "",
                RuleEventStatus.ACTIVE,
                RuleInstant.now(),
                RuleInstant.now(),
                RuleLocalDate.currentDate(),
                null,
                "",
                null,
                listOf(
                    RuleDataValue(
                        "test_dataelement_one",
                        "test_context_value_three",
                    ),
                    RuleDataValue(
                        "test_dataelement_two",
                        "test_context_value_four",
                    ),
                ),
            )
        // values from current ruleEvent should be propagated to the variable values
        val currentEvent =
            RuleEvent(
                "test_event_uid",
                "test_program_stage",
                "",
                RuleEventStatus.ACTIVE,
                eventInstant,
                eventInstant,
                dueDate,
                null,
                "",
                null,
                listOf(
                    RuleDataValue(
                        "test_dataelement_one",
                        "test_value_one",
                    ),
                    RuleDataValue(
                        "test_dataelement_two",
                        "test_value_two",
                    ),
                ),
            )
        val valueMap =
            RuleVariableValueMapBuilder()
                .build(emptyMap(), listOf(ruleVariableOne, ruleVariableTwo), setOf(contextEventOne, contextEventTwo), null, currentEvent)
        assertEquals(14, valueMap.size.toLong())
        RuleVariableValueAssert
            .assertThatVariable(valueMap["current_date"]!!)
            .hasValue(
                currentDate().toString(),
            ).isTypeOf(RuleValueType.DATE)
            .hasCandidates(currentDate().toString())
        RuleVariableValueAssert
            .assertThatVariable(valueMap["event_date"]!!)
            .hasValue(
                eventDate.toString(),
            ).isTypeOf(RuleValueType.DATE)
            .hasCandidates(eventDate.toString())
        RuleVariableValueAssert
            .assertThatVariable(valueMap["event_status"]!!)
            .hasValue(RuleEventStatus.ACTIVE.toString())
            .isTypeOf(RuleValueType.TEXT)
            .hasCandidates(RuleEventStatus.ACTIVE.toString())

        // event count variable should respect current event
        RuleVariableValueAssert
            .assertThatVariable(valueMap["event_count"]!!)
            .hasValue("3")
            .isTypeOf(RuleValueType.NUMERIC)
            .hasCandidates("3")
        RuleVariableValueAssert
            .assertThatVariable(valueMap["event_id"]!!)
            .hasValue("test_event_uid")
            .isTypeOf(RuleValueType.TEXT)
            .hasCandidates("test_event_uid")
        RuleVariableValueAssert
            .assertThatVariable(valueMap["due_date"]!!)
            .hasValue(
                dueDate.toString(),
            ).isTypeOf(RuleValueType.DATE)
            .hasCandidates(dueDate.toString())
        RuleVariableValueAssert
            .assertThatVariable(valueMap["test_variable_one"]!!)
            .hasValue("test_value_one")
            .isTypeOf(RuleValueType.TEXT)
            .hasCandidates("test_value_one")
        RuleVariableValueAssert
            .assertThatVariable(valueMap["test_variable_two"]!!)
            .hasValue("test_value_two")
            .isTypeOf(RuleValueType.TEXT)
            .hasCandidates("test_value_two")
    }

    @Test
    fun currentEventVariableShouldContainValuesFromCurrentEventWhenEventDateIsDistantFuture() {
        val eventInstant = RuleInstant.parse("2015-01-01T01:00:00Z")
        val eventDate = RuleLocalDate.parse("2015-01-01")
        val dueDate = RuleLocalDate.parse("2016-01-01")

        // values from context events should be ignored
        val contextEventOne =
            RuleEvent(
                "test_context_event_one",
                "test_program_stage",
                "",
                RuleEventStatus.ACTIVE,
                RuleInstant.fromInstant(Instant.DISTANT_FUTURE),
                RuleInstant.now(),
                RuleLocalDate.currentDate(),
                null,
                "",
                null,
                listOf(
                    RuleDataValue(
                        "test_dataelement_one",
                        "test_context_value_one",
                    ),
                    RuleDataValue(
                        "test_dataelement_two",
                        "test_context_value_two",
                    ),
                ),
            )

        val valueMap =
            RuleVariableValueMapBuilder()
                .build(emptyMap(), listOf(), setOf(contextEventOne), null, null)

        assertNull(valueMap["event_date"])
    }

    @Test
    fun newestEventProgramVariableShouldContainValueFromNewestContextEvent() {
        val ruleVariableOne: RuleVariable =
            RuleVariableNewestEvent(
                "test_variable_one",
                true,
                ArrayList(),
                "test_dataelement_one",
                RuleValueType.TEXT,
            )
        val ruleVariableTwo: RuleVariable =
            RuleVariableNewestEvent(
                "test_variable_two",
                true,
                ArrayList(),
                "test_dataelement_two",
                RuleValueType.TEXT,
            )
        val oldestEventDate = RuleInstant.parse("2013-01-01T01:00:00Z")
        val newestEventDate = RuleInstant.parse("2017-01-01T01:00:00Z")
        val currentEventInstant = RuleInstant.parse("2015-01-01T01:00:00Z")
        val currentEventDate = RuleLocalDate.parse("2015-01-01")
        val currentEventDueDate: RuleLocalDate? = null
        val oldestRuleEvent =
            RuleEvent(
                "test_event_uid_oldest",
                "test_program_stage",
                "",
                RuleEventStatus.ACTIVE,
                oldestEventDate,
                oldestEventDate,
                RuleLocalDate.currentDate(),
                null,
                "",
                null,
                listOf(
                    RuleDataValue(
                        "test_dataelement_one",
                        "test_value_one_oldest",
                    ),
                    RuleDataValue(
                        "test_dataelement_two",
                        "test_value_two_oldest",
                    ),
                ),
            )
        val newestRuleEvent =
            RuleEvent(
                "test_event_uid_newest",
                "test_program_stage",
                "",
                RuleEventStatus.ACTIVE,
                newestEventDate,
                newestEventDate,
                RuleLocalDate.currentDate(),
                null,
                "",
                null,
                listOf(
                    RuleDataValue(
                        "test_dataelement_one",
                        "test_value_one_newest",
                    ),
                    RuleDataValue(
                        "test_dataelement_two",
                        "test_value_two_newest",
                    ),
                ),
            )
        val currentEvent =
            RuleEvent(
                "test_event_uid_current",
                "test_program_stage",
                "",
                RuleEventStatus.ACTIVE,
                currentEventInstant,
                currentEventInstant,
                currentEventDueDate,
                null,
                "",
                null,
                listOf(
                    RuleDataValue(
                        "test_dataelement_one",
                        "test_value_one_current",
                    ),
                    RuleDataValue(
                        "test_dataelement_two",
                        "test_value_two_current",
                    ),
                ),
            )
        val valueMap =
            RuleVariableValueMapBuilder()
                .build(emptyMap(), listOf(ruleVariableOne, ruleVariableTwo), setOf(oldestRuleEvent, newestRuleEvent), null, currentEvent)
        assertEquals(14, valueMap.size.toLong() )
        RuleVariableValueAssert
            .assertThatVariable(valueMap["current_date"]!!)
            .hasValue(
                currentDate().toString(),
            ).isTypeOf(RuleValueType.DATE)
            .hasCandidates(currentDate().toString())
        RuleVariableValueAssert
            .assertThatVariable(valueMap["event_date"]!!)
            .hasValue(currentEventDate.toString())
            .isTypeOf(RuleValueType.DATE)
            .hasCandidates(currentEventDate.toString())
        RuleVariableValueAssert
            .assertThatVariable(valueMap["event_count"]!!)
            .hasValue("3")
            .isTypeOf(RuleValueType.NUMERIC)
            .hasCandidates("3")
        RuleVariableValueAssert
            .assertThatVariable(valueMap["event_id"]!!)
            .hasValue("test_event_uid_current")
            .isTypeOf(RuleValueType.TEXT)
            .hasCandidates("test_event_uid_current")
        RuleVariableValueAssert
            .assertThatVariable(valueMap["due_date"]!!)
            .hasValue(null)
            .isTypeOf(RuleValueType.DATE)
            .hasCandidates()
        RuleVariableValueAssert
            .assertThatVariable(valueMap["test_variable_one"]!!)
            .hasValue("test_value_one_newest")
            .isTypeOf(RuleValueType.TEXT)
            .hasCandidates(
                "test_value_one_newest",
                "test_value_one_current",
                "test_value_one_oldest",
            )
        RuleVariableValueAssert
            .assertThatVariable(valueMap["test_variable_two"]!!)
            .hasValue("test_value_two_newest")
            .isTypeOf(RuleValueType.TEXT)
            .hasCandidates(
                "test_value_two_newest",
                "test_value_two_current",
                "test_value_two_oldest",
            )
    }

    @Test
    fun newestEventProgramVariableShouldReturnValuesFromCurrentEventWhenIfNewest() {
        val ruleVariableOne: RuleVariable =
            RuleVariableNewestEvent(
                "test_variable_one",
                true,
                ArrayList(),
                "test_dataelement_one",
                RuleValueType.TEXT,
            )
        val ruleVariableTwo: RuleVariable =
            RuleVariableNewestEvent(
                "test_variable_two",
                true,
                ArrayList(),
                "test_dataelement_two",
                RuleValueType.TEXT,
            )
        val dateEventOne = RuleInstant.parse("2013-01-01T01:00:00Z")
        val dateEventTwo = RuleInstant.parse("2014-01-01T01:00:00Z")
        val instantEventCurrent = RuleInstant.parse("2015-01-01T01:00:00Z")
        val dateEventCurrent = RuleLocalDate.parse("2015-01-01")
        val dateEventDueCurrent = RuleLocalDate.parse("2016-01-01")
        val firstRuleEvent =
            RuleEvent(
                "test_event_uid_one",
                "test_program_stage",
                "",
                RuleEventStatus.ACTIVE,
                dateEventOne,
                dateEventOne,
                RuleLocalDate.currentDate(),
                null,
                "",
                null,
                listOf(
                    RuleDataValue(
                        "test_dataelement_one",
                        "test_value_dataelement_one_first",
                    ),
                    RuleDataValue(
                        "test_dataelement_two",
                        "test_value_dataelement_two_first",
                    ),
                ),
            )
        val secondRuleEvent =
            RuleEvent(
                "test_event_uid_two",
                "test_program_stage",
                "",
                RuleEventStatus.ACTIVE,
                dateEventTwo,
                dateEventTwo,
                RuleLocalDate.currentDate(),
                null,
                "",
                null,
                listOf(
                    RuleDataValue(
                        "test_dataelement_one",
                        "test_value_dataelement_one_second",
                    ),
                    RuleDataValue(
                        "test_dataelement_two",
                        "test_value_dataelement_two_second",
                    ),
                ),
            )
        val currentEvent =
            RuleEvent(
                "test_event_uid_current",
                "test_program_stage",
                "",
                RuleEventStatus.ACTIVE,
                instantEventCurrent,
                instantEventCurrent,
                dateEventDueCurrent,
                null,
                "",
                null,
                listOf(
                    RuleDataValue(
                        "test_dataelement_one",
                        "test_value_dataelement_one_current",
                    ),
                    RuleDataValue(
                        "test_dataelement_two",
                        "test_value_dataelement_two_current",
                    ),
                ),
            )
        val valueMap =
            RuleVariableValueMapBuilder()
                .build(emptyMap(), listOf(ruleVariableOne, ruleVariableTwo), setOf(firstRuleEvent, secondRuleEvent), null, currentEvent)
        assertEquals(14, valueMap.size.toLong())
        RuleVariableValueAssert
            .assertThatVariable(valueMap["current_date"]!!)
            .hasValue(
                currentDate().toString(),
            ).isTypeOf(RuleValueType.DATE)
            .hasCandidates(currentDate().toString())
        RuleVariableValueAssert
            .assertThatVariable(valueMap["event_date"]!!)
            .hasValue(dateEventCurrent.toString())
            .isTypeOf(RuleValueType.DATE)
            .hasCandidates(dateEventCurrent.toString())
        RuleVariableValueAssert
            .assertThatVariable(valueMap["event_count"]!!)
            .hasValue("3")
            .isTypeOf(RuleValueType.NUMERIC)
            .hasCandidates("3")
        RuleVariableValueAssert
            .assertThatVariable(valueMap["event_id"]!!)
            .hasValue("test_event_uid_current")
            .isTypeOf(RuleValueType.TEXT)
            .hasCandidates("test_event_uid_current")
        RuleVariableValueAssert
            .assertThatVariable(valueMap["due_date"]!!)
            .hasValue(dateEventDueCurrent.toString())
            .isTypeOf(RuleValueType.DATE)
            .hasCandidates(dateEventDueCurrent.toString())
        RuleVariableValueAssert
            .assertThatVariable(valueMap["test_variable_one"]!!)
            .hasValue("test_value_dataelement_one_current")
            .isTypeOf(RuleValueType.TEXT)
            .hasCandidates(
                "test_value_dataelement_one_current",
                "test_value_dataelement_one_second",
                "test_value_dataelement_one_first",
            )
        RuleVariableValueAssert
            .assertThatVariable(valueMap["test_variable_two"]!!)
            .hasValue("test_value_dataelement_two_current")
            .isTypeOf(RuleValueType.TEXT)
            .hasCandidates(
                "test_value_dataelement_two_current",
                "test_value_dataelement_two_second",
                "test_value_dataelement_two_first",
            )
    }

    @Test
    fun newestEventProgramStageVariableShouldContainValueFromNewestContextEvent() {
        val ruleVariable: RuleVariable =
            RuleVariableNewestStageEvent(
                "test_variable",
                true,
                ArrayList(),
                "test_dataelement",
                RuleValueType.TEXT,
                "test_program_stage_one",
            )
        val dateEventOne = RuleInstant.parse("2014-02-03T01:00:00Z")
        val dateEventTwo = RuleInstant.parse("2014-03-03T01:00:00Z")
        val dateEventThree = RuleInstant.parse("2015-02-03T01:00:00Z")
        val instantEventCurrent = RuleInstant.parse("2011-02-03T01:00:00Z")
        val dateEventCurrent = RuleLocalDate.parse("2011-02-03")
        val dateEventDueCurrent = RuleLocalDate.parse("2011-02-03")
        val eventOne =
            RuleEvent(
                "test_event_uid_one",
                "test_program_stage_one",
                "",
                RuleEventStatus.ACTIVE,
                dateEventOne,
                dateEventOne,
                RuleLocalDate.currentDate(),
                null,
                "",
                null,
                listOf(
                    RuleDataValue(
                        "test_dataelement",
                        "test_value_one",
                    ),
                ),
            )
        val eventTwo =
            RuleEvent(
                "test_event_uid_two",
                "test_program_stage_two",
                "",
                RuleEventStatus.ACTIVE,
                dateEventTwo,
                dateEventTwo,
                RuleLocalDate.currentDate(),
                null,
                "",
                null,
                listOf(
                    RuleDataValue(
                        "test_dataelement",
                        "test_value_two",
                    ),
                ),
            )
        val eventThree =
            RuleEvent(
                "test_event_uid_three",
                "test_program_stage_two",
                "",
                RuleEventStatus.ACTIVE,
                dateEventThree,
                dateEventThree,
                RuleLocalDate.currentDate(),
                null,
                "",
                null,
                listOf(
                    RuleDataValue(
                        "test_dataelement",
                        "test_value_three",
                    ),
                ),
            )
        val eventCurrent =
            RuleEvent(
                "test_event_uid_current",
                "test_program_stage_one",
                "",
                RuleEventStatus.ACTIVE,
                instantEventCurrent,
                instantEventCurrent,
                dateEventDueCurrent,
                null,
                "",
                null,
                listOf(
                    RuleDataValue(
                        "test_dataelement",
                        "test_value_current",
                    ),
                ),
            )
        val valueMap =
            RuleVariableValueMapBuilder()
                .build(emptyMap(), listOf(ruleVariable), setOf(eventOne, eventTwo, eventThree), null, eventCurrent)
        assertEquals(13, valueMap.size.toLong())
        RuleVariableValueAssert
            .assertThatVariable(valueMap["current_date"]!!)
            .hasValue(
                currentDate().toString(),
            ).isTypeOf(RuleValueType.DATE)
            .hasCandidates(currentDate().toString())
        RuleVariableValueAssert
            .assertThatVariable(valueMap["event_date"]!!)
            .hasValue(dateEventCurrent.toString())
            .isTypeOf(RuleValueType.DATE)
            .hasCandidates(dateEventCurrent.toString())
        RuleVariableValueAssert
            .assertThatVariable(valueMap["event_count"]!!)
            .hasValue("4")
            .isTypeOf(RuleValueType.NUMERIC)
            .hasCandidates("4")
        RuleVariableValueAssert
            .assertThatVariable(valueMap["event_id"]!!)
            .hasValue("test_event_uid_current")
            .isTypeOf(RuleValueType.TEXT)
            .hasCandidates("test_event_uid_current")
        RuleVariableValueAssert
            .assertThatVariable(valueMap["due_date"]!!)
            .hasValue(dateEventDueCurrent.toString())
            .isTypeOf(RuleValueType.DATE)
            .hasCandidates(dateEventDueCurrent.toString())
        RuleVariableValueAssert
            .assertThatVariable(valueMap["test_variable"]!!)
            .hasValue("test_value_one")
            .isTypeOf(RuleValueType.TEXT)
            .hasCandidates("test_value_one", "test_value_current")
    }

    @Test
    fun newestEventProgramStageVariableShouldNotContainAnyValues() {
        val ruleVariable: RuleVariable =
            RuleVariableNewestStageEvent(
                "test_variable",
                true,
                ArrayList(),
                "test_dataelement",
                RuleValueType.TEXT,
                "test_program_stage_one",
            )
        val dateEventOne = RuleInstant.parse("2014-03-03T01:00:00Z")
        val instantEventTwo = RuleInstant.parse("2015-02-03T01:00:00Z")
        val dateEventTwo = RuleLocalDate.parse("2015-02-03")
        val dueDateEventTwo = RuleLocalDate.parse("2015-02-03")
        val ruleEventOne =
            RuleEvent(
                "test_event_uid_one",
                "test_program_stage_two",
                "",
                RuleEventStatus.ACTIVE,
                dateEventOne,
                dateEventOne,
                dueDateEventTwo,
                null,
                "",
                null,
                listOf(
                    RuleDataValue(
                        "test_dataelement",
                        "test_value_one",
                    ),
                ),
            )
        val ruleEventTwo =
            RuleEvent(
                "test_event_uid_two",
                "test_program_stage_two",
                "",
                RuleEventStatus.ACTIVE,
                instantEventTwo,
                instantEventTwo,
                dueDateEventTwo,
                null,
                "",
                null,
                listOf(
                    RuleDataValue(
                        "test_dataelement",
                        "test_value_two",
                    ),
                ),
            )
        val valueMap =
            RuleVariableValueMapBuilder()
                .build(emptyMap(), listOf(ruleVariable), setOf(ruleEventOne), null, ruleEventTwo)
        assertEquals(13, valueMap.size.toLong())
        RuleVariableValueAssert
            .assertThatVariable(valueMap["current_date"]!!)
            .hasValue(
                currentDate().toString(),
            ).isTypeOf(RuleValueType.DATE)
            .hasCandidates(currentDate().toString())
        RuleVariableValueAssert
            .assertThatVariable(valueMap["event_date"]!!)
            .hasValue(
                dateEventTwo.toString(),
            ).isTypeOf(RuleValueType.DATE)
            .hasCandidates(dateEventTwo.toString())
        RuleVariableValueAssert
            .assertThatVariable(valueMap["event_count"]!!)
            .hasValue("2")
            .isTypeOf(RuleValueType.NUMERIC)
            .hasCandidates("2")
        RuleVariableValueAssert
            .assertThatVariable(valueMap["event_id"]!!)
            .hasValue("test_event_uid_two")
            .isTypeOf(RuleValueType.TEXT)
            .hasCandidates("test_event_uid_two")
        RuleVariableValueAssert
            .assertThatVariable(valueMap["due_date"]!!)
            .hasValue(
                dueDateEventTwo.toString(),
            ).isTypeOf(RuleValueType.DATE)
            .hasCandidates(dueDateEventTwo.toString())
        RuleVariableValueAssert
            .assertThatVariable(valueMap["test_variable"]!!)
            .hasValue(null)
            .isTypeOf(RuleValueType.TEXT)
            .hasCandidates()
    }

    @Test
    fun previousEventVariableShouldContainValuesFromPreviousEvent() {
        val ruleVariable: RuleVariable =
            RuleVariablePreviousEvent(
                "test_variable",
                true,
                ArrayList(),
                "test_dataelement",
                RuleValueType.TEXT,
            )
        val dateEventOne = RuleInstant.parse("2014-02-03T01:00:00Z")
        val dateEventTwo = RuleInstant.parse("2014-03-03T01:00:00Z")
        val dateEventThree = RuleInstant.parse("2015-02-03T01:00:00Z")
        val instantEventCurrent = RuleInstant.parse("2014-05-03T01:00:00Z")
        val dateEventCurrent = RuleLocalDate.parse("2014-05-03")
        val dueDateEventCurrent = RuleLocalDate.parse("2014-05-03")
        val ruleEventOne =
            RuleEvent(
                "test_event_uid_one",
                "test_program_stage",
                "",
                RuleEventStatus.ACTIVE,
                dateEventOne,
                dateEventOne,
                RuleLocalDate.currentDate(),
                null,
                "",
                null,
                listOf(
                    RuleDataValue(
                        "test_dataelement",
                        "test_value_one",
                    ),
                ),
            )
        val ruleEventTwo =
            RuleEvent(
                "test_event_uid_two",
                "test_program_stage",
                "",
                RuleEventStatus.ACTIVE,
                dateEventTwo,
                dateEventTwo,
                RuleLocalDate.currentDate(),
                null,
                "",
                null,
                listOf(RuleDataValue("test_dataelement", "test_value_two")),
            )
        val ruleEventThree =
            RuleEvent(
                "test_event_uid_three",
                "test_program_stage",
                "",
                RuleEventStatus.ACTIVE,
                dateEventThree,
                dateEventThree,
                RuleLocalDate.currentDate(),
                null,
                "",
                null,
                listOf(
                    RuleDataValue("test_dataelement", "test_value_three"),
                ),
            )
        val ruleEventCurrent =
            RuleEvent(
                "test_event_uid_current",
                "test_program_stage",
                "",
                RuleEventStatus.ACTIVE,
                instantEventCurrent,
                instantEventCurrent,
                dueDateEventCurrent,
                null,
                "",
                null,
                listOf(
                    RuleDataValue(
                        "test_dataelement",
                        "test_value_current",
                    ),
                ),
            )
        val valueMap =
            RuleVariableValueMapBuilder()
                .build(emptyMap(), listOf(ruleVariable), setOf(ruleEventOne, ruleEventTwo, ruleEventThree), null, ruleEventCurrent)
        assertEquals(13, valueMap.size.toLong())
        RuleVariableValueAssert
            .assertThatVariable(valueMap["current_date"]!!)
            .hasValue(
                currentDate().toString(),
            ).isTypeOf(RuleValueType.DATE)
            .hasCandidates(currentDate().toString())
        RuleVariableValueAssert
            .assertThatVariable(valueMap["event_date"]!!)
            .hasValue(dateEventCurrent.toString())
            .isTypeOf(RuleValueType.DATE)
            .hasCandidates(dateEventCurrent.toString())
        RuleVariableValueAssert
            .assertThatVariable(valueMap["event_count"]!!)
            .hasValue("4")
            .isTypeOf(RuleValueType.NUMERIC)
            .hasCandidates("4")
        RuleVariableValueAssert
            .assertThatVariable(valueMap["event_id"]!!)
            .hasValue("test_event_uid_current")
            .isTypeOf(RuleValueType.TEXT)
            .hasCandidates("test_event_uid_current")
        RuleVariableValueAssert
            .assertThatVariable(valueMap["due_date"]!!)
            .hasValue(dueDateEventCurrent.toString())
            .isTypeOf(RuleValueType.DATE)
            .hasCandidates(dueDateEventCurrent.toString())
        RuleVariableValueAssert
            .assertThatVariable(valueMap["test_variable"]!!)
            .hasValue("test_value_two")
            .isTypeOf(RuleValueType.TEXT)
            .hasCandidates("test_value_three", "test_value_current", "test_value_two", "test_value_one")
    }

    @Test
    fun previousEventVariableShouldContainValuesFromPreviousEventWhenTeyAreOnTheSameDate() {
        val ruleVariable: RuleVariable =
            RuleVariablePreviousEvent(
                "test_variable",
                true,
                ArrayList(),
                "test_dataelement",
                RuleValueType.TEXT,
            )
        val dateEventOne = RuleInstant.parse("2014-02-03T01:00:00Z")
        val dateEventTwo = RuleInstant.parse("2014-03-03T01:00:00Z")
        val dateEventThree = RuleInstant.parse("2015-02-03T01:00:00Z")
        val instantEventCurrent = RuleInstant.parse("2014-03-03T01:00:00Z")
        val dateEventCurrent = RuleLocalDate.parse("2014-03-03")
        val dueDateEventCurrent = RuleLocalDate.parse("2014-05-03")
        val ruleEventOne =
            RuleEvent(
                "test_event_uid_one",
                "test_program_stage",
                "",
                RuleEventStatus.ACTIVE,
                dateEventOne,
                dateEventOne,
                RuleLocalDate.currentDate(),
                null,
                "",
                null,
                listOf(
                    RuleDataValue(
                        "test_dataelement",
                        "test_value_one",
                    ),
                ),
            )
        val ruleEventTwo =
            RuleEvent(
                "test_event_uid_two",
                "test_program_stage",
                "",
                RuleEventStatus.ACTIVE,
                dateEventTwo,
                dateEventTwo,
                RuleLocalDate.currentDate(),
                null,
                "",
                null,
                listOf(RuleDataValue("test_dataelement", "test_value_two")),
            )
        val ruleEventThree =
            RuleEvent(
                "test_event_uid_three",
                "test_program_stage",
                "",
                RuleEventStatus.ACTIVE,
                dateEventThree,
                dateEventThree,
                RuleLocalDate.currentDate(),
                null,
                "",
                null,
                listOf(
                    RuleDataValue("test_dataelement", "test_value_three"),
                ),
            )
        val ruleEventCurrent =
            RuleEvent(
                "test_event_uid_current",
                "test_program_stage",
                "",
                RuleEventStatus.ACTIVE,
                instantEventCurrent,
                dateEventOne,
                dueDateEventCurrent,
                null,
                "",
                null,
                listOf(
                    RuleDataValue(
                        "test_dataelement",
                        "test_value_current",
                    ),
                ),
            )
        val valueMap =
            RuleVariableValueMapBuilder()
                .build(emptyMap(), listOf(ruleVariable), setOf(ruleEventOne, ruleEventTwo, ruleEventThree), null, ruleEventCurrent)
        assertEquals(13, valueMap.size.toLong())
        RuleVariableValueAssert
            .assertThatVariable(valueMap["current_date"]!!)
            .hasValue(
                currentDate().toString(),
            ).isTypeOf(RuleValueType.DATE)
            .hasCandidates(currentDate().toString())
        RuleVariableValueAssert
            .assertThatVariable(valueMap["event_date"]!!)
            .hasValue(dateEventCurrent.toString())
            .isTypeOf(RuleValueType.DATE)
            .hasCandidates(dateEventCurrent.toString())
        RuleVariableValueAssert
            .assertThatVariable(valueMap["event_count"]!!)
            .hasValue("4")
            .isTypeOf(RuleValueType.NUMERIC)
            .hasCandidates("4")
        RuleVariableValueAssert
            .assertThatVariable(valueMap["event_id"]!!)
            .hasValue("test_event_uid_current")
            .isTypeOf(RuleValueType.TEXT)
            .hasCandidates("test_event_uid_current")
        RuleVariableValueAssert
            .assertThatVariable(valueMap["due_date"]!!)
            .hasValue(dueDateEventCurrent.toString())
            .isTypeOf(RuleValueType.DATE)
            .hasCandidates(dueDateEventCurrent.toString())
        RuleVariableValueAssert
            .assertThatVariable(valueMap["test_variable"]!!)
            .hasValue("test_value_one")
            .isTypeOf(RuleValueType.TEXT)
            .hasCandidates("test_value_three", "test_value_two", "test_value_current", "test_value_one")
    }

    @Test
    fun attributeVariableShouldContainValuesFromContextEnrollment() {
        val ruleVariableOne: RuleVariable =
            RuleVariableAttribute(
                "test_variable_one",
                true,
                ArrayList(),
                "test_attribute_one",
                RuleValueType.TEXT,
            )
        val ruleVariableTwo: RuleVariable =
            RuleVariableAttribute(
                "test_variable_two",
                true,
                ArrayList(),
                "test_attribute_two",
                RuleValueType.TEXT,
            )
        val eventInstant = RuleInstant.parse("2015-01-01T01:00:00Z")
        val eventDate = RuleLocalDate.parse("2015-01-01")
        val dueEventDate = RuleLocalDate.parse("2015-01-01")
        val enrollmentDate = RuleLocalDate.parse("2014-03-01")

        // values from enrollment should end up in ruleVariables
        val ruleEnrollment =
            RuleEnrollment(
                "test_enrollment",
                "",
                enrollmentDate,
                enrollmentDate,
                RuleEnrollmentStatus.ACTIVE,
                "",
                "",
                listOf(
                    RuleAttributeValue("test_attribute_one", "test_attribute_value_one"),
                    RuleAttributeValue("test_attribute_two", "test_attribute_value_two"),
                ),
            )

        // values from context events should be ignored
        val contextEvent =
            RuleEvent(
                "test_context_event_one",
                "test_program_stage",
                "",
                RuleEventStatus.ACTIVE,
                eventInstant,
                eventInstant,
                dueEventDate,
                null,
                "",
                null,
                listOf(
                    RuleDataValue(
                        "test_dataelement_one",
                        "test_context_value_one",
                    ),
                    RuleDataValue(
                        "test_dataelement_two",
                        "test_context_value_two",
                    ),
                ),
            )
        val currentEvent =
            RuleEvent(
                "test_event_uid",
                "test_program_stage",
                "",
                RuleEventStatus.ACTIVE,
                eventInstant,
                eventInstant,
                dueEventDate,
                null,
                "",
                null,
                listOf(
                    RuleDataValue("test_dataelement_one", "test_value_one"),
                    RuleDataValue("test_dataelement_two", "test_value_two"),
                ),
            )

        // here we will expect correct values to be returned
        val valueMap =
            RuleVariableValueMapBuilder()
                .build(emptyMap(), listOf(ruleVariableOne, ruleVariableTwo), setOf(contextEvent), ruleEnrollment, currentEvent)
        assertEquals(21, valueMap.size.toLong())
        RuleVariableValueAssert
            .assertThatVariable(valueMap["current_date"]!!)
            .hasValue(
                currentDate().toString(),
            ).isTypeOf(RuleValueType.DATE)
            .hasCandidates(currentDate().toString())
        RuleVariableValueAssert
            .assertThatVariable(valueMap["event_date"]!!)
            .hasValue(
                eventDate.toString(),
            ).isTypeOf(RuleValueType.DATE)
            .hasCandidates(eventDate.toString())
        RuleVariableValueAssert
            .assertThatVariable(valueMap["event_count"]!!)
            .hasValue("2")
            .isTypeOf(RuleValueType.NUMERIC)
            .hasCandidates("2")
        RuleVariableValueAssert
            .assertThatVariable(valueMap["event_id"]!!)
            .hasValue("test_event_uid")
            .isTypeOf(RuleValueType.TEXT)
            .hasCandidates("test_event_uid")
        RuleVariableValueAssert
            .assertThatVariable(valueMap["due_date"]!!)
            .hasValue(
                dueEventDate.toString(),
            ).isTypeOf(RuleValueType.DATE)
            .hasCandidates(dueEventDate.toString())
        RuleVariableValueAssert
            .assertThatVariable(valueMap["enrollment_status"]!!)
            .hasValue(RuleEnrollmentStatus.ACTIVE.toString())
            .isTypeOf(RuleValueType.TEXT)
            .hasCandidates(RuleEnrollmentStatus.ACTIVE.toString())
        RuleVariableValueAssert
            .assertThatVariable(valueMap["enrollment_date"]!!)
            .hasValue(enrollmentDate.toString())
            .isTypeOf(RuleValueType.DATE)
            .hasCandidates(enrollmentDate.toString())
        RuleVariableValueAssert
            .assertThatVariable(valueMap["enrollment_id"]!!)
            .hasValue("test_enrollment")
            .isTypeOf(RuleValueType.TEXT)
            .hasCandidates("test_enrollment")
        RuleVariableValueAssert
            .assertThatVariable(valueMap["enrollment_count"]!!)
            .hasValue("1")
            .isTypeOf(RuleValueType.NUMERIC)
            .hasCandidates("1")
        RuleVariableValueAssert
            .assertThatVariable(valueMap["incident_date"]!!)
            .hasValue(enrollmentDate.toString())
            .isTypeOf(RuleValueType.DATE)
            .hasCandidates(enrollmentDate.toString())
        RuleVariableValueAssert
            .assertThatVariable(valueMap["tei_count"]!!)
            .hasValue("1")
            .isTypeOf(RuleValueType.NUMERIC)
            .hasCandidates("1")
        RuleVariableValueAssert
            .assertThatVariable(valueMap["test_variable_one"]!!)
            .hasValue("test_attribute_value_one")
            .isTypeOf(RuleValueType.TEXT)
            .hasCandidates("test_attribute_value_one")
        RuleVariableValueAssert
            .assertThatVariable(valueMap["test_variable_two"]!!)
            .hasValue("test_attribute_value_two")
            .isTypeOf(RuleValueType.TEXT)
            .hasCandidates("test_attribute_value_two")
    }

    @Test
    fun ruleEnrollmentValuesShouldBePropagatedToMapCorrectly() {
        val ruleVariableOne: RuleVariable =
            RuleVariableAttribute(
                "test_variable_one",
                true,
                ArrayList(),
                "test_attribute_one",
                RuleValueType.NUMERIC,
            )
        val ruleVariableTwo: RuleVariable =
            RuleVariableAttribute(
                "test_variable_two",
                true,
                ArrayList(),
                "test_attribute_two",
                RuleValueType.TEXT,
            )
        val ruleVariableThree: RuleVariable =
            RuleVariableCurrentEvent(
                "test_variable_three",
                true,
                ArrayList(),
                "test_dataelement_one",
                RuleValueType.BOOLEAN,
            )
        val currentDate = currentDate()
        val enrollmentDate = RuleLocalDate.parse("2017-02-02")
        val incidentDate = RuleLocalDate.parse("2017-04-02")
        val ruleEnrollment =
            RuleEnrollment(
                "test_enrollment",
                "",
                incidentDate,
                enrollmentDate,
                RuleEnrollmentStatus.ACTIVE,
                "",
                "",
                listOf(
                    RuleAttributeValue("test_attribute_one", "test_attribute_value_one"),
                    RuleAttributeValue("test_attribute_two", "test_attribute_value_two"),
                    RuleAttributeValue("test_attribute_three", "test_attribute_value_three"),
                ),
            )
        val ruleEventOne =
            RuleEvent(
                "test_event_one",
                "test_program_stage",
                "",
                RuleEventStatus.ACTIVE,
                RuleInstant.now(),
                RuleInstant.now(),
                RuleLocalDate.currentDate(),
                null,
                "",
                null,
                ArrayList<RuleDataValue>(),
            )
        val ruleEventTwo =
            RuleEvent(
                "test_event_two",
                "test_program_stage",
                "",
                RuleEventStatus.ACTIVE,
                RuleInstant.now(),
                RuleInstant.now(),
                RuleLocalDate.currentDate(),
                null,
                "",
                null,
                ArrayList<RuleDataValue>(),
            )
        val valueMap =
            RuleVariableValueMapBuilder()
                .build(
                    emptyMap(),
                    listOf(ruleVariableOne, ruleVariableTwo, ruleVariableThree),
                    setOf(ruleEventOne, ruleEventTwo),
                    ruleEnrollment,
                )
        assertEquals(15, valueMap.size.toLong())
        RuleVariableValueAssert
            .assertThatVariable(valueMap["current_date"]!!)
            .hasValue(currentDate.toString())
            .isTypeOf(RuleValueType.DATE)
            .hasCandidates(currentDate.toString())
        RuleVariableValueAssert
            .assertThatVariable(valueMap["event_count"]!!)
            .hasValue("2")
            .isTypeOf(RuleValueType.NUMERIC)
            .hasCandidates("2")
        RuleVariableValueAssert
            .assertThatVariable(valueMap["enrollment_date"]!!)
            .hasValue(enrollmentDate.toString())
            .isTypeOf(RuleValueType.DATE)
            .hasCandidates(enrollmentDate.toString())
        RuleVariableValueAssert
            .assertThatVariable(valueMap["enrollment_id"]!!)
            .hasValue("test_enrollment")
            .isTypeOf(RuleValueType.TEXT)
            .hasCandidates("test_enrollment")
        RuleVariableValueAssert
            .assertThatVariable(valueMap["enrollment_count"]!!)
            .hasValue("1")
            .isTypeOf(RuleValueType.NUMERIC)
            .hasCandidates("1")
        RuleVariableValueAssert
            .assertThatVariable(valueMap["incident_date"]!!)
            .hasValue(incidentDate.toString())
            .isTypeOf(RuleValueType.DATE)
            .hasCandidates(incidentDate.toString())
        RuleVariableValueAssert
            .assertThatVariable(valueMap["tei_count"]!!)
            .hasValue("1")
            .isTypeOf(RuleValueType.NUMERIC)
            .hasCandidates("1")
        RuleVariableValueAssert
            .assertThatVariable(valueMap["test_variable_one"]!!)
            .hasValue("test_attribute_value_one")
            .isTypeOf(RuleValueType.NUMERIC)
            .hasCandidates("test_attribute_value_one")
        RuleVariableValueAssert
            .assertThatVariable(valueMap["test_variable_two"]!!)
            .isTypeOf(RuleValueType.TEXT)
            .hasValue("test_attribute_value_two")
            .hasCandidates("test_attribute_value_two")
    }

    @Test
    fun MultipleMapBuilderShoulCreateCorrectMapForEnrollmentAndEvents() {
        val ruleVariableOne: RuleVariable =
            RuleVariableAttribute(
                "test_variable_one",
                true,
                ArrayList(),
                "test_attribute_one",
                RuleValueType.NUMERIC,
            )
        val ruleVariableTwo: RuleVariable =
            RuleVariableAttribute(
                "test_variable_two",
                true,
                ArrayList(),
                "test_attribute_two",
                RuleValueType.TEXT,
            )
        val ruleVariableThree: RuleVariable =
            RuleVariableCurrentEvent(
                "test_variable_three",
                true,
                ArrayList(),
                "test_dataelement_one",
                RuleValueType.BOOLEAN,
            )
        val currentDate = currentDate()
        val enrollmentDate = RuleLocalDate.parse("2017-02-02")
        val incidentDate = RuleLocalDate.parse("2017-04-02")
        val ruleEnrollment =
            RuleEnrollment(
                "test_enrollment",
                "",
                incidentDate,
                enrollmentDate,
                RuleEnrollmentStatus.ACTIVE,
                "",
                "",
                listOf(
                    RuleAttributeValue("test_attribute_one", "test_attribute_value_one"),
                    RuleAttributeValue("test_attribute_two", "test_attribute_value_two"),
                    RuleAttributeValue("test_attribute_three", "test_attribute_value_three"),
                ),
            )
        val now = currentDate()
        val eventOneInstant = now.minus(1, DateTimeUnit.DAY).atStartOfDayIn(TimeZone.currentSystemDefault())
        val eventOneDate = now.minus(1, DateTimeUnit.DAY)
        val eventOneDueDate = now.minus(2, DateTimeUnit.DAY)
        val eventTwoInstant = now.minus(3, DateTimeUnit.DAY).atStartOfDayIn(TimeZone.currentSystemDefault())
        val eventTwoDate = now.minus(3, DateTimeUnit.DAY)
        val eventTwoDueDate = now.minus(4, DateTimeUnit.DAY)
        val ruleEventOne =
            RuleEvent(
                "test_event_one",
                "test_program_stage",
                "",
                RuleEventStatus.ACTIVE,
                RuleInstant.fromInstant(eventOneInstant),
                    RuleInstant.fromInstant(eventOneInstant),
                RuleLocalDate.fromLocalDate(eventOneDueDate),
                null,
                "",
                null,
                ArrayList<RuleDataValue>(),
            )
        val ruleEventTwo =
            RuleEvent(
                "test_event_two",
                "test_program_stage",
                "",
                RuleEventStatus.ACTIVE,
                RuleInstant.fromInstant(eventTwoInstant),
                    RuleInstant.fromInstant(eventTwoInstant),
                RuleLocalDate.fromLocalDate(eventTwoDueDate),
                null,
                "",
                null,
                ArrayList<RuleDataValue>(),
            )
        val (enrollmentMap, eventMap) =
            RuleVariableValueMapBuilder()
                .multipleBuild(
                    emptyMap(),
                    listOf(ruleVariableOne, ruleVariableTwo, ruleVariableThree),
                    setOf(ruleEventOne, ruleEventTwo),
                    ruleEnrollment,
                )
        assertEquals(1, enrollmentMap.size.toLong())
        assertEquals(2, eventMap.size.toLong())
        val enrollmentValueMap = enrollmentMap[ruleEnrollment]
        val eventOneValueMap = eventMap[ruleEventOne]
        val eventTwoValueMap = eventMap[ruleEventTwo]

        // Enrollment
        RuleVariableValueAssert
            .assertThatVariable(enrollmentValueMap!!["current_date"]!!)
            .hasValue(currentDate.toString())
            .isTypeOf(RuleValueType.DATE)
            .hasCandidates(currentDate.toString())
        RuleVariableValueAssert
            .assertThatVariable(enrollmentValueMap["event_count"]!!)
            .hasValue("2")
            .isTypeOf(RuleValueType.NUMERIC)
            .hasCandidates("2")
        RuleVariableValueAssert
            .assertThatVariable(enrollmentValueMap["enrollment_date"]!!)
            .hasValue(enrollmentDate.toString())
            .isTypeOf(RuleValueType.DATE)
            .hasCandidates(enrollmentDate.toString())
        RuleVariableValueAssert
            .assertThatVariable(enrollmentValueMap["enrollment_id"]!!)
            .hasValue("test_enrollment")
            .isTypeOf(RuleValueType.TEXT)
            .hasCandidates("test_enrollment")
        RuleVariableValueAssert
            .assertThatVariable(enrollmentValueMap["enrollment_count"]!!)
            .hasValue("1")
            .isTypeOf(RuleValueType.NUMERIC)
            .hasCandidates("1")
        RuleVariableValueAssert
            .assertThatVariable(enrollmentValueMap["incident_date"]!!)
            .hasValue(incidentDate.toString())
            .isTypeOf(RuleValueType.DATE)
            .hasCandidates(incidentDate.toString())
        RuleVariableValueAssert
            .assertThatVariable(enrollmentValueMap["tei_count"]!!)
            .hasValue("1")
            .isTypeOf(RuleValueType.NUMERIC)
            .hasCandidates("1")
        RuleVariableValueAssert
            .assertThatVariable(enrollmentValueMap["test_variable_one"]!!)
            .hasValue("test_attribute_value_one")
            .isTypeOf(RuleValueType.NUMERIC)
            .hasCandidates("test_attribute_value_one")
        RuleVariableValueAssert
            .assertThatVariable(enrollmentValueMap["test_variable_two"]!!)
            .isTypeOf(RuleValueType.TEXT)
            .hasValue("test_attribute_value_two")
            .hasCandidates("test_attribute_value_two")

        // Event one
        RuleVariableValueAssert
            .assertThatVariable(eventOneValueMap!!["current_date"]!!)
            .hasValue(currentDate.toString())
            .isTypeOf(RuleValueType.DATE)
            .hasCandidates(currentDate.toString())
        RuleVariableValueAssert
            .assertThatVariable(eventOneValueMap["event_count"]!!)
            .hasValue("2")
            .isTypeOf(RuleValueType.NUMERIC)
            .hasCandidates("2")
        RuleVariableValueAssert
            .assertThatVariable(eventOneValueMap["enrollment_date"]!!)
            .hasValue(enrollmentDate.toString())
            .isTypeOf(RuleValueType.DATE)
            .hasCandidates(enrollmentDate.toString())
        RuleVariableValueAssert
            .assertThatVariable(eventOneValueMap["enrollment_id"]!!)
            .hasValue("test_enrollment")
            .isTypeOf(RuleValueType.TEXT)
            .hasCandidates("test_enrollment")
        RuleVariableValueAssert
            .assertThatVariable(eventOneValueMap["enrollment_count"]!!)
            .hasValue("1")
            .isTypeOf(RuleValueType.NUMERIC)
            .hasCandidates("1")
        RuleVariableValueAssert
            .assertThatVariable(eventOneValueMap["incident_date"]!!)
            .hasValue(incidentDate.toString())
            .isTypeOf(RuleValueType.DATE)
            .hasCandidates(incidentDate.toString())
        RuleVariableValueAssert
            .assertThatVariable(eventOneValueMap["tei_count"]!!)
            .hasValue("1")
            .isTypeOf(RuleValueType.NUMERIC)
            .hasCandidates("1")
        RuleVariableValueAssert
            .assertThatVariable(eventOneValueMap["test_variable_one"]!!)
            .hasValue("test_attribute_value_one")
            .isTypeOf(RuleValueType.NUMERIC)
            .hasCandidates("test_attribute_value_one")
        RuleVariableValueAssert
            .assertThatVariable(eventOneValueMap["test_variable_two"]!!)
            .isTypeOf(
                RuleValueType.TEXT,
            ).hasValue("test_attribute_value_two")
            .hasCandidates("test_attribute_value_two")
        RuleVariableValueAssert
            .assertThatVariable(eventOneValueMap["event_date"]!!)
            .hasValue(eventOneDate.toString())
            .isTypeOf(RuleValueType.DATE)
            .hasCandidates(eventOneDate.toString())
        RuleVariableValueAssert
            .assertThatVariable(eventOneValueMap["due_date"]!!)
            .hasValue(eventOneDueDate.toString())
            .isTypeOf(RuleValueType.DATE)
            .hasCandidates(eventOneDueDate.toString())

        // Event two
        RuleVariableValueAssert
            .assertThatVariable(eventTwoValueMap!!["current_date"]!!)
            .hasValue(currentDate.toString())
            .isTypeOf(RuleValueType.DATE)
            .hasCandidates(currentDate.toString())
        RuleVariableValueAssert
            .assertThatVariable(eventTwoValueMap["event_count"]!!)
            .hasValue("2")
            .isTypeOf(RuleValueType.NUMERIC)
            .hasCandidates("2")
        RuleVariableValueAssert
            .assertThatVariable(eventTwoValueMap["enrollment_date"]!!)
            .hasValue(enrollmentDate.toString())
            .isTypeOf(RuleValueType.DATE)
            .hasCandidates(enrollmentDate.toString())
        RuleVariableValueAssert
            .assertThatVariable(eventTwoValueMap["enrollment_id"]!!)
            .hasValue("test_enrollment")
            .isTypeOf(RuleValueType.TEXT)
            .hasCandidates("test_enrollment")
        RuleVariableValueAssert
            .assertThatVariable(eventTwoValueMap["enrollment_count"]!!)
            .hasValue("1")
            .isTypeOf(RuleValueType.NUMERIC)
            .hasCandidates("1")
        RuleVariableValueAssert
            .assertThatVariable(eventTwoValueMap["incident_date"]!!)
            .hasValue(incidentDate.toString())
            .isTypeOf(RuleValueType.DATE)
            .hasCandidates(incidentDate.toString())
        RuleVariableValueAssert
            .assertThatVariable(eventTwoValueMap["tei_count"]!!)
            .hasValue("1")
            .isTypeOf(RuleValueType.NUMERIC)
            .hasCandidates("1")
        RuleVariableValueAssert
            .assertThatVariable(eventTwoValueMap["test_variable_one"]!!)
            .hasValue("test_attribute_value_one")
            .isTypeOf(RuleValueType.NUMERIC)
            .hasCandidates("test_attribute_value_one")
        RuleVariableValueAssert
            .assertThatVariable(eventTwoValueMap["test_variable_two"]!!)
            .isTypeOf(
                RuleValueType.TEXT,
            ).hasValue("test_attribute_value_two")
            .hasCandidates("test_attribute_value_two")
        RuleVariableValueAssert
            .assertThatVariable(eventTwoValueMap["event_date"]!!)
            .hasValue(eventTwoDate.toString())
            .isTypeOf(RuleValueType.DATE)
            .hasCandidates(eventTwoDate.toString())
        RuleVariableValueAssert
            .assertThatVariable(eventTwoValueMap["due_date"]!!)
            .hasValue(eventTwoDueDate.toString())
            .isTypeOf(RuleValueType.DATE)
            .hasCandidates(eventTwoDueDate.toString())
    }
}
