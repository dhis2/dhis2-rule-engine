package org.hisp.dhis.rules.models;

import javax.annotation.Nonnull;
import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public record RuleEvent(
        @Nonnull
        String event,
        @Nonnull
        String programStage,
        @Nonnull
        String programStageName,
        @Nonnull
        Status status,
        @Nonnull
        Date eventDate,
        @CheckForNull
        Date dueDate,
        @CheckForNull
        Date completedDate,
        @Nonnull
        String organisationUnit,
        @CheckForNull
        String organisationUnitCode,
        @Nonnull
        List<RuleDataValue> dataValues
) {
    public static final Comparator<RuleEvent> EVENT_DATE_COMPARATOR = new EventDateComparator();

    public static final RuleEvent MOCK = new RuleEvent(null, null, null, null, null, null, null, null, null, null);

    @Nonnull
    public static RuleEvent create(
        @Nonnull String event,
        @Nonnull String programStage,
        @Nonnull Status status,
        @Nonnull Date eventDate,
        @Nonnull Date dueDate,
        @Nonnull String organisationUnit,
        @CheckForNull String organisationUnitCode,
        @Nonnull List<RuleDataValue> ruleDataValues,
        @Nonnull String programStageName,
        @CheckForNull Date completedDate )
    {
        return new Builder()
            .event( event )
            .programStage( programStage )
            .programStageName( programStageName )
            .status( status )
            .eventDate( eventDate )
            .dueDate( dueDate )
            .organisationUnit( organisationUnit )
            .organisationUnitCode( organisationUnitCode )
            .completedDate( completedDate )
            .dataValues( List.copyOf(ruleDataValues ) )
            .build();
    }

    public static Builder builder()
    {
        return new Builder();
    }

    public enum Status
    {
        ACTIVE, COMPLETED, SCHEDULE, SKIPPED, VISITED, OVERDUE
    }

    @Deprecated // use data class copy/named assign once this is KMP
    public static class Builder
    {
        String event;
        String programStage;
        String programStageName;
        Status status;
        Date eventDate;
        Date dueDate;
        Date completedDate;
        String organisationUnit;
        String organisationUnitCode;
        List<RuleDataValue> dataValues;

        public Builder event( String event ) {
            this.event = event;
            return this;
        }

        public Builder programStage( String programStage ) {
            this.programStage = programStage;
            return this;
        }


        public Builder programStageName( String programStageName ) {
            this.programStageName = programStageName;
            return this;
        }

        public Builder status( Status status ) {
            this.status = status;
            return this;
        }

        public Builder eventDate( Date eventDate ) {
            this.eventDate = eventDate;
            return this;
        }

        public Builder dueDate( Date dueDate ) {
            this.dueDate = dueDate;
            return this;
        }

        public Builder completedDate( Date completedDate ) {
            this.completedDate = completedDate;
            return this;
        }

        public Builder organisationUnit( String organisationUnit ) {
            this.organisationUnit = organisationUnit;
            return this;
        }

        public Builder organisationUnitCode( String organisationUnitCode ) {
            this.organisationUnitCode = organisationUnitCode;
            return this;
        }

        public Builder dataValues( List<RuleDataValue> dataValues ) {
            this.dataValues = dataValues;
            return this;
        }

        public RuleEvent build() {
            return new RuleEvent(event, programStage, programStageName, status, eventDate, dueDate, completedDate, organisationUnit, organisationUnitCode, dataValues);
        }
    }

    private static class EventDateComparator
        implements Comparator<RuleEvent>, Serializable
    {
        private static final long serialVersionUID = 2394142518753625334L;

        @Override
        public int compare( RuleEvent first, RuleEvent second )
        {
            return second.eventDate().compareTo( first.eventDate() );
        }
    }

}
