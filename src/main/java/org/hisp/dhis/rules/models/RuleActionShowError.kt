package org.hisp.dhis.rules.models

import com.google.auto.value.AutoValue

data class RuleActionShowError(override var content: String?, override var data: String?, override var field: String?) : RuleActionMessage() {
    companion object {

        fun create(content: String?,
                   data: String?, field: String): RuleActionShowError {
            if (content == null && data == null) {
                throw IllegalArgumentException("Both content and data must not be null")
            }

            return RuleActionShowError(content ?: "",
                    data ?: "", field)
        }
    }
}
