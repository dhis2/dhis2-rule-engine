package org.hisp.dhis.rules.models

import com.google.auto.value.AutoValue

data class RuleActionHideProgramStage(
        val programStage: String?
) : RuleAction() {

    companion object {

        fun create(programStage: String): RuleActionHideProgramStage {
            return RuleActionHideProgramStage(programStage)
        }
    }
}
