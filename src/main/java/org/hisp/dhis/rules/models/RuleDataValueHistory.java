package org.hisp.dhis.rules.models;

import java.util.Date;

public class RuleDataValueHistory {
    private final String value;
    private final Date eventDate;
    private final Date createdDate;
    private final String programStage;
    public RuleDataValueHistory( String value, Date eventDate, Date createdDate, String programStage) {
        this.value = value;
        this.eventDate = eventDate;
        this.createdDate = createdDate;
        this.programStage = programStage;
    }

    public String getValue() {
        return value;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public String getProgramStage() {
        return programStage;}
}