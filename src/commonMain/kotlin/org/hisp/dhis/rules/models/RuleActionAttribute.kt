package org.hisp.dhis.rules.models

interface RuleActionAttribute : RuleAction {
    fun attributeType(): AttributeType
}
