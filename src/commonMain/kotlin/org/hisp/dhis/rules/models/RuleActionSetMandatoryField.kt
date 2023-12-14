package org.hisp.dhis.rules.models

data class RuleActionSetMandatoryField(
    val field: String,
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
