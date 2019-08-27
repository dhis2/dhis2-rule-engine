package org.hisp.dhis.rules.models


data class RuleActionHideOption(val content: String?, val option: String?, val field: String?) : RuleAction() {

    companion object {

        @JvmStatic
        fun create(content: String?, option: String, field: String) =
                RuleActionHideOption(content ?: "", option, field)

    }
}