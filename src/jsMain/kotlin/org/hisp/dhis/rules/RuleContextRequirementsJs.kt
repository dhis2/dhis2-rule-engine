package org.hisp.dhis.rules

@JsExport
data class RuleContextRequirementsJs(
    val needsAllEvents: Boolean,
    val needsEnrollment: Boolean,
    val needsDataValues: Boolean,
    val needsAttributes: Boolean,
    val orgUnitGroups: Array<String>,
)
