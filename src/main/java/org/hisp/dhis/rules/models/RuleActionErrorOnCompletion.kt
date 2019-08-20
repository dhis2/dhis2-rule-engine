package org.hisp.dhis.rules.models

data class RuleActionErrorOnCompletion(override var content: String?, override var data: String?, override var field: String?) : RuleActionMessage() {
    companion object {

        fun create(content: String?,
                   data: String?, field: String?): RuleActionErrorOnCompletion {
            if (content == null && data == null && field == null) {
                throw IllegalArgumentException("Content, data and field" + " must not be null at the same time")
            }

            return RuleActionErrorOnCompletion(content ?: "",
                    data ?: "", field ?: "")
        }
    }
}
