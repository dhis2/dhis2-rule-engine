package org.hisp.dhis.rules.models;

import javax.annotation.Nonnull;

public record RuleActionHideSection(
        @Nonnull
        String data,
        @Nonnull
        String programStageSection
) implements RuleAction {

    @Nonnull
    public static RuleActionHideSection create(@Nonnull String section) {
        return new RuleActionHideSection("", section);
    }
}
