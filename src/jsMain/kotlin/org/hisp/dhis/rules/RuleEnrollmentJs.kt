package org.hisp.dhis.rules

import org.hisp.dhis.rules.models.RuleEnrollmentStatus
import org.hisp.dhis.rules.models.RuleAttributeValue

@JsExport
data class RuleEnrollmentJs(
    val enrollment: String,
    val programName: String,
    val incidentDate: kotlinx.datetime.internal.JSJoda.LocalDate,
    val enrollmentDate: kotlinx.datetime.internal.JSJoda.LocalDate,
    val status: RuleEnrollmentStatus,
    val organisationUnit: String,
    val organisationUnitCode: String,
    val attributeValues: Array<RuleAttributeValue>
)
