package org.hisp.dhis.rules.models

/**
 * @param data
 * @param attributeType
 * @param content a message to show to user
 * when a target field is hidden.
 * @param field uid of the target field to hide.
 * It can be both dataElement and trackedEntityAttribute.
 */
data class RuleActionHideField(
                val field: String,
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
