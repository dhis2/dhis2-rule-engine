package org.hisp.dhis.rules

import org.hisp.dhis.rules.api.ItemValueType

@JsExport
@OptIn(ExperimentalJsExport::class)
data class DataItemJs(val displayName: String, val valueType: String){
    init {
        require(TYPES.contains(valueType)) { "ItemValueType type must be one of: $TYPES" }
    }

    companion object {
        val TYPES = ItemValueType.entries.map { it.name } .toTypedArray()
    }
}