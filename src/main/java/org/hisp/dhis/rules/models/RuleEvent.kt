package org.hisp.dhis.rules.models

import com.google.auto.value.AutoValue

import java.io.Serializable
import java.util.ArrayList
import java.util.Collections
import java.util.Comparator
import java.util.Date

data class RuleEvent(
        var event: String?,
        var programStage: String?,
        var programStageName: String?,
        var status: Status?,
        var eventDate: Date?,
        var dueDate: Date?,
        var organisationUnit: String?,
        var organisationUnitCode: String?,
        var dataValues: List<RuleDataValue>?
) {

    class Builder {
        private val ruleEvent = RuleEvent(null, null, null,
                null, null, null, null, null, null)

        fun event(event: String?): Builder {
            ruleEvent.event = event
            return this
        }

        fun programStage(programStage: String?): Builder {
            ruleEvent.programStage = programStage
            return this
        }

        fun programStageName(programStageName: String?): Builder {
            ruleEvent.programStageName = programStageName
            return this
        }

        fun status(status: Status?): Builder {
            ruleEvent.status = status
            return this
        }

        fun eventDate(eventDate: Date?): Builder {
            ruleEvent.eventDate = eventDate
            return this
        }

        fun dueDate(dueDate: Date?): Builder {
            ruleEvent.dueDate = dueDate
            return this
        }

        fun organisationUnit(organisationUnit: String?): Builder {
            ruleEvent.organisationUnit = organisationUnit
            return this
        }

        fun organisationUnitCode(organisationUnitCode: String?): Builder {
            ruleEvent.organisationUnitCode = organisationUnitCode
            return this
        }

        fun dataValues(dataValues: List<RuleDataValue>?): Builder {
            ruleEvent.dataValues = dataValues
            return this
        }

        fun build(): RuleEvent {
            return ruleEvent
        }
    }

    enum class Status {
        ACTIVE, COMPLETED, SCHEDULE, SKIPPED, VISITED, OVERDUE
    }

    private class EventDateComparator : Comparator<RuleEvent>, Serializable {

        override fun compare(first: RuleEvent, second: RuleEvent): Int {
            return second.eventDate!!.compareTo(first.eventDate!!)
        }

        companion object {
            private const val serialVersionUID = 2394142518753625334L
        }
    }

    companion object {
        val EVENT_DATE_COMPARATOR: Comparator<RuleEvent> = EventDateComparator()

        fun create(
                event: String,
                programStage: String,
                status: Status,
                eventDate: Date,
                dueDate: Date,
                organisationUnit: String,
                organisationUnitCode: String?,
                ruleDataValues: List<RuleDataValue>,
                programStageName: String): RuleEvent {
            return Builder()
                    .event(event)
                    .programStage(programStage)
                    .programStageName(programStageName)
                    .status(status)
                    .eventDate(eventDate)
                    .dueDate(dueDate)
                    .organisationUnit(organisationUnit)
                    .organisationUnitCode(organisationUnitCode)
                    .dataValues(Collections.unmodifiableList(ArrayList(ruleDataValues)))
                    .build()
        }

        fun builder(): Builder {
            return Builder()
        }
    }


}
