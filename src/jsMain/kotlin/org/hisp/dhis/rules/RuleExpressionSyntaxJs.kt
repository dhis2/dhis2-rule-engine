package org.hisp.dhis.rules

import org.hisp.dhis.rules.api.EnvironmentVariable

@JsExport
data class RuleExpressionSyntaxJs(
    val functions: Array<ExpressionFunctionJs>,
    val binaryOperators: Array<ExpressionOperatorJs>,
    val unaryOperators: Array<ExpressionOperatorJs>,
    val environmentVariables: Array<EnvironmentVariable>,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class.js != other::class.js) return false

        other as RuleExpressionSyntaxJs

        if (!functions.contentEquals(other.functions)) return false
        if (!binaryOperators.contentEquals(other.binaryOperators)) return false
        if (!unaryOperators.contentEquals(other.unaryOperators)) return false
        if (!environmentVariables.contentEquals(other.environmentVariables)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = functions.contentHashCode()
        result = 31 * result + binaryOperators.contentHashCode()
        result = 31 * result + unaryOperators.contentHashCode()
        result = 31 * result + environmentVariables.contentHashCode()
        return result
    }
}
