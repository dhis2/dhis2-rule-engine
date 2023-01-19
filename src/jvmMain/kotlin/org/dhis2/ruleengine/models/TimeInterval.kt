package org.dhis2.ruleengine.models

import org.joda.time.LocalDate

class TimeInterval private constructor(val startDate: LocalDate?, val endDate: LocalDate?, val isEmpty: Boolean) {

    companion object {
        fun empty(): TimeInterval {
            return TimeInterval(null, null, true)
        }

        fun fromTo(startDate: LocalDate?, endDate: LocalDate?): TimeInterval {
            return TimeInterval(startDate, endDate, false)
        }
    }
}