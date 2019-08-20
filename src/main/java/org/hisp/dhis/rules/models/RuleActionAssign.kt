package org.hisp.dhis.rules.models

data class RuleActionAssign(
        val content: String?,
        val data: String?,
        val field: String?
) : RuleAction() {

    companion object {

        fun create(content: String?,
                   data: String, field: String?): RuleActionAssign {
            if (content == null && field == null) {
                throw IllegalArgumentException("Either content or field " + "parameters must be not null.")
            }

            return RuleActionAssign(content ?: "",
                    data, field ?: "")
        }
    }
}
