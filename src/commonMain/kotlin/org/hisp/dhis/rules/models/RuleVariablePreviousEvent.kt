package org.hisp.dhis.rules.models

import kotlin.jvm.JvmStatic


data class RuleVariablePreviousEvent(var nName: String,
                                     var element: String,
                                     var elementType: RuleValueType):
        RuleVariableDataElement(nName, element, elementType) {

    companion object {

        @JvmStatic fun create(name: String, dataElement: String, valueType: RuleValueType) =
                RuleVariablePreviousEvent(name, dataElement, valueType)

    }
}
