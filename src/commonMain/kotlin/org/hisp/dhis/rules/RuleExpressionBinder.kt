package org.hisp.dhis.rules

import kotlin.jvm.JvmStatic


class RuleExpressionBinder(private val ruleExpression: String,
                           private val ruleVariableValues: HashMap<String, String?>,
                           private val ruleFunctionCalls: HashMap<String, String?>) {

    fun bindVariable(variable: String, value: String) =
        apply {
            if (!ruleVariableValues.containsKey(variable)) {
                throw IllegalArgumentException("Non-existing variable: $variable")
            }

            ruleVariableValues[variable] = value
    }

    fun bindFunction(functionCall: String, value: String) =
            apply {
                if (!ruleFunctionCalls.containsKey(functionCall)) {
                    throw IllegalArgumentException("Non-existing function call: $functionCall")
                }

                ruleFunctionCalls[functionCall] = value
            }

    fun build(): String {
        var expression = ruleExpression

        // iterate over variables and replace them with values
        for ((key, value) in ruleVariableValues) {
            value?.let {
                while (expression.contains(key)) {
                    expression = expression.replace(key, value)
                }
            }
        }

        // iterate over function calls and replace them with values
        for ((key, value) in ruleFunctionCalls) {
            value?.let {
                while (expression.contains(key)) {
                    expression = expression.replace(key, value)
                }
            }
        }

        return expression
    }

    companion object {

        @JvmStatic fun from(ruleExpression: RuleExpression): RuleExpressionBinder {
            val ruleVariablePlaceholders = HashMap<String, String?>(ruleExpression.variable.size)
            val ruleFunctionPlaceholders = HashMap<String, String?>(ruleExpression.functions.size)

            // populate list with placeholders which later will be used as
            // source values in expression
            ruleExpression.variable.forEach { ruleVariablePlaceholders[it] = null }
            ruleExpression.functions.forEach { ruleFunctionPlaceholders[it] = null }

            return RuleExpressionBinder(ruleExpression.expression, ruleVariablePlaceholders, ruleFunctionPlaceholders)
        }
    }
}
