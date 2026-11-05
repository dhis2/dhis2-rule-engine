package org.hisp.dhis.rules.api

/**
 * An operator that can be used in rule engine expressions (conditions and data fields).
 *
 * @param symbol the canonical symbol of the operator, e.g. `&&`
 * @param aliases alternative spellings of the operator that are also accepted, e.g. `and`
 * @param returnType the type of the value returned by the operator
 * @param operandsType the type expected for the operand(s)
 */
data class ExpressionOperator(
    val symbol: String,
    val aliases: List<String>,
    val returnType: ExpressionValueType,
    val operandsType: ExpressionValueType,
)
