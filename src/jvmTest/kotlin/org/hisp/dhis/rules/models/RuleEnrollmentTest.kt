package org.hisp.dhis.rules.models

import io.mockk.mockk
import kotlinx.datetime.LocalDate
import org.hisp.dhis.rules.currentDate
import kotlin.test.Test
import kotlin.test.assertEquals

class RuleEnrollmentTest {
    @Test
    fun createShouldPropagatePropertiesCorrectly() {
        val ruleAttributeValueOne = mockk<org.hisp.dhis.rules.models.RuleAttributeValue>()
        val ruleAttributeValueTwo = mockk<org.hisp.dhis.rules.models.RuleAttributeValue>()
        val ruleAttributeValueThree = mockk<org.hisp.dhis.rules.models.RuleAttributeValue>()
        val incidentDate = LocalDate.Companion.currentDate()
        val enrollmentDate = LocalDate.Companion.currentDate()
        val ruleEnrollment = org.hisp.dhis.rules.models.RuleEnrollment(
            "test_enrollment", "",
            incidentDate, enrollmentDate, org.hisp.dhis.rules.models.RuleEnrollment.Status.ACTIVE, "", "",
            listOf(ruleAttributeValueOne, ruleAttributeValueTwo, ruleAttributeValueThree)
        )
        assertEquals("test_enrollment", ruleEnrollment.enrollment)
        assertEquals(incidentDate, ruleEnrollment.incidentDate)
        assertEquals(enrollmentDate, ruleEnrollment.enrollmentDate)
        assertEquals(org.hisp.dhis.rules.models.RuleEnrollment.Status.ACTIVE, ruleEnrollment.status)
        assertEquals(3, ruleEnrollment.attributeValues.size)
        assertEquals(ruleAttributeValueOne, ruleEnrollment.attributeValues[0])
        assertEquals(ruleAttributeValueTwo, ruleEnrollment.attributeValues[1])
        assertEquals(ruleAttributeValueThree, ruleEnrollment.attributeValues[2])
    }
}
