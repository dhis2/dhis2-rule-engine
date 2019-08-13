package org.hisp.dhis.rules.models

import com.google.auto.value.AutoValue
import java.lang.annotation.ElementType

data class RuleVariableNewestStageEvent(
        var nName: String, var element: String, var elementType: RuleValueType, var programStage: String
) : RuleVariableDataElement(nName, element, elementType) {

    companion object {

        fun create(name: String, dataElement: String,
                   programStage: String, dataElementType: RuleValueType): RuleVariableNewestStageEvent {
            return RuleVariableNewestStageEvent(name, dataElement,
                    dataElementType, programStage)
        }
    }
}
