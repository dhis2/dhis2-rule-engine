package org.hisp.dhis.rules.models


data class RuleActionCreateEvent(val content: String?,
                                 val data: String?,
                                 val programStage: String?) : RuleAction() {

    companion object {

        @JvmStatic
        fun create(content: String?, data: String?, programStage: String) =
                RuleActionCreateEvent(content ?: "", data ?: "", programStage)
    }
}