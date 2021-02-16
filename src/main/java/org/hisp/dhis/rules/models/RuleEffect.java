package org.hisp.dhis.rules.models;

import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@AutoValue
public abstract class RuleEffect
{

    @Nonnull
    public static RuleEffect create( @Nonnull String ruleId, @Nonnull RuleAction ruleAction, @Nullable String data )
    {
        return new AutoValue_RuleEffect( ruleId, ruleAction, data );
    }

    @Nonnull
    public static RuleEffect create( @Nonnull String ruleId, @Nonnull RuleAction ruleAction )
    {
        return new AutoValue_RuleEffect( ruleId, ruleAction, "" );
    }

    @Nullable
    public abstract String ruleId();

    @Nonnull
    public abstract RuleAction ruleAction();

    @Nullable
    public abstract String data();
}
