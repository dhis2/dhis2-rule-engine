package org.hisp.dhis.rules.models


internal interface RuleVariableDataElement : RuleVariable {
    fun dataElement(): String

    fun dataElementType(): RuleValueType
}
