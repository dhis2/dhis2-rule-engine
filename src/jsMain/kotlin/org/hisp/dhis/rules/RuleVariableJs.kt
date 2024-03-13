package org.hisp.dhis.rules

import org.hisp.dhis.rules.models.Option
import org.hisp.dhis.rules.models.RuleValueType

@JsExport
@OptIn(ExperimentalJsExport::class)
data class RuleVariableJs(
    val type: String,
    val name: String,
    val useCodeForOptionSet: Boolean,
    val options: Array<Option>,
    val field: String,
    val fieldType: String,
    val programStage: String?
){
    init {
        require(RULE_VALUE_TYPES.contains(fieldType)) { "RuleValueType type must be one of: $RULE_VALUE_TYPES" }
        require(TYPES.contains(type)) { "RuleVariableType type must be one of: $TYPES" }
    }

    companion object {
        val RULE_VALUE_TYPES = RuleValueType.entries.map { it.name } .toTypedArray()
        val TYPES = RuleVariableType.entries.map { it.name } .toTypedArray()
    }
}
