package org.hisp.dhis.rules

import org.hisp.dhis.rules.api.ItemValueType

@JsExport
@OptIn(ExperimentalJsExport::class)
data class DataItemJs(val displayName: String, val valueType: ItemValueType)