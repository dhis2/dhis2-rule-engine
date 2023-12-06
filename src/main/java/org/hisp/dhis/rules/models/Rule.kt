package org.hisp.dhis.rules.models

data class Rule(
    val condition: String,
    val actions: List<RuleAction>,
    val uid: String = "",
    val name: String? = null,
    val programStage: String? = null,
    val priority: Int? = 0
){

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
}
