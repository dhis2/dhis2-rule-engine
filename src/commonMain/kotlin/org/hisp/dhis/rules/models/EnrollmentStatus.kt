package org.hisp.dhis.rules.models

import kotlin.js.JsExport

@JsExport
enum class EnrollmentStatus {
    ACTIVE,
    COMPLETED,
    CANCELLED
}