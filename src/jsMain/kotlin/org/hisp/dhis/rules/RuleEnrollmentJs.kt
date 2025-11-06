package org.hisp.dhis.rules

import kotlinx.datetime.LocalDate
import org.hisp.dhis.rules.models.RuleAttributeValue
import org.hisp.dhis.rules.models.RuleEnrollmentStatus

@JsExport
data class RuleEnrollmentJs(
    val enrollment: String,
    val programName: String,
    val incidentDate: LocalDate,
    val enrollmentDate: LocalDate,
    val status: RuleEnrollmentStatus,
    val organisationUnit: String,
    val organisationUnitCode: String?,
    val attributeValues: Array<RuleAttributeValue>
)
