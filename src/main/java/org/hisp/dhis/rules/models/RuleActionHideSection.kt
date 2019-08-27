package org.hisp.dhis.rules.models


data class RuleActionHideSection(val programStageSection: String?) : RuleAction() {

    companion object {

        @JvmStatic
        fun create(section: String) = RuleActionHideSection(section)

    }
}