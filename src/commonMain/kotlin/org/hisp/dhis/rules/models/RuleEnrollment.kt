package org.hisp.dhis.rules.models

import kotlinx.datetime.LocalDate

data class RuleEnrollment(
    val enrollment: String,
    val programName: String,
    val incidentDate: LocalDate,
    val enrollmentDate: LocalDate,
    val status: RuleEnrollmentStatus,
    val organisationUnit: String,
    val organisationUnitCode: String?,
    val attributeValues: List<RuleAttributeValue>,
)
