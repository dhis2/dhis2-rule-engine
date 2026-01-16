package org.hisp.dhis.rules.models

data class RuleAction(
    val data: String?,
    val type: String,
    val values: Map<String, String> = emptyMap(),
    val priority: Int? = null,
) : Comparable<RuleAction> {
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
