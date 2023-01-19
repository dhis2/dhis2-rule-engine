package org.dhis2.ruleengine.models

import org.dhis2.ruleengine.RuleEffect

data class RuleEffects(
    private val trackerObjectType: TrackerObjectType,
    val trackerObjectUid: String,
    val ruleEffects: List<RuleEffect>
) {

    val isEnrollment: Boolean
        get() = trackerObjectType === TrackerObjectType.ENROLLMENT
    val isEvent: Boolean
        get() = trackerObjectType === TrackerObjectType.EVENT
}