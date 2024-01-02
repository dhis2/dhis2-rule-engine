package org.hisp.dhis.rules.models

data class RuleActionError(val data2: String) : RuleActionData {
    override fun data(): String {
        return data2
    }
}
