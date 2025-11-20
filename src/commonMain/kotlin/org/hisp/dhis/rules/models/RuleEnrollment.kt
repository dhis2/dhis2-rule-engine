package org.hisp.dhis.rules.models

data class RuleEnrollment(
    val enrollment: String,
    val programName: String,
    val incidentDate: RuleLocalDate,
    val enrollmentDate: RuleLocalDate,
    val status: RuleEnrollmentStatus,
    val organisationUnit: String,
    val organisationUnitCode: String?,
    val attributeValues: List<RuleAttributeValue>,
)
