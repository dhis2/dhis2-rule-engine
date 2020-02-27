package org.hisp.dhis.rules.models;

import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;

@AutoValue
public abstract class RuleActionHideProgramStage
    extends RuleAction
{

    @Nonnull
    public static RuleActionHideProgramStage create( @Nonnull String programStage )
    {
        return new AutoValue_RuleActionHideProgramStage( programStage );
    }

    @Nonnull
    public abstract String programStage();
}
