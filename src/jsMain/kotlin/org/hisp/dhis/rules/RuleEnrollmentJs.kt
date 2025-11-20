package org.hisp.dhis.rules

import org.hisp.dhis.rules.models.RuleAttributeValue
import org.hisp.dhis.rules.models.RuleEnrollmentStatus
import org.hisp.dhis.rules.models.RuleLocalDate

@JsExport
data class RuleEnrollmentJs(
    val enrollment: String,
    val programName: String,
    val incidentDate: RuleLocalDate,
    val enrollmentDate: RuleLocalDate,
    val status: RuleEnrollmentStatus,
    val organisationUnit: String,
    val organisationUnitCode: String?,
    val attributeValues: Array<RuleAttributeValue>
)
