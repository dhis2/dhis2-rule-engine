package org.hisp.dhis.rules.models

data class RuleActionSetMandatoryField(
    val field: String,
    val attributeType2: AttributeType = AttributeType.UNKNOWN
) : RuleActionAttribute {
    override fun attributeType(): AttributeType {
        return attributeType2
    }
}
