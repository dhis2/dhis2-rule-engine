package org.hisp.dhis.rules.models

import java.util.*

data class RuleDataValue(
        val eventDate: Date?,
        val programStage: String?,
        val dataElement: String?,
        val value: String?) {

    companion object {

        @JvmStatic
        fun create(eventDate: Date, programStage: String, dataelement: String, value: String) =
                RuleDataValue(eventDate, programStage, dataelement, value)

    }
}
