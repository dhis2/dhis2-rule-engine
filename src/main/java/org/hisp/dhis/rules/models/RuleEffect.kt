package org.hisp.dhis.rules.models

data class RuleEffect(val ruleAction: RuleAction?, val data: String?) {

    companion object {

        @JvmStatic
        fun create(ruleAction: RuleAction, data: String) = RuleEffect(ruleAction, data)

        @JvmStatic
        fun create(ruleAction: RuleAction) = RuleEffect(ruleAction, "")

    }
}
