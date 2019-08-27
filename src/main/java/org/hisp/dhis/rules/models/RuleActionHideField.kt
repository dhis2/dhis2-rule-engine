package org.hisp.dhis.rules.models


data class RuleActionHideField(var content: String?, var field: String?) : RuleAction() {

    companion object {

        @JvmStatic
        fun create(content: String?, field: String) =
                RuleActionHideField(content ?: "", field)
    }
}