package org.hisp.dhis.rules.models

import com.google.auto.value.AutoValue

data class RuleActionShowOptionGroup(
        val content: String?,
        val optionGroup: String?,
        val field: String?
) : RuleAction() {

    companion object {

        fun create(
                content: String?, optionGroup: String, field: String): RuleActionShowOptionGroup {
            return RuleActionShowOptionGroup(content ?: "", optionGroup, field)
        }
    }
}
