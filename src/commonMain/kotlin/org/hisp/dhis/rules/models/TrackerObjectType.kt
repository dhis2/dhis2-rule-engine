package org.hisp.dhis.rules.models

import kotlin.js.JsExport

/**
 * This Enum specify the type of tracker object.
 */
@JsExport
enum class TrackerObjectType(private val type: String) {
    EVENT("event"),
    ENROLLMENT("enrollment")

}
