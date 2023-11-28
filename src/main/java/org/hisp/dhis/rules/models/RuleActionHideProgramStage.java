package org.hisp.dhis.rules.models;

import javax.annotation.Nonnull;

public record RuleActionHideProgramStage(
        @Nonnull String data,
        @Nonnull String programStage
) implements RuleAction {

    @Nonnull
    public static RuleActionHideProgramStage create(@Nonnull String programStage) {
        return new RuleActionHideProgramStage("", programStage);
    }
}
