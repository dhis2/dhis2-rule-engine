package org.dhis2.ruleengine

import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDate
import org.assertj.core.api.Assertions.assertThat
import org.dhis2.ruleengine.RuleEngineTestUtils.add
import org.dhis2.ruleengine.RuleEngineTestUtils.currentDate
import org.dhis2.ruleengine.RuleVariableValueAssert.Companion.assertThatVariable
import org.dhis2.ruleengine.models.*
import org.junit.jupiter.api.Test
import java.text.ParseException
import java.util.*

class RuleVariableValueMapBuilderTest {

    /* @Test(expected = UnsupportedOperationException::class)
     @Throws(ParseException::class)
     fun buildShouldReturnImmutableMap() {
         val ruleEvent: RuleEvent = Mockito.mock(RuleEvent::class.java)
         `when`(ruleEvent.event).thenReturn("test_event_uid")
         `when`(ruleEvent.status).thenReturn(RuleEvent.Status.ACTIVE)
         `when`(ruleEvent.eventDate).thenReturn("1994-02-03".toLocalDate())
         `when`(ruleEvent.dueDate).thenReturn("1995-02-03".toLocalDate())
         `when`(ruleEvent.programStageName).thenReturn("")
         `when`(ruleEvent.programStage).thenReturn("")
         `when`(ruleEvent.organisationUnit).thenReturn("")

         mutableMapOf<String, RuleVariableValue>().buildValueMap(
             ruleEvents = listOf(),
             ruleEnrollment = null,
             ruleEvent = ruleEvent,
             ruleVariables = listOf(),
             constantsValues = emptyMap()
         ).clear()
     }

     @Test(expected = IllegalStateException::class)
     fun ruleEnrollmentShouldThrowIfTargetEnrollmentIsAlreadySet() {
         val ruleEnrollment: RuleEnrollment = Mockito.mock(RuleEnrollment::class.java)
         mutableMapOf<String, RuleVariableValue>().buildValueMap(
             ruleEvents = listOf(),
             ruleEnrollment = ruleEnrollment,
             ruleEvent = null,
             ruleVariables = listOf(),
             constantsValues = emptyMap()
         )
     }*/

