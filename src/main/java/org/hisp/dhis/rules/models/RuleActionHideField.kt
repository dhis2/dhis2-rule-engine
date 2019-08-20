package org.hisp.dhis.rules.models

data class RuleActionHideField(var content: String?, var field: String?) : RuleAction() {

    companion object {

        fun create(
                content: String?, field: String): RuleActionHideField {
            return RuleActionHideField(content ?: "", field)
        }
    }
}
