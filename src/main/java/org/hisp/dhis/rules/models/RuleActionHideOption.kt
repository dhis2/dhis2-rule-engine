package org.hisp.dhis.rules.models

import com.google.auto.value.AutoValue

data class RuleActionHideOption(
        val content: String?,
        val option: String?,
        val field: String?
) : RuleAction() {


    companion object {

        fun create(
                content: String?, option: String, field: String): RuleActionHideOption {
            return RuleActionHideOption(content ?: "", option, field)
        }
    }
}
