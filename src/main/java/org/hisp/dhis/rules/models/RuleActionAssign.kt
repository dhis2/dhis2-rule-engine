package org.hisp.dhis.rules.models


data class RuleActionAssign(val content: String?, val data: String?, val field: String?) : RuleAction() {

    companion object {

        @JvmStatic
        fun create(content: String?, data: String, field: String?): RuleActionAssign {
            return when {
                content == null && field == null -> throw IllegalArgumentException("Either content or field parameters must be not null.")
                else -> RuleActionAssign(content ?: "", data, field ?: "")
            }
        }
    }
}