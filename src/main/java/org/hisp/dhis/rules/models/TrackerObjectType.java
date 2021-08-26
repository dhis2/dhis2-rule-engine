package org.hisp.dhis.rules.models;

/**
 * This Enum specify the type of tracker object.
 */
public enum TrackerObjectType
{
    EVENT("event"),
    ENROLLMENT("enrollment");

    private final String name;

    TrackerObjectType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
