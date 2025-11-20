package org.hisp.dhis.rules.models

data class RuleEvent(
    val event: String,
    val programStage: String,
    val programStageName: String,
    val status: RuleEventStatus,
    val eventDate: RuleInstant,
    val createdDate: RuleInstant,
    val dueDate: RuleLocalDate?,
    val completedDate: RuleLocalDate?,
    val organisationUnit: String,
    val organisationUnitCode: String?,
    val dataValues: List<RuleDataValue>,
)
