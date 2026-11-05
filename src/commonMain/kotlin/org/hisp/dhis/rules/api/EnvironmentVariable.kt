package org.hisp.dhis.rules.api

import kotlin.js.JsExport

/**
 * An environment variable provided by the rule engine, referenced in an expression as `V{name}`.
 *
 * @param name the name of the variable, e.g. `current_date`
 * @param valueType the type of the value of the variable
 */
@JsExport
data class EnvironmentVariable(
    val name: String,
    val valueType: ItemValueType,
)
