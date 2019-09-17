package org.hisp.dhis.rules

import org.assertj.core.api.Java6Assertions.assertThat
import org.hisp.dhis.rules.RuleVariableValueAssert.Companion.assertThatVariable
import org.hisp.dhis.rules.models.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.test.assertFailsWith

@RunWith(JUnit4::class)
class RuleVariableValueMapBuilderTests {

    private lateinit var dateFormat: SimpleDateFormat

    @Before
    @Throws(Exception::class)
    fun setUp() {
        dateFormat = SimpleDateFormat(DATE_PATTERN, Locale.US)
    }

    @Test
    fun buildShouldReturnImmutableMap() {
        val ruleEvent = mock(RuleEvent::class.java)
        `when`(ruleEvent.event).thenReturn("test_event_uid")
        `when`(ruleEvent.status).thenReturn(RuleEvent.Status.ACTIVE)
        `when`(ruleEvent.eventDate).thenReturn(dateFormat.parse("1994-02-03"))
        `when`(ruleEvent.dueDate).thenReturn(dateFormat.parse("1995-02-03"))
        `when`(ruleEvent.programStageName).thenReturn("")
        `when`(ruleEvent.programStage).thenReturn("")
        `when`(ruleEvent.organisationUnit).thenReturn("")

        val valueMap = RuleVariableValueMapBuilder.target(ruleEvent)
                .ruleVariables(arrayListOf())
                .triggerEnvironment(TriggerEnvironment.SERVER)
                .build()

        assertThat(valueMap.size).isEqualTo(11)

        //valueMap.clear()
        assertThat(valueMap.size).isEqualTo(11)
    }

    @Test
    fun ruleEnrollmentShouldThrowIfTargetEnrollmentIsAlreadySet() {
        assertFailsWith<IllegalStateException> {
            val ruleEnrollment = mock(RuleEnrollment::class.java)
            RuleVariableValueMapBuilder.target(ruleEnrollment)
                    .ruleEnrollment(ruleEnrollment)
                    .build()
        }
    }

    @Test
    @Throws(ParseException::class)
    fun currentEventVariableShouldContainValuesFromCurrentEvent() {
        val ruleVariableOne = RuleVariableCurrentEvent.create(
                "test_variable_one", "test_dataelement_one", RuleValueType.TEXT)
        val ruleVariableTwo = RuleVariableCurrentEvent.create(
                "test_variable_two", "test_dataelement_two", RuleValueType.TEXT)

        val eventDate = dateFormat.parse("2015-01-01")
        val dueDate = dateFormat.parse("2016-01-01")

        // values from context ruleEvents should be ignored
        val contextEventOne = RuleEvent.create("test_context_event_one", "test_program_stage",
                RuleEvent.Status.ACTIVE, Date(), Date(), "", null, listOf(
                RuleDataValue.create(eventDate, "test_program_stage",
                        "test_dataelement_one", "test_context_value_one"),
                RuleDataValue.create(eventDate, "test_program_stage",
                        "test_dataelement_two", "test_context_value_two")), "")
        val contextEventTwo = RuleEvent.create("test_context_event_two", "test_program_stage",
                RuleEvent.Status.ACTIVE, Date(), Date(), "", null, listOf(
                RuleDataValue.create(eventDate, "test_program_stage",
                        "test_dataelement_one", "test_context_value_three"),
                RuleDataValue.create(eventDate, "test_program_stage",
                        "test_dataelement_two", "test_context_value_four")), "")
        // values from current ruleEvent should be propagated to the variable values
        val currentEvent = RuleEvent.create("test_event_uid", "test_program_stage",
                RuleEvent.Status.ACTIVE, eventDate, dueDate, "", null, listOf(
                RuleDataValue.create(eventDate, "test_program_stage",
                        "test_dataelement_one", "test_value_one"),
                RuleDataValue.create(eventDate, "test_program_stage",
                        "test_dataelement_two", "test_value_two")), "")

        val valueMap: Map<String, RuleVariableValue> = RuleVariableValueMapBuilder.target(currentEvent)
                .ruleVariables(listOf<RuleVariable>(ruleVariableOne, ruleVariableTwo))
                .ruleEvents(listOf(contextEventOne, contextEventTwo))
                .triggerEnvironment(TriggerEnvironment.SERVER)
                .build()

        assertThat(valueMap.size).isEqualTo(13)

        assertThatVariable(valueMap["current_date"]).hasValue(dateFormat.format(Date()).wrap())
                .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat.format(Date()))

