package org.hisp.dhis.rules.models

data class RuleActionAssign(
    val field: String,
    val content: String,
    val attributeType: AttributeType,
    val data: String
) : RuleActionAttribute {
    companion object {
        fun create(
            content: String?,
            data: String, field: String?, attributeType: AttributeType?
        ): RuleActionAssign {
            require(!(content == null && field == null)) {
                "Either content or field " +
                        "parameters must be not null."
            }
            return RuleActionAssign(
                field ?: "", content ?: "",attributeType ?: AttributeType.UNKNOWN , data

            )
        }

        fun create(content: String?, data: String, field: String?): RuleActionAssign {
            return create(content, data, field, AttributeType.UNKNOWN)
        }
    }

    override fun attributeType(): AttributeType {
        return attributeType
    }

    override fun data(): String {
        return data
    }
}
