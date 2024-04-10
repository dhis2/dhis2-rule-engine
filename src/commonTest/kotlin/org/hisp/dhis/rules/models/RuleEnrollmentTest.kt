package org.hisp.dhis.rules.models

import kotlinx.datetime.LocalDate
import org.hisp.dhis.rules.utils.currentDate
import kotlin.test.Test
import kotlin.test.assertEquals

class RuleEnrollmentTest {
    @Test
    fun createShouldPropagatePropertiesCorrectly() {
        val ruleAttributeValueOne = RuleAttributeValue("attr1", "value1")
        val ruleAttributeValueTwo = RuleAttributeValue("attr2", "value2")
        val ruleAttributeValueThree = RuleAttributeValue("attr3", "value3")
        val incidentDate = LocalDate.Companion.currentDate()
        val enrollmentDate = LocalDate.Companion.currentDate()
        val ruleEnrollment = RuleEnrollment(
            "test_enrollment", "",
            incidentDate, enrollmentDate, EnrollmentStatus.ACTIVE, "", "",
            listOf(ruleAttributeValueOne, ruleAttributeValueTwo, ruleAttributeValueThree)
        )
        assertEquals("test_enrollment", ruleEnrollment.enrollment)
        assertEquals(incidentDate, ruleEnrollment.incidentDate)
        assertEquals(enrollmentDate, ruleEnrollment.enrollmentDate)
        assertEquals(EnrollmentStatus.ACTIVE, ruleEnrollment.status)
        assertEquals(3, ruleEnrollment.attributeValues.size)
        assertEquals(ruleAttributeValueOne, ruleEnrollment.attributeValues[0])
        assertEquals(ruleAttributeValueTwo, ruleEnrollment.attributeValues[1])
        assertEquals(ruleAttributeValueThree, ruleEnrollment.attributeValues[2])
    }
}