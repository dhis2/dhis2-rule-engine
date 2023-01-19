package org.dhis2.ruleengine

import org.dhis2.ruleengine.models.RuleAction

data class RuleEffect(
    val ruleId: String?,
    val ruleAction: RuleAction,
    val data: String?
)