    @Test
    @Throws(ParseException::class)
    fun currentEventVariableShouldContainValuesFromCurrentEvent() {
        val ruleVariableOne: RuleVariable = RuleVariable.RuleVariableCurrentEvent(
            "test_variable_one", "test_dataelement_one", RuleValueType.TEXT
        )
        val ruleVariableTwo: RuleVariable = RuleVariable.RuleVariableCurrentEvent(
            "test_variable_two", "test_dataelement_two", RuleValueType.TEXT
        )
        val eventDate = "2015-01-01".toLocalDate()
        val dueDate = "2016-01-01".toLocalDate()

        // values from context ruleEvents should be ignored
        val contextEventOne: RuleEvent = RuleEvent(
            "test_context_event_one", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), currentDate(), "", null, Arrays.asList(
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
        val contextEventTwo: RuleEvent = RuleEvent(
            "test_context_event_two", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), currentDate(), "", null, listOf(
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
        val currentEvent: RuleEvent = RuleEvent(
            "test_event_uid", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, eventDate, dueDate, null, "", null, listOf(
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
        val valueMap = mutableMapOf<String, RuleVariableValue>()
            .buildValueMap(
                ruleEvents = listOf(contextEventOne, contextEventTwo),
                ruleEnrollment = null,
                ruleEvent = currentEvent,
                ruleVariables = listOf(ruleVariableOne, ruleVariableTwo),
                constantsValues = emptyMap()
            )

        assertThat(valueMap.size).isEqualTo(13)
        assertThatVariable(valueMap["current_date"]).hasValue(currentDate().toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(currentDate().toString())
        assertThatVariable(valueMap["event_date"]).hasValue(eventDate.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(eventDate.toString())
        assertThatVariable(valueMap["event_status"]).hasValue(RuleEvent.Status.ACTIVE.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(RuleEvent.Status.ACTIVE.toString())

        // event count variable should respect current event
        assertThatVariable(valueMap["event_count"]).hasValue("3")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("3")
        assertThatVariable(valueMap["event_id"]).hasValue("test_event_uid")
            .isTypeOf(RuleValueType.TEXT).hasCandidates("test_event_uid")
        assertThatVariable(valueMap["due_date"]).hasValue(dueDate.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(dueDate.toString())
        assertThatVariable(valueMap["test_variable_one"]).hasValue("test_value_one")
            .isTypeOf(RuleValueType.TEXT).hasCandidates("test_value_one")
        assertThatVariable(valueMap["test_variable_two"]).hasValue("test_value_two")
            .isTypeOf(RuleValueType.TEXT).hasCandidates("test_value_two")
    }

    @Test
    @Throws(ParseException::class)
    fun newestEventProgramVariableShouldContainValueFromNewestContextEvent() {
        val ruleVariableOne: RuleVariable = RuleVariable.RuleVariableNewestEvent(
            "test_variable_one", "test_dataelement_one", RuleValueType.TEXT
        )
        val ruleVariableTwo: RuleVariable = RuleVariable.RuleVariableNewestEvent(
            "test_variable_two", "test_dataelement_two", RuleValueType.TEXT
        )
        val oldestEventDate: LocalDate = "2013-01-01".toLocalDate()
        val newestEventDate: LocalDate = "2017-01-01".toLocalDate()
        val currentEventDate: LocalDate = "2015-01-01".toLocalDate()
        val currentEventDueDate: LocalDate? = null
        val oldestRuleEvent = RuleEvent(
            "test_event_uid_oldest", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, oldestEventDate, oldestEventDate, null, "", null, listOf(
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
            RuleEvent.Status.ACTIVE, newestEventDate, newestEventDate, null, "", null, listOf(
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
        val currentEvent: RuleEvent = RuleEvent(
            "test_event_uid_current", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, currentEventDate, currentEventDueDate, null, "", null, listOf(
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
        val valueMap = buildValueMap(
            ruleEvents = listOf(oldestRuleEvent, newestRuleEvent),
            ruleEnrollment = null,
            ruleEvent = currentEvent,
            ruleVariables = listOf(ruleVariableOne, ruleVariableTwo),
            constantsValues = emptyMap()
        )
        assertThat(valueMap.size).isEqualTo(12)
        assertThatVariable(valueMap["current_date"]).hasValue(
            currentDate().toString()
        )
            .isTypeOf(RuleValueType.TEXT).hasCandidates(currentDate().toString())
        assertThatVariable(valueMap["event_date"])
            .hasValue(currentEventDate.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(currentEventDate.toString())
        assertThatVariable(valueMap["event_count"]).hasValue("3")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("3")
        assertThatVariable(valueMap["event_id"]).hasValue("test_event_uid_current")
            .isTypeOf(RuleValueType.TEXT).hasCandidates("test_event_uid_current")
        assertThat<RuleVariableValue>(valueMap["due_date"]).isNull()
        assertThatVariable(valueMap["test_variable_one"])
            .hasValue("test_value_one_newest")
            .isTypeOf(RuleValueType.TEXT)
            .hasCandidates(
                "test_value_one_newest",
                "test_value_one_current",
                "test_value_one_oldest"
            )
        assertThatVariable(valueMap["test_variable_two"]).hasValue("test_value_two_newest")
            .isTypeOf(RuleValueType.TEXT).hasCandidates(
                "test_value_two_newest",
                "test_value_two_current",
                "test_value_two_oldest"
            )
    }

    @Test
    @Throws(ParseException::class)
    fun newestEventProgramVariableShouldReturnValuesFromCurrentEventWhenIfNewest() {
        val ruleVariableOne: RuleVariable = RuleVariable.RuleVariableNewestEvent(
            "test_variable_one", "test_dataelement_one", RuleValueType.TEXT
        )
        val ruleVariableTwo: RuleVariable = RuleVariable.RuleVariableNewestEvent(
            "test_variable_two", "test_dataelement_two", RuleValueType.TEXT
        )
        val dateEventOne: LocalDate = "2013-01-01".toLocalDate()
        val dateEventTwo: LocalDate = "2014-01-01".toLocalDate()
        val dateEventCurrent: LocalDate = "2015-01-01".toLocalDate()
        val dateEventDueCurrent: LocalDate = "2016-01-01".toLocalDate()
        val firstRuleEvent: RuleEvent = RuleEvent(
            "test_event_uid_one", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, dateEventOne, dateEventOne, null, "", null, listOf(
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
        val secondRuleEvent: RuleEvent = RuleEvent(
            "test_event_uid_two", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, dateEventTwo, dateEventTwo, null, "", null, listOf(
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
        val currentEvent: RuleEvent = RuleEvent(
            "test_event_uid_current", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, dateEventCurrent, dateEventDueCurrent, null, "", null, listOf(
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
        val valueMap: Map<String, RuleVariableValue> = buildValueMap(
            ruleEvents = Arrays.asList(firstRuleEvent, secondRuleEvent),
            ruleEnrollment = null,
            ruleEvent = currentEvent,
            ruleVariables = Arrays.asList(ruleVariableOne, ruleVariableTwo),
            constantsValues = emptyMap()
        )
        assertThat(valueMap.size).isEqualTo(13)
        assertThatVariable(valueMap["current_date"]).hasValue(
            currentDate().toString()
        )
            .isTypeOf(RuleValueType.TEXT).hasCandidates(currentDate().toString())
        assertThatVariable(valueMap["event_date"])
            .hasValue(dateEventCurrent.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(dateEventCurrent.toString())
        assertThatVariable(valueMap["event_count"]).hasValue("3")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("3")
        assertThatVariable(valueMap["event_id"]).hasValue("test_event_uid_current")
            .isTypeOf(RuleValueType.TEXT).hasCandidates("test_event_uid_current")
        assertThatVariable(valueMap["due_date"])
            .hasValue(dateEventDueCurrent.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(dateEventDueCurrent.toString())
        assertThatVariable(valueMap["test_variable_one"])
            .hasValue("test_value_dataelement_one_current")
            .isTypeOf(RuleValueType.TEXT).hasCandidates(
                "test_value_dataelement_one_current",
                "test_value_dataelement_one_second",
                "test_value_dataelement_one_first"
            )
        assertThatVariable(valueMap["test_variable_two"])
            .hasValue("test_value_dataelement_two_current")
            .isTypeOf(RuleValueType.TEXT).hasCandidates(
                "test_value_dataelement_two_current",
                "test_value_dataelement_two_second",
                "test_value_dataelement_two_first"
            )
    }

    @Test
    @Throws(ParseException::class)
    fun newestEventProgramStageVariableShouldContainValueFromNewestContextEvent() {
        val ruleVariable: RuleVariable = RuleVariable.RuleVariableNewestStageEvent(
            "test_variable",
            "test_dataelement", RuleValueType.TEXT, "test_program_stage_one"
        )
        val dateEventOne: LocalDate = "2014-02-03".toLocalDate()
        val dateEventTwo: LocalDate = "2014-03-03".toLocalDate()
        val dateEventThree: LocalDate = "2015-02-03".toLocalDate()
        val dateEventCurrent: LocalDate = "2011-02-03".toLocalDate()
        val dateEventDueCurrent: LocalDate = "2011-02-03".toLocalDate()
        val eventOne: RuleEvent = RuleEvent(
            "test_event_uid_one", "test_program_stage_one", "",
            RuleEvent.Status.ACTIVE, dateEventOne, dateEventOne, null, "", null, listOf(
                RuleDataValue(
                    dateEventOne, "test_program_stage_one",
                    "test_dataelement", "test_value_one"
                )
            )
        )
        val eventTwo: RuleEvent = RuleEvent(
            "test_event_uid_two", "test_program_stage_two", "",
            RuleEvent.Status.ACTIVE, dateEventTwo, dateEventTwo, null, "", null, listOf(
                RuleDataValue(
                    dateEventTwo, "test_program_stage_two",
                    "test_dataelement", "test_value_two"
                )
            )
        )
        val eventThree: RuleEvent = RuleEvent(
            "test_event_uid_three", "test_program_stage_two", "",
            RuleEvent.Status.ACTIVE, dateEventThree, dateEventThree, null, "", null, listOf(
                RuleDataValue(
                    dateEventThree, "test_program_stage_two",
                    "test_dataelement", "test_value_three"
                )
            )
        )
        val eventCurrent: RuleEvent = RuleEvent(
            "test_event_uid_current", "test_program_stage_one", "",
            RuleEvent.Status.ACTIVE, dateEventCurrent, dateEventDueCurrent, null, "", null, listOf(
                RuleDataValue(
                    dateEventCurrent, "test_program_stage_one",
                    "test_dataelement", "test_value_current"
                )
            )
        )
        val valueMap = buildValueMap(
            ruleEvents = listOf(eventOne, eventTwo, eventThree),
            ruleEnrollment = null,
            ruleEvent = eventCurrent,
            ruleVariables = listOf(ruleVariable),
            constantsValues = emptyMap()
        )
        assertThat(valueMap.size).isEqualTo(12)
        assertThatVariable(valueMap["current_date"]).hasValue(
            currentDate().toString()
        )
            .isTypeOf(RuleValueType.TEXT).hasCandidates(currentDate().toString())
        assertThatVariable(valueMap["event_date"])
            .hasValue(dateEventCurrent.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(dateEventCurrent.toString())
        assertThatVariable(valueMap["event_count"]).hasValue("4")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("4")
        assertThatVariable(valueMap["event_id"]).hasValue("test_event_uid_current")
            .isTypeOf(RuleValueType.TEXT).hasCandidates("test_event_uid_current")
        assertThatVariable(valueMap["due_date"])
            .hasValue(dateEventDueCurrent.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(dateEventDueCurrent.toString())
        assertThatVariable(valueMap["test_variable"]).hasValue("test_value_one")
            .isTypeOf(RuleValueType.TEXT).hasCandidates("test_value_one", "test_value_current")
    }

    @Test
    @Throws(ParseException::class)
    fun newestEventProgramStageVariableShouldNotContainAnyValues() {
        val ruleVariable: RuleVariable = RuleVariable.RuleVariableNewestStageEvent(
            "test_variable",
            "test_dataelement", RuleValueType.TEXT, "test_program_stage_one"
        )
        val dateEventOne: LocalDate = "2014-03-03".toLocalDate()
        val dateEventTwo: LocalDate = "2015-02-03".toLocalDate()
        val ruleEventOne: RuleEvent = RuleEvent(
            "test_event_uid_one", "test_program_stage_two", "",
            RuleEvent.Status.ACTIVE, dateEventOne, dateEventOne, null, "", null, listOf(
                RuleDataValue(
                    dateEventOne, "test_program_stage_two",
                    "test_dataelement", "test_value_one"
                )
            )
        )
        val ruleEventTwo: RuleEvent = RuleEvent(
            "test_event_uid_two", "test_program_stage_two", "",
            RuleEvent.Status.ACTIVE, dateEventTwo, dateEventTwo, null, "", null, listOf(
                RuleDataValue(
                    dateEventTwo, "test_program_stage_two",
                    "test_dataelement", "test_value_two"
                )
            )
        )
        val valueMap = buildValueMap(
            ruleEvents = Arrays.asList(ruleEventOne),
            ruleEnrollment = null,
            ruleEvent = ruleEventTwo,
            ruleVariables = Arrays.asList(ruleVariable),
            constantsValues = emptyMap()
        )

        assertThat(valueMap.size).isEqualTo(12)
        assertThatVariable(valueMap["current_date"]).hasValue(
            currentDate().toString()
        )
            .isTypeOf(RuleValueType.TEXT).hasCandidates(currentDate().toString())
        assertThatVariable(valueMap["event_date"]).hasValue(dateEventTwo.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(dateEventTwo.toString())
        assertThatVariable(valueMap["event_count"]).hasValue("2")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("2")
        assertThatVariable(valueMap["event_id"]).hasValue("test_event_uid_two")
            .isTypeOf(RuleValueType.TEXT).hasCandidates("test_event_uid_two")
        assertThatVariable(valueMap["due_date"]).hasValue(dateEventTwo.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(dateEventTwo.toString())
        assertThatVariable(valueMap["test_variable"]).hasValue(null)
            .isTypeOf(RuleValueType.TEXT).hasCandidates()
    }

    @Test
    @Throws(ParseException::class)
    fun previousEventVariableShouldContainValuesFromPreviousEvent() {
        val ruleVariable: RuleVariable = RuleVariable.RuleVariablePreviousEvent(
            "test_variable",
            "test_dataelement", RuleValueType.TEXT
        )
        val dateEventOne: LocalDate = "2014-02-03".toLocalDate()
        val dateEventTwo: LocalDate = "2014-03-03".toLocalDate()
        val dateEventThree: LocalDate = "2015-02-03".toLocalDate()
        val dateEventCurrent: LocalDate = "2014-05-03".toLocalDate()
        val ruleEventOne: RuleEvent = RuleEvent(
            "test_event_uid_one", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, dateEventOne, dateEventOne, null, "", null, listOf(
                RuleDataValue(
                    dateEventOne, "test_program_stage_one",
                    "test_dataelement", "test_value_one"
                )
            )
        )
        val ruleEventTwo: RuleEvent = RuleEvent(
            "test_event_uid_two", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, dateEventTwo, dateEventTwo, null, "", null, listOf(
                RuleDataValue(
                    dateEventTwo, "test_program_stage_two",
                    "test_dataelement", "test_value_two"
                )
            )
        )
        val ruleEventThree: RuleEvent = RuleEvent(
            "test_event_uid_three", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, dateEventThree, dateEventThree, null, "", null, listOf(
                RuleDataValue(
                    dateEventThree, "test_program_stage_two",
                    "test_dataelement", "test_value_three"
                )
            )
        )
        val ruleEventCurrent: RuleEvent = RuleEvent(
            "test_event_uid_current", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, dateEventCurrent, dateEventCurrent, null, "", null, listOf(
                RuleDataValue(
                    dateEventCurrent, "test_program_stage_one",
                    "test_dataelement", "test_value_current"
                )
            )
        )
        val valueMap = buildValueMap(
            ruleEvents = Arrays.asList(ruleEventOne, ruleEventTwo, ruleEventThree),
            ruleEnrollment = null,
            ruleEvent = ruleEventCurrent,
            ruleVariables = Arrays.asList(ruleVariable),
            constantsValues = emptyMap()
        )
        assertThat(valueMap.size).isEqualTo(12)
        assertThatVariable(valueMap["current_date"]).hasValue(
            currentDate().toString()
        )
            .isTypeOf(RuleValueType.TEXT).hasCandidates(currentDate().toString())
        assertThatVariable(valueMap["event_date"])
            .hasValue(dateEventCurrent.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(dateEventCurrent.toString())
        assertThatVariable(valueMap["event_count"]).hasValue("4")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("4")
        assertThatVariable(valueMap["event_id"]).hasValue("test_event_uid_current")
            .isTypeOf(RuleValueType.TEXT).hasCandidates("test_event_uid_current")
        assertThatVariable(valueMap["due_date"])
            .hasValue(dateEventCurrent.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(dateEventCurrent.toString())
        assertThatVariable(valueMap["test_variable"])
            .hasValue("test_value_two")
            .isTypeOf(RuleValueType.TEXT)
            .hasCandidates(
                "test_value_three",
                "test_value_current",
                "test_value_two",
                "test_value_one"
            )
    }

    @Test
    @Throws(ParseException::class)
    fun attributeVariableShouldContainValuesFromContextEnrollment() {
        val ruleVariableOne: RuleVariable = RuleVariable.RuleVariableAttribute(
            "test_variable_one",
            "test_attribute_one", RuleValueType.TEXT
        )
        val ruleVariableTwo: RuleVariable = RuleVariable.RuleVariableAttribute(
            "test_variable_two",
            "test_attribute_two", RuleValueType.TEXT
        )
        val eventDate: LocalDate = "2015-01-01".toLocalDate()
        val enrollmentDate: LocalDate = "2014-03-01".toLocalDate()

        // values from ruleEnrollment should end up in ruleVariables
        val ruleEnrollment: RuleEnrollment = RuleEnrollment(
            "test_enrollment", "",
            enrollmentDate, enrollmentDate, RuleEnrollment.Status.ACTIVE, "", null, listOf(
                RuleAttributeValue("test_attribute_one", "test_attribute_value_one"),
                RuleAttributeValue("test_attribute_two", "test_attribute_value_two")
            )
        )

        // values from context ruleEvents should be ignored
        val contextEvent: RuleEvent = RuleEvent(
            "test_context_event_one", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, eventDate, currentDate(), null, "", null, listOf(
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
        val currentEvent: RuleEvent = RuleEvent(
            "test_event_uid", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, eventDate, eventDate, null, "", null, listOf(
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
        val valueMap = buildValueMap(
            ruleEvents = Arrays.asList(contextEvent),
            ruleEnrollment = ruleEnrollment,
            ruleEvent = currentEvent,
            ruleVariables = Arrays.asList(ruleVariableOne, ruleVariableTwo),
            constantsValues = emptyMap()
        )
        assertThat(valueMap.size).isEqualTo(20)
        assertThatVariable(valueMap["current_date"]).hasValue(
            currentDate().toString()
        )
            .isTypeOf(RuleValueType.TEXT).hasCandidates(currentDate().toString())
        assertThatVariable(valueMap["event_date"]).hasValue(eventDate.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(eventDate.toString())
        assertThatVariable(valueMap["event_count"]).hasValue("2")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("2")
        assertThatVariable(valueMap["event_id"]).hasValue("test_event_uid")
            .isTypeOf(RuleValueType.TEXT).hasCandidates("test_event_uid")
        assertThatVariable(valueMap["due_date"]).hasValue(eventDate.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(eventDate.toString())
        assertThatVariable(valueMap["enrollment_status"])
            .hasValue(RuleEnrollment.Status.ACTIVE.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(RuleEnrollment.Status.ACTIVE.toString())
        assertThatVariable(valueMap["enrollment_date"])
            .hasValue(enrollmentDate.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(enrollmentDate.toString())
        assertThatVariable(valueMap["enrollment_id"]).hasValue("test_enrollment")
            .isTypeOf(RuleValueType.TEXT).hasCandidates("test_enrollment")
        assertThatVariable(valueMap["enrollment_count"]).hasValue("1")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("1")
        assertThatVariable(valueMap["incident_date"])
            .hasValue(enrollmentDate.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(enrollmentDate.toString())
        assertThatVariable(valueMap["tei_count"]).hasValue("1")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("1")
        assertThatVariable(valueMap["test_variable_one"]).hasValue("test_attribute_value_one")
            .isTypeOf(RuleValueType.TEXT).hasCandidates("test_attribute_value_one")
        assertThatVariable(valueMap["test_variable_two"]).hasValue("test_attribute_value_two")
            .isTypeOf(RuleValueType.TEXT).hasCandidates("test_attribute_value_two")
    }

    @Test
    @Throws(ParseException::class)
    fun ruleEnrollmentValuesShouldBePropagatedToMapCorrectly() {
        val ruleVariableOne: RuleVariable = RuleVariable.RuleVariableAttribute(
            "test_variable_one",
            "test_attribute_one", RuleValueType.NUMERIC
        )
        val ruleVariableTwo: RuleVariable = RuleVariable.RuleVariableAttribute(
            "test_variable_two",
            "test_attribute_two", RuleValueType.TEXT
        )
        val ruleVariableThree: RuleVariable = RuleVariable.RuleVariableCurrentEvent(
            "test_variable_three",
            "test_dataelement_one", RuleValueType.BOOLEAN
        )
        val currentDate: LocalDate = currentDate()
        val enrollmentDate: LocalDate = "2017-02-02".toLocalDate()
        val incidentDate: LocalDate = "2017-04-02".toLocalDate()
        val ruleEnrollment: RuleEnrollment = RuleEnrollment(
            "test_enrollment", "", incidentDate,
            enrollmentDate, RuleEnrollment.Status.ACTIVE, "", null, listOf(
                RuleAttributeValue("test_attribute_one", "test_attribute_value_one"),
                RuleAttributeValue("test_attribute_two", "test_attribute_value_two"),
                RuleAttributeValue("test_attribute_three", "test_attribute_value_three")
            )
        )
        val ruleEventOne: RuleEvent = RuleEvent(
            "test_event_one", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), currentDate(), "", null, ArrayList<RuleDataValue>()
        )
        val ruleEventTwo: RuleEvent = RuleEvent(
            "test_event_two", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), currentDate(), "", null, ArrayList<RuleDataValue>()
        )
        val valueMap: Map<String, RuleVariableValue> = buildValueMap(
            ruleEvents = Arrays.asList(ruleEventOne, ruleEventTwo),
            ruleEnrollment = ruleEnrollment,
            ruleEvent = null,
            ruleVariables = Arrays.asList(ruleVariableOne, ruleVariableTwo, ruleVariableThree),
            constantsValues = emptyMap()
        )

        assertThat(valueMap.size).isEqualTo(14)
        assertThatVariable(valueMap["current_date"]).hasValue(currentDate.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(currentDate.toString())
        assertThatVariable(valueMap["event_count"]).hasValue("2")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("2")
        assertThatVariable(valueMap["enrollment_date"])
            .hasValue(enrollmentDate.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(enrollmentDate.toString())
        assertThatVariable(valueMap["enrollment_id"]).hasValue("test_enrollment")
            .isTypeOf(RuleValueType.TEXT).hasCandidates("test_enrollment")
        assertThatVariable(valueMap["enrollment_count"]).hasValue("1")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("1")
        assertThatVariable(valueMap["incident_date"])
            .hasValue(incidentDate.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(incidentDate.toString())
        assertThatVariable(valueMap["tei_count"]).hasValue("1")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("1")
        assertThatVariable(valueMap["test_variable_one"]).hasValue("test_attribute_value_one")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("test_attribute_value_one")
        assertThatVariable(valueMap["test_variable_two"]).isTypeOf(RuleValueType.TEXT)
            .hasValue("test_attribute_value_two").hasCandidates("test_attribute_value_two")
    }

    @Test
    @Throws(ParseException::class)
    fun MultipleMapBuilderShoulCreateCorrectMapForEnrollmentAndEvents() {
        val ruleVariableOne: RuleVariable = RuleVariable.RuleVariableAttribute(
            "test_variable_one",
            "test_attribute_one", RuleValueType.NUMERIC
        )
        val ruleVariableTwo: RuleVariable = RuleVariable.RuleVariableAttribute(
            "test_variable_two",
            "test_attribute_two", RuleValueType.TEXT
        )
        val ruleVariableThree: RuleVariable = RuleVariable.RuleVariableCurrentEvent(
            "test_variable_three",
            "test_dataelement_one", RuleValueType.BOOLEAN
        )
        val now = currentDate()
        val currentDate: LocalDate = now
        val enrollmentDate: LocalDate = "2017-02-02".toLocalDate()
        val incidentDate: LocalDate = "2017-04-02".toLocalDate()
        val ruleEnrollment: RuleEnrollment = RuleEnrollment(
            "test_enrollment", "", incidentDate,
            enrollmentDate, RuleEnrollment.Status.ACTIVE, "", null, listOf(
                RuleAttributeValue("test_attribute_one", "test_attribute_value_one"),
                RuleAttributeValue("test_attribute_two", "test_attribute_value_two"),
                RuleAttributeValue("test_attribute_three", "test_attribute_value_three")
            )
        )

        val eventOneDate = now.add(-1)
        val eventOneDueDate = now.add(-2)
        val eventTwoDate = now.add(-3)
        val eventTwoDueDate = now.add(-4)
        val ruleEventOne: RuleEvent = RuleEvent(
            "test_event_one", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, eventOneDate, eventOneDueDate, null, "", null, ArrayList<RuleDataValue>()
        )
        val ruleEventTwo: RuleEvent = RuleEvent(
            "test_event_two", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, eventTwoDate, eventTwoDueDate, null, "", null, ArrayList<RuleDataValue>()
        )

        val ruleEvents = Arrays.asList(ruleEventOne, ruleEventTwo)
        val ruleVariables = Arrays.asList(ruleVariableOne, ruleVariableTwo, ruleVariableThree)
        val multipleValueMap = RuleVariableValueMap(
            enrollmentMap = mapOf(
                Pair(
                    ruleEnrollment,
                    buildValueMap(
                        ruleEvents = ruleEvents,
                        ruleEnrollment = ruleEnrollment,
                        ruleEvent = null,
                        ruleVariables = ruleVariables,
                        constantsValues = emptyMap()
                    )
                )
            ),
            eventMap = ruleEvents.associateWith { ruleEvent ->
                buildValueMap(
                    ruleEvents = ruleEvents,
                    ruleEnrollment = ruleEnrollment,
                    ruleEvent = ruleEvent,
                    ruleVariables = ruleVariables,
                    constantsValues = emptyMap()
                )
            }
        )

        assertThat(multipleValueMap.enrollmentMap.size).isEqualTo(1)
        assertThat(multipleValueMap.eventMap.size).isEqualTo(2)
        val enrollmentValueMap = multipleValueMap.enrollmentMap[ruleEnrollment]!!
        val eventOneValueMap = multipleValueMap.eventMap[ruleEventOne]!!
        val eventTwoValueMap = multipleValueMap.eventMap[ruleEventTwo]!!

        // Enrollment
        assertThatVariable(enrollmentValueMap["current_date"]).hasValue(currentDate.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(currentDate.toString())
        assertThatVariable(enrollmentValueMap["event_count"]).hasValue("2")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("2")
        assertThatVariable(enrollmentValueMap["enrollment_date"])
            .hasValue(enrollmentDate.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(enrollmentDate.toString())
        assertThatVariable(enrollmentValueMap["enrollment_id"]).hasValue("test_enrollment")
            .isTypeOf(RuleValueType.TEXT).hasCandidates("test_enrollment")
        assertThatVariable(enrollmentValueMap["enrollment_count"]).hasValue("1")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("1")
        assertThatVariable(enrollmentValueMap["incident_date"])
            .hasValue(incidentDate.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(incidentDate.toString())
        assertThatVariable(enrollmentValueMap["tei_count"]).hasValue("1")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("1")
        assertThatVariable(enrollmentValueMap["test_variable_one"]).hasValue("test_attribute_value_one")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("test_attribute_value_one")
        assertThatVariable(enrollmentValueMap["test_variable_two"]).isTypeOf(RuleValueType.TEXT)
            .hasValue("test_attribute_value_two").hasCandidates("test_attribute_value_two")

        // Event one
        assertThatVariable(eventOneValueMap["current_date"]).hasValue(currentDate.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(currentDate.toString())
        assertThatVariable(eventOneValueMap["event_count"]).hasValue("2")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("2")
        assertThatVariable(eventOneValueMap["enrollment_date"])
            .hasValue(enrollmentDate.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(enrollmentDate.toString())
        assertThatVariable(eventOneValueMap["enrollment_id"]).hasValue("test_enrollment")
            .isTypeOf(RuleValueType.TEXT).hasCandidates("test_enrollment")
        assertThatVariable(eventOneValueMap["enrollment_count"]).hasValue("1")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("1")
        assertThatVariable(eventOneValueMap["incident_date"])
            .hasValue(incidentDate.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(incidentDate.toString())
        assertThatVariable(eventOneValueMap["tei_count"]).hasValue("1")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("1")
        assertThatVariable(eventOneValueMap["test_variable_one"]).hasValue("test_attribute_value_one")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("test_attribute_value_one")
        assertThatVariable(eventOneValueMap["test_variable_two"]).isTypeOf(RuleValueType.TEXT)
            .hasValue("test_attribute_value_two").hasCandidates("test_attribute_value_two")
        assertThatVariable(eventOneValueMap["event_date"])
            .hasValue(eventOneDate.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(eventOneDate.toString())
        assertThatVariable(eventOneValueMap["due_date"])
            .hasValue(eventOneDueDate.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(eventOneDueDate.toString())

        // Event two
        assertThatVariable(eventTwoValueMap["current_date"]).hasValue(currentDate.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(currentDate.toString())
        assertThatVariable(eventTwoValueMap["event_count"]).hasValue("2")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("2")
        assertThatVariable(eventTwoValueMap["enrollment_date"])
            .hasValue(enrollmentDate.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(enrollmentDate.toString())
        assertThatVariable(eventTwoValueMap["enrollment_id"]).hasValue("test_enrollment")
            .isTypeOf(RuleValueType.TEXT).hasCandidates("test_enrollment")
        assertThatVariable(eventTwoValueMap["enrollment_count"]).hasValue("1")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("1")
        assertThatVariable(eventTwoValueMap["incident_date"])
            .hasValue(incidentDate.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(incidentDate.toString())
        assertThatVariable(eventTwoValueMap["tei_count"]).hasValue("1")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("1")
        assertThatVariable(eventTwoValueMap["test_variable_one"]).hasValue("test_attribute_value_one")
            .isTypeOf(RuleValueType.NUMERIC).hasCandidates("test_attribute_value_one")
        assertThatVariable(eventTwoValueMap["test_variable_two"]).isTypeOf(RuleValueType.TEXT)
            .hasValue("test_attribute_value_two").hasCandidates("test_attribute_value_two")
        assertThatVariable(eventTwoValueMap["event_date"])
            .hasValue(eventTwoDate.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(eventTwoDate.toString())
        assertThatVariable(eventTwoValueMap["due_date"])
            .hasValue(eventTwoDueDate.toString())
            .isTypeOf(RuleValueType.TEXT).hasCandidates(eventTwoDueDate.toString())
    }

    /*@Test(expected = IllegalStateException::class)
    fun buildShouldThrowOnDuplicateEvent() {
        val ruleEvent: RuleEvent = RuleEvent(
            "test_event_two", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), currentDate(), "", null, listOf()
        )
        RuleVariableValueMapBuilder.target(ruleEvent)
            .ruleVariables(ArrayList<RuleVariable>())
            .ruleEvents(Arrays.asList(ruleEvent))
            .build()
    }
*/
    /*    companion object {
            private const val DATE_PATTERN = "yyyy-MM-dd"
            private fun wrap(source: String): String {
                return String.format(Locale.US, "%s", source)
            }
        }*/
}