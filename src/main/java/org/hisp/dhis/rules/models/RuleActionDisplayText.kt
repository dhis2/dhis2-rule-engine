package org.hisp.dhis.rules.models


data class RuleActionDisplayText(override var content: String?,
                                 override var data: String?,
                                 override var location: String?) : RuleActionText() {

    companion object {

        @JvmStatic
        fun createForFeedback(content: String?, data: String?): RuleActionDisplayText {
            return when {
                content == null && data == null -> throw IllegalArgumentException("Both content and data must not be null")
                else -> RuleActionDisplayText(content ?: "", data ?: "", LOCATION_FEEDBACK_WIDGET)
            }
        }

        @JvmStatic
        fun createForIndicators(content: String?, data: String?): RuleActionDisplayText {
            return when {
                content == null && data == null -> throw IllegalArgumentException("Both content and data must not be null")
                else -> RuleActionDisplayText(content ?: "", data ?: "", LOCATION_INDICATOR_WIDGET)
            }
        }
    }
}