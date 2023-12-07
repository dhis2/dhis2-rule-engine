package org.hisp.dhis.rules.models

/**
 * @param data
 * @param attributeType
 * @param content       a message to show to user
 * when a target option is hidden.
 * @param option        uid of the target option to hide.
 * @param field         uid of the target field to hide.
 */
data class RuleActionHideOption(
    val field: String,
    val option: String,
    val content: String = "",
    val attributeType: AttributeType = AttributeType.UNKNOWN,
    val data: String = ""
) : RuleActionAttribute {
    override fun data(): String {
       return data
    }

    override fun attributeType(): AttributeType {
        return attributeType
    }
}