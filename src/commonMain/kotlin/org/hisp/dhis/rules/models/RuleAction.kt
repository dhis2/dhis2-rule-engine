package org.hisp.dhis.rules.models

data class RuleAction(
    val data: String?,
    val type: String,
    val values: Map<String, String> = emptyMap()
){
    fun content():String? {
        return values["content"]
    }

    fun field():String? {
        return values["field"]
    }

    fun attributeType():String? {
        return values["attributeType"]
    }
}