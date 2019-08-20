package org.hisp.dhis.rules.models

data class RuleVariableNewestEvent(var nName: String,
                                   var element: String,
                                   var elementType: RuleValueType) : RuleVariableDataElement(nName, element, elementType) {
    companion object {

        fun create(name: String,
                   dataElement: String, dataElementValueType: RuleValueType): RuleVariableNewestEvent {
            return RuleVariableNewestEvent(name, dataElement, dataElementValueType)
        }
    }
}
