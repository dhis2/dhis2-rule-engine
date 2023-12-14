package org.hisp.dhis.rules.models

import kotlinx.datetime.LocalDate

data class RuleEnrollment(
    val enrollment: String,
    val programName: String,
    val incidentDate: LocalDate,
    val enrollmentDate: LocalDate,
    val status: org.hisp.dhis.rules.models.RuleEnrollment.Status,
    val organisationUnit: String,
    val organisationUnitCode: String,
    val attributeValues: List<org.hisp.dhis.rules.models.RuleAttributeValue>
) {
    enum class Status {
        ACTIVE,
        COMPLETED,
        CANCELLED
    }
}
