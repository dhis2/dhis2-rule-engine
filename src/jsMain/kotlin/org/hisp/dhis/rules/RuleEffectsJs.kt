package org.hisp.dhis.rules

import org.hisp.dhis.rules.models.TrackerObjectType

@JsExport
@OptIn(ExperimentalJsExport::class)
data class RuleEffectsJs(
    val trackerObjectType: String,
    val trackerObjectUid: String,
    val ruleEffects: Array<RuleEffectJs>
) {
    init {
        require(TYPES.contains(trackerObjectType)) { "trackerObjectType type must be one of: $TYPES" }
    }

    companion object {
        val TYPES = TrackerObjectType.entries.map { it.name } .toTypedArray()
    }
}
