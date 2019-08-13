package org.hisp.dhis.rules.models

import com.google.auto.value.AutoValue

data class RuleVariableCurrentEvent(
        var nName: String?, var element: String?, var elementType: RuleValueType?
) : RuleVariableDataElement(
        nName, element, elementType
) {
    companion object {

        fun create(name: String,
                   dataElement: String, dataElementValueType: RuleValueType): RuleVariableCurrentEvent {
            return RuleVariableCurrentEvent(name, dataElement, dataElementValueType)
        }
    }
}
