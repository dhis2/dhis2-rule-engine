package org.hisp.dhis.rules.models

import kotlin.jvm.JvmStatic


data class RuleVariableCurrentEvent(var nName: String?,
                                    var element: String?,
                                    var elementType: RuleValueType?) :
        RuleVariableDataElement(nName, element, elementType) {

    companion object {

        @JvmStatic fun create(name: String, dataElement: String, dataElementValueType: RuleValueType) =
                RuleVariableCurrentEvent(name, dataElement, dataElementValueType)

    }
}