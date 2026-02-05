package org.hisp.dhis.rules.models

data class RuleEvent(
    val event: String,
    val programStage: String,
    val programStageName: String,
    val status: RuleEventStatus,
    val eventDate: RuleLocalDate,
    val createdDate: RuleInstant,
    val createdAtClientDate: RuleInstant?,
    val dueDate: RuleLocalDate?,
    val completedDate: RuleLocalDate?,
    val organisationUnit: String,
    val organisationUnitCode: String?,
    val dataValues: List<RuleDataValue>,
): Comparable<RuleEvent> {
    val resolvedCreatedDate get() = (createdAtClientDate ?: createdDate).instant

    override fun compareTo(other: RuleEvent): Int {
        val dateComparison = this.eventDate.compareTo(other.eventDate)
        if (dateComparison != 0) return dateComparison
        return this.resolvedCreatedDate.compareTo(other.resolvedCreatedDate)
    }
}
