package org.hisp.dhis.rules.models;

import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@AutoValue
public abstract class RuleEffect
{

    @Nonnull
    public static RuleEffect create( @Nonnull RuleAction ruleAction, @Nullable String data )
    {
        return new AutoValue_RuleEffect( ruleAction, data );
    }

    @Nonnull
    public static RuleEffect create( @Nonnull RuleAction ruleAction )
    {
        return new AutoValue_RuleEffect( ruleAction, "" );
    }

    @Nonnull
    public abstract RuleAction ruleAction();

    @Nonnull
    public abstract String data();
}
