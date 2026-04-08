package org.hisp.dhis.rules.api

data class RuleContextRequirements(
    val needsAllEvents: Boolean = false,
    val needsEnrollment: Boolean = false,
    val needsDataValues: Boolean = false,
    val needsAttributes: Boolean = false,
    val orgUnitGroups: Set<String> = emptySet(),
) {
    companion object {
        val NONE = RuleContextRequirements()
    }
}
