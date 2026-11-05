package org.hisp.dhis.rules.api

/**
 * Everything that can be used to write a program rule expression (conditions and data field expressions).
 *
 * @param functions the functions, e.g. `d2:daysBetween`
 * @param binaryOperators the binary operators in order of precedence (highest first), e.g. `&&`
 * @param unaryOperators the unary operators, e.g. `!`
 * @param environmentVariables the environment variables provided by the rule engine, referenced as `V{name}`
 */
data class RuleExpressionSyntax(
    val functions: List<ExpressionFunction>,
    val binaryOperators: List<ExpressionOperator>,
    val unaryOperators: List<ExpressionOperator>,
    val environmentVariables: List<EnvironmentVariable>,
)
