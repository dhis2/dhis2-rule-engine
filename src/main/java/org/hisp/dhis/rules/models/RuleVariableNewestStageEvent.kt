package org.hisp.dhis.rules.models


data class RuleVariableNewestStageEvent(var nName: String,
                                        var element: String,
                                        var elementType: RuleValueType,
                                        var programStage: String) :
        RuleVariableDataElement(nName, element, elementType) {

    companion object {

        @JvmStatic
        fun create(name: String, dataElement: String, programStage: String, dataElementType: RuleValueType) =
                RuleVariableNewestStageEvent(name, dataElement, dataElementType, programStage)

    }
}