package org.dhis2.ruleengine.models

interface TriggerEnvironment {
    val clientName: String
}

expect fun getClientName(): TriggerEnvironment