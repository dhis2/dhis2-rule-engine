package org.hisp.dhis.rules.models

data class RuleActionError(val data2: String) : RuleAction {
    override fun data(): String {
        return data2
    }
}
