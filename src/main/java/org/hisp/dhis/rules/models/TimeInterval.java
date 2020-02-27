package org.hisp.dhis.rules.models;

import org.joda.time.LocalDate;

public class TimeInterval
{
    private final LocalDate startDate;

    private final LocalDate endDate;

    private final boolean empty;

    private TimeInterval( LocalDate startDate, LocalDate endDate, boolean empty )
    {
        this.startDate = startDate;
        this.endDate = endDate;
        this.empty = empty;
    }

    public static TimeInterval empty()
    {
        return new TimeInterval( null, null, true );
    }

    public static TimeInterval fromTo( LocalDate startDate, LocalDate endDate )
    {
        return new TimeInterval( startDate, endDate, false );
    }

    public LocalDate getStartDate()
    {
        return startDate;
    }

    public LocalDate getEndDate()
    {
        return endDate;
    }

    public boolean isEmpty()
    {
        return this.empty;
    }
}
