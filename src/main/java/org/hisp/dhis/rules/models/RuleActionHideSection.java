package org.hisp.dhis.rules.models;

import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;

@AutoValue
public abstract class RuleActionHideSection
    extends RuleAction
{

    @Nonnull
    public static RuleActionHideSection create( @Nonnull String section )
    {
        return new AutoValue_RuleActionHideSection( "", section );
    }

    @Nonnull
    public abstract String programStageSection();
}
