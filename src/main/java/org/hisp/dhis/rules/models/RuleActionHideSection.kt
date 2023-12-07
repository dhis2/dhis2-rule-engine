package org.hisp.dhis.rules.models

import javax.annotation.Nonnull

data class RuleActionHideSection(
    val programStageSection: String,
    val data: String = ""
) : RuleAction {
    override fun data(): String {
        return data
    }
}
