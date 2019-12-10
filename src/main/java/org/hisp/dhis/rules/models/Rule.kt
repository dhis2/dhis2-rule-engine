package org.hisp.dhis.rules.models

import com.google.common.collect.ImmutableList

data class Rule(
    val programStage: String?,
    val priority: Int?,
    val condition: String,
    val actions: List<RuleAction>,
    val name: String?){
}