package org.hisp.dhis.rules.models

data class RuleActionCreateEvent(
    val programStage: String,
    val content: String = "",
    val data: String = ""
) : RuleAction {
    override fun data(): String {
        return data
    }
}
