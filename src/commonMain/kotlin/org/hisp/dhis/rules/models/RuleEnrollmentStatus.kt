package org.hisp.dhis.rules.models

import kotlin.js.JsExport

@JsExport
enum class RuleEnrollmentStatus {
    ACTIVE,
    COMPLETED,
    CANCELLED,
}
