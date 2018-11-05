package org.hisp.dhis.rules.models;

import com.google.auto.value.AutoValue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@AutoValue
public abstract class RuleEvent {
    public static final Comparator<RuleEvent> EVENT_DATE_COMPARATOR = new EventDateComparator();

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
    public abstract Date dueDate();

    @Nonnull
    public abstract String organisationUnit();

    @Nullable
    public abstract String organisationUnitCode();

    @Nonnull
    public abstract List<RuleDataValue> dataValues();

    @Nonnull
    public static RuleEvent create(
            @Nonnull String event,
            @Nonnull String programStage,
            @Nonnull Status status,
            @Nonnull Date eventDate,
            @Nonnull Date dueDate,
            @Nonnull String organisationUnit,
            @Nullable String organisationUnitCode,
            @Nonnull List<RuleDataValue> ruleDataValues,
            @Nonnull String programStageName) {
        return new AutoValue_RuleEvent(event, programStage, programStageName, status, eventDate, dueDate, organisationUnit,
                organisationUnitCode, Collections.unmodifiableList(new ArrayList<>(ruleDataValues)));
    }

    public enum Status {
        ACTIVE, COMPLETED, SCHEDULE, SKIPPED, VISITED, OVERDUE
    }

    private static class EventDateComparator
            implements Comparator<RuleEvent>, Serializable {
        private static final long serialVersionUID = 2394142518753625334L;

        @Override
        public int compare(RuleEvent first, RuleEvent second) {
            return second.eventDate().compareTo(first.eventDate());
        }
    }
}
