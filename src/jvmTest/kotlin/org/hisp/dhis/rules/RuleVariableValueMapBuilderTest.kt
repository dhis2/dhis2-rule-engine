package org.hisp.dhis.rules

import org.hisp.dhis.rules.models.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.*
import kotlin.collections.ArrayList
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class RuleVariableValueMapBuilderTest {
    private var dateFormat: SimpleDateFormat? = null
    @BeforeTest
    fun setUp() {
        dateFormat = SimpleDateFormat(DATE_PATTERN, Locale.US)
    }

    @Test
    fun currentEventVariableShouldContainNullValueForEnrollmentEvaluation() {
        val ruleVariableOne: RuleVariable = RuleVariableCurrentEvent(
            "test_variable_one", true, ArrayList(), "test_dataelement_one", RuleValueType.TEXT
        )
        val eventDate = dateFormat!!.parse("2015-01-01")

        // values from context events should be ignored
        val ruleEnrollment = RuleEnrollment(
            "test_enrollment", "",
            dateFormat!!.parse("2015-01-01"), dateFormat!!.parse("2015-01-01"),
            RuleEnrollment.Status.ACTIVE, "", "",  listOf(
                RuleAttributeValue("test_attribute_one", "test_attribute_value_one"),
                RuleAttributeValue("test_attribute_two", "test_attribute_value_two")
            )
        )
        val contextEventOne = RuleEvent(
            "test_context_event_one", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, Date(), Date(), null, "", null,  listOf(
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
    @Throws(ParseException::class)
    fun currentEventVariableShouldContainValuesFromCurrentEvent() {
        val ruleVariableOne: RuleVariable = RuleVariableCurrentEvent(
            "test_variable_one", true, ArrayList(), "test_dataelement_one", RuleValueType.TEXT
        )
        val ruleVariableTwo: RuleVariable = RuleVariableCurrentEvent(
            "test_variable_two", true, ArrayList(), "test_dataelement_two", RuleValueType.TEXT
        )
        val eventDate = dateFormat!!.parse("2015-01-01")
        val dueDate = dateFormat!!.parse("2016-01-01")

        // values from context events should be ignored
        val contextEventOne = RuleEvent(
            "test_context_event_one", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, Date(), Date(), null, "", null,  listOf(
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
        val contextEventTwo = RuleEvent(
            "test_context_event_two", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, Date(), Date(), null, "", null,  listOf(
                RuleDataValue(
                    eventDate, "test_program_stage",
                    "test_dataelement_one", "test_context_value_three"
                ),
                RuleDataValue(
                    eventDate, "test_program_stage",
                    "test_dataelement_two", "test_context_value_four"
                )
            )
        )
        // values from current ruleEvent should be propagated to the variable values
        val currentEvent = RuleEvent(
            "test_event_uid", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, eventDate, dueDate, null, "", null,  listOf(
                RuleDataValue(
                    eventDate, "test_program_stage",
                    "test_dataelement_one", "test_value_one"
                ),
                RuleDataValue(
                    eventDate, "test_program_stage",
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
            wrap(
                dateFormat!!.format(Date())
            )
        )
            .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat!!.format(Date()))
        RuleVariableValueAssert.assertThatVariable(valueMap["event_date"]!!).hasValue(
            wrap(
                dateFormat!!.format(eventDate)
            )
        )
            .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat!!.format(eventDate))
        RuleVariableValueAssert.assertThatVariable(valueMap["event_status"]!!)
            .hasValue(wrap(RuleEvent.Status.ACTIVE.toString()))
            .isTypeOf(RuleValueType.TEXT).hasCandidates(RuleEvent.Status.ACTIVE.toString())

        // event count variable should respect current event
        RuleVariableValueAssert.assertThatVariable(valueMap["event_count"]!!).hasValue("3")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("3")
        RuleVariableValueAssert.assertThatVariable(valueMap["event_id"]!!).hasValue("test_event_uid")
            .isTypeOf(RuleValueType.TEXT).hasCandidates("test_event_uid")
        RuleVariableValueAssert.assertThatVariable(valueMap["due_date"]!!).hasValue(
            wrap(
                dateFormat!!.format(dueDate)
            )
        )
            .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat!!.format(dueDate))
        RuleVariableValueAssert.assertThatVariable(valueMap["test_variable_one"]!!).hasValue("test_value_one")
            .isTypeOf(RuleValueType.TEXT).hasCandidates("test_value_one")
        RuleVariableValueAssert.assertThatVariable(valueMap["test_variable_two"]!!).hasValue("test_value_two")
            .isTypeOf(RuleValueType.TEXT).hasCandidates("test_value_two")
    }

    @Test
    @Throws(ParseException::class)
    fun newestEventProgramVariableShouldContainValueFromNewestContextEvent() {
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_variable_one", true, ArrayList(), "test_dataelement_one", RuleValueType.TEXT
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent(
            "test_variable_two", true, ArrayList(), "test_dataelement_two", RuleValueType.TEXT
        )
        val oldestEventDate = dateFormat!!.parse("2013-01-01")
        val newestEventDate = dateFormat!!.parse("2017-01-01")
        val currentEventDate = dateFormat!!.parse("2015-01-01")
        val currentEventDueDate: Date? = null
        val oldestRuleEvent = RuleEvent(
            "test_event_uid_oldest", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, oldestEventDate, oldestEventDate, null, "", null,  listOf(
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
            "test_event_uid_newest", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, newestEventDate, newestEventDate, null, "", null,  listOf(
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
            "test_event_uid_current", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, currentEventDate, currentEventDueDate, null, "", null,  listOf(
                RuleDataValue(
                    currentEventDate, "test_program_stage",
                    "test_dataelement_one", "test_value_one_current"
                ),
                RuleDataValue(
                    currentEventDate, "test_program_stage",
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
            wrap(
                dateFormat!!.format(Date())
            )
        )
            .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat!!.format(Date()))
        RuleVariableValueAssert.assertThatVariable(valueMap["event_date"]!!)
            .hasValue(wrap(dateFormat!!.format(currentEventDate)))
            .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat!!.format(currentEventDate))
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
    @Throws(ParseException::class)
    fun newestEventProgramVariableShouldReturnValuesFromCurrentEventWhenIfNewest() {
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_variable_one", true, ArrayList(), "test_dataelement_one", RuleValueType.TEXT
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent(
            "test_variable_two", true, ArrayList(), "test_dataelement_two", RuleValueType.TEXT
        )
        val dateEventOne = dateFormat!!.parse("2013-01-01")
        val dateEventTwo = dateFormat!!.parse("2014-01-01")
        val dateEventCurrent = dateFormat!!.parse("2015-01-01")
        val dateEventDueCurrent = dateFormat!!.parse("2016-01-01")
        val firstRuleEvent = RuleEvent(
            "test_event_uid_one", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, dateEventOne, dateEventOne, null, "", null,  listOf(
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
            RuleEvent.Status.ACTIVE, dateEventTwo, dateEventTwo, null, "", null,  listOf(
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
            "test_event_uid_current", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, dateEventCurrent, dateEventDueCurrent, null, "", null,  listOf(
                RuleDataValue(
                    dateEventCurrent, "test_program_stage",
                    "test_dataelement_one", "test_value_dataelement_one_current"
                ),
                RuleDataValue(
                    dateEventCurrent, "test_program_stage",
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
            wrap(
                dateFormat!!.format(Date())
            )
        )
            .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat!!.format(Date()))
        RuleVariableValueAssert.assertThatVariable(valueMap["event_date"]!!)
            .hasValue(wrap(dateFormat!!.format(dateEventCurrent)))
            .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat!!.format(dateEventCurrent))
        RuleVariableValueAssert.assertThatVariable(valueMap["event_count"]!!).hasValue("3")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("3")
        RuleVariableValueAssert.assertThatVariable(valueMap["event_id"]!!).hasValue("test_event_uid_current")
            .isTypeOf(RuleValueType.TEXT).hasCandidates("test_event_uid_current")
        RuleVariableValueAssert.assertThatVariable(valueMap["due_date"]!!)
            .hasValue(wrap(dateFormat!!.format(dateEventDueCurrent)))
            .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat!!.format(dateEventDueCurrent))
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
    @Throws(ParseException::class)
    fun newestEventProgramStageVariableShouldContainValueFromNewestContextEvent() {
        val ruleVariable: RuleVariable = RuleVariableNewestStageEvent(
            "test_variable", true, ArrayList(),
            "test_dataelement", RuleValueType.TEXT, "test_program_stage_one"
        )
        val dateEventOne = dateFormat!!.parse("2014-02-03")
        val dateEventTwo = dateFormat!!.parse("2014-03-03")
        val dateEventThree = dateFormat!!.parse("2015-02-03")
        val dateEventCurrent = dateFormat!!.parse("2011-02-03")
        val dateEventDueCurrent = dateFormat!!.parse("2011-02-03")
        val eventOne = RuleEvent(
            "test_event_uid_one", "test_program_stage_one", "",
            RuleEvent.Status.ACTIVE, dateEventOne, dateEventOne, null, "", null,  listOf(
                RuleDataValue(
                    dateEventOne, "test_program_stage_one",
                    "test_dataelement", "test_value_one"
                )
            )
        )
        val eventTwo = RuleEvent(
            "test_event_uid_two", "test_program_stage_two", "",
            RuleEvent.Status.ACTIVE, dateEventTwo, dateEventTwo, null, "", null,  listOf(
                RuleDataValue(
                    dateEventTwo, "test_program_stage_two",
                    "test_dataelement", "test_value_two"
                )
            )
        )
        val eventThree = RuleEvent(
            "test_event_uid_three", "test_program_stage_two", "",
            RuleEvent.Status.ACTIVE, dateEventThree, dateEventThree, null, "", null,  listOf(
                RuleDataValue(
                    dateEventThree, "test_program_stage_two",
                    "test_dataelement", "test_value_three"
                )
            )
        )
        val eventCurrent = RuleEvent(
            "test_event_uid_current", "test_program_stage_one", "",
            RuleEvent.Status.ACTIVE, dateEventCurrent, dateEventDueCurrent, null, "", null,  listOf(
                RuleDataValue(
                    dateEventCurrent, "test_program_stage_one",
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
            wrap(
                dateFormat!!.format(Date())
            )
        )
            .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat!!.format(Date()))
        RuleVariableValueAssert.assertThatVariable(valueMap["event_date"]!!)
            .hasValue(wrap(dateFormat!!.format(dateEventCurrent)))
            .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat!!.format(dateEventCurrent))
        RuleVariableValueAssert.assertThatVariable(valueMap["event_count"]!!).hasValue("4")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("4")
        RuleVariableValueAssert.assertThatVariable(valueMap["event_id"]!!).hasValue("test_event_uid_current")
            .isTypeOf(RuleValueType.TEXT).hasCandidates("test_event_uid_current")
        RuleVariableValueAssert.assertThatVariable(valueMap["due_date"]!!)
            .hasValue(wrap(dateFormat!!.format(dateEventDueCurrent)))
            .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat!!.format(dateEventDueCurrent))
        RuleVariableValueAssert.assertThatVariable(valueMap["test_variable"]!!).hasValue("test_value_one")
            .isTypeOf(RuleValueType.TEXT).hasCandidates("test_value_one", "test_value_current")
    }

    @Test
    @Throws(ParseException::class)
    fun newestEventProgramStageVariableShouldNotContainAnyValues() {
        val ruleVariable: RuleVariable = RuleVariableNewestStageEvent(
            "test_variable", true, ArrayList(),
            "test_dataelement", RuleValueType.TEXT, "test_program_stage_one"
        )
        val dateEventOne = dateFormat!!.parse("2014-03-03")
        val dateEventTwo = dateFormat!!.parse("2015-02-03")
        val ruleEventOne = RuleEvent(
            "test_event_uid_one", "test_program_stage_two", "",
            RuleEvent.Status.ACTIVE, dateEventOne, dateEventOne, null, "", null,  listOf(
                RuleDataValue(
                    dateEventOne, "test_program_stage_two",
                    "test_dataelement", "test_value_one"
                )
            )
        )
        val ruleEventTwo = RuleEvent(
            "test_event_uid_two", "test_program_stage_two", "",
            RuleEvent.Status.ACTIVE, dateEventTwo, dateEventTwo, null, "", null,  listOf(
                RuleDataValue(
                    dateEventTwo, "test_program_stage_two",
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
            wrap(
                dateFormat!!.format(Date())
            )
        )
            .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat!!.format(Date()))
        RuleVariableValueAssert.assertThatVariable(valueMap["event_date"]!!).hasValue(
            wrap(
                dateFormat!!.format(dateEventTwo)
            )
        )
            .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat!!.format(dateEventTwo))
        RuleVariableValueAssert.assertThatVariable(valueMap["event_count"]!!).hasValue("2")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("2")
        RuleVariableValueAssert.assertThatVariable(valueMap["event_id"]!!).hasValue("test_event_uid_two")
            .isTypeOf(RuleValueType.TEXT).hasCandidates("test_event_uid_two")
        RuleVariableValueAssert.assertThatVariable(valueMap["due_date"]!!).hasValue(
            wrap(
                dateFormat!!.format(dateEventTwo)
            )
        )
            .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat!!.format(dateEventTwo))
        RuleVariableValueAssert.assertThatVariable(valueMap["test_variable"]!!).hasValue(null)
            .isTypeOf(RuleValueType.TEXT).hasCandidates()
    }

    @Test
    @Throws(ParseException::class)
    fun previousEventVariableShouldContainValuesFromPreviousEvent() {
        val ruleVariable: RuleVariable = RuleVariablePreviousEvent(
            "test_variable", true, ArrayList(),
            "test_dataelement", RuleValueType.TEXT
        )
        val dateEventOne = dateFormat!!.parse("2014-02-03")
        val dateEventTwo = dateFormat!!.parse("2014-03-03")
        val dateEventThree = dateFormat!!.parse("2015-02-03")
        val dateEventCurrent = dateFormat!!.parse("2014-05-03")
        val ruleEventOne = RuleEvent(
            "test_event_uid_one", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, dateEventOne, dateEventOne, null, "", null,  listOf(
                RuleDataValue(
                    dateEventOne, "test_program_stage_one",
                    "test_dataelement", "test_value_one"
                )
            )
        )
        val ruleEventTwo = RuleEvent(
            "test_event_uid_two", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, dateEventTwo, dateEventTwo, null, "", null,  listOf(
                RuleDataValue(
                    dateEventTwo, "test_program_stage_two",
                    "test_dataelement", "test_value_two"
                )
            )
        )
        val ruleEventThree = RuleEvent(
            "test_event_uid_three", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, dateEventThree, dateEventThree, null, "", null,  listOf(
                RuleDataValue(
                    dateEventThree, "test_program_stage_two",
                    "test_dataelement", "test_value_three"
                )
            )
        )
        val ruleEventCurrent = RuleEvent(
            "test_event_uid_current", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, dateEventCurrent, dateEventCurrent, null, "", null,  listOf(
                RuleDataValue(
                    dateEventCurrent, "test_program_stage_one",
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
            wrap(
                dateFormat!!.format(Date())
            )
        )
            .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat!!.format(Date()))
        RuleVariableValueAssert.assertThatVariable(valueMap["event_date"]!!)
            .hasValue(wrap(dateFormat!!.format(dateEventCurrent)))
            .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat!!.format(dateEventCurrent))
        RuleVariableValueAssert.assertThatVariable(valueMap["event_count"]!!).hasValue("4")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("4")
        RuleVariableValueAssert.assertThatVariable(valueMap["event_id"]!!).hasValue("test_event_uid_current")
            .isTypeOf(RuleValueType.TEXT).hasCandidates("test_event_uid_current")
        RuleVariableValueAssert.assertThatVariable(valueMap["due_date"]!!)
            .hasValue(wrap(dateFormat!!.format(dateEventCurrent)))
            .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat!!.format(dateEventCurrent))
        RuleVariableValueAssert.assertThatVariable(valueMap["test_variable"]!!).hasValue("test_value_two")
            .isTypeOf(RuleValueType.TEXT)
            .hasCandidates("test_value_three", "test_value_current", "test_value_two", "test_value_one")
    }

    @Test
    @Throws(ParseException::class)
    fun attributeVariableShouldContainValuesFromContextEnrollment() {
        val ruleVariableOne: RuleVariable = RuleVariableAttribute(
            "test_variable_one", true, ArrayList(),
            "test_attribute_one", RuleValueType.TEXT
        )
        val ruleVariableTwo: RuleVariable = RuleVariableAttribute(
            "test_variable_two", true, ArrayList(),
            "test_attribute_two", RuleValueType.TEXT
        )
        val eventDate = dateFormat!!.parse("2015-01-01")
        val enrollmentDate = dateFormat!!.parse("2014-03-01")

        // values from enrollment should end up in ruleVariables
        val ruleEnrollment = RuleEnrollment(
            "test_enrollment", "",
            enrollmentDate, enrollmentDate, RuleEnrollment.Status.ACTIVE, "",  "",  listOf(
                RuleAttributeValue("test_attribute_one", "test_attribute_value_one"),
                RuleAttributeValue("test_attribute_two", "test_attribute_value_two")
            )
        )

        // values from context events should be ignored
        val contextEvent = RuleEvent(
            "test_context_event_one", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, eventDate, Date(), null, "", null,  listOf(
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
        val currentEvent = RuleEvent(
            "test_event_uid", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, eventDate, eventDate, null, "", null,  listOf(
                RuleDataValue(
                    eventDate, "test_program_stage",
                    "test_dataelement_one", "test_value_one"
                ),
                RuleDataValue(
                    eventDate, "test_program_stage",
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
            wrap(
                dateFormat!!.format(Date())
            )
        )
            .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat!!.format(Date()))
        RuleVariableValueAssert.assertThatVariable(valueMap["event_date"]!!).hasValue(
            wrap(
                dateFormat!!.format(eventDate)
            )
        )
            .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat!!.format(eventDate))
        RuleVariableValueAssert.assertThatVariable(valueMap["event_count"]!!).hasValue("2")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("2")
        RuleVariableValueAssert.assertThatVariable(valueMap["event_id"]!!).hasValue("test_event_uid")
            .isTypeOf(RuleValueType.TEXT).hasCandidates("test_event_uid")
        RuleVariableValueAssert.assertThatVariable(valueMap["due_date"]!!).hasValue(
            wrap(
                dateFormat!!.format(eventDate)
            )
        )
            .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat!!.format(eventDate))
        RuleVariableValueAssert.assertThatVariable(valueMap["enrollment_status"]!!)
            .hasValue(wrap(RuleEnrollment.Status.ACTIVE.toString()))
            .isTypeOf(RuleValueType.TEXT).hasCandidates(RuleEnrollment.Status.ACTIVE.toString())
        RuleVariableValueAssert.assertThatVariable(valueMap["enrollment_date"]!!)
            .hasValue(wrap(dateFormat!!.format(enrollmentDate)))
            .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat!!.format(enrollmentDate))
        RuleVariableValueAssert.assertThatVariable(valueMap["enrollment_id"]!!).hasValue("test_enrollment")
            .isTypeOf(RuleValueType.TEXT).hasCandidates("test_enrollment")
        RuleVariableValueAssert.assertThatVariable(valueMap["enrollment_count"]!!).hasValue("1")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("1")
        RuleVariableValueAssert.assertThatVariable(valueMap["incident_date"]!!)
            .hasValue(wrap(dateFormat!!.format(enrollmentDate)))
            .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat!!.format(enrollmentDate))
        RuleVariableValueAssert.assertThatVariable(valueMap["tei_count"]!!).hasValue("1")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("1")
        RuleVariableValueAssert.assertThatVariable(valueMap["test_variable_one"]!!).hasValue("test_attribute_value_one")
            .isTypeOf(RuleValueType.TEXT).hasCandidates("test_attribute_value_one")
        RuleVariableValueAssert.assertThatVariable(valueMap["test_variable_two"]!!).hasValue("test_attribute_value_two")
            .isTypeOf(RuleValueType.TEXT).hasCandidates("test_attribute_value_two")
    }

    @Test
    @Throws(ParseException::class)
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
        val currentDate = dateFormat!!.format(Date())
        val enrollmentDate = dateFormat!!.parse("2017-02-02")
        val incidentDate = dateFormat!!.parse("2017-04-02")
        val ruleEnrollment = RuleEnrollment(
            "test_enrollment",  "", incidentDate,
            enrollmentDate, RuleEnrollment.Status.ACTIVE, "",  "",  listOf(
                RuleAttributeValue("test_attribute_one", "test_attribute_value_one"),
                RuleAttributeValue("test_attribute_two", "test_attribute_value_two"),
                RuleAttributeValue("test_attribute_three", "test_attribute_value_three")
            )
        )
        val ruleEventOne = RuleEvent(
            "test_event_one", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, Date(), Date(), null, "", null, ArrayList<RuleDataValue>()
        )
        val ruleEventTwo = RuleEvent(
            "test_event_two", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, Date(), Date(), null, "", null, ArrayList<RuleDataValue>()
        )
        val valueMap = RuleVariableValueMapBuilder.target(ruleEnrollment)
            .ruleVariables( listOf(ruleVariableOne, ruleVariableTwo, ruleVariableThree))
            .ruleEvents( listOf(ruleEventOne, ruleEventTwo))
            .triggerEnvironment(TriggerEnvironment.SERVER)
            .build()
        assertEquals(15, valueMap.size.toLong())
        RuleVariableValueAssert.assertThatVariable(valueMap["current_date"]!!).hasValue(wrap(currentDate))
            .isTypeOf(RuleValueType.TEXT).hasCandidates(currentDate)
        RuleVariableValueAssert.assertThatVariable(valueMap["event_count"]!!).hasValue("2")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("2")
        RuleVariableValueAssert.assertThatVariable(valueMap["enrollment_date"]!!)
            .hasValue(wrap(dateFormat!!.format(enrollmentDate)))
            .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat!!.format(enrollmentDate))
        RuleVariableValueAssert.assertThatVariable(valueMap["enrollment_id"]!!).hasValue("test_enrollment")
            .isTypeOf(RuleValueType.TEXT).hasCandidates("test_enrollment")
        RuleVariableValueAssert.assertThatVariable(valueMap["enrollment_count"]!!).hasValue("1")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("1")
        RuleVariableValueAssert.assertThatVariable(valueMap["incident_date"]!!)
            .hasValue(wrap(dateFormat!!.format(incidentDate)))
            .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat!!.format(incidentDate))
        RuleVariableValueAssert.assertThatVariable(valueMap["tei_count"]!!).hasValue("1")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("1")
        RuleVariableValueAssert.assertThatVariable(valueMap["test_variable_one"]!!).hasValue("test_attribute_value_one")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("test_attribute_value_one")
        RuleVariableValueAssert.assertThatVariable(valueMap["test_variable_two"]!!).isTypeOf(RuleValueType.TEXT)
            .hasValue("test_attribute_value_two").hasCandidates("test_attribute_value_two")
    }

    @Test
    @Throws(ParseException::class)
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
        val currentDate = dateFormat!!.format(Date())
        val enrollmentDate = dateFormat!!.parse("2017-02-02")
        val incidentDate = dateFormat!!.parse("2017-04-02")
        val ruleEnrollment = RuleEnrollment(
            "test_enrollment",  "", incidentDate,
            enrollmentDate, RuleEnrollment.Status.ACTIVE, "", "",  listOf(
                RuleAttributeValue("test_attribute_one", "test_attribute_value_one"),
                RuleAttributeValue("test_attribute_two", "test_attribute_value_two"),
                RuleAttributeValue("test_attribute_three", "test_attribute_value_three")
            )
        )
        val now = LocalDate.now()
        val eventOneDate = toDate(now.minusDays(1))
        val eventOneDueDate = toDate(now.minusDays(2))
        val eventTwoDate = toDate(now.minusDays(3))
        val eventTwoDueDate = toDate(now.minusDays(4))
        val ruleEventOne = RuleEvent(
            "test_event_one", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, eventOneDate, eventOneDueDate, null, "", null, ArrayList<RuleDataValue>()
        )
        val ruleEventTwo = RuleEvent(
            "test_event_two", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, eventTwoDate, eventTwoDueDate, null, "", null, ArrayList<RuleDataValue>()
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
        RuleVariableValueAssert.assertThatVariable(enrollmentValueMap!!["current_date"]!!).hasValue(wrap(currentDate))
            .isTypeOf(RuleValueType.TEXT).hasCandidates(currentDate)
        RuleVariableValueAssert.assertThatVariable(enrollmentValueMap["event_count"]!!).hasValue("2")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("2")
        RuleVariableValueAssert.assertThatVariable(enrollmentValueMap["enrollment_date"]!!)
            .hasValue(wrap(dateFormat!!.format(enrollmentDate)))
            .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat!!.format(enrollmentDate))
        RuleVariableValueAssert.assertThatVariable(enrollmentValueMap["enrollment_id"]!!).hasValue("test_enrollment")
            .isTypeOf(RuleValueType.TEXT).hasCandidates("test_enrollment")
        RuleVariableValueAssert.assertThatVariable(enrollmentValueMap["enrollment_count"]!!).hasValue("1")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("1")
        RuleVariableValueAssert.assertThatVariable(enrollmentValueMap["incident_date"]!!)
            .hasValue(wrap(dateFormat!!.format(incidentDate)))
            .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat!!.format(incidentDate))
        RuleVariableValueAssert.assertThatVariable(enrollmentValueMap["tei_count"]!!).hasValue("1")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("1")
        RuleVariableValueAssert.assertThatVariable(enrollmentValueMap["test_variable_one"]!!)
            .hasValue("test_attribute_value_one")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("test_attribute_value_one")
        RuleVariableValueAssert.assertThatVariable(enrollmentValueMap["test_variable_two"]!!)
            .isTypeOf(RuleValueType.TEXT)
            .hasValue("test_attribute_value_two").hasCandidates("test_attribute_value_two")

        // Event one
        RuleVariableValueAssert.assertThatVariable(eventOneValueMap!!["current_date"]!!).hasValue(wrap(currentDate))
            .isTypeOf(RuleValueType.TEXT).hasCandidates(currentDate)
        RuleVariableValueAssert.assertThatVariable(eventOneValueMap["event_count"]!!).hasValue("2")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("2")
        RuleVariableValueAssert.assertThatVariable(eventOneValueMap["enrollment_date"]!!)
            .hasValue(wrap(dateFormat!!.format(enrollmentDate)))
            .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat!!.format(enrollmentDate))
        RuleVariableValueAssert.assertThatVariable(eventOneValueMap["enrollment_id"]!!).hasValue("test_enrollment")
            .isTypeOf(RuleValueType.TEXT).hasCandidates("test_enrollment")
        RuleVariableValueAssert.assertThatVariable(eventOneValueMap["enrollment_count"]!!).hasValue("1")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("1")
        RuleVariableValueAssert.assertThatVariable(eventOneValueMap["incident_date"]!!)
            .hasValue(wrap(dateFormat!!.format(incidentDate)))
            .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat!!.format(incidentDate))
        RuleVariableValueAssert.assertThatVariable(eventOneValueMap["tei_count"]!!).hasValue("1")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("1")
        RuleVariableValueAssert.assertThatVariable(eventOneValueMap["test_variable_one"]!!)
            .hasValue("test_attribute_value_one")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("test_attribute_value_one")
        RuleVariableValueAssert.assertThatVariable(eventOneValueMap["test_variable_two"]!!).isTypeOf(RuleValueType.TEXT)
            .hasValue("test_attribute_value_two").hasCandidates("test_attribute_value_two")
        RuleVariableValueAssert.assertThatVariable(eventOneValueMap["event_date"]!!)
            .hasValue(wrap(dateFormat!!.format(eventOneDate)))
            .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat!!.format(eventOneDate))
        RuleVariableValueAssert.assertThatVariable(eventOneValueMap["due_date"]!!)
            .hasValue(wrap(dateFormat!!.format(eventOneDueDate)))
            .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat!!.format(eventOneDueDate))

        // Event two
        RuleVariableValueAssert.assertThatVariable(eventTwoValueMap!!["current_date"]!!).hasValue(wrap(currentDate))
            .isTypeOf(RuleValueType.TEXT).hasCandidates(currentDate)
        RuleVariableValueAssert.assertThatVariable(eventTwoValueMap["event_count"]!!).hasValue("2")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("2")
        RuleVariableValueAssert.assertThatVariable(eventTwoValueMap["enrollment_date"]!!)
            .hasValue(wrap(dateFormat!!.format(enrollmentDate)))
            .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat!!.format(enrollmentDate))
        RuleVariableValueAssert.assertThatVariable(eventTwoValueMap["enrollment_id"]!!).hasValue("test_enrollment")
            .isTypeOf(RuleValueType.TEXT).hasCandidates("test_enrollment")
        RuleVariableValueAssert.assertThatVariable(eventTwoValueMap["enrollment_count"]!!).hasValue("1")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("1")
        RuleVariableValueAssert.assertThatVariable(eventTwoValueMap["incident_date"]!!)
            .hasValue(wrap(dateFormat!!.format(incidentDate)))
            .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat!!.format(incidentDate))
        RuleVariableValueAssert.assertThatVariable(eventTwoValueMap["tei_count"]!!).hasValue("1")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("1")
        RuleVariableValueAssert.assertThatVariable(eventTwoValueMap["test_variable_one"]!!)
            .hasValue("test_attribute_value_one")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("test_attribute_value_one")
        RuleVariableValueAssert.assertThatVariable(eventTwoValueMap["test_variable_two"]!!).isTypeOf(RuleValueType.TEXT)
            .hasValue("test_attribute_value_two").hasCandidates("test_attribute_value_two")
        RuleVariableValueAssert.assertThatVariable(eventTwoValueMap["event_date"]!!)
            .hasValue(wrap(dateFormat!!.format(eventTwoDate)))
            .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat!!.format(eventTwoDate))
        RuleVariableValueAssert.assertThatVariable(eventTwoValueMap["due_date"]!!)
            .hasValue(wrap(dateFormat!!.format(eventTwoDueDate)))
            .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat!!.format(eventTwoDueDate))
    }

    @Test(expected = IllegalStateException::class)
    fun buildShouldThrowOnDuplicateEvent() {
        val ruleEvent = RuleEvent(
            "test_event_two", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, Date(), Date(), null, "", null, ArrayList<RuleDataValue>()
        )
        RuleVariableValueMapBuilder.target(ruleEvent)
            .ruleVariables(ArrayList())
            .ruleEvents( listOf(ruleEvent))
            .build()
    }

    companion object {
        private const val DATE_PATTERN = "yyyy-MM-dd"
        private fun wrap(source: String): String {
            return String.format(Locale.US, "%s", source)
        }

        private fun toDate(date: LocalDate): Date {
            return Date.from(date.atStartOfDay().toInstant(ZoneOffset.UTC))
        }
    }
}
