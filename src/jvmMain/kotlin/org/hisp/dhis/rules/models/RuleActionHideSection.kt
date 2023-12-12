package org.hisp.dhis.rules.models

data class RuleActionHideSection(
    val programStageSection: String,
    val data: String = ""
) : RuleAction {
    override fun data(): String {
        return data
    }
}
