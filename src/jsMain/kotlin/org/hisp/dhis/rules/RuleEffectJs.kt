package org.hisp.dhis.rules

@JsExport
@OptIn(ExperimentalJsExport::class)
data class RuleEffectJs(
    val ruleId: String,
    val ruleAction: RuleActionJs,
    val data: String? = ""
)