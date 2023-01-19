package org.dhis2.ruleengine.models

data class Rule(
    val name:String?,
    val programStage:String?,
    val priority:Int?,
    val condition:String,
    val actions:List<RuleAction>,
    val uid:String
)