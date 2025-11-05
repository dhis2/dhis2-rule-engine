package org.hisp.dhis.rules.api

import kotlin.js.JsExport

@JsExport
data class SupplementaryData(
    val userGroups: List<String> = emptyList(),
    val userRoles: List<String> = emptyList(),
    val orgUnitGroups: Map<String, List<String>> = emptyMap(),
)
