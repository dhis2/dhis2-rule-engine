package org.hisp.dhis.rules.models

data class Rule(
    val condition: String,
    val actions: List<RuleAction>,
    val uid: String = "",
    val name: String? = null,
    val programStage: String? = null,
    val priority: Int? = null,
) : Comparable<Rule> {
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
