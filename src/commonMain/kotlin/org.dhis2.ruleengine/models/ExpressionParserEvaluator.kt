package org.dhis2.ruleengine.models

import org.dhis2.ruleengine.DataItem
import org.dhis2.ruleengine.RuleVariableValue

interface ExpressionParserEvaluator {
    fun evaluate(
        condition: String,
        valueMap: Map<String, RuleVariableValue>,
        supplementaryData: Map<String, List<String>>
    ): String

    fun getExpressionDescription(
        expression: String,
        dataItemStore: Map<String, DataItem>,
        castAsBoolean: Boolean = false
    ): String
}