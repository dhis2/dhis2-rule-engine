package org.hisp.dhis.rules.models

/**
 * @param data
 * @param attributeType
 * @param content       a message to show to user
 * when a target option is hidden.
 * @param optionGroup   uid of the target option group to hide.
 * @param field         uid of the target field to hide options.
 */
data class RuleActionHideOptionGroup(
    val field: String,
    val optionGroup: String,
    val content: String = "",
    val attributeType2: AttributeType = AttributeType.UNKNOWN,
    val data2: String = ""
) : RuleActionAttribute {
    override fun data(): String {
        return data2
    }

    override fun attributeType(): AttributeType {
        return attributeType2
    }
}
