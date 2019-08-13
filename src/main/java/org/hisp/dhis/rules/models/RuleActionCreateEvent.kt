package org.hisp.dhis.rules.models


data class RuleActionCreateEvent(
        val content: String?,
        val data: String?,
        val programStage: String?
) : RuleAction() {

    companion object {

        fun create(content: String?,
                   data: String?, programStage: String): RuleActionCreateEvent {
            return RuleActionCreateEvent(content ?: "",
                    data ?: "", programStage)
        }
    }
}
