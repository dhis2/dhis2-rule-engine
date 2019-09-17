package org.hisp.dhis.rules.models

import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*
import kotlin.test.assertFailsWith

@RunWith(JUnit4::class)
class RuleDataValueTests {

    @Test
    fun createShouldThrowOnNullDate() {
        assertFailsWith<NullPointerException> {
            RuleDataValue.create(null!!, "test_program_stage_uid", "test_field", "test_value")
        }
    }

    @Test
    fun createShouldThrowOnNullEvent() {
        assertFailsWith<NullPointerException> {
            RuleDataValue.create(Date(), null!!, "test_field", "test_value")
        }
    }

    @Test
    fun createShouldThrowOnNullDataElement() {
        assertFailsWith<NullPointerException> {
            RuleDataValue.create(Date(), "test_program_stage_uid", null!!, "test_value")
        }
    }

    @Test
    fun createShouldThrowOnNullValue() {
        assertFailsWith<NullPointerException> {
            RuleDataValue.create(Date(), "test_program_stage_uid", "test_dataelement", null!!)
        }
    }

    @Test
    fun createShouldPropagateValuesCorrectly() {
        val eventDate = Date()
        val ruleDataValue = RuleDataValue.create(eventDate,
                "test_program_stage_uid",
                "test_dataelement",
                "test_value")

        assertThat(ruleDataValue.eventDate).isEqualTo(eventDate)
        assertThat(ruleDataValue.programStage).isEqualTo("test_program_stage_uid")
        assertThat(ruleDataValue.dataElement).isEqualTo("test_dataelement")
        assertThat(ruleDataValue.value).isEqualTo("test_value")
    }
}
