package org.hisp.dhis.rules.models

data class RuleEffect(
        val ruleAction: RuleAction?,
        val data: String?
) {

    companion object {

        fun create(ruleAction: RuleAction, data: String): RuleEffect {
            return RuleEffect(ruleAction, data)
        }

        fun create(ruleAction: RuleAction): RuleEffect {
            return RuleEffect(ruleAction, "")
        }
    }
}
