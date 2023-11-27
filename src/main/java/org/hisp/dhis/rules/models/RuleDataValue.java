package org.hisp.dhis.rules.models;

import javax.annotation.Nonnull;
import java.util.Date;

public record RuleDataValue(
        @Nonnull Date eventDate,
        @Nonnull String programStage,
        @Nonnull String dataElement,
        @Nonnull String value
) {
    public static final RuleDataValue MOCK = new RuleDataValue(null, null, null, null);

    public static RuleDataValue create(@Nonnull Date eventDate, @Nonnull String programStage,
                                       @Nonnull String dataelement, @Nonnull String value) {
        return new RuleDataValue(eventDate, programStage, dataelement, value);
    }
}
