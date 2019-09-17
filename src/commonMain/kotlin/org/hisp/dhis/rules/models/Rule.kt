package org.hisp.dhis.rules.models

import kotlin.jvm.JvmStatic

data class Rule(val name: String?,
                val programStage: String?,
                val priority: Int?,
                val condition: String,
                val actions: List<RuleAction>) {

    companion object {

        @JvmStatic fun create(programStage: String?,
                              priority: Int?,
                              condition: String,
                              actions: List<RuleAction>,
                              name: String?) =
                Rule(name, programStage, priority, condition, actions)

    }
}

