package org.hisp.dhis.rules.models

data class RuleActionHideOptionGroup(
        val content: String?,
        val optionGroup: String?
) : RuleAction() {

    companion object {

        fun create(
                content: String?, optionGroup: String): RuleActionHideOptionGroup {
            return RuleActionHideOptionGroup(content ?: "", optionGroup)
        }
    }
}
