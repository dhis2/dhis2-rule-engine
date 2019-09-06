package org.hisp.dhis.rules.models

import kotlin.jvm.JvmStatic


data class RuleActionHideOptionGroup(val content: String?, val optionGroup: String?) : RuleAction() {

    companion object {

        @JvmStatic fun create(content: String?, optionGroup: String) =
                RuleActionHideOptionGroup(content ?: "", optionGroup)

    }
}