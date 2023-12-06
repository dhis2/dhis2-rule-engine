package org.hisp.dhis.rules.models

import javax.annotation.Nonnull

data class RuleEffect(
    val ruleId: String,
    val ruleAction: RuleAction,
    val data: String = ""
)
{
    fun data() : String {
        return data
    }

    fun ruleAction() : RuleAction {
        return ruleAction
    }
}