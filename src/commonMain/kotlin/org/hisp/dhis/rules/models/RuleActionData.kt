package org.hisp.dhis.rules.models

fun interface RuleActionData:RuleAction {
    fun data(): String?
}
