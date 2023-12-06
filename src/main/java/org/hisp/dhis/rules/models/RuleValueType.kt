package org.hisp.dhis.rules.models

enum class RuleValueType(private val defaultValue: Any) {
    TEXT(""),
    NUMERIC(0.0),
    BOOLEAN(false),
    DATE("2020-01-01");

    fun defaultValue(): Any {
        return defaultValue
    }
}
