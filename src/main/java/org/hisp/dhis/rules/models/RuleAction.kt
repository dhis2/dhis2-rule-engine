package org.hisp.dhis.rules.models

import javax.annotation.Nonnull

fun interface RuleAction {
    fun data(): String
}
