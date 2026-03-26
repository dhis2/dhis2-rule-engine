package org.hisp.dhis.rules.models

import org.hisp.dhis.lib.expression.Expression
import org.hisp.dhis.lib.expression.ExpressionMode

data class RuleAction(
    val data: String?,
    val type: String,
    val values: Map<String, String> = emptyMap(),
    val priority: Int? = null,
) : Comparable<RuleAction> {
    internal val dataExpression: Result<Expression?> =
        if (data.isNullOrEmpty()) Result.success(null)
        else runCatching { Expression(data!!, ExpressionMode.RULE_ENGINE_ACTION, false) }

    fun content(): String? = values["content"]

    fun field(): String? = values["field"]

    fun attributeType(): String? = values["attributeType"]

    override fun compareTo(other: RuleAction): Int =
        when {
            this.priority != null && other.priority != null -> {
                this.priority.compareTo(other.priority)
            }
            this.priority != null -> {
                -1
            }
            other.priority != null -> {
                1
            }
            else -> {
                0
            }
        }
}
