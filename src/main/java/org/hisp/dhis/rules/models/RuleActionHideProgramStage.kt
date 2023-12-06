package org.hisp.dhis.rules.models

data class RuleActionHideProgramStage(
    val programStage: String,
    val data: String = ""
) : RuleAction {
    override fun data(): String {
        return data
    }
}