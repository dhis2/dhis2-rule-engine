package org.hisp.dhis.rules

interface RuleExpressionEvaluator {
    fun evaluate(expression: String): String
}
