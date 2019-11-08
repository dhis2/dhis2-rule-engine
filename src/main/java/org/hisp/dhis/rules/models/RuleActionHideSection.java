package org.hisp.dhis.rules.models;

import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@AutoValue
public abstract class RuleActionHideSection
    extends RuleAction
{

        @Nonnull
        public abstract String programStageSection();

        @Nonnull
        public static RuleActionHideSection create( @Nonnull String section )
        {
                return new AutoValue_RuleActionHideSection( section );
        }
}
