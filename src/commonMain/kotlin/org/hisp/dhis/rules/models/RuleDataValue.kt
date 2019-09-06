package org.hisp.dhis.rules.models

import org.hisp.dhis.rules.utils.Date
import kotlin.jvm.JvmStatic

data class RuleDataValue(val eventDate: Date?,
                         val programStage: String?,
                         val dataElement: String?,
                         val value: String?) {

    companion object {

        @JvmStatic fun create(eventDate: Date, programStage: String, dataelement: String, value: String) =
                RuleDataValue(eventDate, programStage, dataelement, value)

    }
}