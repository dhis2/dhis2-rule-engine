package org.hisp.dhis.rules.models

import kotlin.jvm.JvmStatic


data class RuleActionHideSection(val programStageSection: String?) : RuleAction() {

    companion object {

        @JvmStatic fun create(section: String) = RuleActionHideSection(section)

    }
}