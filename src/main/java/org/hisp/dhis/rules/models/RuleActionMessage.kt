package org.hisp.dhis.rules.models

import javax.annotation.Nonnull

data class RuleActionMessage(
    val type: Type,
    val field: String = "",
    val content: String = "",
    val attributeType: AttributeType = AttributeType.UNKNOWN,
    val data: String = ""
) : RuleActionAttribute {
    enum class Type {
        WARNING_ON_COMPILATION,
        ERROR_ON_COMPILATION,
        SHOW_WARNING,
        SHOW_ERROR
    }

    companion object {
        /*
    On Completion
     */
        fun create(
            content: String?,
            data: String?, field: String?, attributeType: AttributeType?, type: Type
        ): RuleActionMessage {
            require(!(content == null && data == null && field == null)) {
                "Content, data and field" +
                        " must not be null at the same time"
            }
            return RuleActionMessage(
                type,field ?: "",  content ?: "",
                attributeType ?: AttributeType.UNKNOWN, data ?: ""
            )
        }
    }

    override fun attributeType(): AttributeType {
        return attributeType
    }

    override fun data(): String {
        return data
    }
}
