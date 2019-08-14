package org.hisp.dhis.rules.models

import javax.annotation.concurrent.Immutable

@Immutable
data class RuleActionDisplayKeyValuePair(override var content: String?, override var data: String?, override var location: String?) : RuleActionText() {
    companion object {

        fun createForFeedback(
                content: String?, data: String?): RuleActionDisplayKeyValuePair {
            if (content == null && data == null) {
                throw IllegalArgumentException("Both content and data must not be null")
            }

            return RuleActionDisplayKeyValuePair(content ?: "",
                    data ?: "", RuleActionText.LOCATION_FEEDBACK_WIDGET)
        }

        fun createForIndicators(
                content: String?, data: String?): RuleActionDisplayKeyValuePair {
            if (content == null && data == null) {
                throw IllegalArgumentException("Both content and data must not be null")
            }

            return RuleActionDisplayKeyValuePair(content ?: "",
                    data ?: "", RuleActionText.LOCATION_INDICATOR_WIDGET)
        }
    }
}
