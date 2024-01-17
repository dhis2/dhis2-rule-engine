package org.hisp.dhis.rules.models


internal interface RuleVariableDataElement : RuleVariable {
    val dataElement: String
    val dataElementType: RuleValueType
}
