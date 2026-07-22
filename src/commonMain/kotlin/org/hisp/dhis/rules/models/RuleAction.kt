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

    fun content(): String? = values[CONTENT]

    fun field(): String? = values[FIELD]

    fun attributeType(): String? = values[ATTRIBUTE_TYPE]

    /**
     * Resolves the target of this action when it is an ASSIGN action.
     *
     * Returns null for any other action type. For ASSIGN actions, returns
     * [AssignTarget.Field] when a non-empty `field` value is present, otherwise
     * [AssignTarget.Variable] with the program rule variable name taken from `content`,
     * accepting both the `#{variableName}` and `A{variableName}` notations. Returns
     * [AssignTarget.Invalid] when the ASSIGN action defines neither a `field` nor a
     * `content` value, or when `content` is not a program rule variable reference like
     * `#{variableName}`.
     */
    internal fun assignTarget(): AssignTarget? {
        if (type != ASSIGN) return null
        val field = field()
        if (!field.isNullOrEmpty()) return AssignTarget.Field(field)
        val content = content()
        if (content.isNullOrEmpty()) {
            return AssignTarget.Invalid(
                "ASSIGN action must define either 'field' (the data element or attribute to assign to) " +
                    "or 'content' (the program rule variable to assign to)",
            )
        }
        val name =
            if ((content.startsWith("#{") || content.startsWith("A{")) && content.endsWith("}")) {
                content.substring(2, content.length - 1)
            } else {
                null
            }
        if (name.isNullOrEmpty() || name.contains('{') || name.contains('}')) {
            return AssignTarget.Invalid(
                "ASSIGN action content must be a program rule variable reference like #{variableName}, but was: $content",
            )
        }
        return AssignTarget.Variable(name)
    }

    /**
     * Copy of this action with [field] and [attributeType] injected into [values], used by
     * the engine to emit self-contained effects for assignments to program rule variables
     * backed by a data element or tracked entity attribute.
     */
    internal fun withAssignBacking(
        field: String,
        attributeType: String,
    ): RuleAction = copy(values = values + listOf(FIELD to field, ATTRIBUTE_TYPE to attributeType))

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

        private const val CONTENT = "content"
        private const val FIELD = "field"
        private const val ATTRIBUTE_TYPE = "attributeType"
    }
}
