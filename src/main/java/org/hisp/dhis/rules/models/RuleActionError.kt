package org.hisp.dhis.rules.models

data class RuleActionError(val data: String) : RuleAction {
    override fun data(): String {
        return data
    }
}
