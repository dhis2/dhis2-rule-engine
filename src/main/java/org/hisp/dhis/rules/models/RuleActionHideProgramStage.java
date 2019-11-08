package org.hisp.dhis.rules.models;

import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@AutoValue
public abstract class RuleActionHideProgramStage
    extends RuleAction
{

        @Nonnull
        public abstract String programStage();

        @Nonnull
        public static RuleActionHideProgramStage create( @Nonnull String programStage )
        {
                return new AutoValue_RuleActionHideProgramStage( programStage );
        }
}
