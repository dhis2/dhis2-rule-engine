package org.hisp.dhis.rules.models

import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.test.assertFailsWith

@RunWith(JUnit4::class)
class RuleEventTests {

    @Test
    fun createShouldThrowExceptionIfEventIsNull() {
        assertFailsWith<NullPointerException> {
            RuleEvent.create(null!!, "test_programstage", RuleEvent.Status.ACTIVE,
                    Date(), Date(), null!!, null, Arrays.asList<RuleDataValue>(), "")
        }
    }

    @Test
    fun createShouldThrowExceptionIfProgramStageIsNull() {
        assertFailsWith<NullPointerException> {
            RuleEvent.create("test_event", null!!, RuleEvent.Status.ACTIVE,
                    Date(), Date(), null!!, null, Arrays.asList<RuleDataValue>(), "")
        }
    }

    @Test
    fun createShouldThrowExceptionIfStatusIsNull() {
        assertFailsWith<NullPointerException> {
            RuleEvent.create("test_event", "test_programstage", null!!,
                    Date(), Date(), null!!, null, Arrays.asList<RuleDataValue>(), "")
        }
    }

    @Test
    fun createShouldThrowExceptionIfEventDateIsNull() {
        assertFailsWith<NullPointerException> {
            RuleEvent.create("test_event", "test_programstage", RuleEvent.Status.ACTIVE,
                    null!!, Date(), null!!, null, Arrays.asList<RuleDataValue>(), "")
        }
    }

    @Test
    fun createShouldThrowExceptionIfDueDateIsNull() {
        assertFailsWith<NullPointerException> {
            RuleEvent.create("test_event", "test_programstage", RuleEvent.Status.ACTIVE,
                    Date(), null!!, null!!, null, Arrays.asList<RuleDataValue>(), "")
        }
    }

    @Test
    fun createShouldThrowExceptionIfListOfDataValuesIsNull() {
        assertFailsWith<NullPointerException> {
            RuleEvent.create("test_event", "test_programstage", RuleEvent.Status.ACTIVE, Date(), Date(),
                    null!!, null, null!!, "")
        }
    }

    @Test
    fun createShouldPropagateImmutableList() {
        val ruleDataValue = mock(RuleDataValue::class.java)
        val ruleDataValueTwo = mock(RuleDataValue::class.java)

        val ruleDataValues = listOf(ruleDataValue)

        val ruleEvent = RuleEvent.create("test_event_uid", "test_stage_uid",
                RuleEvent.Status.ACTIVE, Date(), Date(), "", "", ruleDataValues, "")

        // add another data value
        ruleEvent.dataValues?.add(ruleDataValueTwo)

        assertThat(ruleEvent.dataValues?.size).isEqualTo(1)
        assertThat(ruleEvent.dataValues?.get(0)).isEqualTo(ruleDataValue)
        assertThat(ruleDataValueTwo).isNotIn(ruleEvent.dataValues)

    }

    @Test
    fun createShouldPropagateValuesCorrectly() {
        val ruleDataValue = mock(RuleDataValue::class.java)

        val ruleDataValues = listOf(ruleDataValue)

        val eventDate = Date()
        val dueDate = Date()

        val ruleEvent = RuleEvent.create("test_event_uid", "test_stage_uid",
                RuleEvent.Status.ACTIVE, eventDate, dueDate, "", "", ruleDataValues, "")

        assertThat(ruleEvent.event).isEqualTo("test_event_uid")
        assertThat(ruleEvent.status).isEqualTo(RuleEvent.Status.ACTIVE)
        assertThat(ruleEvent.programStage).isEqualTo("test_stage_uid")
        assertThat(ruleEvent.eventDate).isEqualTo(eventDate)
        assertThat(ruleEvent.dueDate).isEqualTo(dueDate)

        assertThat(ruleEvent.dataValues?.size).isEqualTo(1)
        assertThat(ruleEvent.dataValues?.get(0)).isEqualTo(ruleDataValue)
    }

    @Test
    @Throws(ParseException::class)
    fun eventDateComparatorTest() {
        val dateFormat = SimpleDateFormat(DATE_PATTERN, Locale.US)
        val ruleEvents = listOf(
                RuleEvent.create("test_event_one", "test_program_stage_one", RuleEvent.Status.ACTIVE,
                        dateFormat.parse("2014-02-11"), dateFormat.parse("2014-02-11"), "", null,
                        ArrayList(), ""),
                RuleEvent.create("test_event_two", "test_program_stage_two", RuleEvent.Status.ACTIVE,
                        dateFormat.parse("2017-03-22"), dateFormat.parse("2017-03-22"), "", null,
                        ArrayList(), ""))

        Collections.sort(ruleEvents, RuleEvent.EVENT_DATE_COMPARATOR)

        assertThat(ruleEvents[0].event).isEqualTo("test_event_two")
        assertThat(ruleEvents[1].event).isEqualTo("test_event_one")
    }

    companion object {
        private const val DATE_PATTERN = "yyyy-MM-dd"
    }
}
