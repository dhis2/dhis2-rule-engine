package org.hisp.dhis.rules

import org.hisp.dhis.rules.models.RuleDataValue
import org.hisp.dhis.rules.models.RuleEventStatus
import org.hisp.dhis.rules.models.RuleInstant
import org.hisp.dhis.rules.models.RuleLocalDate


@JsExport
data class RuleEventJs(
    val event: String,
    val programStage: String,
    val programStageName: String,
    val status: RuleEventStatus,
    val eventDate: RuleInstant?,
    val createdDate: RuleInstant,
    val dueDate: RuleLocalDate?,
    val completedDate: RuleLocalDate?,
    val organisationUnit: String,
    val organisationUnitCode: String?,
    val dataValues: Array<RuleDataValue>
)
