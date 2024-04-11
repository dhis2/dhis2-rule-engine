package org.hisp.dhis.rules.models

import kotlinx.datetime.*
import org.hisp.dhis.rules.RuleVariableValueAssert
import org.hisp.dhis.rules.engine.RuleVariableValueMapBuilder
import org.hisp.dhis.rules.utils.currentDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class RuleVariableValueMapBuilderTest {
    @Test
    fun currentEventVariableShouldContainNullValueForEnrollmentEvaluation() {
        val ruleVariableOne: RuleVariable = RuleVariableCurrentEvent(
            "test_variable_one", true, ArrayList(), "test_dataelement_one", RuleValueType.TEXT
        )
        val eventDate = Instant.parse("2015-01-01T01:00:00Z")

        // values from context events should be ignored
        val ruleEnrollment = RuleEnrollment(
            "test_enrollment", "",
            LocalDate.parse("2015-01-01"), LocalDate.parse("2015-01-01"),
            RuleEnrollmentStatus.ACTIVE, "", "", listOf(
                RuleAttributeValue("test_attribute_one", "test_attribute_value_one"),
                RuleAttributeValue("test_attribute_two", "test_attribute_value_two")
            )
        )
        val contextEventOne = RuleEvent(
            "test_context_event_one",
            "test_program_stage",
            "",
            RuleEventStatus.ACTIVE,
            Clock.System.now(),
            LocalDate.currentDate(),
            null,
            "",
            null,
            listOf(
                RuleDataValue(
                    eventDate, "test_program_stage",
                    "test_dataelement_one", "test_context_value_one"
                ),
                RuleDataValue(
                    eventDate, "test_program_stage",
                    "test_dataelement_two", "test_context_value_two"
                )
            )
        )
        val valueMap = RuleVariableValueMapBuilder.target(ruleEnrollment)
            .ruleVariables( listOf(ruleVariableOne))
            .ruleEvents( listOf(contextEventOne))
            .triggerEnvironment(TriggerEnvironment.SERVER)
            .build()
        RuleVariableValueAssert.assertThatVariable(valueMap["test_variable_one"]!!).hasValue(null)
            .isTypeOf(RuleValueType.TEXT)
    }

    @Test
    fun currentEventVariableShouldContainValuesFromCurrentEvent() {
        val ruleVariableOne: RuleVariable = RuleVariableCurrentEvent(
            "test_variable_one", true, ArrayList(), "test_dataelement_one", RuleValueType.TEXT
        )
        val ruleVariableTwo: RuleVariable = RuleVariableCurrentEvent(
            "test_variable_two", true, ArrayList(), "test_dataelement_two", RuleValueType.TEXT
        )
        val eventInstant = Instant.parse("2015-01-01T01:00:00Z")
        val eventDate = LocalDate.parse("2015-01-01")
        val dueDate = LocalDate.parse("2016-01-01")

        // values from context events should be ignored
        val contextEventOne = RuleEvent(
            "test_context_event_one",
            "test_program_stage",
            "",
            RuleEventStatus.ACTIVE,
            Clock.System.now(),
            LocalDate.currentDate(),
            null,
            "",
            null,
            listOf(
                RuleDataValue(
                    eventInstant, "test_program_stage",
                    "test_dataelement_one", "test_context_value_one"
                ),
                RuleDataValue(
                    eventInstant, "test_program_stage",
                    "test_dataelement_two", "test_context_value_two"
                )
            )
        )
        val contextEventTwo = RuleEvent(
            "test_context_event_two",
            "test_program_stage",
            "",
            RuleEventStatus.ACTIVE,
            Clock.System.now(),
            LocalDate.currentDate(),
            null,
            "",
            null,
            listOf(
                RuleDataValue(
                    eventInstant, "test_program_stage",
                    "test_dataelement_one", "test_context_value_three"
                ),
                RuleDataValue(
                    eventInstant, "test_program_stage",
                    "test_dataelement_two", "test_context_value_four"
                )
            )
        )
        // values from current ruleEvent should be propagated to the variable values
        val currentEvent = RuleEvent(
            "test_event_uid", "test_program_stage", "",
            RuleEventStatus.ACTIVE, eventInstant, dueDate, null, "", null, listOf(
                RuleDataValue(
                    eventInstant, "test_program_stage",
                    "test_dataelement_one", "test_value_one"
                ),
                RuleDataValue(
                    eventInstant, "test_program_stage",
                    "test_dataelement_two", "test_value_two"
                )
            )
        )
        val valueMap = RuleVariableValueMapBuilder.target(currentEvent)
            .ruleVariables( listOf(ruleVariableOne, ruleVariableTwo))
            .ruleEvents( listOf(contextEventOne, contextEventTwo))
            .triggerEnvironment(TriggerEnvironment.SERVER)
            .build()
        assertEquals(valueMap.size.toLong(), 13)
        RuleVariableValueAssert.assertThatVariable(valueMap["current_date"]!!).hasValue(
            LocalDate.Companion.currentDate().toString()
        )
            .isTypeOf(RuleValueType.TEXT).hasCandidates(LocalDate.Companion.currentDate().toString())
        RuleVariableValueAssert.assertThatVariable(valueMap["event_date"]!!).hasValue(
            eventDate.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(eventDate.toString())
        RuleVariableValueAssert.assertThatVariable(valueMap["event_status"]!!)
            .hasValue(RuleEventStatus.ACTIVE.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(RuleEventStatus.ACTIVE.toString())

        // event count variable should respect current event
        RuleVariableValueAssert.assertThatVariable(valueMap["event_count"]!!).hasValue("3")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("3")
        RuleVariableValueAssert.assertThatVariable(valueMap["event_id"]!!).hasValue("test_event_uid")
            .isTypeOf(RuleValueType.TEXT).hasCandidates("test_event_uid")
        RuleVariableValueAssert.assertThatVariable(valueMap["due_date"]!!).hasValue(
                dueDate.toString()
        )
            .isTypeOf(RuleValueType.TEXT).hasCandidates(dueDate.toString())
        RuleVariableValueAssert.assertThatVariable(valueMap["test_variable_one"]!!).hasValue("test_value_one")
            .isTypeOf(RuleValueType.TEXT).hasCandidates("test_value_one")
        RuleVariableValueAssert.assertThatVariable(valueMap["test_variable_two"]!!).hasValue("test_value_two")
            .isTypeOf(RuleValueType.TEXT).hasCandidates("test_value_two")
    }

    @Test
    fun newestEventProgramVariableShouldContainValueFromNewestContextEvent() {
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_variable_one", true, ArrayList(), "test_dataelement_one", RuleValueType.TEXT
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent(
            "test_variable_two", true, ArrayList(), "test_dataelement_two", RuleValueType.TEXT
        )
        val oldestEventDate = Instant.parse("2013-01-01T01:00:00Z")
        val newestEventDate = Instant.parse("2017-01-01T01:00:00Z")
        val currentEventInstant = Instant.parse("2015-01-01T01:00:00Z")
        val currentEventDate = LocalDate.parse("2015-01-01")
        val currentEventDueDate: LocalDate? = null
        val oldestRuleEvent = RuleEvent(
            "test_event_uid_oldest",
            "test_program_stage",
            "",
            RuleEventStatus.ACTIVE,
            oldestEventDate,
            LocalDate.currentDate(),
            null,
            "",
            null,
            listOf(
                RuleDataValue(
                    oldestEventDate, "test_program_stage",
                    "test_dataelement_one", "test_value_one_oldest"
                ),
                RuleDataValue(
                    oldestEventDate, "test_program_stage",
                    "test_dataelement_two", "test_value_two_oldest"
                )
            )
        )
        val newestRuleEvent = RuleEvent(
            "test_event_uid_newest",
            "test_program_stage",
            "",
            RuleEventStatus.ACTIVE,
            newestEventDate,
            LocalDate.currentDate(),
            null,
            "",
            null,
            listOf(
                RuleDataValue(
                    newestEventDate, "test_program_stage",
                    "test_dataelement_one", "test_value_one_newest"
                ),
                RuleDataValue(
                    newestEventDate, "test_program_stage",
                    "test_dataelement_two", "test_value_two_newest"
                )
            )
        )
        val currentEvent = RuleEvent(
            "test_event_uid_current",
            "test_program_stage",
            "",
            RuleEventStatus.ACTIVE,
            currentEventInstant,
            currentEventDueDate,
            null,
            "",
            null,
            listOf(
                RuleDataValue(
                    currentEventInstant, "test_program_stage",
                    "test_dataelement_one", "test_value_one_current"
                ),
                RuleDataValue(
                    currentEventInstant, "test_program_stage",
                    "test_dataelement_two", "test_value_two_current"
                )
            )
        )
        val valueMap = RuleVariableValueMapBuilder.target(currentEvent)
            .ruleVariables( listOf(ruleVariableOne, ruleVariableTwo))
            .ruleEvents( listOf(oldestRuleEvent, newestRuleEvent))
            .triggerEnvironment(TriggerEnvironment.SERVER)
            .build()
        assertEquals(valueMap.size.toLong(), 12)
        RuleVariableValueAssert.assertThatVariable(valueMap["current_date"]!!).hasValue(
                LocalDate.Companion.currentDate().toString()
        )
            .isTypeOf(RuleValueType.TEXT).hasCandidates(LocalDate.Companion.currentDate().toString())
        RuleVariableValueAssert.assertThatVariable(valueMap["event_date"]!!)
            .hasValue(currentEventDate.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(currentEventDate.toString())
        RuleVariableValueAssert.assertThatVariable(valueMap["event_count"]!!).hasValue("3")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("3")
        RuleVariableValueAssert.assertThatVariable(valueMap["event_id"]!!).hasValue("test_event_uid_current")
            .isTypeOf(RuleValueType.TEXT).hasCandidates("test_event_uid_current")
        assertNull(valueMap["due_date"])
        RuleVariableValueAssert.assertThatVariable(valueMap["test_variable_one"]!!).hasValue("test_value_one_newest")
            .isTypeOf(RuleValueType.TEXT).hasCandidates(
                "test_value_one_newest",
                "test_value_one_current", "test_value_one_oldest"
            )
        RuleVariableValueAssert.assertThatVariable(valueMap["test_variable_two"]!!).hasValue("test_value_two_newest")
            .isTypeOf(RuleValueType.TEXT).hasCandidates(
                "test_value_two_newest",
                "test_value_two_current", "test_value_two_oldest"
            )
    }

    @Test
    fun newestEventProgramVariableShouldReturnValuesFromCurrentEventWhenIfNewest() {
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_variable_one", true, ArrayList(), "test_dataelement_one", RuleValueType.TEXT
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent(
            "test_variable_two", true, ArrayList(), "test_dataelement_two", RuleValueType.TEXT
        )
        val dateEventOne = Instant.parse("2013-01-01T01:00:00Z")
        val dateEventTwo = Instant.parse("2014-01-01T01:00:00Z")
        val instantEventCurrent = Instant.parse("2015-01-01T01:00:00Z")
        val dateEventCurrent = LocalDate.parse("2015-01-01")
        val dateEventDueCurrent = LocalDate.parse("2016-01-01")
        val firstRuleEvent = RuleEvent(
            "test_event_uid_one", "test_program_stage", "",
            RuleEventStatus.ACTIVE, dateEventOne, LocalDate.currentDate(), null, "", null, listOf(
                RuleDataValue(
                    dateEventOne, "test_program_stage",
                    "test_dataelement_one", "test_value_dataelement_one_first"
                ),
                RuleDataValue(
                    dateEventOne, "test_program_stage",
                    "test_dataelement_two", "test_value_dataelement_two_first"
                )
            )
        )
        val secondRuleEvent = RuleEvent(
            "test_event_uid_two", "test_program_stage", "",
            RuleEventStatus.ACTIVE, dateEventTwo, LocalDate.currentDate(), null, "", null, listOf(
                RuleDataValue(
                    dateEventTwo, "test_program_stage",
                    "test_dataelement_one", "test_value_dataelement_one_second"
                ),
                RuleDataValue(
                    dateEventTwo, "test_program_stage",
                    "test_dataelement_two", "test_value_dataelement_two_second"
                )
            )
        )
        val currentEvent = RuleEvent(
            "test_event_uid_current",
            "test_program_stage",
            "",
            RuleEventStatus.ACTIVE,
            instantEventCurrent,
            dateEventDueCurrent,
            null,
            "",
            null,
            listOf(
                RuleDataValue(
                    instantEventCurrent, "test_program_stage",
                    "test_dataelement_one", "test_value_dataelement_one_current"
                ),
                RuleDataValue(
                    instantEventCurrent, "test_program_stage",
                    "test_dataelement_two", "test_value_dataelement_two_current"
                )
            )
        )
        val valueMap = RuleVariableValueMapBuilder.target(currentEvent)
            .ruleVariables( listOf(ruleVariableOne, ruleVariableTwo))
            .triggerEnvironment(TriggerEnvironment.SERVER)
            .ruleEvents( listOf(firstRuleEvent, secondRuleEvent))
            .build()
        assertEquals(13, valueMap.size.toLong())
        RuleVariableValueAssert.assertThatVariable(valueMap["current_date"]!!).hasValue(
                LocalDate.Companion.currentDate().toString()

        )
            .isTypeOf(RuleValueType.TEXT).hasCandidates(LocalDate.Companion.currentDate().toString())
        RuleVariableValueAssert.assertThatVariable(valueMap["event_date"]!!)
            .hasValue(dateEventCurrent.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(dateEventCurrent.toString())
        RuleVariableValueAssert.assertThatVariable(valueMap["event_count"]!!).hasValue("3")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("3")
        RuleVariableValueAssert.assertThatVariable(valueMap["event_id"]!!).hasValue("test_event_uid_current")
            .isTypeOf(RuleValueType.TEXT).hasCandidates("test_event_uid_current")
        RuleVariableValueAssert.assertThatVariable(valueMap["due_date"]!!)
            .hasValue(dateEventDueCurrent.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(dateEventDueCurrent.toString())
        RuleVariableValueAssert.assertThatVariable(valueMap["test_variable_one"]!!)
            .hasValue("test_value_dataelement_one_current")
            .isTypeOf(RuleValueType.TEXT).hasCandidates(
                "test_value_dataelement_one_current",
                "test_value_dataelement_one_second", "test_value_dataelement_one_first"
            )
        RuleVariableValueAssert.assertThatVariable(valueMap["test_variable_two"]!!)
            .hasValue("test_value_dataelement_two_current")
            .isTypeOf(RuleValueType.TEXT).hasCandidates(
                "test_value_dataelement_two_current",
                "test_value_dataelement_two_second", "test_value_dataelement_two_first"
            )
    }

    @Test
    fun newestEventProgramStageVariableShouldContainValueFromNewestContextEvent() {
        val ruleVariable: RuleVariable = RuleVariableNewestStageEvent(
            "test_variable", true, ArrayList(),
            "test_dataelement", RuleValueType.TEXT, "test_program_stage_one"
        )
        val dateEventOne = Instant.parse("2014-02-03T01:00:00Z")
        val dateEventTwo = Instant.parse("2014-03-03T01:00:00Z")
        val dateEventThree = Instant.parse("2015-02-03T01:00:00Z")
        val instantEventCurrent = Instant.parse("2011-02-03T01:00:00Z")
        val dateEventCurrent = LocalDate.parse("2011-02-03")
        val dateEventDueCurrent = LocalDate.parse("2011-02-03")
        val eventOne = RuleEvent(
            "test_event_uid_one", "test_program_stage_one", "",
            RuleEventStatus.ACTIVE, dateEventOne, LocalDate.currentDate(), null, "", null, listOf(
                RuleDataValue(
                    dateEventOne, "test_program_stage_one",
                    "test_dataelement", "test_value_one"
                )
            )
        )
        val eventTwo = RuleEvent(
            "test_event_uid_two", "test_program_stage_two", "",
            RuleEventStatus.ACTIVE, dateEventTwo, LocalDate.currentDate(), null, "", null, listOf(
                RuleDataValue(
                    dateEventTwo, "test_program_stage_two",
                    "test_dataelement", "test_value_two"
                )
            )
        )
        val eventThree = RuleEvent(
            "test_event_uid_three", "test_program_stage_two", "",
            RuleEventStatus.ACTIVE, dateEventThree, LocalDate.currentDate(), null, "", null, listOf(
                RuleDataValue(
                    dateEventThree, "test_program_stage_two",
                    "test_dataelement", "test_value_three"
                )
            )
        )
        val eventCurrent = RuleEvent(
            "test_event_uid_current",
            "test_program_stage_one",
            "",
            RuleEventStatus.ACTIVE,
            instantEventCurrent,
            dateEventDueCurrent,
            null,
            "",
            null,
            listOf(
                RuleDataValue(
                    instantEventCurrent, "test_program_stage_one",
                    "test_dataelement", "test_value_current"
                )
            )
        )
        val valueMap = RuleVariableValueMapBuilder.target(eventCurrent)
            .ruleVariables( listOf(ruleVariable))
            .ruleEvents( listOf(eventOne, eventTwo, eventThree))
            .triggerEnvironment(TriggerEnvironment.SERVER)
            .build()
        assertEquals(12, valueMap.size.toLong())
        RuleVariableValueAssert.assertThatVariable(valueMap["current_date"]!!).hasValue(
                LocalDate.Companion.currentDate().toString()
        )
            .isTypeOf(RuleValueType.TEXT).hasCandidates(LocalDate.Companion.currentDate().toString())
        RuleVariableValueAssert.assertThatVariable(valueMap["event_date"]!!)
            .hasValue(dateEventCurrent.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(dateEventCurrent.toString())
        RuleVariableValueAssert.assertThatVariable(valueMap["event_count"]!!).hasValue("4")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("4")
        RuleVariableValueAssert.assertThatVariable(valueMap["event_id"]!!).hasValue("test_event_uid_current")
            .isTypeOf(RuleValueType.TEXT).hasCandidates("test_event_uid_current")
        RuleVariableValueAssert.assertThatVariable(valueMap["due_date"]!!)
            .hasValue(dateEventDueCurrent.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(dateEventDueCurrent.toString())
        RuleVariableValueAssert.assertThatVariable(valueMap["test_variable"]!!).hasValue("test_value_one")
            .isTypeOf(RuleValueType.TEXT).hasCandidates("test_value_one", "test_value_current")
    }

    @Test
    fun newestEventProgramStageVariableShouldNotContainAnyValues() {
        val ruleVariable: RuleVariable = RuleVariableNewestStageEvent(
            "test_variable", true, ArrayList(),
            "test_dataelement", RuleValueType.TEXT, "test_program_stage_one"
        )
        val dateEventOne = Instant.parse("2014-03-03T01:00:00Z")
        val instantEventTwo = Instant.parse("2015-02-03T01:00:00Z")
        val dateEventTwo = LocalDate.parse("2015-02-03")
        val dueDateEventTwo = LocalDate.parse("2015-02-03")
        val ruleEventOne = RuleEvent(
            "test_event_uid_one", "test_program_stage_two", "",
            RuleEventStatus.ACTIVE, dateEventOne, dueDateEventTwo, null, "", null, listOf(
                RuleDataValue(
                    dateEventOne, "test_program_stage_two",
                    "test_dataelement", "test_value_one"
                )
            )
        )
        val ruleEventTwo = RuleEvent(
            "test_event_uid_two", "test_program_stage_two", "",
            RuleEventStatus.ACTIVE, instantEventTwo, dueDateEventTwo, null, "", null, listOf(
                RuleDataValue(
                    instantEventTwo, "test_program_stage_two",
                    "test_dataelement", "test_value_two"
                )
            )
        )
        val valueMap = RuleVariableValueMapBuilder.target(ruleEventTwo)
            .ruleVariables( listOf(ruleVariable))
            .triggerEnvironment(TriggerEnvironment.SERVER)
            .ruleEvents( listOf(ruleEventOne))
            .build()
        assertEquals(12, valueMap.size.toLong())
        RuleVariableValueAssert.assertThatVariable(valueMap["current_date"]!!).hasValue(
                LocalDate.Companion.currentDate().toString()
        )
            .isTypeOf(RuleValueType.TEXT).hasCandidates(LocalDate.Companion.currentDate().toString())
        RuleVariableValueAssert.assertThatVariable(valueMap["event_date"]!!).hasValue(
            dateEventTwo.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(dateEventTwo.toString())
        RuleVariableValueAssert.assertThatVariable(valueMap["event_count"]!!).hasValue("2")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("2")
        RuleVariableValueAssert.assertThatVariable(valueMap["event_id"]!!).hasValue("test_event_uid_two")
            .isTypeOf(RuleValueType.TEXT).hasCandidates("test_event_uid_two")
        RuleVariableValueAssert.assertThatVariable(valueMap["due_date"]!!).hasValue(
            dueDateEventTwo.toString()
        )
            .isTypeOf(RuleValueType.TEXT).hasCandidates(dueDateEventTwo.toString())
        RuleVariableValueAssert.assertThatVariable(valueMap["test_variable"]!!).hasValue(null)
            .isTypeOf(RuleValueType.TEXT).hasCandidates()
    }

    @Test
    fun previousEventVariableShouldContainValuesFromPreviousEvent() {
        val ruleVariable: RuleVariable = RuleVariablePreviousEvent(
            "test_variable", true, ArrayList(),
            "test_dataelement", RuleValueType.TEXT
        )
        val dateEventOne = Instant.parse("2014-02-03T01:00:00Z")
        val dateEventTwo = Instant.parse("2014-03-03T01:00:00Z")
        val dateEventThree = Instant.parse("2015-02-03T01:00:00Z")
        val instantEventCurrent = Instant.parse("2014-05-03T01:00:00Z")
        val dateEventCurrent = LocalDate.parse("2014-05-03")
        val dueDateEventCurrent = LocalDate.parse("2014-05-03")
        val ruleEventOne = RuleEvent(
            "test_event_uid_one", "test_program_stage", "",
            RuleEventStatus.ACTIVE, dateEventOne, LocalDate.currentDate(), null, "", null, listOf(
                RuleDataValue(
                    dateEventOne, "test_program_stage_one",
                    "test_dataelement", "test_value_one"
                )
            )
        )
        val ruleEventTwo = RuleEvent(
            "test_event_uid_two", "test_program_stage", "",
            RuleEventStatus.ACTIVE, dateEventTwo, LocalDate.currentDate(), null, "", null, listOf(
                RuleDataValue(
                    dateEventTwo, "test_program_stage_two",
                    "test_dataelement", "test_value_two"
                )
            )
        )
        val ruleEventThree = RuleEvent(
            "test_event_uid_three", "test_program_stage", "",
            RuleEventStatus.ACTIVE, dateEventThree, LocalDate.currentDate(), null, "", null, listOf(
                RuleDataValue(
                    dateEventThree, "test_program_stage_two",
                    "test_dataelement", "test_value_three"
                )
            )
        )
        val ruleEventCurrent = RuleEvent(
            "test_event_uid_current",
            "test_program_stage",
            "",
            RuleEventStatus.ACTIVE,
            instantEventCurrent,
            dueDateEventCurrent,
            null,
            "",
            null,
            listOf(
                RuleDataValue(
                    instantEventCurrent, "test_program_stage_one",
                    "test_dataelement", "test_value_current"
                )
            )
        )
        val valueMap = RuleVariableValueMapBuilder.target(ruleEventCurrent)
            .ruleVariables( listOf(ruleVariable))
            .triggerEnvironment(TriggerEnvironment.SERVER)
            .ruleEvents( listOf(ruleEventOne, ruleEventTwo, ruleEventThree))
            .build()
        assertEquals(12, valueMap.size.toLong())
        RuleVariableValueAssert.assertThatVariable(valueMap["current_date"]!!).hasValue(
                LocalDate.Companion.currentDate().toString()
        )
            .isTypeOf(RuleValueType.TEXT).hasCandidates(LocalDate.Companion.currentDate().toString())
        RuleVariableValueAssert.assertThatVariable(valueMap["event_date"]!!)
            .hasValue(dateEventCurrent.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(dateEventCurrent.toString())
        RuleVariableValueAssert.assertThatVariable(valueMap["event_count"]!!).hasValue("4")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("4")
        RuleVariableValueAssert.assertThatVariable(valueMap["event_id"]!!).hasValue("test_event_uid_current")
            .isTypeOf(RuleValueType.TEXT).hasCandidates("test_event_uid_current")
        RuleVariableValueAssert.assertThatVariable(valueMap["due_date"]!!)
            .hasValue(dueDateEventCurrent.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(dueDateEventCurrent.toString())
        RuleVariableValueAssert.assertThatVariable(valueMap["test_variable"]!!).hasValue("test_value_two")
            .isTypeOf(RuleValueType.TEXT)
            .hasCandidates("test_value_three", "test_value_current", "test_value_two", "test_value_one")
    }

    @Test
    fun attributeVariableShouldContainValuesFromContextEnrollment() {
        val ruleVariableOne: RuleVariable = RuleVariableAttribute(
            "test_variable_one", true, ArrayList(),
            "test_attribute_one", RuleValueType.TEXT
        )
        val ruleVariableTwo: RuleVariable = RuleVariableAttribute(
            "test_variable_two", true, ArrayList(),
            "test_attribute_two", RuleValueType.TEXT
        )
        val eventInstant = Instant.parse("2015-01-01T01:00:00Z")
        val eventDate = LocalDate.parse("2015-01-01")
        val dueEventDate = LocalDate.parse("2015-01-01")
        val enrollmentDate = LocalDate.parse("2014-03-01")

        // values from enrollment should end up in ruleVariables
        val ruleEnrollment = RuleEnrollment(
            "test_enrollment", "",
            enrollmentDate, enrollmentDate, RuleEnrollmentStatus.ACTIVE, "", "", listOf(
                RuleAttributeValue("test_attribute_one", "test_attribute_value_one"),
                RuleAttributeValue("test_attribute_two", "test_attribute_value_two")
            )
        )

        // values from context events should be ignored
        val contextEvent = RuleEvent(
            "test_context_event_one",
            "test_program_stage",
            "",
            RuleEventStatus.ACTIVE,
            eventInstant,
            dueEventDate,
            null,
            "",
            null,
            listOf(
                RuleDataValue(
                    eventInstant, "test_program_stage",
                    "test_dataelement_one", "test_context_value_one"
                ),
                RuleDataValue(
                    eventInstant, "test_program_stage",
                    "test_dataelement_two", "test_context_value_two"
                )
            )
        )
        val currentEvent = RuleEvent(
            "test_event_uid", "test_program_stage", "",
            RuleEventStatus.ACTIVE, eventInstant, dueEventDate, null, "", null, listOf(
                RuleDataValue(
                    eventInstant, "test_program_stage",
                    "test_dataelement_one", "test_value_one"
                ),
                RuleDataValue(
                    eventInstant, "test_program_stage",
                    "test_dataelement_two", "test_value_two"
                )
            )
        )

        // here we will expect correct values to be returned
        val valueMap = RuleVariableValueMapBuilder.target(currentEvent)
            .ruleEnrollment(ruleEnrollment)
            .triggerEnvironment(TriggerEnvironment.SERVER)
            .ruleVariables( listOf(ruleVariableOne, ruleVariableTwo))
            .ruleEvents( listOf(contextEvent))
            .build()
        assertEquals(20, valueMap.size.toLong())
        RuleVariableValueAssert.assertThatVariable(valueMap["current_date"]!!).hasValue(
                LocalDate.Companion.currentDate().toString()
        )
            .isTypeOf(RuleValueType.TEXT).hasCandidates(LocalDate.Companion.currentDate().toString())
        RuleVariableValueAssert.assertThatVariable(valueMap["event_date"]!!).hasValue(
            eventDate.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(eventDate.toString())
        RuleVariableValueAssert.assertThatVariable(valueMap["event_count"]!!).hasValue("2")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("2")
        RuleVariableValueAssert.assertThatVariable(valueMap["event_id"]!!).hasValue("test_event_uid")
            .isTypeOf(RuleValueType.TEXT).hasCandidates("test_event_uid")
        RuleVariableValueAssert.assertThatVariable(valueMap["due_date"]!!).hasValue(
            dueEventDate.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(dueEventDate.toString())
        RuleVariableValueAssert.assertThatVariable(valueMap["enrollment_status"]!!)
            .hasValue(RuleEnrollmentStatus.ACTIVE.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(RuleEnrollmentStatus.ACTIVE.toString())
        RuleVariableValueAssert.assertThatVariable(valueMap["enrollment_date"]!!)
            .hasValue(enrollmentDate.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(enrollmentDate.toString())
        RuleVariableValueAssert.assertThatVariable(valueMap["enrollment_id"]!!).hasValue("test_enrollment")
            .isTypeOf(RuleValueType.TEXT).hasCandidates("test_enrollment")
        RuleVariableValueAssert.assertThatVariable(valueMap["enrollment_count"]!!).hasValue("1")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("1")
        RuleVariableValueAssert.assertThatVariable(valueMap["incident_date"]!!)
            .hasValue(enrollmentDate.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(enrollmentDate.toString())
        RuleVariableValueAssert.assertThatVariable(valueMap["tei_count"]!!).hasValue("1")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("1")
        RuleVariableValueAssert.assertThatVariable(valueMap["test_variable_one"]!!).hasValue("test_attribute_value_one")
            .isTypeOf(RuleValueType.TEXT).hasCandidates("test_attribute_value_one")
        RuleVariableValueAssert.assertThatVariable(valueMap["test_variable_two"]!!).hasValue("test_attribute_value_two")
            .isTypeOf(RuleValueType.TEXT).hasCandidates("test_attribute_value_two")
    }

    @Test
    fun ruleEnrollmentValuesShouldBePropagatedToMapCorrectly() {
        val ruleVariableOne: RuleVariable = RuleVariableAttribute(
            "test_variable_one", true, ArrayList(),
            "test_attribute_one", RuleValueType.NUMERIC
        )
        val ruleVariableTwo: RuleVariable = RuleVariableAttribute(
            "test_variable_two", true, ArrayList(),
            "test_attribute_two", RuleValueType.TEXT
        )
        val ruleVariableThree: RuleVariable = RuleVariableCurrentEvent(
            "test_variable_three", true, ArrayList(),
            "test_dataelement_one", RuleValueType.BOOLEAN
        )
        val currentDate = LocalDate.Companion.currentDate()
        val enrollmentDate = LocalDate.parse("2017-02-02")
        val incidentDate = LocalDate.parse("2017-04-02")
        val ruleEnrollment = RuleEnrollment(
            "test_enrollment", "", incidentDate,
            enrollmentDate, RuleEnrollmentStatus.ACTIVE, "", "", listOf(
                RuleAttributeValue("test_attribute_one", "test_attribute_value_one"),
                RuleAttributeValue("test_attribute_two", "test_attribute_value_two"),
                RuleAttributeValue("test_attribute_three", "test_attribute_value_three")
            )
        )
        val ruleEventOne = RuleEvent(
            "test_event_one",
            "test_program_stage",
            "",
            RuleEventStatus.ACTIVE,
            Clock.System.now(),
            LocalDate.currentDate(),
            null,
            "",
            null,
            ArrayList<RuleDataValue>()
        )
        val ruleEventTwo = RuleEvent(
            "test_event_two",
            "test_program_stage",
            "",
            RuleEventStatus.ACTIVE,
            Clock.System.now(),
            LocalDate.currentDate(),
            null,
            "",
            null,
            ArrayList<RuleDataValue>()
        )
        val valueMap = RuleVariableValueMapBuilder.target(ruleEnrollment)
            .ruleVariables( listOf(ruleVariableOne, ruleVariableTwo, ruleVariableThree))
            .ruleEvents( listOf(ruleEventOne, ruleEventTwo))
            .triggerEnvironment(TriggerEnvironment.SERVER)
            .build()
        assertEquals(15, valueMap.size.toLong())
        RuleVariableValueAssert.assertThatVariable(valueMap["current_date"]!!).hasValue(currentDate.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(currentDate.toString())
        RuleVariableValueAssert.assertThatVariable(valueMap["event_count"]!!).hasValue("2")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("2")
        RuleVariableValueAssert.assertThatVariable(valueMap["enrollment_date"]!!)
            .hasValue(enrollmentDate.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(enrollmentDate.toString())
        RuleVariableValueAssert.assertThatVariable(valueMap["enrollment_id"]!!).hasValue("test_enrollment")
            .isTypeOf(RuleValueType.TEXT).hasCandidates("test_enrollment")
        RuleVariableValueAssert.assertThatVariable(valueMap["enrollment_count"]!!).hasValue("1")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("1")
        RuleVariableValueAssert.assertThatVariable(valueMap["incident_date"]!!)
            .hasValue(incidentDate.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(incidentDate.toString())
        RuleVariableValueAssert.assertThatVariable(valueMap["tei_count"]!!).hasValue("1")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("1")
        RuleVariableValueAssert.assertThatVariable(valueMap["test_variable_one"]!!).hasValue("test_attribute_value_one")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("test_attribute_value_one")
        RuleVariableValueAssert.assertThatVariable(valueMap["test_variable_two"]!!).isTypeOf(RuleValueType.TEXT)
            .hasValue("test_attribute_value_two").hasCandidates("test_attribute_value_two")
    }

    @Test
    fun MultipleMapBuilderShoulCreateCorrectMapForEnrollmentAndEvents() {
        val ruleVariableOne: RuleVariable = RuleVariableAttribute(
            "test_variable_one", true, ArrayList(),
            "test_attribute_one", RuleValueType.NUMERIC
        )
        val ruleVariableTwo: RuleVariable = RuleVariableAttribute(
            "test_variable_two", true, ArrayList(),
            "test_attribute_two", RuleValueType.TEXT
        )
        val ruleVariableThree: RuleVariable = RuleVariableCurrentEvent(
            "test_variable_three", true, ArrayList(),
            "test_dataelement_one", RuleValueType.BOOLEAN
        )
        val currentDate = LocalDate.Companion.currentDate()
        val enrollmentDate = LocalDate.parse("2017-02-02")
        val incidentDate = LocalDate.parse("2017-04-02")
        val ruleEnrollment = RuleEnrollment(
            "test_enrollment", "", incidentDate,
            enrollmentDate, RuleEnrollmentStatus.ACTIVE, "", "", listOf(
                RuleAttributeValue("test_attribute_one", "test_attribute_value_one"),
                RuleAttributeValue("test_attribute_two", "test_attribute_value_two"),
                RuleAttributeValue("test_attribute_three", "test_attribute_value_three")
            )
        )
        val now = LocalDate.Companion.currentDate()
        val eventOneInstant = now.minus(1, DateTimeUnit.DAY).atStartOfDayIn(TimeZone.currentSystemDefault())
        val eventOneDate = now.minus(1, DateTimeUnit.DAY)
        val eventOneDueDate = now.minus(2, DateTimeUnit.DAY)
        val eventTwoInstant = now.minus(3, DateTimeUnit.DAY).atStartOfDayIn(TimeZone.currentSystemDefault())
        val eventTwoDate = now.minus(3, DateTimeUnit.DAY)
        val eventTwoDueDate = now.minus(4, DateTimeUnit.DAY)
        val ruleEventOne = RuleEvent(
            "test_event_one",
            "test_program_stage",
            "",
            RuleEventStatus.ACTIVE,
            eventOneInstant,
            eventOneDueDate,
            null,
            "",
            null,
            ArrayList<RuleDataValue>()
        )
        val ruleEventTwo = RuleEvent(
            "test_event_two",
            "test_program_stage",
            "",
            RuleEventStatus.ACTIVE,
            eventTwoInstant,
            eventTwoDueDate,
            null,
            "",
            null,
            ArrayList<RuleDataValue>()
        )
        val (enrollmentMap, eventMap) = RuleVariableValueMapBuilder.target(ruleEnrollment)
            .ruleVariables( listOf(ruleVariableOne, ruleVariableTwo, ruleVariableThree))
            .ruleEvents( listOf(ruleEventOne, ruleEventTwo))
            .triggerEnvironment(TriggerEnvironment.SERVER)
            .multipleBuild()
        assertEquals(1, enrollmentMap.size.toLong())
        assertEquals(2, eventMap.size.toLong())
        val enrollmentValueMap = enrollmentMap[ruleEnrollment]
        val eventOneValueMap = eventMap[ruleEventOne]
        val eventTwoValueMap = eventMap[ruleEventTwo]

        // Enrollment
        RuleVariableValueAssert.assertThatVariable(enrollmentValueMap!!["current_date"]!!).hasValue(currentDate.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(currentDate.toString())
        RuleVariableValueAssert.assertThatVariable(enrollmentValueMap["event_count"]!!).hasValue("2")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("2")
        RuleVariableValueAssert.assertThatVariable(enrollmentValueMap["enrollment_date"]!!)
            .hasValue(enrollmentDate.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(enrollmentDate.toString())
        RuleVariableValueAssert.assertThatVariable(enrollmentValueMap["enrollment_id"]!!).hasValue("test_enrollment")
            .isTypeOf(RuleValueType.TEXT).hasCandidates("test_enrollment")
        RuleVariableValueAssert.assertThatVariable(enrollmentValueMap["enrollment_count"]!!).hasValue("1")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("1")
        RuleVariableValueAssert.assertThatVariable(enrollmentValueMap["incident_date"]!!)
            .hasValue(incidentDate.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(incidentDate.toString())
        RuleVariableValueAssert.assertThatVariable(enrollmentValueMap["tei_count"]!!).hasValue("1")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("1")
        RuleVariableValueAssert.assertThatVariable(enrollmentValueMap["test_variable_one"]!!)
            .hasValue("test_attribute_value_one")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("test_attribute_value_one")
        RuleVariableValueAssert.assertThatVariable(enrollmentValueMap["test_variable_two"]!!)
            .isTypeOf(RuleValueType.TEXT)
            .hasValue("test_attribute_value_two").hasCandidates("test_attribute_value_two")

        // Event one
        RuleVariableValueAssert.assertThatVariable(eventOneValueMap!!["current_date"]!!).hasValue(currentDate.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(currentDate.toString())
        RuleVariableValueAssert.assertThatVariable(eventOneValueMap["event_count"]!!).hasValue("2")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("2")
        RuleVariableValueAssert.assertThatVariable(eventOneValueMap["enrollment_date"]!!)
            .hasValue(enrollmentDate.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(enrollmentDate.toString())
        RuleVariableValueAssert.assertThatVariable(eventOneValueMap["enrollment_id"]!!).hasValue("test_enrollment")
            .isTypeOf(RuleValueType.TEXT).hasCandidates("test_enrollment")
        RuleVariableValueAssert.assertThatVariable(eventOneValueMap["enrollment_count"]!!).hasValue("1")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("1")
        RuleVariableValueAssert.assertThatVariable(eventOneValueMap["incident_date"]!!)
            .hasValue(incidentDate.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(incidentDate.toString())
        RuleVariableValueAssert.assertThatVariable(eventOneValueMap["tei_count"]!!).hasValue("1")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("1")
        RuleVariableValueAssert.assertThatVariable(eventOneValueMap["test_variable_one"]!!)
            .hasValue("test_attribute_value_one")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("test_attribute_value_one")
        RuleVariableValueAssert.assertThatVariable(eventOneValueMap["test_variable_two"]!!).isTypeOf(
            RuleValueType.TEXT)
            .hasValue("test_attribute_value_two").hasCandidates("test_attribute_value_two")
        RuleVariableValueAssert.assertThatVariable(eventOneValueMap["event_date"]!!)
            .hasValue(eventOneDate.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(eventOneDate.toString())
        RuleVariableValueAssert.assertThatVariable(eventOneValueMap["due_date"]!!)
            .hasValue(eventOneDueDate.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(eventOneDueDate.toString())

        // Event two
        RuleVariableValueAssert.assertThatVariable(eventTwoValueMap!!["current_date"]!!).hasValue(currentDate.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(currentDate.toString())
        RuleVariableValueAssert.assertThatVariable(eventTwoValueMap["event_count"]!!).hasValue("2")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("2")
        RuleVariableValueAssert.assertThatVariable(eventTwoValueMap["enrollment_date"]!!)
            .hasValue(enrollmentDate.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(enrollmentDate.toString())
        RuleVariableValueAssert.assertThatVariable(eventTwoValueMap["enrollment_id"]!!).hasValue("test_enrollment")
            .isTypeOf(RuleValueType.TEXT).hasCandidates("test_enrollment")
        RuleVariableValueAssert.assertThatVariable(eventTwoValueMap["enrollment_count"]!!).hasValue("1")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("1")
        RuleVariableValueAssert.assertThatVariable(eventTwoValueMap["incident_date"]!!)
            .hasValue(incidentDate.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(incidentDate.toString())
        RuleVariableValueAssert.assertThatVariable(eventTwoValueMap["tei_count"]!!).hasValue("1")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("1")
        RuleVariableValueAssert.assertThatVariable(eventTwoValueMap["test_variable_one"]!!)
            .hasValue("test_attribute_value_one")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("test_attribute_value_one")
        RuleVariableValueAssert.assertThatVariable(eventTwoValueMap["test_variable_two"]!!).isTypeOf(
            RuleValueType.TEXT)
            .hasValue("test_attribute_value_two").hasCandidates("test_attribute_value_two")
        RuleVariableValueAssert.assertThatVariable(eventTwoValueMap["event_date"]!!)
            .hasValue(eventTwoDate.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(eventTwoDate.toString())
        RuleVariableValueAssert.assertThatVariable(eventTwoValueMap["due_date"]!!)
            .hasValue(eventTwoDueDate.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(eventTwoDueDate.toString())
    }

    @Test
    fun buildShouldThrowOnDuplicateEvent() {
        val ruleEvent = RuleEvent(
            "test_event_two",
            "test_program_stage",
            "",
            RuleEventStatus.ACTIVE,
            Clock.System.now(),
            LocalDate.currentDate(),
            null,
            "",
            null,
            ArrayList<RuleDataValue>()
        )
        assertFailsWith(IllegalStateException::class)
        {
            RuleVariableValueMapBuilder.target(ruleEvent)
                .ruleVariables(ArrayList())
                .ruleEvents(listOf(ruleEvent))
                .build()
        }
    }
}
