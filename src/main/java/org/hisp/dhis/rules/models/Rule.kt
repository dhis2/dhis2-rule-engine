package org.hisp.dhis.rules.models

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class Rule(val name: String?, val programStage: String?, val priority: Int?,
                val condition: String, val actions: ImmutableList<RuleAction>, val actionslist: List<RuleAction> = emptyList()) {

    companion object {
        @JvmStatic
        fun create(programStage: String?, priority: Int?,
                   condition: String, actions: ImmutableList<RuleAction>, name: String?): Rule {
            return Rule(name, programStage, priority, condition, actions)
        }

        @Deprecated(message = "Meanwhile migrating to kotlin, this is available", replaceWith = ReplaceWith("create()"))
        @JvmStatic
        fun create(programStage: String?, priority: Int?,
                   condition: String, actions: List<RuleAction>, name: String?): Rule {
            return Rule(name, programStage, priority, condition, persistentListOf(), actions)
        }
    }
}

