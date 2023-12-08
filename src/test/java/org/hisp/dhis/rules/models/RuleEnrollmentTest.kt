package org.hisp.dhis.rules.models

import io.mockk.mockk
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class RuleEnrollmentTest {
    @Test
    fun createShouldPropagatePropertiesCorrectly() {
        val ruleAttributeValueOne = mockk<RuleAttributeValue>()
        val ruleAttributeValueTwo = mockk<RuleAttributeValue>()
        val ruleAttributeValueThree = mockk<RuleAttributeValue>()
        val incidentDate = Date()
        val enrollmentDate = Date()
        val ruleEnrollment = RuleEnrollment(
            "test_enrollment", "",
            incidentDate, enrollmentDate, RuleEnrollment.Status.ACTIVE, "", "",
            listOf(ruleAttributeValueOne, ruleAttributeValueTwo, ruleAttributeValueThree)
        )
        assertEquals("test_enrollment", ruleEnrollment.enrollment)
        assertEquals(incidentDate, ruleEnrollment.incidentDate)
        assertEquals(enrollmentDate, ruleEnrollment.enrollmentDate)
        assertEquals(RuleEnrollment.Status.ACTIVE, ruleEnrollment.status)
        assertEquals(3, ruleEnrollment.attributeValues.size)
        assertEquals(ruleAttributeValueOne, ruleEnrollment.attributeValues[0])
        assertEquals(ruleAttributeValueTwo, ruleEnrollment.attributeValues[1])
        assertEquals(ruleAttributeValueThree, ruleEnrollment.attributeValues[2])
    }
}
