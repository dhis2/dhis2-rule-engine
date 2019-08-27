package org.hisp.dhis.rules.models


data class RuleActionHideOptionGroup(val content: String?, val optionGroup: String?) : RuleAction() {

    companion object {

        @JvmStatic
        fun create(content: String?, optionGroup: String) =
                RuleActionHideOptionGroup(content ?: "", optionGroup)

    }
}