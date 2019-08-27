package org.hisp.dhis.rules.models


data class RuleActionShowError(override var content: String?,
                               override var data: String?,
                               override var field: String?) : RuleActionMessage() {

    companion object {

        @JvmStatic
        fun create(content: String?, data: String?, field: String): RuleActionShowError {
            return when {
                content == null && data == null -> throw IllegalArgumentException("Both content and data must not be null")
                else -> RuleActionShowError(content ?: "", data ?: "", field)
            }
        }

    }
}