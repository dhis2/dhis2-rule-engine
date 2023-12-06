package org.hisp.dhis.rules.models
data class RuleEffects(
    val trackerObjectType: TrackerObjectType,
    val trackerObjectUid: String,
    val ruleEffects: List<RuleEffect>
) {
    val isEnrollment: Boolean
        get() = trackerObjectType == TrackerObjectType.ENROLLMENT
    val isEvent: Boolean
        get() = trackerObjectType == TrackerObjectType.EVENT

    fun ruleEffects(): List<RuleEffect> {
        return ruleEffects
    }

    fun trackerObjectUid(): String {
        return trackerObjectUid
    }
}
