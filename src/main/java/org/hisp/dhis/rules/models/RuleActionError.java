package org.hisp.dhis.rules.models;

import javax.annotation.Nonnull;

public record RuleActionError(@Nonnull String data) implements RuleAction {

    @Nonnull
    public static RuleActionError create( @Nonnull String data )
    {
        return new RuleActionError( data );
    }
}
