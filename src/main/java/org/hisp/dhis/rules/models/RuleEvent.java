package org.hisp.dhis.rules.models;

import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@AutoValue
public abstract class RuleEvent
{
    public static final Comparator<RuleEvent> EVENT_DATE_COMPARATOR = new EventDateComparator();

    @Nonnull
    public static RuleEvent create(
        @Nonnull String event,
        @Nonnull String programStage,
        @Nonnull Status status,
        @Nonnull Date eventDate,
        @Nonnull Date createdDate,
        @Nonnull Date dueDate,
        @Nonnull String organisationUnit,
        @Nullable String organisationUnitCode,
        @Nonnull List<RuleDataValue> ruleDataValues,
        @Nonnull String programStageName,
        @Nullable Date completedDate )
    {
        return new AutoValue_RuleEvent.Builder()
            .event( event )
            .programStage( programStage )
            .programStageName( programStageName )
            .status( status )
            .eventDate( eventDate )
            .createdDate( createdDate )
            .dueDate( dueDate )
            .organisationUnit( organisationUnit )
            .organisationUnitCode( organisationUnitCode )
            .completedDate( completedDate )
            .dataValues( Collections.unmodifiableList( new ArrayList<>( ruleDataValues ) ) )
            .build();
    }

    public static Builder builder()
    {
        return new AutoValue_RuleEvent.Builder();
    }

    @Nonnull
    public abstract String event();

    @Nonnull
    public abstract String programStage();

    @Nonnull
    public abstract String programStageName();

    @Nonnull
    public abstract Status status();

    @Nonnull
    public abstract Date eventDate();

    @Nonnull
    public abstract Date createdDate();

    @Nullable
    public abstract Date dueDate();

    @Nullable
    public abstract Date completedDate();

    @Nonnull
    public abstract String organisationUnit();

    @Nullable
    public abstract String organisationUnitCode();

    @Nonnull
    public abstract List<RuleDataValue> dataValues();

    public enum Status
    {
        ACTIVE, COMPLETED, SCHEDULE, SKIPPED, VISITED, OVERDUE
    }

    @AutoValue.Builder
    public static abstract class Builder
    {
        public abstract Builder event( String event );

        public abstract Builder programStage( String programStage );

        public abstract Builder programStageName( String programStageName );

        public abstract Builder status( Status status );

        public abstract Builder eventDate( Date eventDate );

        public abstract Builder dueDate( Date dueDate );

        public abstract Builder createdDate( Date createdDate );

        public abstract Builder completedDate( Date completedDate );

        public abstract Builder organisationUnit( String organisationUnit );

        public abstract Builder organisationUnitCode( String organisationUnitCode );

        public abstract Builder dataValues( List<RuleDataValue> dataValues );

        public abstract RuleEvent build();
    }

    private static class EventDateComparator
        implements Comparator<RuleEvent>, Serializable
    {
        private static final long serialVersionUID = 2394142518753625334L;

        @Override
        public int compare( RuleEvent first, RuleEvent second )
        {
            int compare = second.eventDate().compareTo(first.eventDate());
            if (compare == 0){
                return second.createdDate().compareTo(first.createdDate());
            }
            return compare;
        }
    }

}
