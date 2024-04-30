package org.hisp.dhis.rules

import org.hisp.dhis.rules.models.TrackerObjectType

@JsExport
data class RuleEffectsJs(
    val trackerObjectType: TrackerObjectType,
    val trackerObjectUid: String,
    val ruleEffects: Array<RuleEffectJs>
)
