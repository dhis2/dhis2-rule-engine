package org.dhis2.ruleengine.models

import org.dhis2.ruleengine.DataItem
import org.dhis2.ruleengine.RuleVariableValue
import org.dhis2.ruleengine.exprk.Expressions

actual fun expressionEvaluator(): ExpressionParserEvaluator {
    return object : ExpressionParserEvaluator {
        override fun evaluate(
            condition: String,
            valueMap: Map<String, RuleVariableValue>,
            supplementaryData: Map<String, List<String>>
        ): String {
            return Expressions()
                .withValueMap(valueMap)
                .eval(condition)
        }

        override fun getExpressionDescription(
            expression: String,
            dataItemStore: Map<String, DataItem>,
            castAsBoolean: Boolean
        ): String {
            TODO("Not yet implemented")
        }

    }
}