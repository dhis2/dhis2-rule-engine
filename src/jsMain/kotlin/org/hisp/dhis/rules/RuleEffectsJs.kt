package org.hisp.dhis.rules

import org.hisp.dhis.rules.models.TrackerObjectType

@JsExport
@OptIn(ExperimentalJsExport::class)
data class RuleEffectsJs(
    val trackerObjectType: TrackerObjectType,
    val trackerObjectUid: String,
    val ruleEffects: Array<RuleEffectJs>
)
