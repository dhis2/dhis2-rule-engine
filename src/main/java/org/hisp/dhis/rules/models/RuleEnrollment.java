package org.hisp.dhis.rules.models;

import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@AutoValue
public abstract class RuleEnrollment
{

    @Nonnull
    public static RuleEnrollment create( @Nonnull String enrollment, @Nonnull Date incidentDate,
        @Nonnull Date enrollmentDate, @Nonnull Status status, @Nonnull String organisationUnit,
        @Nullable String organisationUnitCode,
        @Nonnull List<RuleAttributeValue> attributeValues, String programName )
    {

        return AutoValue_RuleEnrollment.builder()
            .enrollment( enrollment )
            .programName( programName )
            .incidentDate( incidentDate )
            .enrollmentDate( enrollmentDate )
            .status( status )
            .organisationUnit( organisationUnit )
            .organisationUnitCode( organisationUnitCode )
            .attributeValues( Collections.unmodifiableList( attributeValues ) )
            .build();
    }

    public static Builder builder()
    {
        return new AutoValue_RuleEnrollment.Builder();
    }

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

    @Nullable
    public abstract String organisationUnitCode();

    @Nonnull
    public abstract List<RuleAttributeValue> attributeValues();

    public enum Status
    {
        ACTIVE, COMPLETED, CANCELLED
    }

    @AutoValue.Builder
    public static abstract class Builder
    {
        public abstract RuleEnrollment.Builder enrollment( String enrollment );

        public abstract RuleEnrollment.Builder programName( String programName );

        public abstract RuleEnrollment.Builder incidentDate( Date incidentDate );

        public abstract RuleEnrollment.Builder enrollmentDate( Date enrollmentDate );

        public abstract RuleEnrollment.Builder status( RuleEnrollment.Status status );

        public abstract RuleEnrollment.Builder organisationUnit( String organisationUnit );

        public abstract RuleEnrollment.Builder organisationUnitCode( String organisationUnitCode );

        public abstract RuleEnrollment.Builder attributeValues( List<RuleAttributeValue> attributeValues );

        public abstract RuleEnrollment build();
    }
}
