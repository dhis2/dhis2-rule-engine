package org.hisp.dhis.rules.models

data class Rule(
    val condition: String,
    val actions: List<RuleAction>,
    val uid: String = "",
    val name: String? = null,
    val programStage: String? = null,
    val priority: Int? = 0
):Comparable<Rule>{

    fun name(): String? {
        return name
    }

    fun condition(): String {
        return condition
    }

    fun actions(): List<RuleAction> {
        return actions
    }

    fun uid(): String {
        return uid
    }

    fun programStage(): String? {
        return programStage
    }

    fun priority(): Int? {
        return priority
    }

    override fun compareTo(other: Rule): Int {
        return if (this.priority != null && other.priority != null) {
            this.priority.compareTo(other.priority)
        } else if (this.priority != null) {
            -1
        } else if (other.priority != null) {
            1
        } else {
            0
        }
    }
}
