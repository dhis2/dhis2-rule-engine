package org.hisp.dhis.rules.api

data class RuleSupplementaryData(
    val userGroups: List<String> = emptyList(),
    val userRoles: List<String> = emptyList(),
    val orgUnitGroups: Map<String, List<String>> = emptyMap(),
)
