package org.hisp.dhis.rules.models;

import javax.annotation.CheckForNull;;
import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public record RuleEnrollment(
        @Nonnull String enrollment,
        @Nonnull String programName,
        @Nonnull Date incidentDate,
        @Nonnull Date enrollmentDate,
        @Nonnull Status status,
        @Nonnull String organisationUnit,
        @CheckForNull String organisationUnitCode,
        @Nonnull List<RuleAttributeValue> attributeValues
) {

    public static final RuleEnrollment MOCK = new RuleEnrollment(null, null, null, null, null, null, null, null);

    @Nonnull
    public static RuleEnrollment create(@Nonnull String enrollment, @Nonnull Date incidentDate,
                                        @Nonnull Date enrollmentDate, @Nonnull Status status, @Nonnull String organisationUnit,
                                        @CheckForNull String organisationUnitCode,
                                        @Nonnull List<RuleAttributeValue> attributeValues, String programName) {

        return builder()
                .enrollment(enrollment)
                .programName(programName)
                .incidentDate(incidentDate)
                .enrollmentDate(enrollmentDate)
                .status(status)
                .organisationUnit(organisationUnit)
                .organisationUnitCode(organisationUnitCode)
                .attributeValues(Collections.unmodifiableList(attributeValues))
                .build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public enum Status {
        ACTIVE, COMPLETED, CANCELLED
    }

    @Deprecated // use KMP data class
    public static class Builder {
        String enrollment;
        String programName;
        Date incidentDate;
        Date enrollmentDate;
        Status status;
        String organisationUnit;
        String organisationUnitCode;
        List<RuleAttributeValue> attributeValues;

        public RuleEnrollment.Builder enrollment(String enrollment) {
            this.enrollment = enrollment;
            return this;
        }

        public RuleEnrollment.Builder programName(String programName) {
            this.programName = programName;
            return this;
        }

        public RuleEnrollment.Builder incidentDate(Date incidentDate) {
            this.incidentDate = incidentDate;
            return this;
        }

        public RuleEnrollment.Builder enrollmentDate(Date enrollmentDate) {
            this.enrollmentDate = enrollmentDate;
            return this;
        }

        public RuleEnrollment.Builder status(RuleEnrollment.Status status) {
            this.status = status;
            return this;
        }

        public RuleEnrollment.Builder organisationUnit(String organisationUnit) {
            this.organisationUnit = organisationUnit;
            return this;
        }

        public RuleEnrollment.Builder organisationUnitCode(String organisationUnitCode) {
            this.organisationUnitCode = organisationUnitCode;
            return this;
        }

        public RuleEnrollment.Builder attributeValues(List<RuleAttributeValue> attributeValues) {
            this.attributeValues = attributeValues;
            return this;
        }

        public RuleEnrollment build() {
            return new RuleEnrollment(enrollment, programName, incidentDate, enrollmentDate, status, organisationUnit, organisationUnitCode, attributeValues);
        }
    }
}
