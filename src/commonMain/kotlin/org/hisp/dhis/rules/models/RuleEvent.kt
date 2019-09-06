package org.hisp.dhis.rules.models


import org.hisp.dhis.rules.utils.Date
import org.hisp.dhis.rules.utils.compareTo
import java.io.Serializable
import java.util.*
import kotlin.jvm.JvmField
import kotlin.jvm.JvmStatic

class RuleEvent(val event: String?,
                val programStage: String?,
                val programStageName: String?,
                val status: Status?,
                val eventDate: Date?,
                val dueDate: Date?,
                val organisationUnit: String?,
                val organisationUnitCode: String?,
                val dataValues: PersistentList<RuleDataValue>?) {

    data class Builder(var event: String?,
                       var programStage: String?,
                       var programStageName: String?,
                       var status: Status?,
                       var eventDate: Date?,
                       var dueDate: Date?,
                       var organisationUnit: String?,
                       var organisationUnitCode: String?,
                       var dataValues: PersistentList<RuleDataValue>?) {

        fun event(event: String?) = apply { this.event = event }

        fun programStage(programStage: String?) = apply { this.programStage = programStage }

        fun programStageName(programStageName: String?) = apply { this.programStageName = programStageName }

        fun status(status: Status?) = apply { this.status = status }

        fun eventDate(eventDate: Date?) = apply { this.eventDate = eventDate }

        fun dueDate(dueDate: Date?) = apply { this.dueDate = dueDate }

        fun organisationUnit(organisationUnit: String?) = apply { this.organisationUnit = organisationUnit }

        fun organisationUnitCode(organisationUnitCode: String?) = apply { this.organisationUnitCode = organisationUnitCode }

        fun dataValues(dataValues: List<RuleDataValue>?) = apply { this.dataValues = dataValues?.toPersistentList() }

        fun build() = RuleEvent(event, programStage, programStageName, status, eventDate,
                dueDate, organisationUnit, organisationUnitCode, dataValues)
    }

    enum class Status {
        ACTIVE, COMPLETED, SCHEDULE, SKIPPED, VISITED, OVERDUE
    }

    private class EventDateComparator : Comparator<RuleEvent> {

        override fun compare(first: RuleEvent, second: RuleEvent): Int {
            return second.eventDate!!.compareTo(first.eventDate!!)
        }

        companion object {
            private const val serialVersionUID = 2394142518753625334L
        }
    }

    companion object {
        @JvmField val EVENT_DATE_COMPARATOR: Comparator<RuleEvent> = EventDateComparator()

        @JvmStatic fun create(event: String,
                   programStage: String,
                   status: Status,
                   eventDate: Date,
                   dueDate: Date,
                   organisationUnit: String,
                   organisationUnitCode: String?,
                   ruleDataValues: List<RuleDataValue>,
                   programStageName: String): RuleEvent {
            return builder()
                    .event(event)
                    .programStage(programStage)
                    .programStageName(programStageName)
                    .status(status)
                    .eventDate(eventDate)
                    .dueDate(dueDate)
                    .organisationUnit(organisationUnit)
                    .organisationUnitCode(organisationUnitCode)
                    .dataValues(ruleDataValues.toPersistentList())
                    .build()
        }

        @JvmStatic fun builder() = Builder(null, null, null, null, null,
                null, null, null, null)
    }
}