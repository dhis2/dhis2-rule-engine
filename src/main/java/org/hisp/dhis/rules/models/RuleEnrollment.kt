package org.hisp.dhis.rules.models

import java.util.*

data class RuleEnrollment(
    val enrollment: String,
    val programName: String,
    val incidentDate: Date,
    val enrollmentDate: Date,
    val status: Status,
    val organisationUnit: String,
    val organisationUnitCode: String,
    val attributeValues: List<RuleAttributeValue>
) {
    enum class Status {
        ACTIVE,
        COMPLETED,
        CANCELLED
    }
}
