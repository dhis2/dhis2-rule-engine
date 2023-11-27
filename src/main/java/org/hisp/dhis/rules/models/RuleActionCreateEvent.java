package org.hisp.dhis.rules.models;

import javax.annotation.Nonnull;
import javax.annotation.CheckForNull;

public record RuleActionCreateEvent(
        @Nonnull String data,
        @Nonnull String content,
        @Nonnull String programStage
) implements RuleAction {

    @Nonnull
    public static RuleActionCreateEvent create(@CheckForNull String content,
                                               @CheckForNull String data, @Nonnull String programStage) {
        return new RuleActionCreateEvent(data == null ? "" : data, content == null ? "" : content, programStage);
    }
}
