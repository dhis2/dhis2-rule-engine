package org.hisp.dhis.rules

import org.hisp.dhis.rules.models.TriggerEnvironment

actual fun getEnvironment(): TriggerEnvironment {
    return TriggerEnvironment.SERVER
}