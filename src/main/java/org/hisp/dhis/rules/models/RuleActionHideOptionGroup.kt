package org.hisp.dhis.rules.models

import com.google.auto.value.AutoValue

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
