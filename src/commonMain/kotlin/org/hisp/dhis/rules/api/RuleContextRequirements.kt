package org.hisp.dhis.rules.api

import kotlin.js.JsExport

@JsExport
data class RuleContextRequirements(
    val needsAllEvents: Boolean,
    val needsEnrollment: Boolean,
    val needsDataValues: Boolean,
    val needsAttributes: Boolean,
    val needsOrgUnitGroups: Boolean,
) {
    companion object {
        internal val NONE = RuleContextRequirements(
            needsAllEvents = false,
            needsEnrollment = false,
            needsDataValues = false,
            needsAttributes = false,
            needsOrgUnitGroups = false,
        )
    }
}
