package org.hisp.dhis.rules.models

import kotlin.js.JsExport

@JsExport
enum class RuleEventStatus {
    ACTIVE,
    COMPLETED,
    SCHEDULE,
    SKIPPED,
    VISITED,
    OVERDUE,
}
