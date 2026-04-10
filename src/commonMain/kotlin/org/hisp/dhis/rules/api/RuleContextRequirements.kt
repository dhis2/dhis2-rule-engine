package org.hisp.dhis.rules.api

data class RuleContextRequirements(
    val needsAllEvents: Boolean,
    val needsEnrollment: Boolean,
    val needsDataValues: Boolean,
    val needsAttributes: Boolean,
    val orgUnitGroups: Set<String>,
) {
    companion object {
        internal val NONE = RuleContextRequirements(
            needsAllEvents = false,
            needsEnrollment = false,
            needsDataValues = false,
            needsAttributes = false,
            orgUnitGroups = emptySet()
        )
    }
}
