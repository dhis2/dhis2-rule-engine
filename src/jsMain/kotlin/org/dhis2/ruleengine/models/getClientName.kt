package org.dhis2.ruleengine.models

actual fun getClientName(): TriggerEnvironment {
    return object : TriggerEnvironment{
        override val clientName: String
            get() = "JS"
    }
}