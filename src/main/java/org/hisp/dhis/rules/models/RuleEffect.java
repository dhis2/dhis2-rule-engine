package org.hisp.dhis.rules.models;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

;

public record RuleEffect( @Nonnull String ruleId, @Nonnull RuleAction ruleAction, @CheckForNull String data )
{
    public RuleEffect( @Nonnull String ruleId, @Nonnull RuleAction ruleAction )
    {
        this( ruleId, ruleAction, "" );
    }
}
