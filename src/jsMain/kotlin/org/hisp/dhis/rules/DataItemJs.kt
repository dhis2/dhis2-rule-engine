package org.hisp.dhis.rules

import org.hisp.dhis.rules.api.ItemValueType

@JsExport
data class DataItemJs(val displayName: String, val valueType: ItemValueType)