package org.hisp.dhis.rules.models

import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock
import java.util.*
import kotlin.test.assertFailsWith

@RunWith(JUnit4::class)
class RuleEnrollmentTests {

    @Test
    fun createShouldThrowOnNullEnrollment() {
        assertFailsWith<NullPointerException> {
            RuleEnrollment.create(null!!, Date(), Date(),
                    RuleEnrollment.Status.ACTIVE, null!!, null, ArrayList<RuleAttributeValue>(), "")
        }
    }

    @Test
    fun createShouldThrowOnNullIncidentDate() {
        assertFailsWith<NullPointerException> {
            RuleEnrollment.create("test_enrollment", null!!, Date(),
                    RuleEnrollment.Status.ACTIVE, null!!, null, ArrayList<RuleAttributeValue>(), "")
        }
    }

    @Test
    fun createShouldThrowOnNullEnrollmentDate() {
        assertFailsWith<NullPointerException> {
            RuleEnrollment.create("test_enrollment", Date(), null!!,
                    RuleEnrollment.Status.ACTIVE, null!!, null, ArrayList<RuleAttributeValue>(), "")
        }
    }

    @Test
    fun createShouldThrowOnNullStatus() {
        assertFailsWith<NullPointerException> {
            RuleEnrollment.create("test_enrollment", Date(), Date(),
                    null!!, null!!, null, ArrayList<RuleAttributeValue>(), "")
        }
    }

    @Test
    fun createShouldThrowOnNullValueList() {
        assertFailsWith<NullPointerException> {
            RuleEnrollment.create("test_enrollment", Date(), Date(),
                    RuleEnrollment.Status.ACTIVE, null!!, null, null!!, "")
        }
    }

    @Test
    fun createShouldPropagatePropertiesCorrectly() {
        val ruleAttributeValueOne = mock(RuleAttributeValue::class.java)
        val ruleAttributeValueTwo = mock(RuleAttributeValue::class.java)
        val ruleAttributeValueThree = mock(RuleAttributeValue::class.java)

        val incidentDate = Date()
        val enrollmentDate = Date()

        val ruleEnrollment = RuleEnrollment.create(
                "test_enrollment",
                incidentDate,
                enrollmentDate,
                RuleEnrollment.Status.ACTIVE,
                "",
                "",
                listOf(ruleAttributeValueOne, ruleAttributeValueTwo, ruleAttributeValueThree),
                "")

        assertThat(ruleEnrollment.enrollment).isEqualTo("test_enrollment")
        assertThat(ruleEnrollment.incidentDate).isEqualTo(incidentDate)
        assertThat(ruleEnrollment.enrollmentDate).isEqualTo(enrollmentDate)
        assertThat(ruleEnrollment.status).isEqualTo(RuleEnrollment.Status.ACTIVE)
        assertThat(ruleEnrollment.attributeValues?.size).isEqualTo(3)
        assertThat(ruleEnrollment.attributeValues?.get(0)).isEqualTo(ruleAttributeValueOne)
        assertThat(ruleEnrollment.attributeValues?.get(1)).isEqualTo(ruleAttributeValueTwo)
        assertThat(ruleEnrollment.attributeValues?.get(2)).isEqualTo(ruleAttributeValueThree)
    }

    @Test
    fun createShouldReturnImmutableList() {
        val ruleAttributeValueOne = mock(RuleAttributeValue::class.java)
        val ruleAttributeValueTwo = mock(RuleAttributeValue::class.java)
        val ruleAttributeValueThree = mock(RuleAttributeValue::class.java)

        val attributeValues = mutableListOf<RuleAttributeValue>()
        attributeValues.add(ruleAttributeValueOne)
        attributeValues.add(ruleAttributeValueTwo)

        val ruleEnrollment = RuleEnrollment.create("test_enrollment",
                Date(), Date(), RuleEnrollment.Status.ACTIVE, "", null, attributeValues, "")

        // mutating source array
        ruleEnrollment.attributeValues?.add(ruleAttributeValueThree)

        assertThat(ruleEnrollment.attributeValues?.size).isEqualTo(2)
        assertThat(ruleEnrollment.attributeValues?.get(0)).isEqualTo(ruleAttributeValueOne)
        assertThat(ruleEnrollment.attributeValues?.get(1)).isEqualTo(ruleAttributeValueTwo)
        assertThat(ruleAttributeValueThree).isNotIn(ruleEnrollment.attributeValues)
    }
}
