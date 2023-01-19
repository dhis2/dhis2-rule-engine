package org.dhis2.ruleengine.models

import kotlinx.datetime.LocalDate


data class RuleEnrollment(
    val enrollment: String,
    val programName: String,
    val incidentDate: LocalDate,
    val enrollmentDate: LocalDate,
    val status: Status,
    val organisationUnit: String,
    val organisationUnitCode: String?,
    val attributeValues: List<RuleAttributeValue>
) {
    enum class Status {
        ACTIVE, COMPLETED, CANCELLED
    }
}