package org.hisp.dhis.rules.models;

import javax.annotation.Nonnull;

public record RuleAttributeValue(
        @Nonnull String trackedEntityAttribute,
        @Nonnull String value
) {

    public static final RuleAttributeValue MOCK = new RuleAttributeValue(null, null);

    @Nonnull
    public static RuleAttributeValue create(@Nonnull String attribute, @Nonnull String value) {
        return new RuleAttributeValue(attribute, value);
    }
}
