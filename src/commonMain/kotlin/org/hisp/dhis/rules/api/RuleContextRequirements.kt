package org.hisp.dhis.rules.api

data class RuleContextRequirements(
    val needsAllEvents: Boolean,
) {
    companion object {
        val NONE = RuleContextRequirements(needsAllEvents = false)
    }
}
