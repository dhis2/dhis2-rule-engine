package org.hisp.dhis.rules

import js.collections.JsMap
import org.hisp.dhis.rules.api.SupplementaryData

@JsExport
data class RuleEngineContextJs(
    val rules: Array<RuleJs>,
    val ruleVariables: Array<RuleVariableJs>,
    val supplementaryData: JsMap<SupplementaryData, Array<String>> = JsMap(),
    val constantsValues: JsMap<String, String> = JsMap()
)
