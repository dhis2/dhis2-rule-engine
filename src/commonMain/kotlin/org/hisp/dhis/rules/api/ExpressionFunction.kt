package org.hisp.dhis.rules.api

import kotlin.js.JsExport

/**
 * A function that can be used in rule engine expressions (conditions and data fields).
 *
 * @param name the name of the function as written in an expression, e.g. `d2:daysBetween`
 * @param returnType the type of the value returned by the function
 * @param parameters the parameters in the order they are accepted
 */
data class ExpressionFunction(
    val name: String,
    val returnType: ExpressionValueType,
    val parameters: List<ExpressionFunctionParameter>,
)

/**
 * A parameter of an [ExpressionFunction].
 *
 * @param type the type expected for the argument
 * @param isOptional true, if the argument can be omitted (optional parameters are always at the end)
 * @param isVarargs true, if this is the last parameter and it can be repeated
 * @param name a descriptive name of the parameter (e.g. `regex`), or null if it is not named because the meaning is
 * obvious from the function name (e.g. the only parameter of `d2:ceil`)
 */
@JsExport
data class ExpressionFunctionParameter(
    val type: ExpressionValueType,
    val isOptional: Boolean,
    val isVarargs: Boolean,
    val name: String? = null,
)
