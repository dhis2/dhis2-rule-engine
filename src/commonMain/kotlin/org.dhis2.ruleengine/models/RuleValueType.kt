package org.dhis2.ruleengine.models

enum class RuleValueType(val defaultValue:String) {
    TEXT(""),
    NUMERIC("0.0"),
    BOOLEAN("false"),
    DATE("2020-01-01")
}