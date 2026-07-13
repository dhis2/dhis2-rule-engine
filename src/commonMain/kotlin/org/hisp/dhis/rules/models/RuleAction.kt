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

    /**
     * Resolves the target of this action when it is an ASSIGN action.
     *
     * Returns null for any other action type. For ASSIGN actions, returns
     * [AssignTarget.Field] when a non-empty `field` value is present, otherwise
     * [AssignTarget.Variable] with the program rule variable name taken from `content`,
     * accepting both the `#{variableName}` and `A{variableName}` notations.
     *
     * @throws IllegalArgumentException if the ASSIGN action defines neither a `field` nor a
     * `content` value, or if `content` is not a program rule variable reference like
     * `#{variableName}`.
     */
    fun assignTarget(): AssignTarget? {
        if (type != ASSIGN) return null
        val field = field()
        if (!field.isNullOrEmpty()) return AssignTarget.Field(field)
        val content = content()
        require(!content.isNullOrEmpty()) {
            "ASSIGN action must define either 'field' (the data element or attribute to assign to) " +
                "or 'content' (the program rule variable to assign to)"
        }
        require((content.startsWith("#{") || content.startsWith("A{")) && content.endsWith("}")) {
            "ASSIGN action content must be a program rule variable reference like #{variableName}, but was: $content"
        }
        return AssignTarget.Variable(content.substring(2, content.length - 1))
    }

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

    companion object {
        /** Action type interpreted by the engine: assigns a value to a field or variable. */
        const val ASSIGN = "ASSIGN"

        /** Action type of the synthetic effect emitted when a rule or action fails to evaluate. */
        const val ERROR = "ERROR"
    }
}
