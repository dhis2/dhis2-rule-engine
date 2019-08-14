package org.hisp.dhis.rules.models

import com.google.auto.value.AutoValue
import javax.annotation.concurrent.Immutable

@Immutable
data class RuleActionDisplayText(override var content: String?, override var data: String?, override var location: String?) : RuleActionText() {
    companion object {

        fun createForFeedback(
                content: String?, data: String?): RuleActionDisplayText {
            if (content == null && data == null) {
                throw IllegalArgumentException("Both content and data must not be null")
            }

            return RuleActionDisplayText(content ?: "",
                    data ?: "", RuleActionText.Companion.LOCATION_FEEDBACK_WIDGET)
        }

        fun createForIndicators(
                content: String?, data: String?): RuleActionDisplayText {
            if (content == null && data == null) {
                throw IllegalArgumentException("Both content and data must not be null")
            }

            return RuleActionDisplayText(content ?: "",
                    data ?: "", RuleActionText.Companion.LOCATION_INDICATOR_WIDGET)
        }
    }
}
