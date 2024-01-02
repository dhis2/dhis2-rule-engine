package org.hisp.dhis.rules.models

/**
 * @param data
 * @param attributeType
 * @param content       a message to show to user
 * when a option group is shown.
 * @param optionGroup   uid of the target option group to show.
 * @param field         uid of the field of option group to show.
 */
data class RuleActionShowOptionGroup(
    val field: String,
    val optionGroup: String,
    val content: String = "",
    val attributeType2: AttributeType = AttributeType.UNKNOWN
) : RuleActionAttribute {
    override fun attributeType(): AttributeType {
        return attributeType2
    }
}
