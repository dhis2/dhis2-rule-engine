package org.hisp.dhis.rules.models

data class RuleVariablePreviousEvent(
        var nName: String, var element: String, var elementType: RuleValueType
): RuleVariableDataElement(nName, element, elementType) {
    companion object {

        fun create(name: String,
                   dataElement: String, valueType: RuleValueType): RuleVariablePreviousEvent {
            return RuleVariablePreviousEvent(name, dataElement, valueType)
        }
    }
}
