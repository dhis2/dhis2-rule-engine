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
        public abstract Date incidentDate();

        @Nonnull
        public abstract Date enrollmentDate();

        @Nonnull
        public abstract Status status();

        @Nonnull
        public abstract String organisationUnit();

        @Nonnull
        public abstract List<RuleAttributeValue> attributeValues();

        public enum Status
        {
                ACTIVE, COMPLETED, CANCELLED
        }

        @Nonnull
        public static RuleEnrollment create( @Nonnull String enrollment, @Nonnull Date incidentDate,
            @Nonnull Date enrollmentDate, @Nonnull Status status, @Nonnull String organisationUnit,
            @Nonnull List<RuleAttributeValue> attributeValues )
        {
                return new AutoValue_RuleEnrollment( enrollment, incidentDate, enrollmentDate, status, organisationUnit,
                    Collections.unmodifiableList( new ArrayList<>( attributeValues ) ) );
        }
}
