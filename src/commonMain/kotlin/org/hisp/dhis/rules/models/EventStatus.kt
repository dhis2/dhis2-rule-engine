package org.hisp.dhis.rules.models

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@JsExport
@OptIn(ExperimentalJsExport::class)
enum class EventStatus {
    ACTIVE,
    COMPLETED,
    SCHEDULE,
    SKIPPED,
    VISITED,
    OVERDUE
}