package org.hisp.dhis.rules.models

data class RuleActionHideProgramStage(
        val programStage: String?
) : RuleAction() {

    companion object {

        fun create(programStage: String): RuleActionHideProgramStage {
            return RuleActionHideProgramStage(programStage)
        }
    }
}
