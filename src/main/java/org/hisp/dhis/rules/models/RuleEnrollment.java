package org.hisp.dhis.rules.models;

import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@AutoValue
public abstract class RuleEnrollment
{

        @Nonnull
        public abstract String enrollment();

        @Nonnull
        public abstract String programName();

        @Nonnull
        public abstract Date incidentDate();

        @Nonnull
        public abstract Date enrollmentDate();

        @Nonnull
        public abstract Status status();

        @Nonnull
        public abstract String organisationUnit();

        @Nonnull
        public abstract String organisationUnitCode();

        @Nonnull
        public abstract List<RuleAttributeValue> attributeValues();

        public enum Status
        {
                ACTIVE, COMPLETED, CANCELLED
        }

        @Nonnull
        public static RuleEnrollment create(@Nonnull String enrollment, @Nonnull Date incidentDate,
                                            @Nonnull Date enrollmentDate, @Nonnull Status status, @Nonnull String organisationUnit, @Nonnull String organisationUnitCode,
                                            @Nonnull List<RuleAttributeValue> attributeValues, String programName)
        {
                return new AutoValue_RuleEnrollment( enrollment, programName, incidentDate, enrollmentDate, status, organisationUnit, organisationUnitCode,
                    Collections.unmodifiableList( new ArrayList<>( attributeValues ) ) );
        }
}
