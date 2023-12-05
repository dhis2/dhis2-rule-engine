package org.hisp.dhis.rules

fun interface RuleExpressionEvaluator {

    fun evaluate(expression: String): String
}
