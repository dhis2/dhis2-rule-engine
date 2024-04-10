package org.hisp.dhis.rules

import org.hisp.dhis.rules.models.Option
import org.hisp.dhis.rules.models.RuleValueType

@JsExport
@OptIn(ExperimentalJsExport::class)
data class RuleVariableJs(
    val type: RuleVariableType,
    val name: String,
    val useCodeForOptionSet: Boolean,
    val options: Array<Option>,
    val field: String,
    val fieldType: RuleValueType,
    val programStage: String?
)
