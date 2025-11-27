package org.hisp.dhis.rules

import js.collections.JsMap

@JsExport
data class RuleSupplementaryDataJs(
    val userGroups: Array<String>,
    val userRoles: Array<String>,
    val orgUnitGroups: JsMap<String, Array<String>>,
)
