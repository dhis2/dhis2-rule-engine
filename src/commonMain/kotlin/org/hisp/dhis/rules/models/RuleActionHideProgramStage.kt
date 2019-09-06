package org.hisp.dhis.rules.models

import kotlin.jvm.JvmStatic


data class RuleActionHideProgramStage(val programStage: String?) : RuleAction() {

    companion object {

        @JvmStatic fun create(programStage: String) = RuleActionHideProgramStage(programStage)

    }
}