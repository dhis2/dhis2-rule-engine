package org.hisp.dhis.rules.models

import kotlin.js.JsExport

/**
 * A problem found while validating an expression.
 *
 * @property message what is wrong, e.g. ``Unknown variable: `weight` ``
 * @property position the normalised sub-expression the problem was found in, or an empty string if unknown
 */
@JsExport
data class RuleValidationIssue(
    val message: String,
    val position: String = "",
)
