package org.hisp.dhis.rules

import org.hisp.dhis.lib.expression.js.Entry
import org.hisp.dhis.rules.models.Rule
import org.hisp.dhis.rules.models.RuleEnrollment
import org.hisp.dhis.rules.models.RuleEvent
import org.hisp.dhis.rules.models.RuleVariable

@JsExport
@OptIn(ExperimentalJsExport::class)
data class RuleEngineContextJs(
    val rules: Array<RuleJs>,
    val ruleVariables: Array<RuleVariableJs>,
    val supplementaryData: Array<Entry<String, Array<String>>> = emptyArray(),
    val constantsValues: Array<Entry<String, String>> = emptyArray()
)
