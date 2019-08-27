package org.hisp.dhis.rules.models


data class RuleVariableNewestEvent(var nName: String,
                                   var element: String,
                                   var elementType: RuleValueType) :
        RuleVariableDataElement(nName, element, elementType) {

    companion object {

        @JvmStatic
        fun create(name: String, dataElement: String, dataElementValueType: RuleValueType) =
                RuleVariableNewestEvent(name, dataElement, dataElementValueType)

    }
}