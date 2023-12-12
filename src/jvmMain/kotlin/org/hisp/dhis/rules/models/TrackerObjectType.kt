package org.hisp.dhis.rules.models

/**
 * This Enum specify the type of tracker object.
 */
enum class TrackerObjectType(private val type: String) {
    EVENT("event"),
    ENROLLMENT("enrollment")

}
