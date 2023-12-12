package org.hisp.dhis.rules.models

data class RuleActionSetMandatoryField(
    val field: String,
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
