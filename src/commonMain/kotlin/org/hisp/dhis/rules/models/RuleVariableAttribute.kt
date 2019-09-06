package org.hisp.dhis.rules.models

import kotlin.jvm.JvmStatic


data class RuleVariableAttribute(var rName: String?,
                                 val trackedEntityAttribute: String?,
                                 val trackedEntityAttributeType: RuleValueType?) : RuleVariable(rName) {

    companion object {

        @JvmStatic fun create(name: String, attribute: String, attributeType: RuleValueType) =
                RuleVariableAttribute(name, attribute, attributeType)

    }
}