        assertThatVariable(valueMap["event_date"]).hasValue(dateFormat.format(eventDate).wrap())
                .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat.format(eventDate))

        assertThatVariable(valueMap["event_status"]).hasValue(RuleEvent.Status.ACTIVE.toString().wrap())
                .isTypeOf(RuleValueType.TEXT).hasCandidates(RuleEvent.Status.ACTIVE.toString())

        // event count variable should respect current event
        assertThatVariable(valueMap["event_count"]).hasValue("3")
                .isTypeOf(RuleValueType.NUMERIC).hasCandidates("3")

        assertThatVariable(valueMap["event_id"]).hasValue("'test_event_uid'")
                .isTypeOf(RuleValueType.TEXT).hasCandidates("test_event_uid")

        assertThatVariable(valueMap["due_date"]).hasValue(dateFormat.format(dueDate).wrap())
                .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat.format(dueDate))

        assertThatVariable(valueMap["test_variable_one"]).hasValue("'test_value_one'")
                .isTypeOf(RuleValueType.TEXT).hasCandidates("test_value_one")

        assertThatVariable(valueMap["test_variable_two"]).hasValue("'test_value_two'")
                .isTypeOf(RuleValueType.TEXT).hasCandidates("test_value_two")
    }

    @Test
    @Throws(ParseException::class)
    fun newestEventProgramVariableShouldContainValueFromNewestContextEvent() {
        val ruleVariableOne = RuleVariableNewestEvent.create(
                "test_variable_one", "test_dataelement_one", RuleValueType.TEXT)
        val ruleVariableTwo = RuleVariableNewestEvent.create(
                "test_variable_two", "test_dataelement_two", RuleValueType.TEXT)

        val oldestEventDate = dateFormat.parse("2013-01-01")
        val newestEventDate = dateFormat.parse("2017-01-01")
        val currentEventDate = dateFormat.parse("2015-01-01")
        val currentEventDueDate = dateFormat.parse("2016-01-01")

        val oldestRuleEvent = RuleEvent.create("test_event_uid_oldest", "test_program_stage",
                RuleEvent.Status.ACTIVE, oldestEventDate, oldestEventDate, "", null, listOf(
                RuleDataValue.create(oldestEventDate, "test_program_stage",
                        "test_dataelement_one", "test_value_one_oldest"),
                RuleDataValue.create(oldestEventDate, "test_program_stage",
                        "test_dataelement_two", "test_value_two_oldest")), "")
        val newestRuleEvent = RuleEvent.create("test_event_uid_newest", "test_program_stage",
                RuleEvent.Status.ACTIVE, newestEventDate, newestEventDate, "", null, listOf(
                RuleDataValue.create(newestEventDate, "test_program_stage",
                        "test_dataelement_one", "test_value_one_newest"),
                RuleDataValue.create(newestEventDate, "test_program_stage",
                        "test_dataelement_two", "test_value_two_newest")), "")
        val currentEvent = RuleEvent.create("test_event_uid_current", "test_program_stage",
                RuleEvent.Status.ACTIVE, currentEventDate, currentEventDueDate, "", null, listOf(
                RuleDataValue.create(currentEventDate, "test_program_stage",
                        "test_dataelement_one", "test_value_one_current"),
                RuleDataValue.create(currentEventDate, "test_program_stage",
                        "test_dataelement_two", "test_value_two_current")), "")

        val valueMap = RuleVariableValueMapBuilder.target(currentEvent)
                .ruleVariables(listOf<RuleVariable>(ruleVariableOne, ruleVariableTwo))
                .ruleEvents(listOf(oldestRuleEvent, newestRuleEvent))
                .triggerEnvironment(TriggerEnvironment.SERVER)
                .build()

        assertThat(valueMap.size).isEqualTo(13)

        assertThatVariable(valueMap["current_date"]).hasValue(dateFormat.format(Date()).wrap())
                .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat.format(Date()))

        assertThatVariable(valueMap["event_date"])
                .hasValue(dateFormat.format(currentEventDate).wrap())
                .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat.format(currentEventDate))

        assertThatVariable(valueMap["event_count"]).hasValue("3")
                .isTypeOf(RuleValueType.NUMERIC).hasCandidates("3")

        assertThatVariable(valueMap["event_id"]).hasValue("'test_event_uid_current'")
                .isTypeOf(RuleValueType.TEXT).hasCandidates("test_event_uid_current")

        assertThatVariable(valueMap["due_date"])
                .hasValue(dateFormat.format(currentEventDueDate).wrap())
                .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat.format(currentEventDueDate))

        assertThatVariable(valueMap["test_variable_one"]).hasValue("'test_value_one_newest'")
                .isTypeOf(RuleValueType.TEXT).hasCandidates("test_value_one_newest",
                        "test_value_one_current", "test_value_one_oldest")
        assertThatVariable(valueMap["test_variable_two"]).hasValue("'test_value_two_newest'")
                .isTypeOf(RuleValueType.TEXT).hasCandidates("test_value_two_newest",
                        "test_value_two_current", "test_value_two_oldest")
    }

    @Test
    @Throws(ParseException::class)
    fun newestEventProgramVariableShouldReturnValuesFromCurrentEventWhenIfNewest() {
        val ruleVariableOne = RuleVariableNewestEvent.create(
                "test_variable_one", "test_dataelement_one", RuleValueType.TEXT)
        val ruleVariableTwo = RuleVariableNewestEvent.create(
                "test_variable_two", "test_dataelement_two", RuleValueType.TEXT)

        val dateEventOne = dateFormat.parse("2013-01-01")
        val dateEventTwo = dateFormat.parse("2014-01-01")
        val dateEventCurrent = dateFormat.parse("2015-01-01")
        val dateEventDueCurrent = dateFormat.parse("2016-01-01")

        val firstRuleEvent = RuleEvent.create("test_event_uid_one", "test_program_stage",
                RuleEvent.Status.ACTIVE, dateEventOne, dateEventOne, "", null, listOf(
                RuleDataValue.create(dateEventOne, "test_program_stage",
                        "test_dataelement_one", "test_value_dataelement_one_first"),
                RuleDataValue.create(dateEventOne, "test_program_stage",
                        "test_dataelement_two", "test_value_dataelement_two_first")), "")
        val secondRuleEvent = RuleEvent.create("test_event_uid_two", "test_program_stage",
                RuleEvent.Status.ACTIVE, dateEventTwo, dateEventTwo, "", null, listOf(
                RuleDataValue.create(dateEventTwo, "test_program_stage",
                        "test_dataelement_one", "test_value_dataelement_one_second"),
                RuleDataValue.create(dateEventTwo, "test_program_stage",
                        "test_dataelement_two", "test_value_dataelement_two_second")), "")
        val currentEvent = RuleEvent.create("test_event_uid_current", "test_program_stage",
                RuleEvent.Status.ACTIVE, dateEventCurrent, dateEventDueCurrent, "", null, listOf(
                RuleDataValue.create(dateEventCurrent, "test_program_stage",
                        "test_dataelement_one", "test_value_dataelement_one_current"),
                RuleDataValue.create(dateEventCurrent, "test_program_stage",
                        "test_dataelement_two", "test_value_dataelement_two_current")), "")

        val valueMap = RuleVariableValueMapBuilder.target(currentEvent)
                .ruleVariables(listOf<RuleVariable>(ruleVariableOne, ruleVariableTwo))
                .triggerEnvironment(TriggerEnvironment.SERVER)
                .ruleEvents(listOf(firstRuleEvent, secondRuleEvent))
                .build()

        assertThat(valueMap.size).isEqualTo(13)

        assertThatVariable(valueMap["current_date"]).hasValue(dateFormat.format(Date()).wrap())
                .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat.format(Date()))

        assertThatVariable(valueMap["event_date"])
                .hasValue(dateFormat.format(dateEventCurrent).wrap())
                .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat.format(dateEventCurrent))

        assertThatVariable(valueMap["event_count"]).hasValue("3")
                .isTypeOf(RuleValueType.NUMERIC).hasCandidates("3")

        assertThatVariable(valueMap["event_id"]).hasValue("'test_event_uid_current'")
                .isTypeOf(RuleValueType.TEXT).hasCandidates("test_event_uid_current")

        assertThatVariable(valueMap["due_date"])
                .hasValue(dateFormat.format(dateEventDueCurrent).wrap())
                .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat.format(dateEventDueCurrent))

        assertThatVariable(valueMap["test_variable_one"])
                .hasValue("'test_value_dataelement_one_current'")
                .isTypeOf(RuleValueType.TEXT).hasCandidates("test_value_dataelement_one_current",
                        "test_value_dataelement_one_second", "test_value_dataelement_one_first")

        assertThatVariable(valueMap["test_variable_two"])
                .hasValue("'test_value_dataelement_two_current'")
                .isTypeOf(RuleValueType.TEXT).hasCandidates("test_value_dataelement_two_current",
                        "test_value_dataelement_two_second", "test_value_dataelement_two_first")
    }

    @Test
    @Throws(ParseException::class)
    fun newestEventProgramStageVariableShouldContainValueFromNewestContextEvent() {
        val ruleVariable = RuleVariableNewestStageEvent.create("test_variable",
                "test_dataelement", "test_program_stage_one", RuleValueType.TEXT)

        val dateEventOne = dateFormat.parse("2014-02-03")
        val dateEventTwo = dateFormat.parse("2014-03-03")
        val dateEventThree = dateFormat.parse("2015-02-03")
        val dateEventCurrent = dateFormat.parse("2011-02-03")
        val dateEventDueCurrent = dateFormat.parse("2011-02-03")

        val eventOne = RuleEvent.create("test_event_uid_one", "test_program_stage_one",
                RuleEvent.Status.ACTIVE, dateEventOne, dateEventOne, "", null, listOf(
                RuleDataValue.create(dateEventOne, "test_program_stage_one",
                        "test_dataelement", "test_value_one")), "")
        val eventTwo = RuleEvent.create("test_event_uid_two", "test_program_stage_two",
                RuleEvent.Status.ACTIVE, dateEventTwo, dateEventTwo, "", null, listOf(
                RuleDataValue.create(dateEventTwo, "test_program_stage_two",
                        "test_dataelement", "test_value_two")), "")
        val eventThree = RuleEvent.create("test_event_uid_three", "test_program_stage_two",
                RuleEvent.Status.ACTIVE, dateEventThree, dateEventThree, "", null, listOf(
                RuleDataValue.create(dateEventThree, "test_program_stage_two",
                        "test_dataelement", "test_value_three")), "")
        val eventCurrent = RuleEvent.create("test_event_uid_current", "test_program_stage_one",
                RuleEvent.Status.ACTIVE, dateEventCurrent, dateEventDueCurrent, "", null, listOf(
                RuleDataValue.create(dateEventCurrent, "test_program_stage_one",
                        "test_dataelement", "test_value_current")), "")

        val valueMap = RuleVariableValueMapBuilder.target(eventCurrent)
                .ruleVariables(listOf<RuleVariable>(ruleVariable))
                .ruleEvents(listOf(eventOne, eventTwo, eventThree))
                .triggerEnvironment(TriggerEnvironment.SERVER)
                .build()

        assertThat(valueMap.size).isEqualTo(12)

        assertThatVariable(valueMap["current_date"]).hasValue(dateFormat.format(Date()).wrap())
                .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat.format(Date()))

        assertThatVariable(valueMap["event_date"])
                .hasValue(dateFormat.format(dateEventCurrent).wrap())
                .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat.format(dateEventCurrent))

        assertThatVariable(valueMap["event_count"]).hasValue("4")
                .isTypeOf(RuleValueType.NUMERIC).hasCandidates("4")

        assertThatVariable(valueMap["event_id"]).hasValue("'test_event_uid_current'")
                .isTypeOf(RuleValueType.TEXT).hasCandidates("test_event_uid_current")

        assertThatVariable(valueMap["due_date"])
                .hasValue(dateFormat.format(dateEventDueCurrent).wrap())
                .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat.format(dateEventDueCurrent))

        assertThatVariable(valueMap["test_variable"]).hasValue("'test_value_one'")
                .isTypeOf(RuleValueType.TEXT).hasCandidates("test_value_one", "test_value_current")
    }

    @Test
    @Throws(ParseException::class)
    fun newestEventProgramStageVariableShouldNotContainAnyValues() {
        val ruleVariable = RuleVariableNewestStageEvent.create("test_variable",
                "test_dataelement", "test_program_stage_one", RuleValueType.TEXT)

        val dateEventOne = dateFormat.parse("2014-03-03")
        val dateEventTwo = dateFormat.parse("2015-02-03")

        val ruleEventOne = RuleEvent.create("test_event_uid_one", "test_program_stage_two",
                RuleEvent.Status.ACTIVE, dateEventOne, dateEventOne, "", null, listOf(
                RuleDataValue.create(dateEventOne, "test_program_stage_two",
                        "test_dataelement", "test_value_one")), "")
        val ruleEventTwo = RuleEvent.create("test_event_uid_two", "test_program_stage_two",
                RuleEvent.Status.ACTIVE, dateEventTwo, dateEventTwo, "", null, listOf(
                RuleDataValue.create(dateEventTwo, "test_program_stage_two",
                        "test_dataelement", "test_value_two")), "")

        val valueMap = RuleVariableValueMapBuilder.target(ruleEventTwo)
                .ruleVariables(listOf<RuleVariable>(ruleVariable))
                .triggerEnvironment(TriggerEnvironment.SERVER)
                .ruleEvents(listOf(ruleEventOne))
                .build()

        assertThat(valueMap.size).isEqualTo(12)

        assertThatVariable(valueMap["current_date"]).hasValue(dateFormat.format(Date()).wrap())
                .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat.format(Date()))

        assertThatVariable(valueMap["event_date"]).hasValue(dateFormat.format(dateEventTwo).wrap())
                .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat.format(dateEventTwo))

        assertThatVariable(valueMap["event_count"]).hasValue("2")
                .isTypeOf(RuleValueType.NUMERIC).hasCandidates("2")

        assertThatVariable(valueMap["event_id"]).hasValue("'test_event_uid_two'")
                .isTypeOf(RuleValueType.TEXT).hasCandidates("test_event_uid_two")

        assertThatVariable(valueMap["due_date"]).hasValue(dateFormat.format(dateEventTwo).wrap())
                .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat.format(dateEventTwo))

        assertThatVariable(valueMap["test_variable"]).hasValue(null)
                .isTypeOf(RuleValueType.TEXT).hasCandidates()
    }

    @Test
    @Throws(ParseException::class)
    fun previousEventVariableShouldContainValuesFromPreviousEvent() {
        val ruleVariable = RuleVariablePreviousEvent.create("test_variable",
                "test_dataelement", RuleValueType.TEXT)

        val dateEventOne = dateFormat.parse("2014-02-03")
        val dateEventTwo = dateFormat.parse("2014-03-03")
        val dateEventThree = dateFormat.parse("2015-02-03")
        val dateEventCurrent = dateFormat.parse("2014-05-03")

        val ruleEventOne = RuleEvent.create("test_event_uid_one", "test_program_stage",
                RuleEvent.Status.ACTIVE, dateEventOne, dateEventOne, "", null, listOf(
                RuleDataValue.create(dateEventOne, "test_program_stage_one",
                        "test_dataelement", "test_value_one")), "")
        val ruleEventTwo = RuleEvent.create("test_event_uid_two", "test_program_stage",
                RuleEvent.Status.ACTIVE, dateEventTwo, dateEventTwo, "", null, listOf(
                RuleDataValue.create(dateEventTwo, "test_program_stage_two",
                        "test_dataelement", "test_value_two")), "")
        val ruleEventThree = RuleEvent.create("test_event_uid_three", "test_program_stage",
                RuleEvent.Status.ACTIVE, dateEventThree, dateEventThree, "", null, listOf(
                RuleDataValue.create(dateEventThree, "test_program_stage_two",
                        "test_dataelement", "test_value_three")), "")
        val ruleEventCurrent = RuleEvent.create("test_event_uid_current", "test_program_stage",
                RuleEvent.Status.ACTIVE, dateEventCurrent, dateEventCurrent, "", null, listOf(
                RuleDataValue.create(dateEventCurrent, "test_program_stage_one",
                        "test_dataelement", "test_value_current")), "")

        val valueMap = RuleVariableValueMapBuilder.target(ruleEventCurrent)
                .ruleVariables(listOf<RuleVariable>(ruleVariable))
                .triggerEnvironment(TriggerEnvironment.SERVER)
                .ruleEvents(listOf(ruleEventOne, ruleEventTwo, ruleEventThree))
                .build()

        assertThat(valueMap.size).isEqualTo(12)

        assertThatVariable(valueMap["current_date"]).hasValue(dateFormat.format(Date()).wrap())
                .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat.format(Date()))

        assertThatVariable(valueMap["event_date"])
                .hasValue(dateFormat.format(dateEventCurrent).wrap())
                .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat.format(dateEventCurrent))

        assertThatVariable(valueMap["event_count"]).hasValue("4")
                .isTypeOf(RuleValueType.NUMERIC).hasCandidates("4")

        assertThatVariable(valueMap["event_id"]).hasValue("'test_event_uid_current'")
                .isTypeOf(RuleValueType.TEXT).hasCandidates("test_event_uid_current")

        assertThatVariable(valueMap["due_date"])
                .hasValue(dateFormat.format(dateEventCurrent).wrap())
                .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat.format(dateEventCurrent))

        assertThatVariable(valueMap["test_variable"]).hasValue("'test_value_two'")
                .isTypeOf(RuleValueType.TEXT)
                .hasCandidates("test_value_three", "test_value_current", "test_value_two", "test_value_one")
    }

    @Test
    @Throws(ParseException::class)
    fun attributeVariableShouldContainValuesFromContextEnrollment() {
        val ruleVariableOne = RuleVariableAttribute.create("test_variable_one",
                "test_attribute_one", RuleValueType.TEXT)
        val ruleVariableTwo = RuleVariableAttribute.create("test_variable_two",
                "test_attribute_two", RuleValueType.TEXT)

        val eventDate = dateFormat.parse("2015-01-01")
        val enrollmentDate = dateFormat.parse("2014-03-01")

        // values from ruleEnrollment should end up in ruleVariables
        val ruleEnrollment = RuleEnrollment.create("test_enrollment",
                enrollmentDate, enrollmentDate, RuleEnrollment.Status.ACTIVE, "", null, listOf(
                RuleAttributeValue.create("test_attribute_one", "test_attribute_value_one"),
                RuleAttributeValue.create("test_attribute_two", "test_attribute_value_two")), "")

        // values from context ruleEvents should be ignored
        val contextEvent = RuleEvent.create("test_context_event_one", "test_program_stage",
                RuleEvent.Status.ACTIVE, eventDate, Date(), "", null, listOf(
                RuleDataValue.create(eventDate, "test_program_stage",
                        "test_dataelement_one", "test_context_value_one"),
                RuleDataValue.create(eventDate, "test_program_stage",
                        "test_dataelement_two", "test_context_value_two")), "")
        val currentEvent = RuleEvent.create("test_event_uid", "test_program_stage",
                RuleEvent.Status.ACTIVE, eventDate, eventDate, "", null, listOf(
                RuleDataValue.create(eventDate, "test_program_stage",
                        "test_dataelement_one", "test_value_one"),
                RuleDataValue.create(eventDate, "test_program_stage",
                        "test_dataelement_two", "test_value_two")), "")

        // here we will expect correct values to be returned
        val valueMap = RuleVariableValueMapBuilder.target(currentEvent)
                .ruleEnrollment(ruleEnrollment)
                .triggerEnvironment(TriggerEnvironment.SERVER)
                .ruleVariables(listOf<RuleVariable>(ruleVariableOne, ruleVariableTwo))
                .ruleEvents(listOf(contextEvent))
                .build()

        assertThat(valueMap.size).isEqualTo(20)

        assertThatVariable(valueMap["current_date"]).hasValue(dateFormat.format(Date()).wrap())
                .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat.format(Date()))

        assertThatVariable(valueMap["event_date"]).hasValue(dateFormat.format(eventDate).wrap())
                .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat.format(eventDate))

        assertThatVariable(valueMap["event_count"]).hasValue("2")
                .isTypeOf(RuleValueType.NUMERIC).hasCandidates("2")

        assertThatVariable(valueMap["event_id"]).hasValue("'test_event_uid'")
                .isTypeOf(RuleValueType.TEXT).hasCandidates("test_event_uid")

        assertThatVariable(valueMap["due_date"]).hasValue(dateFormat.format(eventDate).wrap())
                .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat.format(eventDate))

        assertThatVariable(valueMap["enrollment_status"]).hasValue(RuleEnrollment.Status.ACTIVE.toString().wrap())
                .isTypeOf(RuleValueType.TEXT).hasCandidates(RuleEnrollment.Status.ACTIVE.toString())

        assertThatVariable(valueMap["enrollment_date"])
                .hasValue(dateFormat.format(enrollmentDate).wrap())
                .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat.format(enrollmentDate))

        assertThatVariable(valueMap["enrollment_id"]).hasValue("'test_enrollment'")
                .isTypeOf(RuleValueType.TEXT).hasCandidates("test_enrollment")

        assertThatVariable(valueMap["enrollment_count"]).hasValue("1")
                .isTypeOf(RuleValueType.NUMERIC).hasCandidates("1")

        assertThatVariable(valueMap["incident_date"])
                .hasValue(dateFormat.format(enrollmentDate).wrap())
                .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat.format(enrollmentDate))

        assertThatVariable(valueMap["tei_count"]).hasValue("1")
                .isTypeOf(RuleValueType.NUMERIC).hasCandidates("1")

        assertThatVariable(valueMap["test_variable_one"]).hasValue("'test_attribute_value_one'")
                .isTypeOf(RuleValueType.TEXT).hasCandidates("test_attribute_value_one")
        assertThatVariable(valueMap["test_variable_two"]).hasValue("'test_attribute_value_two'")
                .isTypeOf(RuleValueType.TEXT).hasCandidates("test_attribute_value_two")
    }

    @Test
    @Throws(ParseException::class)
    fun ruleEnrollmentValuesShouldBePropagatedToMapCorrectly() {
        val ruleVariableOne = RuleVariableAttribute.create("test_variable_one",
                "test_attribute_one", RuleValueType.NUMERIC)
        val ruleVariableTwo = RuleVariableAttribute.create("test_variable_two",
                "test_attribute_two", RuleValueType.TEXT)
        val ruleVariableThree = RuleVariableCurrentEvent.create("test_variable_three",
                "test_dataelement_one", RuleValueType.BOOLEAN)

        val currentDate = dateFormat.format(Date())
        val enrollmentDate = dateFormat.parse("2017-02-02")
        val incidentDate = dateFormat.parse("2017-04-02")
        val ruleEnrollment = RuleEnrollment.create("test_enrollment", incidentDate,
                enrollmentDate, RuleEnrollment.Status.ACTIVE, "", null, listOf(
                RuleAttributeValue.create("test_attribute_one", "test_attribute_value_one"),
                RuleAttributeValue.create("test_attribute_two", "test_attribute_value_two"),
                RuleAttributeValue.create("test_attribute_three", "test_attribute_value_three")), "")

        val ruleEventOne = RuleEvent.create("test_event_one", "test_program_stage",
                RuleEvent.Status.ACTIVE, Date(), Date(), "", null, arrayListOf(), "")
        val ruleEventTwo = RuleEvent.create("test_event_two", "test_program_stage",
                RuleEvent.Status.ACTIVE, Date(), Date(), "", null, arrayListOf(), "")

        val valueMap = RuleVariableValueMapBuilder.target(ruleEnrollment)
                .ruleVariables(listOf(ruleVariableOne, ruleVariableTwo, ruleVariableThree))
                .ruleEvents(listOf(ruleEventOne, ruleEventTwo))
                .triggerEnvironment(TriggerEnvironment.SERVER)
                .build()

        assertThat(valueMap.size).isEqualTo(14)

        assertThatVariable(valueMap["current_date"]).hasValue(currentDate.wrap())
                .isTypeOf(RuleValueType.TEXT).hasCandidates(currentDate)

        assertThatVariable(valueMap["event_count"]).hasValue("2")
                .isTypeOf(RuleValueType.NUMERIC).hasCandidates("2")

        assertThatVariable(valueMap["enrollment_date"])
                .hasValue(dateFormat.format(enrollmentDate).wrap())
                .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat.format(enrollmentDate))

        assertThatVariable(valueMap["enrollment_id"]).hasValue("'test_enrollment'")
                .isTypeOf(RuleValueType.TEXT).hasCandidates("test_enrollment")

        assertThatVariable(valueMap["enrollment_count"]).hasValue("1")
                .isTypeOf(RuleValueType.NUMERIC).hasCandidates("1")

        assertThatVariable(valueMap["incident_date"])
                .hasValue(dateFormat.format(incidentDate).wrap())
                .isTypeOf(RuleValueType.TEXT).hasCandidates(dateFormat.format(incidentDate))

        assertThatVariable(valueMap["tei_count"]).hasValue("1")
                .isTypeOf(RuleValueType.NUMERIC).hasCandidates("1")

        assertThatVariable(valueMap["test_variable_one"]).hasValue("test_attribute_value_one")
                .isTypeOf(RuleValueType.NUMERIC).hasCandidates("test_attribute_value_one")

        assertThatVariable(valueMap["test_variable_two"]).isTypeOf(RuleValueType.TEXT)
                .hasValue("'test_attribute_value_two'").hasCandidates("test_attribute_value_two")
    }

    @Test
    fun buildShouldThrowOnDuplicateEvent() {
        val ruleEvent = RuleEvent.create("test_event_two", "test_program_stage",
                RuleEvent.Status.ACTIVE, Date(), Date(), "", null, arrayListOf(), "")

        assertFailsWith<IllegalStateException> {
            RuleVariableValueMapBuilder.target(ruleEvent)
                    .ruleVariables(arrayListOf())
                    .ruleEvents(listOf(ruleEvent))
                    .build()
        }
    }

    companion object {
        private const val DATE_PATTERN = "yyyy-MM-dd"
    }
}
