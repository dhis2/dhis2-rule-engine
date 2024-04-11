package org.hisp.dhis.rules.models

import kotlin.js.JsExport

@JsExport
enum class EventStatus {
    ACTIVE,
    COMPLETED,
    SCHEDULE,
    SKIPPED,
    VISITED,
    OVERDUE
}