package org.hisp.dhis.rules

import js.collections.JsMap

@JsExport
data class RuleEngineContextJs(
    val rules: Array<RuleJs>,
    val ruleVariables: Array<RuleVariableJs>,
    val supplementaryData: RuleSupplementaryDataJs,
    val constantsValues: JsMap<String, String> = JsMap()
)
