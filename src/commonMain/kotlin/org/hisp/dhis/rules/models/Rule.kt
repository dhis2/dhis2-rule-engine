package org.hisp.dhis.rules.models

import org.hisp.dhis.lib.expression.Expression
import org.hisp.dhis.lib.expression.ExpressionMode

data class Rule(
    val condition: String,
    val actions: List<RuleAction>,
    val uid: String = "",
    val name: String? = null,
    val programStage: String? = null,
    val priority: Int? = null,
) : Comparable<Rule> {
    internal val conditionExpression: Result<Expression?> =
        if (condition.isEmpty()) Result.success(null)
        else runCatching { Expression(condition, ExpressionMode.RULE_ENGINE_CONDITION, false) }

    internal val actionsForDataElement: List<RuleAction> =
        actions.filter { it.attributeType() == null || it.attributeType() == AttributeType.DATA_ELEMENT.name || it.attributeType() == AttributeType.UNKNOWN.name }.sorted()

    internal val actionsForEnrollment: List<RuleAction> =
        actions.filter { it.attributeType() == null || it.attributeType() == AttributeType.TRACKED_ENTITY_ATTRIBUTE.name || it.attributeType() == AttributeType.UNKNOWN.name }.sorted()

    override fun compareTo(other: Rule): Int =
        if (this.priority != null && other.priority != null) {
            this.priority.compareTo(other.priority)
        } else if (this.priority != null) {
            -1
        } else if (other.priority != null) {
            1
        } else {
            0
        }
}
