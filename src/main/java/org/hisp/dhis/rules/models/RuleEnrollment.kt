package org.hisp.dhis.rules.models

import java.util.Collections
import java.util.Date

data class RuleEnrollment(
        var enrollment: String?,
        var programName: String?,
        var incidentDate: Date?,
        var enrollmentDate: Date?,
        var status: Status?,
        var organisationUnit: String?,
        var organisationUnitCode: String?,
        var attributeValues: List<RuleAttributeValue>?
) {


    enum class Status {
        ACTIVE, COMPLETED, CANCELLED
    }


    class Builder {
        private val ruleEnrollment = RuleEnrollment(null, null, null,
                null, null, null, null, null)


        fun enrollment(enrollment: String): Builder {
            ruleEnrollment.enrollment = enrollment
            return this
        }

        fun programName(programName: String): Builder {
            ruleEnrollment.programName = programName
            return this
        }

        fun incidentDate(incidentDate: Date): Builder{
            ruleEnrollment.incidentDate = incidentDate
            return this
        }

        fun enrollmentDate(enrollmentDate: Date): Builder {
            ruleEnrollment.enrollmentDate = enrollmentDate
            return this
        }

        fun status(status: Status): Builder {
            ruleEnrollment.status = status
            return this
        }

        fun organisationUnit(organisationUnit: String): Builder {
            ruleEnrollment.organisationUnit = organisationUnit
            return this
        }

        fun organisationUnitCode(organisationUnitCode: String?): Builder {
            ruleEnrollment.organisationUnitCode = organisationUnitCode
            return this
        }

        fun attributeValues(attributeValues: List<RuleAttributeValue>): Builder {
            ruleEnrollment.attributeValues = attributeValues
            return this
        }

        fun build(): RuleEnrollment {
            return ruleEnrollment
        }
    }

    companion object {

        fun create(enrollment: String, incidentDate: Date,
                   enrollmentDate: Date, status: Status, organisationUnit: String, organisationUnitCode: String?,
                   attributeValues: List<RuleAttributeValue>, programName: String): RuleEnrollment {

            return RuleEnrollment.builder()
                    .enrollment(enrollment)
                    .programName(programName)
                    .incidentDate(incidentDate)
                    .enrollmentDate(enrollmentDate)
                    .status(status)
                    .organisationUnit(organisationUnit)
                    .organisationUnitCode(organisationUnitCode)
                    .attributeValues(Collections.unmodifiableList(attributeValues))
                    .build()
        }

        fun builder(): Builder {
            return Builder()
        }
    }
}
