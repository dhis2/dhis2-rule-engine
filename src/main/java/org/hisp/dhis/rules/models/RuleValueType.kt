package org.hisp.dhis.rules.models


enum class RuleValueType constructor(private val defaultValue: String) {
    TEXT("''"), NUMERIC("0.0"), BOOLEAN("false");

    fun defaultValue() = defaultValue

}