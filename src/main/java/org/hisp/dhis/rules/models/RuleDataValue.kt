package org.hisp.dhis.rules.models

import com.google.auto.value.AutoValue
import java.util.Date

data class RuleDataValue(
        val eventDate: Date?,
        val programStage: String?,
        val dataElement: String?,
        val value: String?
) {


    companion object {

        fun create(eventDate: Date, programStage: String,
                   dataelement: String, value: String): RuleDataValue {
            return RuleDataValue(eventDate, programStage, dataelement, value)
        }
    }
}
