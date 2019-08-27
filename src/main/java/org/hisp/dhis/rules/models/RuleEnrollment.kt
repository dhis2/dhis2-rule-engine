package org.hisp.dhis.rules.models

import kotlinx.collections.immutable.toPersistentList
import java.util.Date

class RuleEnrollment(val enrollment: String?,
                     val programName: String?,
                     val incidentDate: Date?,
                     val enrollmentDate: Date?,
                     val status: Status?,
                     val organisationUnit: String?,
                     val organisationUnitCode: String?,
                     val attributeValues: List<RuleAttributeValue>?) {

    enum class Status {
        ACTIVE, COMPLETED, CANCELLED
    }


    data class Builder(var enrollment: String?,
                       var programName: String?,
                       var incidentDate: Date?,
                       var enrollmentDate: Date?,
                       var status: Status?,
                       var organisationUnit: String?,
                       var organisationUnitCode: String?,
                       var attributeValues: List<RuleAttributeValue>?) {

        fun enrollment(enrollment: String?) = apply { this.enrollment = enrollment }

        fun programName(programName: String?) = apply { this.programName = programName }

        fun incidentDate(incidentDate: Date?) = apply { this.incidentDate = incidentDate }

        fun enrollmentDate(enrollmentDate: Date?) = apply { this.enrollmentDate = enrollmentDate }

        fun status(status: Status?) = apply { this.status = status }

        fun organisationUnit(organisationUnit: String?) = apply { this.organisationUnit = organisationUnit }

        fun organisationUnitCode(organisationUnitCode: String?) = apply { this.organisationUnitCode = organisationUnitCode }

        fun attributeValues(attributeValues: List<RuleAttributeValue>?) = apply { this.attributeValues = attributeValues?.toPersistentList() }

        fun build() = RuleEnrollment(enrollment, programName, incidentDate, enrollmentDate,
                status, organisationUnit, organisationUnitCode, attributeValues)
    }

    companion object {

        @JvmStatic
        fun create(enrollment: String,
                   incidentDate: Date,
                   enrollmentDate: Date,
                   status: Status,
                   organisationUnit: String,
                   organisationUnitCode: String?,
                   attributeValues: List<RuleAttributeValue>,
                   programName: String): RuleEnrollment {

            return builder()
                    .enrollment(enrollment)
                    .programName(programName)
                    .incidentDate(incidentDate)
                    .enrollmentDate(enrollmentDate)
                    .status(status)
                    .organisationUnit(organisationUnit)
                    .organisationUnitCode(organisationUnitCode)
                    .attributeValues(attributeValues.toPersistentList())
                    .build()
        }

        @JvmStatic
        fun builder() = Builder(null, null, null, null,
                null, null, null, null)

    }
}