package org.hisp.dhis.rules

import org.hisp.dhis.lib.expression.js.Entry

@JsExport
@OptIn(ExperimentalJsExport::class)
data class RuleEngineContextJs(
    val rules: Array<RuleJs>,
    val ruleVariables: Array<RuleVariableJs>,
    val supplementaryData: Array<Entry<String, Array<String>>> = emptyArray(),
    val constantsValues: Array<Entry<String, String>> = emptyArray()
)
