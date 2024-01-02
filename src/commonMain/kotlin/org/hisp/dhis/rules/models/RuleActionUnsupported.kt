package org.hisp.dhis.rules.models

/**
 * @param data
 * @param content         a message to show to user
 * when an actionType is not supported
 * @param actionValueType name of the unsupported action.
 */

data class RuleActionUnsupported(
    val content: String,
    val actionValueType: String,
    val data: String
) : RuleAction
